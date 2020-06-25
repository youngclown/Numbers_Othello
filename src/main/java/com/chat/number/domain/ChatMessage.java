package com.chat.number.domain;

import com.chat.number.type.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatMessage {
    private String chatRoomId;
    private String name;
    private String message;
    private MessageType type;
}
