package com.wdj.mankai.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wdj.mankai.R;
import com.wdj.mankai.data.model.LoggedInUser;
import com.wdj.mankai.data.model.Room;
import com.wdj.mankai.data.model.User;
import com.wdj.mankai.ui.chat.ChatContainerActivity;
import com.wdj.mankai.ui.chat.ChatCreateActivity;
import com.wdj.mankai.ui.chat.ui.ChatInviteActivity;

import java.util.ArrayList;

public class FollowingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    String type;
    public static ArrayList<User> followings = new ArrayList<User>();
    public static ArrayList<User> checkUsers = new ArrayList<User>();
    ChatCreateActivity chatCreateActivity = new ChatCreateActivity();
    ChatInviteActivity chatInviteActivity = new ChatInviteActivity();

    public void addFollowing(User user) {
        followings.add(user);
    }


    public FollowingsAdapter(Context mContext, String type) {
        this.mContext = mContext;
        this.type = type;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.following_list_item, parent, false);
        if(type.equals("create")) {
            return new CreateViewHolder(itemView);
        }else if(type.equals("invite")) {
            return new InviteViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User user = followings.get(position);
        if(type.equals("create")) {
            ((CreateViewHolder) holder).setItem(user);
        }else if(type.equals("invite")) {
            System.out.println(ChatInviteActivity.existUser.indexOf(position));
            if(ChatInviteActivity.existUser.indexOf(position) != -1) {
                ((InviteViewHolder) holder).createCheckBox.setChecked(true);
                ((InviteViewHolder) holder).createCheckBox.setEnabled(false);
            }
            ((InviteViewHolder) holder).setItem(user);
        }
    }

    @Override
    public int getItemCount() {
        return followings.size();
    }

    public class CreateViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProfile;
        TextView textFollowingName;
        CheckBox createCheckBox;


        public CreateViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            textFollowingName = (TextView) itemView.findViewById(R.id.followingName);
            createCheckBox = itemView.findViewById(R.id.createCheckBox);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                }
            });

            createCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (createCheckBox.isChecked()) {
                        System.out.println(followings.get(getAdapterPosition()).name + " check 되있음");
                        checkUsers.add(followings.get(getAdapterPosition()));
                    } else {
                        System.out.println(followings.get(getAdapterPosition()).name + " check 안되있음");
                        checkUsers.remove(followings.get(getAdapterPosition()));
                    }
                    System.out.println(checkUsers);
                    if(checkUsers.size() >= 1) {
                        chatCreateActivity.btCreate.setEnabled(true);
                    }else {
                        chatCreateActivity.btCreate.setEnabled(false);
                    }
//                    System.out.println(followings.get(getAdapterPosition()));
                }
            });


        }

        public void setItem(User user) {
            textFollowingName.setText(user.name);
            if(!user.profile.equals("null")){
                Glide.with(itemView)
                        .load(user.profile )
                        .into(imageProfile);
            }else {
                imageProfile.setImageResource(R.drawable.profileimage);

            }

        }
    }

    public class InviteViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProfile;
        TextView textFollowingName;
        CheckBox createCheckBox;


        public InviteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            textFollowingName = (TextView) itemView.findViewById(R.id.followingName);
            createCheckBox = itemView.findViewById(R.id.createCheckBox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                }
            });

            createCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (createCheckBox.isChecked()) {
                        System.out.println(followings.get(getAdapterPosition()).name + " check 되있음");
                        checkUsers.add(followings.get(getAdapterPosition()));
                    } else {
                        System.out.println(followings.get(getAdapterPosition()).name + " check 안되있음");
                        checkUsers.remove(followings.get(getAdapterPosition()));
                    }
                    System.out.println(checkUsers);
                    if(checkUsers.size() >= 1) {
                        chatInviteActivity.bt_invite.setEnabled(true);
                    }else {
                        chatInviteActivity.bt_invite.setEnabled(false);
                    }
//                    System.out.println(followings.get(getAdapterPosition()));
                }
            });


        }

        public void setItem(User user) {
            textFollowingName.setText(user.name);
            if(!user.profile.equals("null")){
                Glide.with(itemView)
                        .load(user.profile )
                        .into(imageProfile);
            }else {
                imageProfile.setImageResource(R.drawable.profileimage);
            }


        }
    }


}
