package com.wdj.mankai.ui.Board;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdj.mankai.R;
import com.wdj.mankai.data.BoardData;
import com.wdj.mankai.data.model.AppHelper;
import com.wdj.mankai.ui.main.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BoardCategoryActivity extends AppCompatActivity {

    private CategoryAdapter adapter;
    public static Activity CategoryActivity;

    private String[] CategoryList = {"전체","영화", "미술", "공연", "음악", "드라마", "연예인", "만화", "방송",
            "패션", "일상", "육아", "동물", "요리", "인테리어", "할인","게임", "스포츠","자동차",
            "취미","해외여행","국내여행","맛집","IT", "컴퓨터", "정치", "건강", "일본", "중국", "미국", "해외"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CategoryActivity = BoardCategoryActivity.this;

        setContentView(R.layout.board_category);
        Button backBtn = findViewById(R.id.backBtn);

        RecyclerView recyclerView = findViewById(R.id.categoryRecycle);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new CategoryAdapter(CategoryList);
        recyclerView.setAdapter(adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
               overridePendingTransition(R.anim.slide_wait,R.anim.slide_out_right);
            }
        });
    }

    public static void BackPage(){

    }


}
