package com.wdj.mankai.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wdj.mankai.R;
import com.wdj.mankai.data.model.Room;

import java.util.ArrayList;


public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder> {
    Context mContext;
    ArrayList<Room> rooms = new ArrayList<>();

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
        holder.setItem(room);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageProfile;
        TextView text_room_name;
        TextView text_room_last_message;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text_room_name = (TextView) itemView.findViewById(R.id.text_room_name);
            text_room_last_message = (TextView) itemView.findViewById(R.id.text_room_last_message);

        }

        public void setItem(Room room) {

            text_room_name.setText(room.title);
            text_room_last_message.setText(room.last_message);

        }
    }

}
