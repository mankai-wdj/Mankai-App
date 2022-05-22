package com.wdj.mankai.ui.main;

import static org.webrtc.ContextUtils.getApplicationContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdj.mankai.R;
import com.wdj.mankai.data.GroupData;
import com.wdj.mankai.data.model.AppHelper;
import com.wdj.mankai.ui.Board.BoardActivity;
import com.wdj.mankai.ui.Group.GroupActivity;
import com.wdj.mankai.ui.Group.GroupAdapter;
import com.wdj.mankai.ui.Group.GroupMake2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroupFragment extends Fragment {

    private View view;

    Button btn1, btn2, btn3;
    TextView groupmake;

    //리사이클1 : 로그인 한 ID의 가입 그룹 리스트
    //리사이클2 : 전체 그룹 리스트
    private RecyclerView recyclerView, recyclerView2;
    private LinearLayoutManager layoutManager, layoutManager2;
    private String LoginUserId;
    private GridLayoutManager testmanager;

    private GroupAdapter adapter, adapter2;
    private ArrayList<GroupData> list = new ArrayList<>();
    private ArrayList<GroupData> list2 = new ArrayList<>();
    private GroupData groupData, groupData2;
    private String URL = "https://api.mankai.shop/api";
    private String URL2 = "https://api.mankai.shop/api";

    String userId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_group, container, false);

        // 그룹 생성 페이지 이동
        groupmake = (TextView) view.findViewById(R.id.groupmake);
        groupmake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GroupMake2.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.group_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);
        adapter = new GroupAdapter(getContext(),list);
        recyclerView.setAdapter(adapter);

        if (AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getContext());

        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("userId", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId","");

        StringRequest request = new StringRequest(Request.Method.GET,
                URL + "/show/mygroup/"+MainActivity.userId,
                //url 끌고와서 다시 배열로 제작후 각각의 함수에 넣어 줌
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Group", response);
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
        AppHelper.requestQueue.add(request);



        recyclerView2 = (RecyclerView) view.findViewById(R.id.group_recycler_view2);
        recyclerView2.setHasFixedSize(true);
        testmanager = new GridLayoutManager(this.getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView2.setLayoutManager(testmanager);
        recyclerView2.scrollToPosition(0);
        adapter2 = new GroupAdapter(getContext(),list2);
        recyclerView2.setAdapter(adapter2);

        if (AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getContext());


        StringRequest request2 = new StringRequest(Request.Method.GET,
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
                                groupData2 = new GroupData(groupJson);
                                list2.add(groupData2);
                            }
                            adapter2.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },null);
        AppHelper.requestQueue.add(request2);



        return view;
    }
}
