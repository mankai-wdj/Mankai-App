package com.wdj.mankai.data.model;

import java.io.Serializable;

public class User implements Serializable {
    public String id, name, email, profile, country, description, position;

    public User(String id, String name, String email, String profile, String country, String description, String position) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.country = country;
        this.description = description;
        this.position = position;
    }
}
