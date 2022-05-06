package com.wdj.mankai.data.model;

import java.io.Serializable;

public class Message implements Serializable {
    public String messageId, userId, roomId,messageType, message, created_at, user;

    public Message(String messageId, String userId, String roomId, String messageType, String message, String created_at, String user) {
        this.messageId = messageId;
        this.userId = userId;
        this.roomId = roomId;
        this.messageType = messageType;
        this.message = message;
        this.created_at = created_at;
        this.user = user;

    }
}
