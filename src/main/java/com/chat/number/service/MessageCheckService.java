package com.chat.number.service;

import com.chat.number.domain.ChatMessage;
import com.chat.number.domain.GameRoom;
import com.chat.number.model.GameMessage;
import com.chat.number.model.GameRule;
import com.chat.number.model.GameUser;
import com.chat.number.type.MessageType;
import com.chat.number.type.NumberOthelloType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

//import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 메시지를 체크하여 해당 메시지에 해당하는 로직을 수행
 */
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
                      gameRoom.getRoomUserCount() == 1 ?
                              NumberOthelloType.PLAYER_ONE.getValue() :
                              NumberOthelloType.PLAYER_TWO.getValue()));

      chatMessage.setMessage(chatMessage.getName() + " game join");
    } else if(chatMessage.getType() == MessageType.CHAT){
      GameMessage gameMessage = new GameMessage();
      gameMessage.setChatRoomId(gameRoom.getRoomId());
      gameMessage.setMsg(chatMessage.getMessage().toString());
      gameMessage.setType(MessageType.CHAT.name());
      gameMessage.setWriter(chatMessage.getName());
    } else if (chatMessage.getType() == MessageType.GAME) {

      String gameRule = (String) chatMessage.getMessage();
      String[] number = gameRule.split("##");
      String numberOthello = number[0];
      String numberChoice = number[1];

      GameUser gameUser = gameRoom.getWriteUser().get(session.getId());
      GamePlayService gamePlayService = gameRoom.getGamePlayService();
      boolean check = gamePlayService.gamePlay(numberOthello, numberChoice, gameUser.getType());

      if (check) {
        GameRule gameMaterRuleJson = new GameRule();
        gameMaterRuleJson.setChatRoomId(gameRoom.getRoomId());
        gameMaterRuleJson.setGame(gamePlayService.getOthelloList());
        gameMaterRuleJson.setType(MessageType.GAME.name());
        gameMaterRuleJson.setWriter(gameUser.getUsername());
        chatMessage.setMessage(gameMaterRuleJson);
      } else {
        GameMessage message = new GameMessage();
        message.setChatRoomId(gameRoom.getRoomId());
        message.setMsg("그곳에 둘수는 없습니다.");
        message.setType(MessageType.MESSAGE.name());
        message.setWriter(gameUser.getUsername());
        chatMessage.setMessage(message);
      }
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
