package com.wdj.mankai.ui.Board;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BoardActivity extends AppCompatActivity {

    private String URL = "https://api.mankai.shop/api";
    private ArrayList<BoardData> list = new ArrayList<BoardData>();
    private BoardData boardData;
    private BoardAdapter adapter;
    private int CurrentPage = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        RequestFuture<JSONObject> future = RequestFuture.newFuture();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new BoardAdapter(list);
        recyclerView.setAdapter(adapter);

        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());

        this.GETBOARD("/board/show/전체/?page="+CurrentPage);
        CurrentPage+=1;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == list.size() -1 ) {
                        GETBOARD("/board/show/전체/?page="+CurrentPage);
                        CurrentPage+=1;
                    }

            }
        });

    }


    public void GETBOARD(String subPoint) {
//        1차 기본 보드 데이터
        StringRequest request = new StringRequest(Request.Method.POST, URL+subPoint,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray BoardJsonArray = json.getJSONArray("data");

//                          보드데이터 별로 하나씩 요청
                            for (int i = 0; i < BoardJsonArray.length(); i++) {
                                JSONObject boardJson = BoardJsonArray.getJSONObject(i);
                                boardData = new BoardData(boardJson);
//                               2차 데이터 처리
                                StringRequest subData = new StringRequest(Request.Method.GET,
                                        URL + "/upload_image/" + boardJson.getString("id"),
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject subJson = new JSONObject(response);
                                                    JSONArray subArray = subJson.getJSONArray("images");
                                                    if(subArray.length()==0){
                                                        for(int i =0 ;i<list.size();i++)
                                                        {
                                                            if(list.get(i).getId() == boardJson.getString("id")){
                                                                list.get(i).setViewType(0);
                                                                Log.d("Board", "사진 없음 = " + i);
                                                                adapter.notifyItemChanged(i);
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    else{
                                                        String  saveJson = subArray.getJSONObject(0).getString("url");
                                                        for(int i =0 ;i<list.size();i++)
                                                        {
                                                            if(list.get(i).getId() == boardJson.getString("id")){
                                                                list.get(i).setBoardImage(saveJson);
                                                                list.get(i).setViewType(1);
                                                                Log.d("Board", "사진 있음 =" +i);
                                                                adapter.notifyItemChanged(i);
                                                                break;
                                                            }
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, null);
                                AppHelper.requestQueue.add(subData);
                                list.add(boardData);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },null);
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }
}
