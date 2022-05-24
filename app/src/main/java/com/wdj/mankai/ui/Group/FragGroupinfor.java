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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.wdj.mankai.R;
import com.wdj.mankai.data.model.AppHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Group;

public class FragGroupinfor extends Fragment {
    private View view;
    private WebView wb;
    private String group_id = Groupinfor.group_id;
    private ImageView group_image;
    private TextView group_intro_name,group_oneline,group_category;

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

        wb.setWebViewClient(new WebViewClient());
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setDomStorageEnabled(true);
        wb.loadUrl("https://mankai.shop/groupintro/webview/"+group_id);


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
//                            group_data.getString("logoImage");
                            group_intro_name.setText(group_data.getString("name"));
                            Groupinfor.group_top.setText(group_data.getString("name"));
                            group_oneline.setText(group_data.getString("onelineintro"));
                            group_category.setText(group_data.getString("category"));
                            Glide.with(getContext())
                                    .load(group_data.getString("logoImage"))
                                    .into(group_image);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("group", "onResponse: "+response);
                    }
                },null);
        AppHelper.requestQueue.add(request);
    }





}
