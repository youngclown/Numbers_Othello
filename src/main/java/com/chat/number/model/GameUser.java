package com.chat.number.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class GameUser {
    String username;
    String type;
    @Setter int score;
    @Setter boolean ready;

    @Builder
    public GameUser(String username, String type) {
        this.username = username;
        this.type = type;
    }


}
