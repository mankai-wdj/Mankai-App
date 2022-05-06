package com.wdj.mankai.ui.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.wdj.mankai.R;
import com.wdj.mankai.adapter.MessagesAdapter;
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

public class ChatContainerActivity extends AppCompatActivity {
    Room room;
    RecyclerView chat_message_list;
    MessagesAdapter messagesAdapter;
    ProgressBar progressBar;
    TextView textName;
    private JSONObject currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_container);
        progressBar = findViewById(R.id.progressBar);
        textName = findViewById(R.id.textName);
        room = (Room) getIntent().getSerializableExtra("room");

        SharedPreferences sharedPreferences= getSharedPreferences("login_token", MODE_PRIVATE);
        String token = sharedPreferences.getString("login_token","");
        getUser(token);

        chat_message_list = (RecyclerView) findViewById(R.id.chat_message_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatContainerActivity.this, LinearLayoutManager.VERTICAL, false);
        chat_message_list.setLayoutManager(layoutManager);


    }

    private String userName(String users, String roomType) throws JSONException {
        JSONArray users2 = new JSONArray(users);
        ArrayList<JSONObject> roomUsers = new ArrayList<>();
        if(users2.length() > 0) {
            for(int i = 0; i< users2.length(); i++) {
                if(!currentUser.getString("id").equals(users2.getJSONObject(i).getString("user_id"))) {
                    roomUsers.add(users2.getJSONObject(i));
                }
            }
        }

        if(roomType.equals("dm")) {
            return roomUsers.get(0).getString("user_name");
        }else {
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


    }

    private void setMessages(JSONObject messages, String userId) throws JSONException, ParseException {
        try {
            textName.setText(userName(room.users, room.type));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = messages.getJSONArray("data");
        messagesAdapter = new MessagesAdapter(ChatContainerActivity.this, userId, room.roomId);
        chat_message_list.setAdapter(messagesAdapter);
        for (int i = 0; i< jsonArray.length(); i++) {
            JSONObject message = jsonArray.getJSONObject(i);
            SimpleDateFormat newDtFormat2 = new SimpleDateFormat("h:mm");
            SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date formatDate2 = dtFormat.parse(message.getString("created_at"));
            messagesAdapter.addMessage(new Message(message.getString("id"), message.getString("user_id"), message.getString("room_id"), message.getString("type"), message.getString("message"), newDtFormat2.format(formatDate2), message.getString("user")));
            messagesAdapter.notifyDataSetChanged();
        }

        loading(false);
    }

    private void getMessages(String token, String roomId, String userId) {
        loading(true);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    setMessages(jsonObject, userId);
                } catch(JSONException | ParseException err) {
                    err.printStackTrace();
                }
            }
        };
        ChatRoomMessageRequest chatRoomMessageRequest = new ChatRoomMessageRequest(token, roomId, userId,responseListener);
        RequestQueue queue = Volley.newRequestQueue(ChatContainerActivity.this);
        queue.add(chatRoomMessageRequest);
    }

    private void loading(Boolean isLoading) {
        if(isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            chat_message_list.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.INVISIBLE);
            chat_message_list.setVisibility(View.VISIBLE);
        }
    }

    private void getUser(String token) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String userName = jsonObject.getString("name");
                    SharedPreferences sharedPreferences = getSharedPreferences("user_info",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_info",response);
                    editor.commit();
                    if(userName != null) {
                        System.out.println("유저 정보 받아옴");
                        currentUser = jsonObject;
                        getMessages(token, room.roomId, jsonObject.getString("id"));

                    } else{
                        Toast.makeText(ChatContainerActivity.this,"토큰 만료 다시 로그인", Toast.LENGTH_SHORT).show();
                    }
                } catch(JSONException err) {
                    err.printStackTrace();
                }
            }
        };
        UserRequest userRequest = new UserRequest(token,responseListener);
        RequestQueue queue = Volley.newRequestQueue(ChatContainerActivity.this);
        queue.add(userRequest);
    }
}