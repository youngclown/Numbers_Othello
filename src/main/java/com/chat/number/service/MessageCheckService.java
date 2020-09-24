package com.chat.number.service;

import com.chat.number.domain.ChatMessage;
import com.chat.number.domain.GameRoom;
import com.chat.number.model.GameUser;
import com.chat.number.type.MessageType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
@Slf4j
public class MessageCheckService {

  final RedisService redisService;

  public MessageCheckService(RedisService redisService) {
    this.redisService = redisService;
  }

  public void handleMessage(GameRoom gameRoom, WebSocketSession session, ChatMessage chatMessage, ObjectMapper objectMapper) throws IOException {
    if(chatMessage.getType() == MessageType.ENTER){
      gameRoom.getSessions().add(session);
      gameRoom.getWriteUser().put(session.getId(),new GameUser(chatMessage.getName())); // TODO 정합성 맞추는 작업이 필요함.
      chatMessage.setMessage(chatMessage.getName() + " hello");
    }
    else if(chatMessage.getType() == MessageType.CHAT){
      chatMessage.setMessage(chatMessage.getName() + " : " + chatMessage.getMessage());
    } else if (chatMessage.getType() == MessageType.GAME) {
      gameRoom.setRuleChange(true);
    }
    send(gameRoom, chatMessage,objectMapper);
  }

  public void send(GameRoom gameRoom, ChatMessage chatMessage, ObjectMapper objectMapper) throws JsonProcessingException {
    TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(chatMessage.getMessage()));
    for (WebSocketSession sess : gameRoom.getSessions()) {
      try {
        sess.sendMessage(textMessage);
      } catch (Exception e) {
        log.info(e.getMessage(),e);
      }
    }
  }
}
