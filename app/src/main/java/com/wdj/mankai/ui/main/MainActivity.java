package com.wdj.mankai.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.wdj.mankai.R;
import com.wdj.mankai.ui.chat.ChatContainerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Group;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    JSONObject currentUser;
    String fcmToken;
    PusherOptions options = new PusherOptions() .setCluster("ap3");
    Pusher pusher = new Pusher("04847be41be2cbe59308",options);
    Channel channel; // 채널 연결
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences= getSharedPreferences("login_token", MODE_PRIVATE);
        String token = sharedPreferences.getString("login_token","");
        getUser(token); // 유저 정보 받아오기
        SharedPreferences sharedPreferences2 = getSharedPreferences("current_room_id",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        editor.putString("current_room_id","");
        editor.commit();
        SharedPreferences sharedPreferences1= getSharedPreferences("fcm_token", MODE_PRIVATE);
        fcmToken = sharedPreferences1.getString("fcm_token","");
        // fcm token 받아서 shaere에 저장
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String token) {
                System.out.println("FCM Token : "+ token);
                SharedPreferences sharedPreferences = getSharedPreferences("fcm_token",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("fcm_token",token);
                editor.commit();
            }
        });



        bottomNavigationView = findViewById(R.id.bottomNavigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new HomeFragment()).commit();

        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.chat:
                        fragment = new ChatFragment();
                        break;
                    case R.id.group:
                        fragment = new GroupFragment();
                        break;
                    case R.id.mypage:
                        fragment = new MyPageFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,fragment).commit();
                return true;
            }
        });
    }

    private void getUser(String token) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    System.out.println(response);
                    String userName = jsonObject.getString("name");
                    SharedPreferences sharedPreferences = getSharedPreferences("user_info",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_info",response);
                    editor.commit();
                    if(userName != null) {
                        currentUser = jsonObject;
                        System.out.println("유저 정보 받아옴");
                        Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();
                        channelSubscribe(currentUser.getInt("id"));
                    } else{
                        Toast.makeText(MainActivity.this,"토큰 만료 다시 로그인", Toast.LENGTH_SHORT).show();
                        System.out.println(response);
                    }
                } catch(JSONException err) {
                    err.printStackTrace();
                }
            }
        };
        UserRequest userRequest = new UserRequest(token,responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(userRequest);
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

        channel = pusher.subscribe("user."+userID);

        channel.bind("user-connect", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                try {
                    JSONObject jsonObject = new JSONObject(event.getData());
                    if(!currentUser.getString("id").equals(jsonObject.getJSONObject("message").getString("user_id"))) {
                        System.out.println("여기는 메인 엑티비티" + jsonObject);
                        fcmMessage(jsonObject);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    // fcm message 보내주기
    private void fcmMessage(JSONObject jsonObject) {
        JSONObject json = new JSONObject();
        try {
            json.put("token", fcmToken);
            json.put("body", jsonObject.getJSONObject("message").getString("message"));
            json.put("user_id", jsonObject.getJSONObject("message").getString("user_id"));
            json.put("room_id", jsonObject.getJSONObject("message").getString("room_id"));
            json.put("type", jsonObject.getJSONObject("message").getString("type"));
            json.put("serverKey", "AAAAyHNj1PU:APA91bGTHakJiPoM3gBUvETE5jxDLTDkPejPWLKc1Vx9qbPZWFNmukec16arubGCZ6-lwceJaPSPleykqjEGwKDsZOiCtGsl21eA8pYABWZGzdA8JfHt6kY_tAk9Si_4ShNmEbBPQK4b");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("dzzzzzzzzzzzzzzzzz" + json);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                "https://api.mankai.shop/api/fcm/message",
                json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //통신 ERROR
                    }
                }
        );
        Volley.newRequestQueue(MainActivity.this).add(jsonObjectRequest);
    }
}