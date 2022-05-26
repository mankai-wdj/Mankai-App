package com.wdj.mankai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wdj.mankai.R;
import com.wdj.mankai.data.model.ChatMemo;
import com.wdj.mankai.ui.chat.MemoListActivity;

import java.util.ArrayList;


public class MemosAdapter extends RecyclerView.Adapter<MemosAdapter.ViewHolder>{
    Context mContext;
    public static ArrayList<ChatMemo> memos = new ArrayList<ChatMemo>();
    public static ArrayList<ChatMemo> checkMemos = new ArrayList<ChatMemo>();
    MemoListActivity memoListActivity = new MemoListActivity();

    public MemosAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addMemo(ChatMemo memo) {
        memos.add(memo);
    }
    @NonNull
    @Override
    public MemosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.memo_list_item, parent, false);

        return new MemosAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MemosAdapter.ViewHolder holder, int position) {
        ChatMemo memo = memos.get(position);
        holder.setItem(memo);
    }

    @Override
    public int getItemCount() {
        return memos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMemoTitle;
        CheckBox memoCheckBox;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMemoTitle = itemView.findViewById(R.id.memoTitle);
            memoCheckBox = itemView.findViewById(R.id.memoCheckBox);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();

                }
            });
            memoCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (memoCheckBox.isChecked()) {

                        checkMemos.add(memos.get(getAdapterPosition()));
                    } else {
                        checkMemos.remove(memos.get(getAdapterPosition()));
                    }
                    System.out.println(checkMemos);
                    if(checkMemos.size() >= 1) {
                        memoListActivity.btSendMemo.setEnabled(true);
                    }else {
                        memoListActivity.btSendMemo.setEnabled(false);
                    }
//                    System.out.println(followings.get(getAdapterPosition()));
                }
            });

        }

        public void setItem(ChatMemo memo) {
            tvMemoTitle.setText(memo.getMemo_title());

        }
    }
}
