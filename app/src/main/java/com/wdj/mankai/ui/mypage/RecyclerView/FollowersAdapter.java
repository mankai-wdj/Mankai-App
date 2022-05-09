package com.wdj.mankai.ui.mypage.RecyclerView;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder> {

    ArrayList<FollowerData> followerData = new ArrayList<FollowerData>();

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public FollowersAdapter(ArrayList<FollowerData> followerData){
        this.followerData = followerData;
    }
    public FollowersAdapter(){

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.followers_item, parent, false);

        return new ViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FollowerData data = followerData.get(position);
        holder.name_txt.setText(data.getName());
//        image 주소가 null 일 경우
        if(data.getProfile() == null){
            Glide.with(holder.itemView)
                    .load(R.drawable.a)
                    .into(holder.myImageView);
        }
//        image 주소가 있을 경우
        Glide.with(holder.itemView.getContext())
                .load(data.getProfile())
                .centerCrop()
                .into(holder.myImageView);
    }

    @Override
    public int getItemCount() {
        return followerData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name_txt;
        ImageView myImageView;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener){
            super(itemView);
            name_txt = itemView.findViewById(R.id.name_txt);
            myImageView = itemView.findViewById(R.id.myImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }


    }
}
