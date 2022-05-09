package com.wdj.mankai.ui.mypage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class YouPage extends AppCompatActivity {

    private ViewPagerAdapter viewPagerAdapter;
    String userName;
    String userDescription;
    String userProfile;
    String userId;
    boolean first = true;
    String url;


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

        url = "https://api.mankai.shop/api/follow/";



        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(this);

       Response();


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

    public void Setting(String userName,String userDescription,String userProfile){
        TextView userNameView = findViewById(R.id.userName);
        TextView userDescriptionView = findViewById(R.id.userDescription);
        CircleImageView userProfileView = (CircleImageView) findViewById(R.id.userProfile);

        userNameView.setText(userName);
        userDescriptionView.setText(userDescription);
        Glide.with(this).load(userProfile).placeholder(R.drawable.ic_launcher_foreground).dontAnimate().into(userProfileView);
    }

}