package com.wdj.mankai.ui.mypage.RecyclerView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wdj.mankai.R;

import java.util.ArrayList;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder> {

    Context followerContext;
    ArrayList<Followers> followers = new ArrayList<>();

    public FollowersAdapter(Context followerContext){
        this.followerContext = followerContext;
    }


    @NonNull
    @Override
    public FollowersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) followerContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.followers_item, parent, false);


        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowersAdapter.ViewHolder holder, int position) {

        Followers follower = followers.get(position);
        holder.setItem(follower);


    }

    @Override
    public int getItemCount() {
        return followers.size();
    }

    public void addFollowers(Followers followerss){
        followers.add(followerss);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name_txt;
        ImageView myImageView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            name_txt = itemView.findViewById(R.id.name_txt);
            myImageView = itemView.findViewById(R.id.myImageView);
        }

        public void setItem(Followers follower) {
            name_txt.setText(follower.name_txt);
            myImageView.setImageDrawable(Drawable.createFromPath(follower.myImageView));
        }
    }
}
