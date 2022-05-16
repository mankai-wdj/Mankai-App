package com.wdj.mankai.ui.chat.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wdj.mankai.R;
import com.wdj.mankai.adapter.FollowingsAdapter;
import com.wdj.mankai.data.model.Room;
import com.wdj.mankai.data.model.User;
import com.wdj.mankai.ui.chat.ChatContainerActivity;
import com.wdj.mankai.ui.chat.ChatCreateActivity;
import com.wdj.mankai.ui.chat.FollowingsRequest;
import com.wdj.mankai.ui.main.UserRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

public class ChatInviteActivity extends AppCompatActivity {
    private RecyclerView invite_room_recyclerview;
    FollowingsAdapter followingsAdapter;
    public static Button bt_invite;
    User currentUser;
    Room room;
    public static ArrayList<Integer> existUser = new ArrayList<>();
    @Override
    protected void onStop() {
        super.onStop();
        FollowingsAdapter.checkUsers.clear();  // 꺼지면 배열 비움
        FollowingsAdapter.followings.clear();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_invite);
        SharedPreferences sharedPreferences= getSharedPreferences("login_token",MODE_PRIVATE);
        String token = sharedPreferences.getString("login_token","");

        getUser(token); // 유저 정보 받아오기

        room = (Room) getIntent().getSerializableExtra("room");

        System.out.println(room.id);

        invite_room_recyclerview = (RecyclerView) findViewById(R.id.invite_room_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatInviteActivity.this, LinearLayoutManager.VERTICAL, false);
        invite_room_recyclerview.setLayoutManager(layoutManager);
        followingsAdapter = new FollowingsAdapter(ChatInviteActivity.this, "invite");
        invite_room_recyclerview.setAdapter(followingsAdapter);




        bt_invite = findViewById(R.id.bt_invite);
        bt_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JSONObject reqJsonObject = new JSONObject(); //JSON 객체를 생성
                JSONArray jsonArray = new JSONArray();
                for(int i=0; i< FollowingsAdapter.checkUsers.size(); i++) {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("id", FollowingsAdapter.checkUsers.get(i).id);
                        json.put("name", FollowingsAdapter.checkUsers.get(i).name);
                        json.put("email", FollowingsAdapter.checkUsers.get(i).email);
                        json.put("profile", FollowingsAdapter.checkUsers.get(i).profile);
                        json.put("country", FollowingsAdapter.checkUsers.get(i).country);
                        json.put("description", FollowingsAdapter.checkUsers.get(i).description);
                        json.put("position", FollowingsAdapter.checkUsers.get(i).position);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(json);
                }
                JSONObject user = new JSONObject();
                JSONObject room1 = new JSONObject();
                try {
                    user.put("id", currentUser.id);
                    user.put("name", currentUser.name);
                    user.put("email", currentUser.email);
                    user.put("profile", currentUser.profile);
                    user.put("country", currentUser.country);
                    user.put("description", currentUser.description);
                    user.put("position", currentUser.position);
                    room1.put("id", room.id);
                    room1.put("title", room.title);
                    room1.put("users", room.users);
                    room1.put("last_message", room.last_message);
                    room1.put("type", room.type);
                    room1.put("updated_at", room.updated_at);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    reqJsonObject.put("inviteUsers", jsonArray); //각종 데이터 입력
                    reqJsonObject.put("user", user);
                    reqJsonObject.put("room", room1);
                } catch (JSONException e) {
                    //JSON error
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        "https://api.mankai.shop/api/user/invite",
                        reqJsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("TAG", "onResponse:"+response);
                                Intent intent = new Intent(ChatInviteActivity.this, ChatContainerActivity.class);
                                finish();

                                try {
                                    System.out.println("rooo23123123" + new Room(response.getString("id"), "","", response.getString("type"), response.getString("users"), response.getString("updated_at")));
                                    intent.putExtra("room", new Room(response.getString("id"), "","", response.getString("type"), response.getString("users"), response.getString("updated_at")));
//                                    intent.putExtra("room", new Room(response.getString("id"), "",response.getString("last_message"), response.getString("type"), response.getString("users"), response.getString("updated_at")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(intent);

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //통신 ERROR
                            }
                        }
                );
                Volley.newRequestQueue(ChatInviteActivity.this).add(jsonObjectRequest);
            }
        });
    }

    private  void existsUsers(JSONArray users) throws JSONException {
        System.out.println(FollowingsAdapter.followings);

        for (int i = 0; i< FollowingsAdapter.followings.size(); i++) {
            for (int j = 0; j < users.length(); j++) {
                if(FollowingsAdapter.followings.get(i).id.equals(users.getJSONObject(j).getString("user_id"))) {
                    System.out.println(i + "번째 checkbox 해줘야됨");
                    existUser.add(i);
                }
            }
        }
    }

    private void setFollowings(JSONArray followings) throws JSONException, ParseException {

        for (int i = 0; i< followings.length(); i++) {
            JSONObject following = followings.getJSONObject(i);
            followingsAdapter.addFollowing(new User(following.getString("id"), following.getString("name"), following.getString("email"), following.getString("profile"), following.getString("country"), following.getString("description"),following.getString("position")));
            followingsAdapter.notifyDataSetChanged();

        }
        existsUsers(new JSONArray(room.users));

    }

    private void getFollowings(String userId) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
//                    System.out.println(response);
                    setFollowings(jsonArray);

                } catch(JSONException | ParseException err) {
                    err.printStackTrace();
                }
            }
        };
        FollowingsRequest followingsRequest = new FollowingsRequest(userId, responseListener);
        RequestQueue queue = Volley.newRequestQueue(ChatInviteActivity.this);
        queue.add(followingsRequest);
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
                        getFollowings(jsonObject.getString("id"));
//                        System.out.println(jsonObject.getClass().getName());
                        currentUser = new User(jsonObject.getString("id"), jsonObject.getString("name"), jsonObject.getString("email"), jsonObject.getString("profile"), jsonObject.getString("country"), jsonObject.getString("description"), jsonObject.getString("position"));
                    } else{
                        Toast.makeText(ChatInviteActivity.this,"토큰 만료 다시 로그인", Toast.LENGTH_SHORT).show();
                        System.out.println(response);
                    }
                } catch(JSONException err) {
                    err.printStackTrace();
                }
            }
        };
        UserRequest userRequest = new UserRequest(token,responseListener);
        RequestQueue queue = Volley.newRequestQueue(ChatInviteActivity.this);
        queue.add(userRequest);
    }
}