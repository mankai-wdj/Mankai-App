package com.wdj.mankai.ui.chat;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdj.mankai.R;
import com.wdj.mankai.adapter.FollowingsAdapter;
import com.wdj.mankai.adapter.MemosAdapter;
import com.wdj.mankai.data.model.ChatMemo;
import com.wdj.mankai.data.model.Room;
import com.wdj.mankai.ui.main.UserRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MemoListActivity extends AppCompatActivity {
    JSONObject currentUser;
    RecyclerView memoListRecyclerView;
    MemosAdapter memosAdapter;
    Room room;
    public static Button btSendMemo;

    @Override
    protected void onStop() {
        super.onStop();
        MemosAdapter.checkMemos.clear();  // 꺼지면 배열 비움
        MemosAdapter.memos.clear();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);
        SharedPreferences sharedPreferences= getSharedPreferences("login_token", MODE_PRIVATE);
        String token = sharedPreferences.getString("login_token","");
        getUser(token);

        memoListRecyclerView = findViewById(R.id.memoListRecyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MemoListActivity.this, LinearLayoutManager.VERTICAL, false);
        memoListRecyclerView.setLayoutManager(layoutManager);
        btSendMemo = findViewById(R.id.btSendMemo);
        room = (Room) getIntent().getSerializableExtra("room");
        // memo 보내기
        btSendMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray toUsers = null;
                JSONArray toUsers2 = new JSONArray();
                try {
                    toUsers = new JSONArray(room.users);
                    for(int i = 0; i < toUsers.length(); i++) {
                        toUsers2.put(toUsers.getJSONObject(i).getString("user_id"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray memos = new JSONArray();

                for(int i =0; i< MemosAdapter.checkMemos.size(); i++) {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("id", MemosAdapter.checkMemos.get(i).getId());
                        json.put("type", MemosAdapter.checkMemos.get(i).getType());
                        json.put("memo_title", MemosAdapter.checkMemos.get(i).getMemo_title());
                        json.put("user_id", MemosAdapter.checkMemos.get(i).getUser_id());
                        json.put("content_text", MemosAdapter.checkMemos.get(i).getContent_text());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    memos.put(json);
                }
                System.out.println(memos);
                JSONObject reqJsonObject = new JSONObject(); //JSON 객체를 생성
                try {
                    reqJsonObject.put("memos", memos);
                    reqJsonObject.put("room_id", room.id);
                    reqJsonObject.put("to_users", toUsers2);
                    reqJsonObject.put("user_id", currentUser.getString("id"));
                    reqJsonObject.put("type", "memo");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                StringRequest jsonObjectRequest = new StringRequest(
                        Request.Method.POST,
                        "https://api.mankai.shop/api/message/send",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println( "memo 보내기 성공");
                                Intent intent = new Intent(MemoListActivity.this, ChatContainerActivity.class);
                                intent.putExtra("room", room);
                                finish();
                                startActivity(intent);

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //통신 ERROR
                                System.out.println(error);
                            }
                        }
                ){
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        return reqJsonObject.toString().getBytes();
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }
                };
                Volley.newRequestQueue(MemoListActivity.this).add(jsonObjectRequest);
            }
        });


    }

    // 어텝터에 메모 넣어주기,
    private void setMemoList(JSONArray memos) throws JSONException {
        memosAdapter = new MemosAdapter(MemoListActivity.this);
        memoListRecyclerView.setAdapter(memosAdapter);
        for(int i = 0; i< memos.length(); i++){
//            if(memos.getJSONObject(i).getString("type").equals("SNS")) {
//                //여기 합치고나서 viewtype 값넣어줄 때 Code클래스에서 가져와주기
//                SNSMemoRequest(memos.getJSONObject(i).getInt("id"), memos.getJSONObject(i).getString("type"),memos.getJSONObject(i).getString("memo_title"), currentUser.getString("id"), memos.getJSONObject(i).getString("content_text"));
//            }else {
                memosAdapter.addMemo(new ChatMemo(memos.getJSONObject(i).getInt("id"), memos.getJSONObject(i).getString("type"),memos.getJSONObject(i).getString("memo_title"),currentUser.getString("id"), memos.getJSONObject(i).getString("content_text"), null));
//            }
        }
        memosAdapter.notifyDataSetChanged();

    }

    //내 memos 가져오기
    private void getMemoLists(String id) throws JSONException {

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET,
                "https://api.mankai.shop/api/memo/"+id,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("TAG", "get memo list success");
                        System.out.println("resresrser"+response);
                        try {
                            setMemoList(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //통신 ERROR
                    }
                }
        );
        Volley.newRequestQueue(MemoListActivity.this).add(jsonObjectRequest);
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
                        getMemoLists(jsonObject.getString("id"));


                    } else{
                        Toast.makeText(MemoListActivity.this,"토큰 만료 다시 로그인", Toast.LENGTH_SHORT).show();
                    }
                } catch(JSONException err) {
                    err.printStackTrace();
                }
            }
        };
        UserRequest userRequest = new UserRequest(token,responseListener);
        RequestQueue queue = Volley.newRequestQueue(MemoListActivity.this);
        queue.add(userRequest);
    }

}