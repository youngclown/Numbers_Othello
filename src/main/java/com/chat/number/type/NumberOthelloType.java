package com.chat.number.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NumberOthelloType {
  PLAYER_ONE("1"),       // 플레이1
  PLAYER_TWO("2"),       // 플레이2
  BLANK("x"),            // 빈화면
  PLAYER_ONE_BLOCK("x1"), // 플레이1의 배경
  PLAYER_TWO_BLOCK("x2");  // 플레이2의 배경
  private final String value;
}
