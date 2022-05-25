package com.wdj.mankai.ui.mypage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.wdj.mankai.ui.main.MyPageFragment;
import com.wdj.mankai.ui.mypage.RecyclerView.FollowersAdapter;
import com.wdj.mankai.ui.mypage.toolbar.FragMyMemoExceptToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Struct;
import java.util.ArrayList;

public class FragYouFollowers extends Fragment {
    private View view;
    private ArrayList<FollowerData> list = new ArrayList<FollowerData>();
    private FollowerData followerData;

    private RecyclerView myFollowers_recyclerview;
    private FollowersAdapter adapter;

    String url;
    String userId;
    String LoginUserId;
    static final String PASS = "pass";

    public interface OnInputListener {
        void sendInput(String input);
    }

    public OnInputListener mOnInputListener;

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

        Log.d("123456789", String.valueOf(list));

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

        // 상대방 페이지 유저 ID
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("userId", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId","");
        Log.d("userId",userId);

        // 로그인한 유저 ID
        SharedPreferences sharedPreferencesMy = getActivity().getSharedPreferences("user_info",Context.MODE_PRIVATE);
        String user_info = sharedPreferencesMy.getString("user_info", "");


        try {
            JSONObject object = new JSONObject(user_info);
            LoginUserId = object.getString("id");
            Log.d("LoginUserId", LoginUserId);
        } catch (Throwable t) {

        }

        memoResponse(userId);


        return view;
    }
    @Override public void onAttach(Context context)
    {
        super.onAttach(context);
        try {
            mOnInputListener
                    = (OnInputListener)getActivity();
        }
        catch (ClassCastException e) {
            Log.e("TAG", "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }
    public void memoResponse(String userId ) {

//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("followCheck", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor= sharedPreferences.edit();
//        YouPage youpage = new YouPage();

        StringRequest myPageStringRequest = new StringRequest(
                Request.Method.GET, url+userId,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("responseInFollowers", response);
                        try {
                            Log.i("youFollowerData받기","");
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                followerData = new FollowerData(jsonObject.getString("name"), jsonObject.getString("profile"), jsonObject.getString("id"));
                                list.add(followerData);
                                Log.d("good", list.get(i).getId());

                                // 만약 상대 페이지 팔로우 id에 로그인한 유저 id가 있을 경우
                                if (list.get(i).getId().equals(LoginUserId)){
                                    // YouPage에 팔로우 되있는 상태라는 걸 알려줘야함
                                    Log.d("check", userId+"팔로우듕");

                                    mOnInputListener.sendInput(PASS);

//                                    editor.putString("followCheck",PASS);
//                                    editor.commit();

//                                    Log.d("check", String.valueOf(sharedPreferences));
                                    break;
                                }else{
                                    Log.d("이거아님", "이거아님");
//                                    editor.clear();
//                                    editor.commit();
//
//                                    youpage.followChekck();
                                }
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