package com.chat.number.service;

import com.chat.number.domain.ChatMessage;
import com.chat.number.domain.GameRoom;
import com.chat.number.model.GameMaterGameRule;
import com.chat.number.model.GameUser;
import com.chat.number.type.MessageType;
import com.chat.number.type.NumberOthelloType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

//import com.fasterxml.jackson.core.JsonProcessingException;

@Service
@Slf4j
public class MessageCheckService {

  final RedisService redisService;

  public MessageCheckService(RedisService redisService) {
    this.redisService = redisService;
  }

  public void handleMessage(GameRoom gameRoom, WebSocketSession session, ChatMessage chatMessage, ObjectMapper objectMapper) throws Exception {
    if(chatMessage.getType() == MessageType.ENTER){
      gameRoom.getSessions().add(session);
      gameRoom.setRoomUserCount(gameRoom.getRoomUserCount()+1);
      gameRoom.getWriteUser().put(
              session.getId(),
              new GameUser(
                      chatMessage.getName(),
                      gameRoom.getRoomUserCount() == 0 ?
                              NumberOthelloType.PLAYER_ONE.getValue() :
                              NumberOthelloType.PLAYER_TWO.getValue()));
      chatMessage.setMessage(chatMessage.getName() + " game join");
    } else if(chatMessage.getType() == MessageType.CHAT){
      chatMessage.setMessage(chatMessage.getName() + " : " + chatMessage.getMessage());
    } else if (chatMessage.getType() == MessageType.GAME) {

      String gameRule = (String) chatMessage.getMessage();
      String[] number = gameRule.split("##");
      String numberOthello = number[0];
      String numberChoice = number[1];

      GameMaterGameRule gameMaterRuleJson = new GameMaterGameRule();
      GameUser gameUser = gameRoom.getWriteUser().get(session.getId());
      GamePlayService gamePlayService = gameRoom.getGamePlayService();
      gamePlayService.gamePlay(numberOthello, numberChoice, gameUser.getType());

      gameMaterRuleJson.setChatRoomId(gameRoom.getRoomId());
      gameMaterRuleJson.setGame(gamePlayService.getOthelloList());
      gameMaterRuleJson.setType(MessageType.GAME.name());
      gameMaterRuleJson.setWriter(gameUser.getUsername());
      chatMessage.setMessage(gameMaterRuleJson);
    }
    send(gameRoom, chatMessage, objectMapper);
  }

  public void send(GameRoom gameRoom, ChatMessage chatMessage, ObjectMapper objectMapper)  {
    try {
      TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(chatMessage.getMessage()));
      for (WebSocketSession sess : gameRoom.getSessions()) {
        try {
          sess.sendMessage(textMessage);
        } catch (Exception e) {
          log.info(e.getMessage(),e);
        }
      }
    } catch (Exception e) {
      log.info(e.getMessage(),e);
    }
  }
}
