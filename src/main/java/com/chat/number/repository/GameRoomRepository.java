package com.chat.number.repository;

import com.chat.number.domain.GameRoom;
import com.chat.number.service.GameMasterMessage;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class GameRoomRepository {
    private Map<String, GameRoom> gameRoomMap;

    @PostConstruct
    private void init(){
        this.gameRoomMap = new LinkedHashMap<>();
    }

    // 방찾기 List
    public List<GameRoom> findAllRoom(){
        List<GameRoom> gameRooms = new ArrayList<>(this.gameRoomMap.values());
        Collections.reverse(gameRooms);
        return gameRooms;
    }

    // 방
    public GameRoom findRoomById(String id){
        return this.gameRoomMap.get(id);
    }

    // 방 생성
    public GameRoom createChatRoom(String name){
        GameRoom gameRoom = new GameRoom();
        gameRoom.setRoomId(UUID.randomUUID().toString());
        gameRoom.setName(name);

        // 방생성 후 게임 스타트, 게임 마스터 동작
        GameMasterMessage t = new GameMasterMessage(gameRoom);
        t.start();
        
        this.gameRoomMap.put(gameRoom.getRoomId(), gameRoom);
        return gameRoom;
    }
}
