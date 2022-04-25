package com.wdj.mankai.data.model;

import java.io.Serializable;

public class Room  {
    public String title;
    public String last_message;

    public Room (String title, String last_message) {
        this.title = title;
        this.last_message = last_message;
    }
}
