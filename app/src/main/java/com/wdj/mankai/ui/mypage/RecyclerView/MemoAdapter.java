package com.wdj.mankai.ui.mypage.RecyclerView;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wdj.mankai.R;

import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {

    Context mContext;
    ArrayList<Memo> memos = new ArrayList<>();

    public MemoAdapter (Context mContext){
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MemoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.memo_item,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoAdapter.ViewHolder holder, int position) {
        Memo memo = memos.get(position);
        holder.setItem(memo);
    }

    @Override
    public int getItemCount() {
        return memos.size();
    }

    public void addMemo(Memo memo){
        memos.add(memo);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
    TextView memo_title;
    TextView memo_preview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            memo_title = itemView.findViewById(R.id.memo_title);
            memo_preview = itemView.findViewById(R.id.memo_preview);
        }

        public void setItem(Memo memo){
            memo_title.setText(memo.memo_title);
            memo_preview.setText(memo.memo_preview);
        }
    }
}
