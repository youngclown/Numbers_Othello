package com.chat.number.service;

import com.chat.number.model.GameUser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

  final RedisTemplate<String, Object> redisTemplate;

  public RedisService(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  // 사용자의
  public void saveGameRoomIdUser(String key, GameUser user) {
    //get/set을 위한 객체
    ValueOperations<String, Object> vop = redisTemplate.opsForValue();
    vop.set(key, user);
  }

  // 사용자의
  public GameUser getGameRoomIdUser(String key) {
    //get/set을 위한 객체
    ValueOperations<String, Object> vop = redisTemplate.opsForValue();
    GameUser user = (GameUser)vop.get(key);
    return user;
  }


}
