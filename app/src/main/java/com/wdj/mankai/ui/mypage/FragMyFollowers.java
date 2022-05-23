package com.wdj.mankai.ui.mypage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

public class FragMyFollowers extends Fragment implements ViewPagerData {
    private View view;
    private String myFollowersData;
    private ArrayList<FollowerData> list = new ArrayList<FollowerData>();
    private FollowerData followerData;

    private RecyclerView myFollowers_recyclerview;
    private FollowersAdapter adapter;

    String url;
    String userId;

    public static FragMyFollowers newInstance(String myFollowersData){
        FragMyFollowers fragFollowers = new FragMyFollowers();
        return fragFollowers;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_myfollowers,container,false);

        url = "https://api.mankai.shop/api/followers/";

        myFollowers_recyclerview = view.findViewById(R.id.myFollowers_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL,false);
        myFollowers_recyclerview.setLayoutManager(layoutManager);

        adapter = new FollowersAdapter(list);
        myFollowers_recyclerview.setAdapter(adapter);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info",Context.MODE_PRIVATE);
        String user_info = sharedPreferences.getString("user_info", "");


        try {
            JSONObject object = new JSONObject(user_info);
            userId = object.getString("id");
            Log.d("text", userId);
        } catch (Throwable t) {

        }

        adapter.setOnItemClickListener(new FollowersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                list.get(position);
                Intent intent = new Intent(getActivity(), YouPage.class);
//                 user id 넣은 url 전송
                intent.putExtra("youURL", list.get(position).getId());
                startActivity(intent);

                // fragment 일 때 화면 전환
//                getParentFragmentManager().beginTransaction()
//                        .replace(R.id.my_page_head, new YouPageFragment())
//                        .commit();

            }
        });

        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getContext());

        memoResponse();

        return view;
    }

    public void memoResponse() {

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
                                followerData = new FollowerData(jsonObject.getString("name"), jsonObject.getString("profile"), jsonObject.getString("id"));
                                list.add(followerData);
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
