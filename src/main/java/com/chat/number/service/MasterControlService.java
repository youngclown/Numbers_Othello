package com.chat.number.service;

import com.chat.number.domain.GameRoom;
import com.chat.number.model.ScoreRule;
import com.chat.number.model.GameUser;
import com.chat.number.model.UserScore;
import com.chat.number.type.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

//
@Slf4j
public class MasterControlService extends Thread {
    GameRoom gameRoom;  // 세션 유지
    private final ObjectMapper objectMapper;

    public MasterControlService(GameRoom gameRoom, ObjectMapper objectMapper) {
        this.gameRoom = gameRoom;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    public void run() {
        Set<WebSocketSession> sessions = this.gameRoom.getSessions();
        Map<String, GameUser> writeUser = this.gameRoom.getWriteUser();

        // game master는 항시 동작. geme play를 check.
        while (true) {

            // 1. 전체 Check 판을 체크합니다.
            Iterator<WebSocketSession> i = sessions.iterator();
            List<String> writerList = new ArrayList<>();
            List<UserScore> userScores = new ArrayList<>();


            boolean removeCheck = false;

            // 1. score 정리 작업
            while (i.hasNext()) {
                WebSocketSession session = i.next(); // must be called before you can call i.remove()
                try {
                    GameUser gameUser = gameRoom.getWriteUser().get(session.getId());
                    GamePlayService gamePlayService = this.gameRoom.getGamePlayService();
                    int score = gamePlayService.getScore(gameUser.getType());
                    gameUser.setScore(score);
                    userScores.add(new UserScore(gameUser.getType(), score));
                    session.sendMessage(new TextMessage(String.valueOf(score)));
                } catch (Exception e) {
                    removeCheck = true;
                    writerList.add(writeUser.get(session.getId()).getUsername());
                    i.remove();
                }
            }

            // 2. 나간 User 체크
            // TODO 나간 유저 정리... (유저가 나갔을 경우에 무언가 동작하는 로직이 필요!
            if (removeCheck) {
                for (WebSocketSession session : sessions) {
                    for (String writer : writerList) {
                        String json = objectMapper.writeValueAsString(writer + " bye bye!!!!!");
                        if (json != null) {
                            TextMessage textMessage = new TextMessage(json);
                            session.sendMessage(textMessage);
                        }
                    }
                }
                // TODO GAME CLOSE
            } else {
                for (WebSocketSession session : sessions) {
                    ScoreRule scoreRule = new ScoreRule();
                    scoreRule.setChatRoomId(gameRoom.getRoomId());
                    scoreRule.setType(MessageType.GAME_SCOPE.name());
                    scoreRule.setGame(userScores);
//          String gameScore = new Gson().toJson(gameMaterScoreRule);
                    String gameScore = objectMapper.writeValueAsString(scoreRule);
                    if (gameScore != null) {
                        TextMessage textMessage = new TextMessage(gameScore);
                        session.sendMessage(textMessage);
                    }
                }

            }

            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                log.info("Thread.sleep {}", e.getMessage());
            }
        }
    }
}