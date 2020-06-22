package com.chat.number.repository;

import com.chat.number.domain.GameRoom;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class GameRoomRepository {
    private Map<String, GameRoom> gameRoomMap;

    @PostConstruct
    private void init(){
        gameRoomMap = new LinkedHashMap<>();
    }

    // 방찾기 List
    public List<GameRoom> findAllRoom(){
        List<GameRoom> gameRooms = new ArrayList<>(gameRoomMap.values());
        Collections.reverse(gameRooms);
        return gameRooms;
    }

    // 방
    public GameRoom findRoomById(String id){
        return gameRoomMap.get(id);
    }

    // 방 생성.
    public GameRoom createChatRoom(String name){
        GameRoom chatRoom = GameRoom.create(name);
        gameRoomMap.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }
}
