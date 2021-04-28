package com.chat.number.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ScoreRule {

    String chatRoomId;
    String type;
    String writer;
    List<UserScore> game;


}
