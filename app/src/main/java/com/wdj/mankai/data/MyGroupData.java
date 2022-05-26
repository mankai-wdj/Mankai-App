package com.wdj.mankai.data;

public class MyGroupData {
     String id;
     String name;
     String onelineintro;
     String category;
     String logoImage;

    public MyGroupData(String id, String name, String onelineintro, String category, String logoImage){
        this.id = id;
        this.name = name;
        this.onelineintro =onelineintro;
        this.category = category;
        this.logoImage = logoImage;
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

    public String getOnelineintro() {
        return onelineintro;
    }

    public void setOnelineintro(String onelineintro) {
        this.onelineintro = onelineintro;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }


}
