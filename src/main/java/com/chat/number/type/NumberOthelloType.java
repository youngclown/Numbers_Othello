package com.chat.number.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NumberOthelloType {
  PLAYER_ONE("PO"),       // 플레이1
  PLAYER_TWO("PT"),       // 플레이2
  BLANK("B"),            // 빈화면
  PLAYER_ONE_BLOCK("POB"), // 플레이1의 배경
  PLAYER_TWO_BLOCK("PTB");  // 플레이2의 배경
  private final String value;
}
