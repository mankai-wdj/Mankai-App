package com.wdj.mankai.ui.Board;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.wdj.mankai.R;
import com.wdj.mankai.data.BoardData;
import com.wdj.mankai.data.CommentData;
import com.wdj.mankai.data.model.AppHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class BoardCommentActivity extends AppCompatActivity {
    private CommentAdapter adapter;
    public static Activity commentActivity;

    private String board_id;
    private String content_text;
    private String user_name;
    private String user_profile;
    private TextView sns_text ;
    private TextView sns_name ;
    private ImageView sns_profile ;

    private  ArrayList<CommentData> list = new ArrayList<CommentData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commentActivity = BoardCommentActivity.this;

        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(this);

//      Extra로 값 넘겨 받아서 보여줌
        setContentView(R.layout.board_comment);
        sns_text = findViewById(R.id.snsCommentContent);
        sns_profile=findViewById(R.id.snsCommentUserImage);
        sns_name=findViewById(R.id.snsCommentName);
        board_id = getIntent().getStringExtra("id");
        content_text = getIntent().getStringExtra("content");
        user_name = getIntent().getStringExtra("name");
        user_profile = getIntent().getStringExtra("profile");
        Log.d("Board", content_text);
        sns_text.setText(content_text);
        sns_name.setText(user_name);
        Glide.with(this)
                .load(user_profile)
                .centerCrop()
                .into(sns_profile);


//        댓글 리스트
        RecyclerView recyclerView = findViewById(R.id.commentRecycle);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new CommentAdapter(list);
        recyclerView.setAdapter(adapter);

        StringRequest request = new StringRequest(Request.Method.POST, "https://api.mankai.shop/api/show/comment/" + board_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for(int i = 0 ; i< jsonArray.length() ;i ++){
                                CommentData commentData = new CommentData(jsonArray.getJSONObject(i));
                                list.add(commentData);
                            }
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },null);

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }
}
