package com.wdj.mankai.data;

import java.io.Serializable;

public class MemoData implements Serializable {

    String title;
    String content;

    public MemoData(String title,String content){
        this.title=title;
        this.content=content;
    }
    public MemoData(){

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
