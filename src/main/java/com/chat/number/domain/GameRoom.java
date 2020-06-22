package com.chat.number.domain;

import com.chat.number.service.GameMasterMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

@Slf4j
@Getter @Setter
public class GameRoom {
    private String roomId;
    private String name;
    private boolean gameStart = false;

    private Set<WebSocketSession> sessions = new HashSet<>();
    private Map<String,String> writeUser = new HashMap<>();

    // 넘어온 이름으로 게임 생성
    public static GameRoom create(String name){
        GameRoom gameRoom = new GameRoom();
        gameRoom.roomId = UUID.randomUUID().toString();
        gameRoom.name = name;

        GameMasterMessage t = new GameMasterMessage(gameRoom.getSessions(), gameRoom.getWriteUser());
        t.start();
        return gameRoom;
    }
}
