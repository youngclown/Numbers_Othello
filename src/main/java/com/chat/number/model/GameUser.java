package com.chat.number.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameUser {
  String username;
  String type;
  int score;

//  int one, two = 4;
//  int three, four, five, six  = 3;
//  int seven  = 4;

  public GameUser(String username, String type) {
    this.username = username;
    this.type = type;
  }
}
