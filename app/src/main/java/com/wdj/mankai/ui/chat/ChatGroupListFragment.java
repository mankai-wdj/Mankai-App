package com.wdj.mankai.ui.chat;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.wdj.mankai.R;
import com.wdj.mankai.adapter.RoomsAdapter;
import com.wdj.mankai.data.model.Room;
import com.wdj.mankai.ui.main.UserRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatGroupListFragment extends Fragment {
    RecyclerView roomList_list;
    RoomsAdapter roomsAdapter;
    private JSONObject currentUser;
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_chat_list, container, false);
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("login_token", getActivity().MODE_PRIVATE);
        String token = sharedPreferences.getString("login_token","");

        progressBar = rootView.findViewById(R.id.progressBar);

        getUser(token); // 유저 정보 받아오기
//        ChatActivity.setRoomList(rootView);
        roomList_list = (RecyclerView) rootView.findViewById(R.id.roomList_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        roomList_list.setLayoutManager(layoutManager);

        roomsAdapter = new RoomsAdapter(getActivity());
        roomList_list.setAdapter(roomsAdapter);
        return rootView;
    }
    private void setRooms(JSONArray jsonArray) throws JSONException {
        Date nowDate = new Date();
        String formatDate = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject room = jsonArray.getJSONObject(i);
            String title = room.getString("title");
            String lastMessage = room.getString("last_message");
            if(title.equals("null")) {
                title = userName(room.getString("users"));
            }
            if(lastMessage.equals("null")) {
                lastMessage = "";
            }

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


            if(room.getString("type").equals("group")) {
                roomsAdapter.addRoom(new Room(room.getString("id"), title, lastMessage, room.getString("type"), room.getString("users"), formatDate));
            }
            roomsAdapter.notifyDataSetChanged();
        }
        loading(false);
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
        String title = "";
        for (int i = 0; i< roomUsers.size(); i++) {
            if(i != roomUsers.size()-1) {
                title += (roomUsers.get(i).getString("user_name") + ", ");

            }else{
                title += roomUsers.get(i).getString("user_name");
            }
        }

        return title;
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

    private void getRooms(String token, int userId) {
        loading(true);
        System.out.println(userId);
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

    private void getUser(String token) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    System.out.println(jsonObject.getInt("id"));
                    String userName = jsonObject.getString("name");
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info",getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_info",response);
                    editor.commit();
                    if(userName != null) {
                        System.out.println("유저 정보 받아옴");
                        currentUser = jsonObject;
                        getRooms(token, jsonObject.getInt("id")); // room list 받아오기

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
