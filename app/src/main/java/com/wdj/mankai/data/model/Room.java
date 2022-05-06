package com.wdj.mankai.data.model;

import java.io.Serializable;

public class Room implements Serializable{
    public String roomId;
    public String title;
    public String last_message;
    public String type;
    public String users;
    public String updated_at;

    public Room (String roomId, String title, String last_message, String type, String users, String updated_at) {
        this.roomId = roomId;
        this.title = title;
        this.last_message = last_message;
        this.type = type;
        this.users = users;
        this.updated_at = updated_at;
    }
}
