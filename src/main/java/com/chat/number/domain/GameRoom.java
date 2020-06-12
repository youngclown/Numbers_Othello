package com.chat.number.domain;

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

    private static boolean messageCheckPush (Iterator<WebSocketSession> i, TextMessage textMessage, List<String> writerList, Map<String,String> writeUser) {
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
                if (messageCheckPush(i, textMessage, writerList, writeUser)) {
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



    public void handleMessage(WebSocketSession session, ChatMessage chatMessage, ObjectMapper objectMapper) throws IOException {
        if(chatMessage.getType() == MessageType.ENTER){
            System.out.println(session.getId());
            sessions.add(session);
            writeUser.put(session.getId(),chatMessage.getWriter());

            chatMessage.setMessage(chatMessage.getWriter() + "님이 입장하셨습니다.");
        } else if(chatMessage.getType() == MessageType.LEAVE){
            sessions.remove(session);
            writeUser.put(session.getId(),chatMessage.getWriter());

            chatMessage.setMessage(chatMessage.getWriter() + "님이 퇴장하셨습니다.");
        } else if(chatMessage.getType() == MessageType.CHAT){
            chatMessage.setMessage(chatMessage.getWriter() + " : " + chatMessage.getMessage());
        }
        send(chatMessage,objectMapper);
    }

    private void send(ChatMessage chatMessage, ObjectMapper objectMapper) throws IOException {
        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(chatMessage.getMessage()));
        Iterator<WebSocketSession> i = sessions.iterator();
        List<String> writerList = new ArrayList<>();
        if (messageCheckPush(i, textMessage, writerList, writeUser)) {
            for (WebSocketSession sess:sessions) {
                for (String writer:writerList) {
                    textMessage = new TextMessage(objectMapper.writeValueAsString(writer + "님이 퇴장하셨습니다."));
                    sess.sendMessage(textMessage);
                }
            }
        }
    }
}
