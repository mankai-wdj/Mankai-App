package com.wdj.mankai.ui.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.wdj.mankai.R;
import com.wdj.mankai.adapter.RoomsAdapter;
import com.wdj.mankai.data.model.Message;
import com.wdj.mankai.data.model.Room;
import com.wdj.mankai.ui.main.UserRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatDMListFragment extends Fragment {
    RecyclerView roomList_list;
    RoomsAdapter roomsAdapter;
    private JSONObject currentUser;
    ProgressBar progressBar;
    Channel channel;
    PusherOptions options = new PusherOptions() .setCluster("ap3");
    Pusher pusher = new Pusher("04847be41be2cbe59308",options);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_chat_list, container, false);
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("login_token", getActivity().MODE_PRIVATE);
        String token = sharedPreferences.getString("login_token","");

        progressBar = rootView.findViewById(R.id.progressBar);

        getUser(token); // 유저 정보 받아오기
        roomList_list = (RecyclerView) rootView.findViewById(R.id.roomList_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        roomList_list.setLayoutManager(layoutManager);
        roomsAdapter = new RoomsAdapter(getActivity());
        roomList_list.setAdapter(roomsAdapter);

//        try {
//            channel = pusher.subscribe("user."+currentUser.getString("id")); // 채널 연결
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        channel.bind("invite-event", new SubscriptionEventListener() {
//            @Override
//            public void onEvent(PusherEvent event) {
//                System.out.println("event : " + event);
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//
//            }
//        });
        return rootView;
    }
    private void setRooms(JSONArray jsonArray) throws JSONException {
        Date nowDate = new Date();
        String formatDate = null;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject room = jsonArray.getJSONObject(i);
            Log.d("Check", "setRooms: " + jsonArray.getString(i));
            Log.e("TAG", String.valueOf(room));

            String title = room.getString("title");
            String lastMessage = room.getString("last_message");
//            String profile = room.getString("profile");
            JSONArray users = new JSONArray(room.getString("users"));
            String profile = room.getString("profile");

            if(title.equals("null")) {
                title = userName(room.getString("users"));
            }
            if(lastMessage.equals("null")) {
                lastMessage = "";
            }
            if(profile.equals("null")) {
                profile = "";
            }
//            if(profile.equals("null")) {
//                profile ="";
//            }
            try {
                SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date formatDate2 = dtFormat.parse(room.getString("updated_at"));
                SimpleDateFormat newDtFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat newDtFormat2 = new SimpleDateFormat("h:mm");
                SimpleDateFormat newDtFormat3 = new SimpleDateFormat("MM월 dd일");

                if(newDtFormat.parse(newDtFormat.format(nowDate)).compareTo(formatDate2) > 0) {
                    formatDate = newDtFormat3.format(formatDate2);
                }else {
                    formatDate = newDtFormat2.format(formatDate2);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }


            if(room.getString("type").equals("dm")) {
                roomsAdapter.addRoom(new Room(room.getString("id"), title, lastMessage, room.getString("type"), room.getString("users"), formatDate,profile));
            }
            roomsAdapter.notifyDataSetChanged();
        }
        loading(false);
    }

    private void channelSubscribe(int userID) {
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                System.out.println("State changed to " + change.getCurrentState() +
                        " from " + change.getPreviousState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                System.out.println("There was a problem connecting!");
                System.out.println(code);
                System.out.println(message);
            }
        }, ConnectionState.ALL);



    }

    private String userName(String users) throws JSONException {
        JSONArray users2 = new JSONArray(users);
        ArrayList<JSONObject> roomUsers = new ArrayList<>();
        if(users2.length() > 0) {
            for(int i = 0; i< users2.length(); i++) {
                if(!currentUser.getString("id").equals(users2.getJSONObject(i).getString("user_id"))) {
                    roomUsers.add(users2.getJSONObject(i));
                }
            }
        }

        if(roomUsers.size() != 0) {
            return roomUsers.get(0).getString("user_name");
        }
        return "";
    }

    private void getRooms(String token, int userId) {
        loading(true);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    setRooms(jsonArray);
                } catch(JSONException err) {
                    err.printStackTrace();
                }
            }
        };
        ChatRoomListRequest chatRoomListRequest = new ChatRoomListRequest(token, userId,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(chatRoomListRequest);
    }

    private void loading(Boolean isLoading) {
        if(isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            roomList_list.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.INVISIBLE);
            roomList_list.setVisibility(View.VISIBLE);
        }
    }

    private void getUser(String token) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String userName = jsonObject.getString("name");
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info",getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_info",response);
                    editor.commit();
                    if(userName != null) {
                        System.out.println("유저 정보 받아옴");
                        currentUser = jsonObject;
                        getRooms(token, jsonObject.getInt("id")); // room list 받아오기
                        channelSubscribe(currentUser.getInt("id"));

                    } else{
                        Toast.makeText(getActivity(),"토큰 만료 다시 로그인", Toast.LENGTH_SHORT).show();
                        System.out.println(response);
                    }
                } catch(JSONException err) {
                    err.printStackTrace();
                }
            }
        };
        UserRequest userRequest = new UserRequest(token,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(userRequest);
    }
}
