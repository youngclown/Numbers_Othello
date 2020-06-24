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
public class GameMasterControlService extends Thread {
  GameRoom gameRoom;

  public GameMasterControlService(GameRoom gameRoom) {
    this.gameRoom = gameRoom;
  }

  public void run()
  {
    Set<WebSocketSession> sessions = this.gameRoom.getSessions();
    Map<String, GameUser> writeUser = this.gameRoom.getWriteUser();

    // game master는 항시 동작. geme play를 check.
    while (true) {
      Iterator<WebSocketSession> i = sessions.iterator();
      List<String> writerList = new ArrayList<>();
      TextMessage textMessage = new TextMessage((new Date()).toString());
      if (MessageCheckService.messageCheckPush(i, textMessage, writerList, writeUser)) {
        for (WebSocketSession sess:sessions) {
          for (String writer:writerList) {
            textMessage = new TextMessage(writer + "님이 퇴장하셨습니다.");
            try {
              sess.sendMessage(textMessage);
            } catch (IOException e) {
              log.info("removeCheck {}", e.getMessage());
            }
          }
        }
      }

      try {
        Thread.sleep(2000L);
      } catch (Exception e) {
        log.info("Thread.sleep {}", e.getMessage());
      }
    }
  }
}