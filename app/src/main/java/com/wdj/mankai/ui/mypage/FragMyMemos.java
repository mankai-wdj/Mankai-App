package com.wdj.mankai.ui.mypage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdj.mankai.R;
import com.wdj.mankai.data.MemoData;
import com.wdj.mankai.data.model.AppHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragMyMemos extends Fragment {
    private View view;
    private MyAdapter adapter;
    private RecyclerView recycleView;

    public static FragMyMemos newInstance(){
        FragMyMemos fragMyMemos = new FragMyMemos();
        return fragMyMemos;
    }

    private ArrayList<MemoData> list = new ArrayList<MemoData>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.GET, "https://api.mankai.shop/api/memo/37",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray memoarray = new JSONArray(response);
                            for(int i = 0 ;i<memoarray.length();i++){
                                JSONObject obj = memoarray.getJSONObject(i);
                                String ch = obj.getString("memo_title");
                                MemoData memoData = new MemoData(ch,"nooo");
                                list.add(memoData);
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_mymemos,container,false);
        recycleView = view.findViewById(R.id.recycleView);

        adapter = new MyAdapter(getActivity(), list);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleView.setAdapter(adapter);

        return view;
    }
}
