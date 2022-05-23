package com.wdj.mankai.ui.Group;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wdj.mankai.R;
import com.wdj.mankai.data.GroupNotices;

import org.json.JSONException;

import java.util.ArrayList;

public class FragNoticeAdapter extends RecyclerView.Adapter<FragNoticeAdapter.ViewHolder> {

    private ArrayList<GroupNotices> gNotice = null;
    private  Context mContext;
    public FragNoticeAdapter(Context mContext , ArrayList list) {
        this.mContext = mContext;
        this.gNotice = list;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
        // layout ID
        TextView group_info_notice_id = itemView.findViewById(R.id.group_info_notice_id);
        TextView group_info_notice_title = itemView.findViewById(R.id.group_info_notice_title);
        TextView group_info_notice_time = itemView.findViewById(R.id.group_info_notice_time);
    }

    FragNoticeAdapter(ArrayList<GroupNotices> list) {gNotice = list;}



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        view = inflater.inflate(R.layout.group_notice_list,parent,false);
        FragNoticeAdapter.ViewHolder gv = new FragNoticeAdapter.ViewHolder(view);

        return gv;
    }



    @Override
    public void onBindViewHolder(FragNoticeAdapter.ViewHolder holder, int position) {
        GroupNotices groupNotices = gNotice.get(position);
        try {
            holder.group_info_notice_id.setText(groupNotices.getId());
            holder.group_info_notice_title.setText(groupNotices.getTitle());
            holder.group_info_notice_time.setText(groupNotices.getCreated_at());
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return gNotice.size();
    }


}
