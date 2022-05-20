package com.wdj.mankai.ui.mypage;

import android.content.Context;
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
import com.wdj.mankai.data.model.AppHelper;
import com.wdj.mankai.ui.mypage.RecyclerView.Code;
import com.wdj.mankai.ui.mypage.RecyclerView.Memo;
import com.wdj.mankai.ui.mypage.RecyclerView.MemoAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;

public class FragMyMemos extends Fragment{
    private View view;
    RecyclerView memoRecyclerView;
    MemoAdapter adapter;
    String userId;
    ArrayList<Memo> memos = new ArrayList<>();
    boolean notfirst;


    public static FragMyMemos newInstance(){
        FragMyMemos fragMyMemos = new FragMyMemos();

        return fragMyMemos;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_mymemos,container,false);
        adapter = new MemoAdapter(memos);
        memoRecyclerView = view.findViewById(R.id.mymemo_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        memoRecyclerView.setLayoutManager(layoutManager);
        memoRecyclerView.setAdapter(adapter);
        notfirst = false;


        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String user_info = sharedPreferences2.getString("user_info","");

        JSONObject obj = null;
        try {
            obj = new JSONObject(user_info);
            userId = obj.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getActivity());
        }

        memoResponse(userId);


        return view;
    }

    @Override
    public void onResume() {
        if(notfirst) {
            memos.clear();
            memoResponse(userId);
        }
        notfirst = true;
        super.onResume();
    }

    public void memoResponse(String userId){
            StringRequest myMemoStringRequest = new StringRequest(
                    Request.Method.GET,
                    "https://api.mankai.shop/api/memo/"+userId,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("memoResponse",response);
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(response);
                                for(int i = 0 ; i< jsonArray.length();i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.i("post_type",jsonObject.getString("type"));
                                    if(jsonObject.getString("type").equals("SNS")){
                                        SNSMemoRequest(jsonObject.getInt("id"),Code.ViewType.SNS_VIEWTYPE,jsonObject.getString("memo_title"),jsonObject.getString("content_text"));
                                    }
                                    else{
                                        Memo memo = new Memo(jsonObject.getInt("id"),Code.ViewType.BOARD_VIEWTYPE,jsonObject.getString("memo_title"),jsonObject.getString("content_text"),null);
                                        Log.i("메모 갯수. 2개떠야됨","");
                                        memos.add(memo);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                             }
                        }
                        },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("error",""+error);
                        }
                    });

            myMemoStringRequest.setShouldCache(false);
            AppHelper.requestQueue.add(myMemoStringRequest);
            /*myPageRequest지우자*/
        }

    public void SNSMemoRequest(int id, int viewType, String memo_title, String content_text){
        Log.i("SNSMemoRequest",content_text);
        StringRequest SNSStringRRequest = new StringRequest(
                Request.Method.GET,
                "https://api.mankai.shop/api/getmemoimages/"+id,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.i("snsMemoImage",response);
                        ArrayList<String> imageUrl = new ArrayList<>();
                        JSONArray jsonArray = null;
                        try{
                            jsonArray = new JSONArray(response);
                            for(int i = 0 ; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                imageUrl.add(jsonObject.getString("url"));
                            }
                            Memo memo = new Memo(id,viewType,memo_title,content_text,imageUrl);
                            memos.add(memo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error",""+error);
                    }
                }
        );

        SNSStringRRequest.setShouldCache(false);
        AppHelper.requestQueue.add(SNSStringRRequest);
    }

}
