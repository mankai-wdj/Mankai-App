package com.wdj.mankai.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class BoardData implements Serializable {

    //    BoardData 데이터 type / name 목록
    //    String id;
    //    String user_id;
    //    String name;
    //    String profile;
    //    String content_text;
    //    String category;
    //    String created_at;
    //    String updated_at;

    JSONObject jsonData;

    public BoardData(JSONObject jsonData){
        this.jsonData = jsonData;
        try {
            this.jsonData.put("viewType",0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public BoardData(){

    }
    public String getId() throws JSONException {
        return jsonData.getString("id");
    }

    public void setId(String id) throws JSONException {
        this.jsonData.put("id",id);
    }

    public String getUser_id() throws JSONException {
        return jsonData.getString("user_id");
    }

    public void setUser_id(String user_id)throws JSONException  {
        this.jsonData.put("user_id",user_id);
    }

    public String getName() throws JSONException {
        return jsonData.getString("name");
    }

    public void setName(String name) throws JSONException {
        this.jsonData.put("name",name);
    }

    public String getProfile() throws JSONException {
        return jsonData.getString("profile");
    }

    public void setProfile(String profile)throws JSONException  {
        this.jsonData.put("profile",profile);
    }

    public String getContent_text() throws JSONException {
        return jsonData.getString("content_text");
    }

    public void setContent_text(String content_text) throws JSONException {
        this.jsonData.put("content_text",content_text);
    }

    public String getCategory() throws JSONException {

        return jsonData.getString("category");
    }

    public void setCategory(String category) throws JSONException {
        this.jsonData.put("category",category);
    }

    public String getCreated_at() throws JSONException {
        return jsonData.getString("created_at");
    }

    public void setCreated_at(String created_at) throws JSONException {
        this.jsonData.put("created_at",created_at);
    }

    public String getUpdated_at() throws JSONException {
        return jsonData.getString("updated_at");
    }

    public void setUpdated_at(String updated_at)throws JSONException  {
        this.jsonData.put("updated_at",updated_at);
    }
    public String getBoardImage() throws JSONException {
        return jsonData.getString("board_image");
    }
    public void setBoardImage(String board_image)throws JSONException  {
        this.jsonData.put("board_image",board_image);
    }
    public int getViewType() throws JSONException {
        return jsonData.getInt("viewType");
    }
    public void setViewType(int viewType)throws JSONException  {
        this.jsonData.put("viewType",viewType);
    }
}
