package com.chat.number.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GameUser {
    String userName;
    String type;
    int score;
    boolean ready;

    // Jackson 역직렬화를 위한 기본 생성자
    public GameUser() {
    }

    // Builder를 위한 모든 필드 생성자
    public GameUser(String userName, String type, int score, boolean ready) {
        this.userName = userName;
        this.type = type;
        this.score = score;
        this.ready = ready;
    }
}
