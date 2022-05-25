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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.wdj.mankai.R;
import com.wdj.mankai.data.model.AppHelper;
import com.wdj.mankai.ui.main.MyPageFragment;
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


        SharedPreferences sharedPreferences= getSharedPreferences("userId", MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("userId",userId);
        editor.commit();

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
            JSONObject object = new JSONObject(user_info);
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

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendFollow();
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

    }




    public void Response(){
        StringRequest youPageStringRequest = new StringRequest(
                Request.Method.GET, url + userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("name", "onResponse:" +response);
                            JSONObject jsonObject = new JSONObject(response);
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