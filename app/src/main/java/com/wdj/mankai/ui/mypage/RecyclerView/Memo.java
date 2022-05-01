package com.wdj.mankai.ui.mypage.RecyclerView;

public class Memo {
    int memoId;
    /*memoId가 int일수도 있고 String일수도 있음*/
    String memo_title;
    String memo_preview;

    public Memo(int memoId, String memo_title, String memo_preview){
        this.memoId = memoId;
        this.memo_title = memo_title;
        this.memo_preview = memo_preview;
    }

}
