package com.wdj.mankai.ui.Board;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.wdj.mankai.R;
import com.wdj.mankai.data.BoardData;
import com.wdj.mankai.data.CommentData;
import com.wdj.mankai.data.model.AppHelper;
import com.wdj.mankai.ui.main.HomeFragment;
import com.wdj.mankai.ui.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BoardCommentActivity extends AppCompatActivity {
    private CommentAdapter adapter;
    public static Activity commentActivity;

    private String board_id;
    private String content_text;
    private String user_name;
    private String user_profile;
    private TextView sns_text,translate_text ;
    private TextView sns_name ;
    private ImageView sns_profile ;
    private ImageView back_image;
    private TextView comment_count;
    private TextView comment_btn_count;
    private TextView like_count;
    private int CurrentPage=1;
    private int board_count;
    private ImageView comment_image;
    private String isGroup;
    private String sub;
    private int type;
    private ImageView trans_image;
    private EditText comment_edit;
    private  ArrayList<CommentData> list = new ArrayList<CommentData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commentActivity = BoardCommentActivity.this;

//        초기 선언
        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.board_comment);
        sns_text = findViewById(R.id.snsCommentContent);
        sns_profile=findViewById(R.id.snsCommentUserImage);
        sns_name=findViewById(R.id.snsCommentName);
        back_image=findViewById(R.id.backImage);
        comment_count=findViewById(R.id.CommentCount);
        comment_btn_count=findViewById(R.id.commentBtn);
        like_count=findViewById(R.id.likeCnt);
        comment_image = findViewById(R.id.comment_btn);
        comment_edit = findViewById(R.id.commentText);
        trans_image = findViewById(R.id.translate_btn);
        translate_text = findViewById(R.id.translate_text);

//      Extra로 값 넘겨 받아서 보여줌
        board_id = getIntent().getStringExtra("id");
        isGroup = getIntent().getStringExtra("isGroup");
        content_text = getIntent().getStringExtra("content");
        user_name = getIntent().getStringExtra("name");
        user_profile = getIntent().getStringExtra("profile");
        like_count.setText(getIntent().getStringExtra("like_count"));
        board_count = getIntent().getIntExtra("board_count",0);
        isGroup = getIntent().getStringExtra("isGroup");

        sns_text.setText(content_text);
        sns_name.setText(user_name);
        Glide.with(this)
                .load(user_profile)
                .centerCrop()
                .into(sns_profile);

        if(isGroup.equals("SNS")){
            sub = "comment";
            type = Request.Method.POST;
        }
        else {
            sub = "groupcomment";
            type = Request.Method.GET;
        }

//        댓글 리스트
        RecyclerView recyclerView = findViewById(R.id.commentRecycle);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new CommentAdapter(list);
        recyclerView.setAdapter(adapter);
        comment_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendComment(comment_edit.getText().toString());
                Log.d("Comment", "Send Message " + comment_edit.getText());
            }
        });
        trans_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String translate;
                translate = PapagoTranslate.getTranslation(content_text,"ko");
                translate_text.setText(translate);
                translate_text.setVisibility(View.VISIBLE);
            }
        });


        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Board", "Back Click");
                finish();
                overridePendingTransition(R.anim.slide_wait,R.anim.slide_out_right);
            }
        });
//      시작 호출
        GETCOMMENT();

        Log.d("Comment", "user_id?: "+ MainActivity.userId);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == list.size()-1) {
                    Log.d("Board", "1이상 이라 작동");
                    GETCOMMENT();
                }
            }
        });

    }
    public void GETCOMMENT(){
        StringRequest request = new StringRequest(type, "https://api.mankai.shop/api/show/"+sub+"/"+board_id+"?page="+CurrentPage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            comment_count.setText("댓글 ("+jsonObject.getString("total")+")");
                            comment_btn_count.setText(jsonObject.getString("total"));

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for(int i = 0 ; i< jsonArray.length() ;i ++){
                                CommentData commentData = new CommentData(jsonArray.getJSONObject(i));
                                list.add(commentData);
                            }
                            adapter.notifyItemRangeChanged(adapter.getItemCount(),jsonArray.length());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },null);

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
        CurrentPage+=1;
    }
    public void SendComment(String Text){
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.mankai.shop/api/post/"+sub,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        comment_edit.setText("");
                        list.clear();
                        CurrentPage=1;
                        GETCOMMENT();
                        try {
                            String listString = HomeFragment.list.get(board_count).getComment_length();
                            int cnt = Integer.parseInt(listString)+1;
                            listString = Integer.toString(cnt);
                            HomeFragment.list.get(board_count).setComment_length(listString);
                            HomeFragment.adapter.notifyItemChanged(board_count);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("board_id", board_id);
                params.put("user_id", MainActivity.userId);
                params.put("content",Text);
                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

}
