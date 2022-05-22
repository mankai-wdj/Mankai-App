package com.wdj.mankai.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class GroupData implements Serializable {

    //GroupData 데이터 Type / name 목록
    // String id;
    // String name;
    // String intro;
    // String onelineintro;
    // String category;
    // String password;
    // String created_at;
    // String updated_at;
    // String logoImage

    JSONObject jsonGData;

    public GroupData(JSONObject jsonGData){
        this.jsonGData = jsonGData;
    }

    public GroupData(String id, String name, String onelineintro, String category, String logoImage) {

    }

    public String getId() throws JSONException{
        return jsonGData.getString("id");
    }

    public void setId(String id) throws JSONException{
        this.jsonGData.put("id",id);
    }

    public String getName() throws JSONException{
        return jsonGData.getString("name");
    }

    public void setName(String name) throws JSONException{
        this.jsonGData.put("name",name);
    }

    public String getIntro() throws JSONException{
        return jsonGData.getString("intro");
    }

    public void setIntro(String intro) throws JSONException{
        this.jsonGData.put("intro",intro);
    }

    public String getOnelineintro() throws JSONException{
        return jsonGData.getString("onelineintro");
    }

    public void setOnelineintro(String onelineintro) throws JSONException{
        this.jsonGData.put("onelineintro", onelineintro);
    }

    public String getCategory() throws JSONException{
        return jsonGData.getString("category");
    }

    public void setCategory(String category) throws JSONException{
        this.jsonGData.put("category", category);
    }

    public String getPassword() throws JSONException{
        return jsonGData.getString("password");
    }

    public void setPassword(String password) throws JSONException{
        this.jsonGData.put("password", password);
    }

    public String getCreated_at() throws JSONException {
        return jsonGData.getString("created_at");
    }

    public void setCreated_at(String created_at) throws JSONException {
        this.jsonGData.put("created_at",created_at);
    }

    public String getUpdated_at() throws JSONException {
        return jsonGData.getString("updated_at");
    }

    public void setUpdated_at(String updated_at)throws JSONException {
        this.jsonGData.put("updated_at", updated_at);
    }

    public JSONObject getJsonGData() {
        return jsonGData;
    }

    public void setJsonGData(JSONObject jsonGData) {
        this.jsonGData = jsonGData;
    }
    public String getLogoImage(){
        try {
            return jsonGData.getString("logoImage");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void setLogoImage(String logoImage){
        try {
            this.jsonGData.put("logoImage",logoImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
