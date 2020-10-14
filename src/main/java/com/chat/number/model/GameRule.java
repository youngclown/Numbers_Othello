package com.chat.number.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GameRule {

  String chatRoomId;
  String type;
  String writer;
  List<NumberOthello> game;

}
