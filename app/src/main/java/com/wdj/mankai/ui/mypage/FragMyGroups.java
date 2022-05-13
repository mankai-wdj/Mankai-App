package com.wdj.mankai.ui.mypage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdj.mankai.R;
import com.wdj.mankai.data.FollowerData;
import com.wdj.mankai.data.MyGroupData;
import com.wdj.mankai.data.model.AppHelper;
import com.wdj.mankai.ui.mypage.RecyclerView.MyGroupAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragMyGroups extends Fragment implements ViewPagerData{
    private View view;
    private ArrayList<MyGroupData> list = new ArrayList<MyGroupData>();
    private MyGroupData myGroupData;

    private MyGroupAdapter adapter;
    private RecyclerView myGroup_recyclerview;

    String url;
    String userId;

    public static FragMyGroups newInstance(){
        FragMyGroups fragMyGroups = new FragMyGroups();

        return fragMyGroups;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_mygroups,container,false);
        url = "https://api.mankai.shop/api/show/mygroup/";

        myGroup_recyclerview = view.findViewById(R.id.myGroup_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2, GridLayoutManager.VERTICAL, false);
        myGroup_recyclerview.setLayoutManager(gridLayoutManager);

        adapter = new MyGroupAdapter(list);
        myGroup_recyclerview.setAdapter(adapter);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String user_info = sharedPreferences.getString("user_info", "");


        try {
            JSONObject object = new JSONObject(user_info);
            userId = object.getString("id");
            Log.d("text", userId);
        } catch (Throwable t) {

        }

        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getContext());

        response();
        return view;
    }

    public void response() {
        StringRequest myPageStringRequest = new StringRequest(
                Request.Method.GET, url+userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("responseGroups", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                myGroupData = new MyGroupData(jsonObject.getString("id"), jsonObject.getString("name"), jsonObject.getString("onelineintro"), jsonObject.getString("category"), jsonObject.getString("logoImage"));
                                list.add(myGroupData);
                                Log.d("TEXT", list.get(i).getId());
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },null);
        myPageStringRequest.setShouldCache(false);
        AppHelper.requestQueue.add(myPageStringRequest);
    }

    @Override
    public void memoResponse(String response) {
        Log.d("response", response);
    }
}
