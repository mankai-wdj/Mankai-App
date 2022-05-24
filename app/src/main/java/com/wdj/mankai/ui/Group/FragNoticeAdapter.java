package com.wdj.mankai.ui.Group;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wdj.mankai.R;
import com.wdj.mankai.data.GroupNotices;
import com.wdj.mankai.ui.Board.BoardCommentActivity;

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
        LinearLayout group_layout = itemView.findViewById(R.id.noticelayout);
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
            holder.group_info_notice_id.setText(position+1+"");
            holder.group_info_notice_title.setText(groupNotices.getTitle());
            holder.group_info_notice_time.setText(groupNotices.getCreated_at());

            holder.group_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(view.getContext(), GroupNoticeShow.class);
                        try {
                            intent.putExtra("id",groupNotices.getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Bundle bundle = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.slide_in_right,R.anim.slide_wait).toBundle();
                        view.getContext().startActivity(intent, bundle);

                        Log.d("Group", "onClick: "+groupNotices.getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return gNotice.size();
    }


}
