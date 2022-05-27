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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.wdj.mankai.R;
import com.wdj.mankai.data.model.AppHelper;
import com.wdj.mankai.data.model.Room;
import com.wdj.mankai.ui.chat.ChatContainerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageProfile;
        TextView text_room_name;
        TextView text_room_last_message;
        TextView text_room_time;
        TextView profile_count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            text_room_name = (TextView) itemView.findViewById(R.id.text_room_name);
            text_room_last_message = (TextView) itemView.findViewById(R.id.text_room_last_message);
            text_room_time = (TextView) itemView.findViewById(R.id.text_room_time);
            profile_count = (TextView) itemView.findViewById(R.id.profile_count);

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

        public void setItem(Room room) {
            try {
                JSONArray jsonArray = new JSONArray(room.users);
                if(jsonArray.length()>2){
                    profile_count.setText(""+jsonArray.length());
                }
                else {
                    profile_count.setVisibility(View.GONE);
                }
                String user_id = jsonArray.getJSONObject(0).getString("user_id");
                Log.d("Check", "Room Image: " + user_id);
                CheckProfile(user_id);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            text_room_name.setText(room.title);
            text_room_last_message.setText(room.last_message);
            text_room_time.setText(room.updated_at);

        }
        public void CheckProfile(String user_id){
            StringRequest request = new StringRequest(Request.Method.GET, "https://api.mankai.shop/api/getuserprofile/"+user_id,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Check", "profile response: " + response);
                            if(!response.isEmpty()) {
                                Glide.with(itemView)
                                        .load(response)
                                        .into(imageProfile);
                            }else {
                                imageProfile.setImageResource(R.drawable.profileimage);

                            }
                        }
                    }
                    ,null);


            AppHelper.requestQueue.add(request);

        }
    }



}
