package com.chat.number.service;

import com.chat.number.domain.ChatMessage;
import com.chat.number.domain.GameRoom;
import com.chat.number.type.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class PlayService {

  public void handleMessage(GameRoom gameRoom, WebSocketSession session, ChatMessage chatMessage, ObjectMapper objectMapper) throws IOException {
    if(chatMessage.getType() == MessageType.ENTER){
      gameRoom.getSessions().add(session);
      gameRoom.getWriteUser().put(session.getId(),chatMessage.getWriter());
      chatMessage.setMessage(chatMessage.getWriter() + "님이 입장하셨습니다.");
    } else if(chatMessage.getType() == MessageType.LEAVE){
      gameRoom.getSessions().remove(session);
      gameRoom.getWriteUser().put(session.getId(),chatMessage.getWriter());

      chatMessage.setMessage(chatMessage.getWriter() + "님이 퇴장하셨습니다.");
    } else if(chatMessage.getType() == MessageType.CHAT){
      chatMessage.setMessage(chatMessage.getWriter() + " : " + chatMessage.getMessage());
    } else if (chatMessage.getType() == MessageType.GAME) {
      String gameRule = "GAME^^1";
      chatMessage.setMessage(gameRule);
    }
    send(gameRoom, chatMessage,objectMapper);
  }

  public void send(GameRoom gameRoom, ChatMessage chatMessage, ObjectMapper objectMapper) throws IOException {
    TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(chatMessage.getMessage()));
    Iterator<WebSocketSession> i = gameRoom.getSessions().iterator();
    
    
    // 세션 체크 및 나간 유저 체크
    List<String> writerList = new ArrayList<>();
    if (messageCheckPush(i, textMessage, writerList, gameRoom.getWriteUser())) {
      for (WebSocketSession sess:gameRoom.getSessions()) {
        for (String writer:writerList) {
          textMessage = new TextMessage(objectMapper.writeValueAsString(writer + "님이 퇴장하셨습니다."));
          sess.sendMessage(textMessage);
        }
      }
    }
  }

  public static boolean messageCheckPush (Iterator<WebSocketSession> i, TextMessage textMessage, List<String> writerList, Map<String,String> writeUser) {
    boolean removeCheck = false;
    while (i.hasNext()) {
      WebSocketSession sess = i.next(); // must be called before you can call i.remove()
      try {
        sess.sendMessage(textMessage);
      } catch (Exception e) {
        removeCheck = true;
        writerList.add(writeUser.get(sess.getId()));
        i.remove();
      }
    }

    return removeCheck;
  }
}
