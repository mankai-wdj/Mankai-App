package com.wdj.mankai.ui.Group;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.wdj.mankai.R;
import com.wdj.mankai.data.model.AppHelper;
import com.wdj.mankai.ui.Board.BoardCommentActivity;
import com.wdj.mankai.ui.Board.BoardCreateActivity;
import com.wdj.mankai.ui.mypage.YouPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GroupNoticeShow extends AppCompatActivity {
    private WebView wb;
    private String notice_id;
    private ImageView back_btn,noticeImage;
    private TextView noticeName;
    private Context mContext;
    private LinearLayout layout;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_notice_show);
        mContext = this;
        layout = findViewById(R.id.linear3);
        wb = findViewById(R.id.notice_webview);
        back_btn = findViewById(R.id.backBtn);
        noticeName = findViewById(R.id.notice_name);
        noticeImage = findViewById(R.id.noticeImage);
        intent = new Intent(mContext, YouPage.class);
        Bundle bundle = ActivityOptions.makeCustomAnimation(mContext, R.anim.slide_in_right, R.anim.slide_wait).toBundle();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                overridePendingTransition(R.anim.slide_wait,R.anim.slide_out_right);
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent,bundle);
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
        StringRequest request = new StringRequest(Request.Method.GET, "https://api.mankai.shop/api/show/groupnoticeweb/user/"+notice_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            jsonArray.getJSONObject(0).getString("name");
                            Log.d("notice", "onResponse: "+
                                    jsonArray.getJSONObject(0).getString("name"));
                            noticeName.setText(jsonArray.getJSONObject(0).getString("name"));
                            Glide.with(mContext)
                                    .load(jsonArray.getJSONObject(0).getString("profile"))
                                    .into(noticeImage);
                            intent.putExtra("youURL",jsonArray.getJSONObject(0).getString("id"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("notice", "onResponse: " + response);

                    }
                },null);

        AppHelper.requestQueue.add(request);
    }


}
