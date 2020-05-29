package com.chat.tabula.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Getter @Setter
public class ChatRoom {
    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();
    private Map<String,String> writeUser = new HashMap<>();

    public static ChatRoom create(String name){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;

        ChatPushMessage t = new ChatPushMessage(chatRoom.getSessions(), chatRoom.getWriteUser());
        t.start();

        return chatRoom;
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
                TextMessage textMessage;
                Iterator<WebSocketSession> i = sessions.iterator();
                List<String> writerList = new ArrayList<>();

                boolean removeCheck = false;
                while (i.hasNext()) {
                    WebSocketSession sess = i.next(); // must be called before you can call i.remove()
                    try {
                        textMessage = new TextMessage((new Date()).toString());
                        sess.sendMessage(textMessage);
                    } catch (Exception e) {
                        removeCheck = true;
                        writerList.add(writeUser.get(sess.getId()));
                        i.remove();

                    }
                }

                if (removeCheck) {
                    for (WebSocketSession sess:sessions) {
                        for (String writer:writerList) {
                            textMessage = new TextMessage(writer + "님이 퇴장하셨습니다.");
                            try {
                                sess.sendMessage(textMessage);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(2000L);
                } catch (Exception e) {
                    e.printStackTrace();
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

        if (removeCheck) {
            for (WebSocketSession sess:sessions) {
                for (String writer:writerList) {
                    textMessage = new TextMessage(objectMapper.writeValueAsString(writer + "님이 퇴장하셨습니다."));
                    sess.sendMessage(textMessage);
                }
            }
        }
    }






}
