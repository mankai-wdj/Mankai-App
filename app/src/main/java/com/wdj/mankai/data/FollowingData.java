package com.wdj.mankai.data;

public class FollowingData {

    String name;
    String profile;
    String id;

    public FollowingData(String name, String profile, String id){
        this.name = name;
        this.profile = profile;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
