package com.wdj.mankai.ui.Group;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdj.mankai.R;
import com.wdj.mankai.data.GroupData;
import com.wdj.mankai.data.model.AppHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// 이거는 이제 안쓰는 파일임 그냥 테스트용
public class GroupActivity extends AppCompatActivity {

    private GroupAdapter adapter;
    private ArrayList<GroupData> list = new ArrayList<>();
    private GroupData groupData;
    private String URL = "https://api.mankai.shop/api";


    @Override
    protected void onCreate(Bundle savedInstanceState) {    // 액티비티 시작시 처음으로 실행
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_group);

        RecyclerView recyclerView = findViewById(R.id.group_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        adapter = new GroupAdapter(list);
        recyclerView.setAdapter(adapter);

        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
            AppHelper.requestQueue.add(request);

        }
        StringRequest request = new StringRequest(Request.Method.GET,
                URL + "/show/group/NULLDATA",
            //url 끌고와서 다시 배열로 제작후 각각의 함수에 넣어 줌
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.d("Group", response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject groupJson = jsonArray.getJSONObject(i);
                            groupData = new GroupData(groupJson);
                            list.add(groupData);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },null);






}

