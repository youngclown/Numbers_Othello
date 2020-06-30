package com.chat.number.service;

import com.chat.number.domain.GameRoom;
import com.chat.number.model.GameUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

//
@Slf4j
public class MasterControlService extends Thread {
  GameRoom gameRoom;

  public MasterControlService(GameRoom gameRoom) {
    this.gameRoom = gameRoom;
  }

  public void run() {
    Set<WebSocketSession> sessions = this.gameRoom.getSessions();
    Map<String, GameUser> writeUser = this.gameRoom.getWriteUser();

    // game master는 항시 동작. geme play를 check.
    while (true) {
      Iterator<WebSocketSession> i = sessions.iterator();
      List<String> writerList = new ArrayList<>();
      String gameRule = "";

      TextMessage textMessage = new TextMessage(gameRule);
      boolean removeCheck = false;
      while (i.hasNext()) {
        WebSocketSession sess = i.next(); // must be called before you can call i.remove()
        try {
          sess.sendMessage(textMessage);
        } catch (Exception e) {
          removeCheck = true;
          writerList.add(writeUser.get(sess.getId()).getUsername());
          i.remove();
        }
      }

      if (removeCheck) {
        for (WebSocketSession sess : sessions) {
          for (String writer : writerList) {
            textMessage = new TextMessage(writer + " bye");
            try {
              sess.sendMessage(textMessage);
            } catch (IOException e) {
              log.info("removeCheck {}", e.getMessage());
            }
          }
        }
      }

      try {
        Thread.sleep(1000L);
      } catch (Exception e) {
        log.info("Thread.sleep {}", e.getMessage());
      }
    }
  }
}