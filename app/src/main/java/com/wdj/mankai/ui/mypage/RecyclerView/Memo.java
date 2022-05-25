package com.wdj.mankai.ui.mypage.RecyclerView;

import java.util.ArrayList;

public class Memo {
    int memo_id;
    boolean visibility;
    int viewType;
    String memo_title;
    String memo_content_text;
    ArrayList<String> imageUrl;

    public Memo(int memo_id, int viewType, String memo_title, String memo_content_text, ArrayList<String> imageUrl){
       this.memo_id = memo_id;
       this.visibility = false;
       this.viewType = viewType;
       this.memo_title = memo_title;
       this.memo_content_text = memo_content_text;
       this.imageUrl = imageUrl;
    }

    public int getMemo_id() {
        return memo_id;
    }

    public void setMemo_id(int memo_id) {
        this.memo_id = memo_id;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getMemo_content_text() {
        return memo_content_text;
    }

    public void setMemo_content_text(String memo_content_text) {
        this.memo_content_text = memo_content_text;
    }

    public ArrayList<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(ArrayList<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMemo_title() {
        return memo_title;
    }

    public void setMemo_title(String memo_title) {
        this.memo_title = memo_title;
    }
}
