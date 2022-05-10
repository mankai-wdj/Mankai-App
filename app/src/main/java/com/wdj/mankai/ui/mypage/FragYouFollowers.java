package com.wdj.mankai.ui.mypage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.wdj.mankai.data.FollowerData;
import com.wdj.mankai.data.model.AppHelper;
import com.wdj.mankai.ui.mypage.RecyclerView.FollowersAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragYouFollowers extends Fragment {
    private View view;
    private ArrayList<FollowerData> list = new ArrayList<FollowerData>();
    private FollowerData followerData;

    private RecyclerView myFollowers_recyclerview;
    private FollowersAdapter adapter;

    String url;
    String userId;

    @NonNull
    public static FragYouFollowers newInstance(String param1, String param2) {
     FragYouFollowers fragYouFollowers = new FragYouFollowers();
        return fragYouFollowers;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frag_you_followers, container, false);

        url = "https://api.mankai.shop/api/followers/";

        myFollowers_recyclerview = view.findViewById(R.id.you_Followers_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL,false);
        myFollowers_recyclerview.setLayoutManager(layoutManager);

        adapter = new FollowersAdapter(list);
        myFollowers_recyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener(new FollowersAdapter.OnItemClickListener() {
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

//        if(getArguments() != null) {
//            userId = getArguments().getString("userId");
//            Log.i("ho", userId);
//        }

        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("userId", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId","");

        memoResponse(userId);

        return view;
    }

    public void memoResponse(String userId ) {

        StringRequest myPageStringRequest = new StringRequest(

                //userID만 activity에서 받아오면 됨
                Request.Method.GET, url+userId,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("responseInFollowers", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                followerData = new FollowerData(jsonObject.getString("name"), jsonObject.getString("profile"), jsonObject.getString("id"));
                                list.add(followerData);
                                Log.d("good", list.get(i).getId());
                            }
                            Log.d("sdf", list.toString());
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },null);

        myPageStringRequest.setShouldCache(false);
        AppHelper.requestQueue.add(myPageStringRequest);
    }


}