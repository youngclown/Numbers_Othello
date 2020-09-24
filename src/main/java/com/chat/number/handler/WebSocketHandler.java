package com.chat.number.handler;

import com.chat.number.domain.ChatMessage;
import com.chat.number.domain.GameRoom;
import com.chat.number.repository.GameRoomRepository;
import com.chat.number.service.MessageCheckService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    private final GameRoomRepository gameRoomRepository;
    private final ObjectMapper objectMapper;
    private final MessageCheckService playService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }

    @SneakyThrows
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("메세지 전송 = {} : {}",session,message.getPayload());
        String msg = message.getPayload();
        ChatMessage chatMessage = objectMapper.readValue(msg,ChatMessage.class);
        GameRoom gameRoom = gameRoomRepository.findRoomById(chatMessage.getChatRoomId());
        playService.handleMessage(gameRoom, session,chatMessage,objectMapper);
    }

}