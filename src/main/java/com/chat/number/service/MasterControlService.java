package com.chat.number.service;

import com.chat.number.domain.GameRoom;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

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
//    Set<WebSocketSession> sessions = this.gameRoom.getSessions();
//    Map<String, GameUser> writeUser = this.gameRoom.getWriteUser();
//
//    int count = 0;
//
//    // game master는 항시 동작. geme play를 check.
//    while (true) {
//
//      count++;
//
//      // 1. 전체 Check 판을 체크합니다.
//      Iterator<WebSocketSession> i = sessions.iterator();
//      List<String> writerList = new ArrayList<>();
//      String gameRule = objectMapper.writeValueAsString(new Date().getTime() + " -_-");
//      TextMessage textMessage = new TextMessage(gameRule);
//
//
//      boolean removeCheck = false;
//      while (i.hasNext()) {
//        WebSocketSession sess = i.next(); // must be called before you can call i.remove()
//        try {
//          sess.sendMessage(textMessage);
//        } catch (Exception e) {
//          removeCheck = true;
//          writerList.add(writeUser.get(sess.getId()).getUsername());
//          i.remove();
//        }
//      }
//
//
//      // 2. score 를 관리합니다.
//
//
//
//
//
//
//      // 3. 나간 User 체크
//      if (removeCheck) {
//        for (WebSocketSession sess : sessions) {
//          for (String writer : writerList) {
//            String json = objectMapper.writeValueAsString(writer + " bye");
//            if (json != null) {
//              textMessage = new TextMessage(json);
//              log.info(json);
//              sess.sendMessage(textMessage);
//            }
//          }
//        }
//      }
//
//      try {
//        Thread.sleep(1000L);
//      } catch (Exception e) {
//        log.info("Thread.sleep {}", e.getMessage());
//      }
//    }
  }
}