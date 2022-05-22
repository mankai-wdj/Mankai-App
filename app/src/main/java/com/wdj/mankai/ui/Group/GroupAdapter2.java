package com.wdj.mankai.ui.Group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wdj.mankai.R;
import com.wdj.mankai.data.GroupData;

import org.json.JSONException;

import java.util.ArrayList;

public class GroupAdapter2 extends RecyclerView.Adapter<GroupAdapter2.ViewHolder> {

    private ArrayList<GroupData> gData = null;
    private  Context mContext;
    public GroupAdapter2(Context mContext , ArrayList list) {
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
    }

    GroupAdapter2(ArrayList<GroupData> list) {gData = list;}



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        view = inflater.inflate(R.layout.group_list_view,parent,false);

        GroupAdapter2.ViewHolder gv = new GroupAdapter2.ViewHolder(view);

        return gv;
    }

    @Override
    public void onBindViewHolder(GroupAdapter2.ViewHolder holder, int position) {
        GroupData groupData = gData.get(position);
        try {
            holder.groupName.setText(groupData.getName());
            holder.groupOneline.setText(groupData.getOnelineintro());
            holder.groupCate.setText(groupData.getCategory());
            Glide.with(holder.itemView.getContext())
                    .load(groupData.getLogoImage())
                    .into(holder.groupImg);

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return gData.size();
    }


}
