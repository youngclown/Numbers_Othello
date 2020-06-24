package com.chat.number.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

  final RedisTemplate<String, Object> redisTemplate;

  public RedisService(RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public void saveGameRule(String key, String value) {
    //get/set을 위한 객체
    ValueOperations<String, Object> vop = redisTemplate.opsForValue();
    vop.set(key, value);
  }
}
