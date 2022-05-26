package com.wdj.mankai.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wdj.mankai.R;
import com.wdj.mankai.data.model.Room;
import com.wdj.mankai.ui.chat.ChatContainerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder> {
    Context mContext;
    ArrayList<Room> rooms = new ArrayList<>();
    ImageView imageProfile;

    public RoomsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.chat_room_list_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomsAdapter.ViewHolder holder, int position) {
        Room room = rooms.get(position);
        try {
            holder.setItem(room);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProfile;
        TextView text_room_name;
        TextView text_room_last_message;
        TextView text_room_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            imageProfile = itemView.findViewById(R.id.imageProfile);
            text_room_name = (TextView) itemView.findViewById(R.id.text_room_name);
            text_room_last_message = (TextView) itemView.findViewById(R.id.text_room_last_message);
            text_room_time = (TextView) itemView.findViewById(R.id.text_room_time);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    System.out.println(rooms.get(pos).getClass().getName());
                    Intent intent = new Intent(view.getContext(), ChatContainerActivity.class);
                    intent.putExtra("room" , rooms.get(pos));
                    mContext.startActivity(intent);
                }
            });

        }

        public void setItem(Room room) throws JSONException  {
//            imageProfile.setImageResource((room.users));

            text_room_name.setText(room.title);
            text_room_last_message.setText(room.last_message);
            text_room_time.setText(room.updated_at);
            Glide.with(itemView.getContext()).load(new JSONObject(room.users).getString("profile")).into((ImageView) itemView.findViewById(R.id.imageProfile));
        }
    }

}
