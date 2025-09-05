package com.chat.number.repository;

import com.chat.number.domain.GameRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class GameRoomRepository {

    private final RedisTemplate<String, GameRoom> gameRoomRedisTemplate;
    private final RedisTemplate<String, String> stringRedisTemplate;
    private static final String ROOMS_KEY = "gamerooms:active";

    public GameRoomRepository(RedisTemplate<String, GameRoom> gameRoomRedisTemplate, RedisTemplate<String, String> stringRedisTemplate) {
        this.gameRoomRedisTemplate = gameRoomRedisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    private String getRoomKey(String roomId) {
        return "gameroom:" + roomId;
    }

    public List<GameRoom> findAllRoom() {
        Set<String> roomIds = stringRedisTemplate.opsForSet().members(ROOMS_KEY);
        if (roomIds == null || roomIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<GameRoom> rooms = gameRoomRedisTemplate.opsForValue().multiGet(
                roomIds.stream().map(this::getRoomKey).collect(Collectors.toList())
        );
        return rooms.stream().filter(Objects::nonNull).sorted(Comparator.comparing(GameRoom::getRoomId).reversed()).collect(Collectors.toList());
    }

    public GameRoom findRoomById(String id) {
        return gameRoomRedisTemplate.opsForValue().get(getRoomKey(id));
    }

    public GameRoom createChatRoom(String name) {
        GameRoom gameRoom = new GameRoom();
        gameRoom.setRoomId(UUID.randomUUID().toString());
        gameRoom.setName(name);

        gameRoomRedisTemplate.opsForValue().set(getRoomKey(gameRoom.getRoomId()), gameRoom);
        stringRedisTemplate.opsForSet().add(ROOMS_KEY, gameRoom.getRoomId());

        return gameRoom;
    }

    public void save(GameRoom gameRoom) {
        gameRoomRedisTemplate.opsForValue().set(getRoomKey(gameRoom.getRoomId()), gameRoom);
    }

    public void deleteRoom(String roomId) {
        gameRoomRedisTemplate.delete(getRoomKey(roomId));
        stringRedisTemplate.opsForSet().remove(ROOMS_KEY, roomId);
    }
}
