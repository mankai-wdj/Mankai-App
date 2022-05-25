package com.wdj.mankai.ui.Group;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.wdj.mankai.R;
import com.wdj.mankai.data.model.AppHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GroupNoticeShow extends AppCompatActivity {
    private WebView wb;
    private String notice_id;
    private ImageView back_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_notice_show);
        wb = findViewById(R.id.notice_webview);
        back_btn = findViewById(R.id.backBtn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_wait,R.anim.slide_out_right);
            }
        });

        notice_id = getIntent().getStringExtra("id");
        wb.setWebViewClient(new WebViewClient());
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setDomStorageEnabled(true);
        wb.loadUrl("https://mankai.shop/groupboard/webview/"+notice_id);

        ShowUser();

    }

    public void ShowUser(){
        StringRequest request = new StringRequest(Request.Method.GET, "https://api.mankai.shop/api/show/groupnoticeweb/"+notice_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = new JSONArray(jsonObject).getString();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("notice", "onResponse: " + response);

                    }
                },null);

        AppHelper.requestQueue.add(request);
    }


}
