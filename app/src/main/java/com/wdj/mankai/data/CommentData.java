package com.wdj.mankai.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class CommentData implements Serializable {

    JSONObject jsonData = new JSONObject();

    public CommentData(JSONObject jsonObject) {
        this.jsonData=jsonObject;
    }

//    private String id ;
//    private String comment;
//    private String name;
//    private String profile;

    public JSONObject getJsonData() {
        return jsonData;
    }

    public void setJsonData(JSONObject jsonData) {
        this.jsonData = jsonData;
    }

    public String getId() {
        try {
            return jsonData.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void setId(String id) {
        try {
            this.jsonData.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getComment() {
        try {
            return jsonData.getString("comment");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setComment(String comment) {
        try {
            this.jsonData.put("comment",comment);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        try {
            return jsonData.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setName(String name) {
        try {
            this.jsonData.put("name",name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getProfile() {
        try {
            Log.d("Board", jsonData.getString("profile"));
            return jsonData.getString("profile");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setProfile(String profile) {
        try {
            this.jsonData.put("profile",profile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getUser_id() {
        try {
            return jsonData.getString("user_id");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setUser_id(String user_id) {
        try {
            this.jsonData.put("user_id",user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
