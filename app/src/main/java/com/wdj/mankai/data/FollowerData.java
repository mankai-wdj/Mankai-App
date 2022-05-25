package com.wdj.mankai.data;

import org.json.JSONObject;

public class FollowerData {

    String name;
    String profile;
    String id;

    public FollowerData(String name, String profile, String id){
        this.name = name;
        this.profile = profile;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
