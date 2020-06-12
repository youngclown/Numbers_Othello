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

    public List<GameRoom> findAllRoom(){
        List<GameRoom> gameRooms = new ArrayList<>(gameRoomMap.values());
        Collections.reverse(gameRooms);
        return gameRooms;
    }

    public GameRoom findRoomById(String id){
        return gameRoomMap.get(id);
    }

    public GameRoom createChatRoom(String name){
        GameRoom chatRoom = GameRoom.create(name);
        gameRoomMap.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }
}
