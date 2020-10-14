package com.chat.number.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
/*
 chatRoomId : '현재 접속한 룸 ID',
 type : 'MESSAGE', // CHAT, GAME, LEAVE, ENTER 등
 writer : 'USER_NM', // 사용자
 msg : '내용', // 내용
 */
public class GameMessage {
    String chatRoomId;
    String type;
    String writer;
    String msg;
}
