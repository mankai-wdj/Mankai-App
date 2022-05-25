package com.wdj.mankai.ui.mypage.RecyclerView;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wdj.mankai.R;
import com.wdj.mankai.data.FollowerData;
import com.wdj.mankai.data.MyGroupData;
import com.wdj.mankai.ui.main.GroupFragment;

import java.util.ArrayList;

public class MyGroupAdapter extends RecyclerView.Adapter<MyGroupAdapter.ViewHolder> {

    ArrayList<MyGroupData> groupData = new ArrayList<MyGroupData>();
    LayoutInflater inflater;

    public MyGroupAdapter(ArrayList<MyGroupData> groupData){
        this.groupData = groupData;
    }

    @NonNull
    @Override
    public MyGroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.my_group_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyGroupAdapter.ViewHolder holder, int position) {

        MyGroupData data = groupData.get(position);
        holder.name.setText(data.getName());
        holder.onelineintro.setText(data.getOnelineintro());
        holder.category.setText(data.getCategory());

        //        image 주소가 null 일 경우
        if(data.getLogoImage() == null){
            Glide.with(holder.itemView)
                    .load(R.drawable.a)
                    .into(holder.logoImage);
        }
//        image 주소가 있을 경우
        Glide.with(holder.itemView.getContext())
                .load(data.getLogoImage())
                .centerCrop()
                .into(holder.logoImage);

    }

    @Override
    public int getItemCount() {
        return groupData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView onelineintro;
        TextView category;
        ImageView logoImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.Groupname);
            onelineintro = itemView.findViewById(R.id.Grouponeline);
            category = itemView.findViewById(R.id.Groupcategory);
            logoImage = itemView.findViewById(R.id.GroupimageView);
        }
    }
}
