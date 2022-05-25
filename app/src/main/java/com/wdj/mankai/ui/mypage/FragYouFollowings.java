package com.wdj.mankai.ui.mypage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdj.mankai.R;
import com.wdj.mankai.data.FollowingData;
import com.wdj.mankai.data.model.AppHelper;
import com.wdj.mankai.ui.mypage.RecyclerView.FollowingsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragYouFollowings extends Fragment {
    private View view;

    private ArrayList<FollowingData> list = new ArrayList<FollowingData>();
    private FollowingData followingData;

    private RecyclerView youFollowings_recyclerview;
    private FollowingsAdapter followingsAdapter;

    String url;
    String userId;

    public FragYouFollowings() {
        // Required empty public constructor
    }

    public static FragYouFollowings newInstance() {
        FragYouFollowings fragYouFollowings = new FragYouFollowings();
        return fragYouFollowings;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frag_you_followers,container,false);
        url = "https://api.mankai.shop/api/follows/";

        youFollowings_recyclerview = view.findViewById(R.id.you_Followers_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        youFollowings_recyclerview.setLayoutManager(layoutManager);

        followingsAdapter = new FollowingsAdapter(list);
        youFollowings_recyclerview.setAdapter(followingsAdapter);



        followingsAdapter.setOnItemClickListener(new FollowingsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                list.get(position);
                Intent intent = new Intent(getActivity(), YouPage.class);
//                 user id 넣은 url 전송
                intent.putExtra("youURL", list.get(position).getId());
                startActivity(intent);
            }
        });

        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getContext());

        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("userId", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId","");

        memoResponse();

        return view;
    }
    private void memoResponse() {
        StringRequest myPageStringRequest = new StringRequest(
                Request.Method.GET, url+userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("responseInFollowers", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                followingData = new FollowingData(jsonObject.getString("name"), jsonObject.getString("profile"), jsonObject.getString("id"));
                                list.add(followingData);
                                Log.d("TEXT", list.get(i).getId());
                            }
                            followingsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },null);
        myPageStringRequest.setShouldCache(false);
        AppHelper.requestQueue.add(myPageStringRequest);
    }

}