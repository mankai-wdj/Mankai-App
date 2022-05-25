package com.wdj.mankai.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class GroupNotices implements Serializable{

    //Group notice 그룹 공지사항 Type / name 목록
    // String id;
    // String user_id;
    // String group_id;
    // String category;
    // String title;
    // String content;
    // String created_at;
    // String updated_at;

    JSONObject jsonGnotice;

    public GroupNotices(JSONObject jsonGnotice) {this.jsonGnotice = jsonGnotice;}

    public GroupNotices() {

    }

    public String getId() throws JSONException{
        return jsonGnotice.getString("id");
    }

    public void setId(String id) throws JSONException{
        this.jsonGnotice.put("id",id);
    }

    public String getUser_id() throws JSONException{
        return jsonGnotice.getString("user_id");
    }

    public void setUser_id(String user_id) throws JSONException{
        this.jsonGnotice.put("user_id",user_id);
    }

    public String getGroup_id() throws JSONException{
        return jsonGnotice.getString("group_id");
    }

    public void setGroup_id(String group_id) throws JSONException{
        this.jsonGnotice.put("group_id",group_id);
    }

    public String getCategory() throws JSONException{
        return jsonGnotice.getString("category");
    }

    public void setCategory(String category) throws JSONException{
        this.jsonGnotice.put("category",category);
    }

    public String getTitle() throws JSONException{
        return jsonGnotice.getString("title");
    }

    public void setTitle(String title) throws JSONException{
        this.jsonGnotice.put("title",title);
    }

    public String getContent() throws JSONException{
        return jsonGnotice.getString("content");
    }

    public void setContent(String content) throws JSONException{
        this.jsonGnotice.put("content",content);
    }

    public String getCreated_at() throws JSONException {
        return jsonGnotice.getString("created_at");
    }

    public void setCreated_at(String created_at) throws JSONException {
        this.jsonGnotice.put("created_at",created_at);
    }

    public String getUpdated_at() throws JSONException {
        return jsonGnotice.getString("updated_at");
    }

    public void setUpdated_at(String updated_at)throws JSONException {
        this.jsonGnotice.put("updated_at", updated_at);
    }

    public JSONObject getJsonGnotice() {
        return jsonGnotice;
    }

}















