package com.wdj.mankai.ui.Group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Groupinfor extends AppCompatActivity {


    private ViewPager viewPager ;
    private FragmentPagerAdapter fragmentPagerAdapter;
    public static String group_id;
    private ArrayList<String> categoryName = new ArrayList<String>();
    private ArrayList<String> categoryType = new ArrayList<String>();
    private ArrayList<String> categoryId = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_infor);
        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(this);

        group_id = getIntent().getStringExtra("id");
        //뷰페이저 세팅
            viewPager = findViewById(R.id.viewpg);

            StringRequest request = new StringRequest(Request.Method.GET, "https://api.mankai.shop/api/show/detail_group/"+group_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Group", "onResponse: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("category");
//                            Log.d("Group", "for: " + jsonArray);
                            for(int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject mData = jsonArray.getJSONObject(i);
                                categoryName.add(mData.getString("title"));
                                categoryType.add(mData.getString("type"));
                                categoryId.add(mData.getString("id"));

                                Log.d("Group", "for: " + categoryName.get(i)+ categoryType.get(i));
                            }
                            TabLayout tabLayout = findViewById(R.id.tabLayout);
                            fragmentPagerAdapter = new ViewPageAdapter(getSupportFragmentManager(),categoryName,categoryType,categoryId);
                            viewPager.setAdapter(fragmentPagerAdapter);
                            tabLayout.setupWithViewPager(viewPager);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null);

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }


}