package com.wdj.mankai.data.model;

import java.util.ArrayList;

public class ChatMemo {
    int id;
    String type;
    String user_id;
    String memo_title;
    String content_text;
    ArrayList<String> imageUrl;

    public ChatMemo(int id, String type, String memo_title, String user_id, String content_text, ArrayList<String> imageUrl){
        this.id = id;
        this.type = type;
        this.user_id = user_id;
        this.memo_title = memo_title;
        this.content_text = content_text;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent_text() {
        return content_text;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setContent_text(String content_text) {
        this.content_text = content_text;
    }

    public ArrayList<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(ArrayList<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMemo_title() {
        return memo_title;
    }

    public void setMemo_title(String memo_title) {
        this.memo_title = memo_title;
    }
}
