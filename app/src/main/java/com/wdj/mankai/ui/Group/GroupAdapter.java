package com.wdj.mankai.ui.Group;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wdj.mankai.R;
import com.wdj.mankai.data.GroupData;
import com.wdj.mankai.ui.Board.BoardCommentActivity;

import org.json.JSONException;

import java.security.acl.Group;
import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private ArrayList<GroupData> gData = null;
    private  Context mContext;
    public GroupAdapter(Context mContext , ArrayList list) {
        this.mContext = mContext;
        this.gData = list;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
        // layout ID
        ImageView groupImg = itemView.findViewById(R.id.GroupimageView);
        TextView groupName = itemView.findViewById(R.id.Groupname);
        TextView groupOneline = itemView.findViewById(R.id.Grouponeline);
        TextView groupCate = itemView.findViewById(R.id.Groupcategory);
        LinearLayout layout = itemView.findViewById(R.id.group_linearLayout);
    }

    GroupAdapter(ArrayList<GroupData> list) {gData = list;}



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        view = inflater.inflate(R.layout.group_list_view,parent,false);

        GroupAdapter.ViewHolder gv = new GroupAdapter.ViewHolder(view);

        return gv;
    }

    @Override
    public void onBindViewHolder(GroupAdapter.ViewHolder holder, int position) {
        GroupData groupData = gData.get(position);
        try {
            holder.groupName.setText(groupData.getName());
            holder.groupOneline.setText(groupData.getOnelineintro());
            holder.groupCate.setText(groupData.getCategory());
            Glide.with(holder.itemView.getContext())
                    .load(groupData.getLogoImage())
                    .override(380,180)
                    .into(holder.groupImg);

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),Groupinfor.class);
                    try {
                        intent.putExtra("id",groupData.getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Bundle bundle = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.slide_in_right,R.anim.slide_wait).toBundle();
                    view.getContext().startActivity(intent, bundle);

                }
            });
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return gData.size();
    }



}
