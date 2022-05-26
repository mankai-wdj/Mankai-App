package com.wdj.mankai.ui.mypage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.wdj.mankai.R;
import com.wdj.mankai.adapter.FollowingsAdapter;
import com.wdj.mankai.data.model.AppHelper;
import com.wdj.mankai.data.model.Room;
import com.wdj.mankai.ui.chat.ChatContainerActivity;
import com.wdj.mankai.ui.chat.ChatCreateActivity;
import com.wdj.mankai.ui.main.MainActivity;
import com.wdj.mankai.ui.main.MyPageFragment;
import com.wdj.mankai.ui.main.UserRequest;
import com.wdj.mankai.ui.mypage.toolbar.FragMyMemoExceptToolbar;
import com.wdj.mankai.ui.mypage.toolbar.FragMyMemoToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class YouPage extends AppCompatActivity implements FragYouFollowers.OnInputListener {

    String userName;
    String userDescription;
    String userProfile;
    String userId;
    String url;
    Button followButton;
    Button followingButton;
    Button MSGButton;
    static final String PASS = "pass";
    String input;
    String LoginUserId;
    JSONObject object , jsonObject ,currentUser;
    @Override
    public void sendInput(String input) {
        Log.d("popopo",input);
        this.input = input;
        if (input == PASS) {
            Log.d("popopo1",input);
            followButton.setVisibility(View.GONE);
            followingButton.setVisibility(View.VISIBLE);
            MSGButton.setVisibility(View.VISIBLE);
            Log.i("팔로우한 사람","ㄹㄹㅇㄹㅇㅇㄹ");
        }
    }



    public static YouPage newInstance(){
        YouPage YouPageHead = new YouPage();

        return YouPageHead;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_page);

        Intent intent = getIntent();
        userId = intent.getStringExtra("youURL");

         followButton = findViewById(R.id.followButton);
         followingButton = findViewById(R.id.followingButton);
         MSGButton = findViewById(R.id.MSGButton);

        url = "https://api.mankai.shop/api/follow/";


        SharedPreferences sharedPreferences= getSharedPreferences("login_token", MODE_PRIVATE);
        String token = sharedPreferences.getString("login_token","");
        getUser(token); // 유저 정보 받아오기

        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(this);

       Response();

        ViewPager pager = findViewById(R.id.viewPager);
        YouViewPagerAdapter adapter = new YouViewPagerAdapter(getSupportFragmentManager(), 1);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);


        followButton.setVisibility(View.VISIBLE);
        followingButton.setVisibility(View.GONE);
        MSGButton.setVisibility(View.GONE);

        // 로그인한 유저 ID
        SharedPreferences sharedPreferencesMy = getSharedPreferences("user_info",Context.MODE_PRIVATE);
        String user_info = sharedPreferencesMy.getString("user_info", "");


        try {
            object = new JSONObject(user_info);
            LoginUserId = object.getString("id");
            Log.d("LoginUserId", LoginUserId);
        } catch (Throwable t) {

        }

        // 다시 자기 페이지 눌렀을 경우 -> 수정 필요
//         if (userId.equals(LoginUserId)){
//            Log.d("나야", "나야");
//            MyPageFragment myPageFragment = new MyPageFragment();
//            getSupportFragmentManager().beginTransaction().replace(R.id.mypage,myPageFragment).commit();
//        }
        if (userId.equals(LoginUserId)){
            Log.d("나야", "나야");
//            MyPageFragment myPageFragment = new MyPageFragment();
//            getSupportFragmentManager().beginTransaction().replace(R.id.mypage,myPageFragment).commit();

        }

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendFollow();

                sendInput(PASS);
                sendInput(input);
            }
        });

        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendFollow();
                followButton.setVisibility(View.VISIBLE);
                followingButton.setVisibility(View.GONE);
                MSGButton.setVisibility(View.GONE);

            }
        });

        MSGButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject reqJsonObject = new JSONObject(); //JSON 객체를 생성
                JSONArray jsonArray = new JSONArray();



                JSONObject json2 = new JSONObject();
                try {
                    json2.put("id", jsonObject.getInt("id"));
                    json2.put("name", jsonObject.getString("name"));
                    json2.put("email", jsonObject.getString("email"));
                    json2.put("profile",jsonObject.getString("profile"));
                    json2.put("country", jsonObject.getString("country"));
                    json2.put("description", jsonObject.getString("description"));
                    json2.put("position", jsonObject.getString("position"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject json = new JSONObject();
                try {
                    json.put("id", currentUser.getInt("id"));
                    json.put("name", currentUser.getString("name"));
                    json.put("email", currentUser.getString("email"));
                    json.put("profile",currentUser.getString("profile"));
                    json.put("country", currentUser.getString("country"));
                    json.put("description", currentUser.getString("description"));
                    json.put("position", currentUser.getString("position"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(json);
                jsonArray.put(json2);


                try {
                    reqJsonObject.put("users", jsonArray); //각종 데이터 입력
                } catch (JSONException e) {
                    //JSON error
                }

                Log.e("room", String.valueOf(jsonArray));
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        "https://api.mankai.shop/api/room/create",
                        reqJsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("TAG", "onResponse:"+response);
                                Intent intent = new Intent(YouPage.this, ChatContainerActivity.class);
                                Log.e("room", String.valueOf(jsonArray));
                                try {
                                    System.out.println("room id " + response.getString("id"));
                                    intent.putExtra("room", new Room(response.getString("id"), "","", response.getString("type"), response.getString("users"), response.getString("updated_at")));
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
                Volley.newRequestQueue(YouPage.this).add(jsonObjectRequest);
            }
        });

    }




    public void Response(){
        StringRequest youPageStringRequest = new StringRequest(
                Request.Method.GET, url + userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("name", "onResponse:" +response);
                            jsonObject = new JSONObject(response);
                            userName = jsonObject.getString("name");
                            userDescription = jsonObject.getString("description");
                            userProfile = jsonObject.getString("profile");
                            Setting(userName,userDescription,userProfile);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null
        );
        AppHelper.requestQueue.add(youPageStringRequest);
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

                    } else{
                        Toast.makeText(YouPage.this,"토큰 만료 다시 로그인", Toast.LENGTH_SHORT).show();
                        System.out.println(response);
                    }
                } catch(JSONException err) {
                    err.printStackTrace();
                }
            }
        };
        UserRequest userRequest = new UserRequest(token,responseListener);
        RequestQueue queue = Volley.newRequestQueue(YouPage.this);
        queue.add(userRequest);
    }
    public void SendFollow(){
        // 팔로우, 팔로잉 버튼 눌렀을때 api에 요청보내기
        // 요청은 가는거 같은데 웹에서 변화없음 -> 요청이 안가는 건가?
        // 요청이 간다는걸 확인하고 화면 바꿔줘야함
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.mankai.shop/api/user/follow",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Response();

                    }
                }, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("to_user_id", userId);
                params.put("user_id", LoginUserId);
                Log.d("Sendfollow", String.valueOf(params));
                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }


    public void Setting(String userName,String userDescription,String userProfile){
        TextView userNameView = findViewById(R.id.userName);
        TextView userDescriptionView = findViewById(R.id.userDescription);
        CircleImageView userProfileView = (CircleImageView) findViewById(R.id.userProfile);


        userNameView.setText(userName);
        userDescriptionView.setText(userDescription);
        Glide.with(this).load(userProfile).placeholder(R.drawable.ic_launcher_foreground).dontAnimate().into(userProfileView);
    }


}