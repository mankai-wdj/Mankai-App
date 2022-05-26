package com.wdj.mankai.data.model;

import java.io.Serializable;

public class Message implements Serializable {
    public String messageId, userId, roomId,messageType, message, created_at, user, translation;
    private int viewType;
    public Message(String messageId, String userId, String roomId, String messageType, String message, String created_at, String user , int viewType) {
        this.messageId = messageId;
        this.userId = userId;
        this.roomId = roomId;
        this.messageType = messageType;
        this.message = message;
        this.created_at = created_at;
        this.user = user;
        this.viewType = viewType;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getViewType() {
        return viewType;
    }
}
