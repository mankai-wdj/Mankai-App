package com.wdj.mankai.ui.Group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.wdj.mankai.R;
import com.wdj.mankai.data.model.AppHelper;
import com.wdj.mankai.ui.main.HomeFragment;
import com.wdj.mankai.ui.main.MainActivity;
import com.wdj.mankai.ui.mypage.toolbar.FragMyMemoExceptToolbar;
import com.wdj.mankai.ui.mypage.toolbar.FragMyMemoToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

public class Groupinfor extends AppCompatActivity {


    private ViewPager viewPager ;
    private static FragmentPagerAdapter fragmentPagerAdapter;
    public static String group_id;
    public static TextView group_top;
    private ImageView back_btn;
    private static ArrayList<String> categoryName = new ArrayList<String>();
    private static ArrayList<String> categoryType = new ArrayList<String>();
    private static ArrayList<String> categoryId = new ArrayList<String>();
    private ArrayList<String> GroupUser = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_infor);

        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(this);

        back_btn = findViewById(R.id.backImage);
        group_top = findViewById(R.id.group_top_text);
        group_id = getIntent().getStringExtra("id");
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewpg);
        fragmentPagerAdapter = new ViewPageAdapter(getSupportFragmentManager(),categoryName,categoryType,categoryId);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
                overridePendingTransition(R.anim.slide_wait,R.anim.slide_out_right);
            }
        });

//        GroupCheck();
        //뷰페이저 세팅
    }

    public static void CheckCategory(){
        StringRequest request = new StringRequest(Request.Method.GET, "https://api.mankai.shop/api/show/detail_group/"+group_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Group", " 카테고리 체크 " + response);
                        try {
                            ArrayList<String> cnt1 = new ArrayList<String>();
                            ArrayList<String> cnt2 = new ArrayList<String>();
                            ArrayList<String> cnt3 = new ArrayList<String>();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("category");
                            for(int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject mData = jsonArray.getJSONObject(i);
                                categoryName.add(mData.getString("title"));
                                categoryType.add(mData.getString("type"));
                                categoryId.add(mData.getString("id"));

                                Log.d("Group", " 카테고리 체크 " + mData.getString("title"));
                            }
                            fragmentPagerAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null);
        AppHelper.requestQueue.add(request);
    }

    public static void GroupCheck(){
        categoryName.clear();
        categoryType.clear();
        categoryId.clear();
        fragmentPagerAdapter.notifyDataSetChanged();

        StringRequest group_request = new StringRequest(Request.Method.GET, "https://api.mankai.shop/api/show/groupuser/"+group_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Group", "onResponse: "+response);
                        try {
                            JSONArray UserArray = new JSONArray(response);
                            for(int i = 0 ;i < UserArray.length();i++){
                                String group_user = UserArray.getJSONObject(i).getString("user_id");
                                if(group_user == MainActivity.userId){
                                    Log.d("Group", "infor 가입되있음" );
                                    CheckCategory();
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, null);
        AppHelper.requestQueue.add(group_request);
    }


}