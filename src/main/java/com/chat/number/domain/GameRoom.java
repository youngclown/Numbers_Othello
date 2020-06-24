package com.chat.number.domain;

import com.chat.number.model.GameUser;
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

    private Set<WebSocketSession> sessions = new HashSet<>();   // 세션 유저 정보
    private Map<String, GameUser> writeUser = new HashMap<>();     // 실제 유저 정보
}
