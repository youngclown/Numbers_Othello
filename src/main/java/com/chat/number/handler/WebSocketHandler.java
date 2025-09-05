package com.chat.number.handler;

import com.chat.number.domain.ChatMessage;
import com.chat.number.domain.GameRoom;
import com.chat.number.dto.GameStatusDto;
import com.chat.number.model.GameUser;
import com.chat.number.repository.GameRoomRepository;
import com.chat.number.type.RoleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private final GameRoomRepository gameRoomRepository;
    private final ObjectMapper objectMapper;

    public WebSocketHandler(GameRoomRepository gameRoomRepository, ObjectMapper objectMapper) {
        this.gameRoomRepository = gameRoomRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload : {}", payload);
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        GameRoom gameRoom = gameRoomRepository.findRoomById(chatMessage.getChatRoomId());
        Map<String, GameUser> userList = gameRoom.getWriteUser();

        GameUser gameUser = null;
        if (userList.containsKey(session.getId())) {
            gameUser = userList.get(session.getId());
        } else {
            log.error("not found user!!");
        }

        switch (chatMessage.getType()) {
            case ENTER:
                gameRoom.setRoomUserCount(gameRoom.getRoomUserCount() + 1);
                if (gameRoom.getRoomUserCount() <= gameRoom.getRoomMaxCount()) {
                    // 세션에 roomId와 sessionId 저장 (연결 종료 시 사용)
                    session.getAttributes().put("roomId", chatMessage.getChatRoomId());
                    session.getAttributes().put("sessionId", session.getId());

                    userList.put(session.getId(), GameUser.builder().userName(chatMessage.getWriter()).type(RoleType.MEMBER.getValue()).build());
                    gameRoom.getSessions().add(session);
                    sendToAll(gameRoom, new TextMessage(chatMessage.getWriter() + "님이 입장했습니다"));
                    // 변경된 상태 저장
                    gameRoomRepository.save(gameRoom);
                }
                break;
            case LEAVE:
                handleLeave(session, gameRoom, userList);
                break;
            case READY:
                gameUser.setReady(!gameUser.isReady());
                int readyCount = 0;
                for (GameUser user : userList.values()) {
                    if (user.isReady()) readyCount++;
                }

                sendToAll(gameRoom, new TextMessage(chatMessage.getWriter() + "님이 준비했습니다. " + readyCount + "/" + gameRoom.getRoomMaxCount()));

                if (userList.size() == 2 && readyCount == 2) {
                    log.info("GAME START");
                    // 1. 게임 초기화
                    gameRoom.initializeGame();

                    // 2. 초기 게임 상태 전송
                    GameStatusDto statusDto = new GameStatusDto(gameRoom);
                    String initialStatusMessage = objectMapper.writeValueAsString(statusDto);
                    sendToAll(gameRoom, new TextMessage(initialStatusMessage));
                }
                break;
            case GAME:
                if (!gameRoom.isGameStart()) {
                    sendToPlayer(session, new TextMessage("{\"type\": \"ERROR\", \"message\": \"게임이 아직 시작되지 않았습니다.\"}"));
                    return;
                }

                String[] i = chatMessage.getMessage().split("##");
                GameUser currentUser = userList.get(session.getId());
                if (currentUser == null || currentUser.getType() == null) {
                    sendToPlayer(session, new TextMessage("{\"type\": \"ERROR\", \"message\": \"플레이어 정보가 올바르지 않습니다.\"}"));
                    return;
                }

                boolean success = gameRoom.getGamePlayService().gamePlay(gameRoom, i[0], i[1], currentUser.getType());

                if (success) {
                    // 성공 시, 모든 플레이어에게 게임 상태 업데이트
                    GameStatusDto statusDto = new GameStatusDto(gameRoom);
                    String updatedStatusMessage = objectMapper.writeValueAsString(statusDto);
                    sendToAll(gameRoom, new TextMessage(updatedStatusMessage));
                    // 변경된 게임 상태를 Redis에 저장
                    gameRoomRepository.save(gameRoom);
                } else {
                    // 실패 시, 해당 플레이어에게만 에러 메시지 전송
                    sendToPlayer(session, new TextMessage("{\"type\": \"ERROR\", \"message\": \"규칙에 맞지 않는 플레이입니다.\"}"));
                }
                break;
            case CHAT:
                sendToAll(gameRoom, new TextMessage(chatMessage.getWriter() + ": " + chatMessage.getMessage()));
                break;
        }
    }

    private void sendToAll(GameRoom gameRoom, TextMessage message) {
        gameRoom.getSessions().parallelStream().forEach(session -> {
            try {
                session.sendMessage(message);
            } catch (IOException e) {
                log.error("Error sending message to session {}: {}", session.getId(), e.getMessage());
            }
        });
    }

    private void sendToPlayer(WebSocketSession session, TextMessage message) {
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            log.error("Error sending message to session {}: {}", session.getId(), e.getMessage());
        }
    }

    private void handleLeave(WebSocketSession session, GameRoom gameRoom, Map<String, GameUser> userList) {
        GameUser leavingUser = userList.get(session.getId());
        if (leavingUser == null) return; // 이미 처리된 경우

        gameRoom.setRoomUserCount(gameRoom.getRoomUserCount() - 1);
        userList.remove(session.getId());
        gameRoom.getSessions().remove(session);

        sendToAll(gameRoom, new TextMessage(leavingUser.getUserName() + "님이 퇴장했습니다"));

        if (gameRoom.getRoomUserCount() <= 0) {
            log.info("Room {} is empty. Deleting from Redis.", gameRoom.getRoomId());
            gameRoomRepository.deleteRoom(gameRoom.getRoomId());
        } else {
            // 남은 플레이어가 있으면 상태 저장
            log.info("User left room {}. Saving updated state.", gameRoom.getRoomId());
            gameRoomRepository.save(gameRoom);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("{} 연결됨", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} 연결 끊김", session.getId());
        String roomId = (String) session.getAttributes().get("roomId");
        if (roomId != null) {
            GameRoom gameRoom = gameRoomRepository.findRoomById(roomId);
            if (gameRoom != null) {
                handleLeave(session, gameRoom, gameRoom.getWriteUser());
            }
        }
    }
}
