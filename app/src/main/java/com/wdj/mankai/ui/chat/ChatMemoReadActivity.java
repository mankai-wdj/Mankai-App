package com.wdj.mankai.ui.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.wdj.mankai.R;
import com.wdj.mankai.data.model.ChatMemo;
import com.wdj.mankai.ui.main.UserRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChatMemoReadActivity extends AppCompatActivity {
    TextView tvSnsMemoContent;
    EditText tvMemotitle;
    LinearLayout snsMemoLayout, boardMemoLayout;
    WebView boardMemoWebview;
    ImageSlider snsMemoSlider;
    Button btSaveMemo, btClose;
    String memo;
    JSONObject jsonObject;
    List<SlideModel> slideModels = new ArrayList<>();
    JSONObject currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_memo_read);

        tvMemotitle = findViewById(R.id.tvMemoTitle);
        tvSnsMemoContent = findViewById(R.id.tvSnsMemoContent);
        snsMemoLayout = findViewById(R.id.snsMemoLayout);
        boardMemoLayout = findViewById(R.id.boardMemoLayout);
        boardMemoWebview = findViewById(R.id.boardMemoWebview);
        snsMemoSlider = findViewById(R.id.snsMemoSlider);
        btSaveMemo = findViewById(R.id.btSaveMemo);
        btClose = findViewById(R.id.btClose);
        SharedPreferences sharedPreferences= getSharedPreferences("login_token", MODE_PRIVATE);
        String token = sharedPreferences.getString("login_token","");
        getUser(token);

        memo = getIntent().getStringExtra("memo");
        try {
            jsonObject = new JSONObject(memo);
            tvMemotitle.setText(jsonObject.getString("memo_title"));
            if(jsonObject.getString("type").equals("SNS")) {
                snsMemoLayout.setVisibility(View.VISIBLE);
                tvSnsMemoContent.setText(jsonObject.getString("content_text"));
                System.out.println(jsonObject);
                SNSMemoRequest(jsonObject.getInt("id"));

            }else {
                boardMemoLayout.setVisibility(View.VISIBLE);
                boardMemoWebview.loadData(jsonObject.getString("content_text"), "text/html", "UTF-8");

                WebSettings settings = boardMemoWebview.getSettings();
                settings.setDomStorageEnabled(true);

                boardMemoWebview.getSettings().setJavaScriptEnabled(true);
                boardMemoWebview.loadUrl("https://mankai.shop/boardmemo/"+jsonObject.getString("id"));
                boardMemoWebview.setWebChromeClient(new WebChromeClient());
                boardMemoWebview.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btSaveMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    storeMemo(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    //memo 저장
    private void storeMemo(JSONObject jsonObject) throws JSONException {
        JSONObject reqJson = new JSONObject();
        reqJson.put("user_id", currentUser.getString("id"));
        reqJson.put("content_text", jsonObject.getString("content_text"));
        reqJson.put("memo_title", tvMemotitle.getText());
        reqJson.put("memo_type", jsonObject.getString("type"));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                "https://api.mankai.shop/api/storememo",
                reqJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", "get memo list success");
                        System.out.println("resresrser"+response);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //통신 ERROR
                    }
                }
        );
        Volley.newRequestQueue(ChatMemoReadActivity.this).add(jsonObjectRequest);
    }

    // sns 메모 사진 가져오기
    public void SNSMemoRequest(int id){
        StringRequest SNSStringRRequest = new StringRequest(
                Request.Method.GET,
                "https://api.mankai.shop/api/getmemoimages/"+id,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.i("snsMemoImage",response);
                        JSONArray jsonArray = null;
                        try{
                            jsonArray = new JSONArray(response);
                            for(int i = 0 ; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                slideModels.add(new SlideModel(jsonObject.getString("url")));
                            }
                            snsMemoSlider.setImageList(slideModels, true);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error",""+error);
                    }
                }
        );

        Volley.newRequestQueue(ChatMemoReadActivity.this).add(SNSStringRRequest);
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


                    } else{
                        Toast.makeText(ChatMemoReadActivity.this,"토큰 만료 다시 로그인", Toast.LENGTH_SHORT).show();
                    }
                } catch(JSONException err) {
                    err.printStackTrace();
                }
            }
        };
        UserRequest userRequest = new UserRequest(token,responseListener);
        RequestQueue queue = Volley.newRequestQueue(ChatMemoReadActivity.this);
        queue.add(userRequest);
    }
}