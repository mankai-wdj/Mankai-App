package com.wdj.mankai.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

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
            this.jsonData.put("comment_length","0");
            this.jsonData.put("like_length","0");
            this.jsonData.put("translate_text","");

            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("comments","댓글이 없습니다.");
            jsonArray.put(jsonObject);
            this.jsonData.put("comments",jsonArray);
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
    public String getTranslateText() throws JSONException {
        return jsonData.getString("translate_text");
    }
    public void setTranslateText(String translate_text) throws JSONException {
        this.jsonData.put("translate_text",translate_text);
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
    public void setIsGroup(String isGroup) throws  JSONException{
        this.jsonData.put("isGroup",isGroup);
    }
    public String getIsGroup() throws  JSONException{
        return jsonData.getString("isGroup");
    }

    public void setUpdated_at(String updated_at)throws JSONException  {
        this.jsonData.put("updated_at",updated_at);
    }
    public String getComment_length() throws JSONException {
        return jsonData.getString("comment_length");
    }
    public void setComment_length(String comment_length)throws JSONException  {
        this.jsonData.put("comment_length",comment_length);
    }
    public String getLike_length() throws JSONException {
        return jsonData.getString("like_length");
    }
    public void setLike_length(String like_length)throws JSONException  {
        this.jsonData.put("like_length",like_length);
    }

    public ArrayList<String> getBoardImage() throws JSONException {
        ArrayList<String> cntString = new ArrayList<String>();
        JSONArray images = jsonData.getJSONArray("board_image");
        for(int i = 0 ; i< images.length() ; i++){
            Log.d("Image", "getBoardImage: " +images.getString(i));
            cntString.add(images.getString(i));
        }
        return cntString;
    }
    public void setBoardImage(ArrayList<String> board_image)throws JSONException  {
        JSONArray jsonArray = new JSONArray();
        for(int i = 0 ;i<board_image.size();i++){
            jsonArray.put(board_image.get(i));
            Log.d("Image", "setBoardImage:" + board_image.get(i));
        }
        this.jsonData.put("board_image",jsonArray);
    }
    public int getViewType() throws JSONException {
        return jsonData.getInt("viewType");
    }
    public void setViewType(int viewType)throws JSONException  {
        this.jsonData.put("viewType",viewType);
    }
    public ArrayList<String> getComments() throws JSONException {
        JSONArray jsonArray = jsonData.getJSONArray("comments");
        ArrayList<String> arrayList = new ArrayList<String>();
        for(int i = 0 ;i < jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            arrayList.add(jsonObject.getString("comments"));
        }
        return arrayList;
    }
    public void setComments(ArrayList<String> comments)throws JSONException  {

        JSONArray jsonArray = new JSONArray();

        for(int i = 0 ;i<comments.size();i++){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("comments",comments.get(i));
            jsonArray.put(i,jsonObject);
        }

        this.jsonData.put("comments",jsonArray);
    }


}
