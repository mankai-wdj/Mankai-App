package com.wdj.mankai.ui.Group;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.wdj.mankai.R;
import com.wdj.mankai.data.model.AppHelper;
import com.wdj.mankai.ui.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Group;
import java.util.HashMap;
import java.util.Map;

public class FragGroupinfor extends Fragment {
    private View view;
    private WebView wb;
    private String group_id = Groupinfor.group_id;
    private ImageView group_image;
    private TextView group_intro_name,group_oneline,group_category,group_out_btn,group_user_count,group_in_btn;

    public static FragGroupinfor newInstance() {
        FragGroupinfor fragGroupinfor = new FragGroupinfor();
        return  fragGroupinfor;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_frg_groupinfor, container, false);
        wb = view.findViewById(R.id.group_intro);
        group_intro_name = view.findViewById(R.id.group_intro_name);
        group_oneline = view.findViewById(R.id.group_oneline);
        group_category= view.findViewById(R.id.group_category);
        group_image= view.findViewById(R.id.Groupimage);
        group_user_count = view.findViewById(R.id.group_user_count);
        group_in_btn = view.findViewById(R.id.group_in_btn);
        group_out_btn = view.findViewById(R.id.group_out_btn);

        wb.setWebViewClient(new WebViewClient());
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setDomStorageEnabled(true);
        wb.loadUrl("https://mankai.shop/groupintro/webview/"+group_id);


        group_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                그룹가입
                ClickIn();
            }
        });

        group_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickOut();
            }
        });


        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(this.getContext());

        ShowGroupData();
        return view;
    }

    public void ShowGroupData(){
        StringRequest request  = new StringRequest(Request.Method.GET, "https://api.mankai.shop/api/show/detail_group/"+group_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject group_data = jsonObject.getJSONObject("group");
                            group_intro_name.setText(group_data.getString("name"));
                            Groupinfor.group_top.setText(group_data.getString("name"));
                            group_oneline.setText(group_data.getString("onelineintro"));
                            group_category.setText(group_data.getString("category"));
                            Glide.with(getContext())
                                    .load(group_data.getString("logoImage"))
                                    .into(group_image);

                            Groupinfor.GroupCheck();
                            GroupInCheck();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("group", "onResponse: "+response);
                    }
                },null);
        AppHelper.requestQueue.add(request);

    }
    public void GroupInCheck(){
        StringRequest group_request = new StringRequest(Request.Method.GET, "https://api.mankai.shop/api/show/groupuser/"+group_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Group", "onResponse: "+response);
                        try {
                            JSONArray UserArray = new JSONArray(response);
                            for(int i = 0 ;i < UserArray.length();i++){
                                String group_user = UserArray.getJSONObject(i).getString("user_id");
                                group_user_count.setText(UserArray.length()+"");

                                if(group_user == MainActivity.userId){
                                    Log.d("Group", "onResponse: 가입되있음");
                                    group_in_btn.setVisibility(View.GONE);
                                    group_out_btn.setVisibility(View.VISIBLE);
                                    break;
                                }
                                group_in_btn.setVisibility(View.VISIBLE);
                                group_out_btn.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, null);
        AppHelper.requestQueue.add(group_request);
    }
    public void ClickIn(){
        StringRequest group_request = new StringRequest(Request.Method.POST, "https://api.mankai.shop/api/post/groupuser/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Groupinfor.GroupCheck();
                        GroupInCheck();
                    }
                }, null){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", MainActivity.userId);
                params.put("group_id",group_id);
                return params;
            }
        };
        AppHelper.requestQueue.add(group_request);
    }
    public void ClickOut(){
        StringRequest group_request = new StringRequest(Request.Method.POST, "https://api.mankai.shop/api/delete/groupuser/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Groupinfor.GroupCheck();
                        GroupInCheck();
                    }
                }, null){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", MainActivity.userId);
                params.put("group_id",group_id);
                return params;
            }
        };
        AppHelper.requestQueue.add(group_request);
    }





}
