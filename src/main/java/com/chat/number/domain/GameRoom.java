package com.chat.number.domain;

import com.chat.number.service.PlayService;
import com.chat.number.type.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Slf4j
@Getter @Setter
public class GameRoom {
    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();
    private Map<String,String> writeUser = new HashMap<>();


    public static GameRoom create(String name){
        GameRoom gameRoom = new GameRoom();
        gameRoom.roomId = UUID.randomUUID().toString();
        gameRoom.name = name;

        ChatPushMessage t = new ChatPushMessage(gameRoom.getSessions(), gameRoom.getWriteUser());
        t.start();

        return gameRoom;
    }

    static class ChatPushMessage extends Thread {
        Set<WebSocketSession> sessions;
        Map<String,String> writeUser;
        public ChatPushMessage(Set<WebSocketSession> sessions, Map<String,String> writeUser) {
            this.sessions = sessions;
            this.writeUser = writeUser;
        }

        public void run()
        {
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
}
