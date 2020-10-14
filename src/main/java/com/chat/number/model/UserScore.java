package com.chat.number.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserScore {
  String user;
  int score;

  public UserScore(String user, int score) {
    this.user = user;
    this.score = score;
  }
}
