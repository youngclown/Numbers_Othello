package com.chat.number.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

//
@Slf4j
public class GameMasterMessage extends Thread {
  Set<WebSocketSession> sessions;
  Map<String,String> writeUser;
  public GameMasterMessage(Set<WebSocketSession> sessions, Map<String,String> writeUser) {
    this.sessions = sessions;
    this.writeUser = writeUser;
  }

  public void run()
  {
    // game master는 항시 동작. geme play를 check.
    while (true) {
      Iterator<WebSocketSession> i = sessions.iterator();
      List<String> writerList = new ArrayList<>();
      TextMessage textMessage = new TextMessage((new Date()).toString());
      if (PlayService.messageCheckPush(i, textMessage, writerList, writeUser)) {
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