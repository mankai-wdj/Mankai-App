package com.wdj.mankai.ui.Group;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdj.mankai.R;
import com.wdj.mankai.data.GroupNotices;
import com.wdj.mankai.data.model.AppHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragnotice extends Fragment {
    private View view;
    private String category_id;
    private FragNoticeAdapter fragNoticeAdapter;
    private ArrayList<GroupNotices> list = new ArrayList<>();
    private GroupNotices groupNotices;
    private String URL = "https://api.mankai.shop/api";
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    public static String group_id;

    Button btn1;


    public static Fragnotice newInstance() {
        Fragnotice fragnotice = new Fragnotice();
        return  fragnotice;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_frg_notice, container, false);
        category_id = getArguments().getString("category_id");
        group_id = getActivity().getIntent().getStringExtra("id");

        recyclerView = (RecyclerView) view.findViewById(R.id.group_info_notice);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);
        fragNoticeAdapter = new FragNoticeAdapter(getContext(),list);
        recyclerView.setAdapter(fragNoticeAdapter);


        if (AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getContext());

        StringRequest request = new StringRequest(Request.Method.POST, URL + "/show/groupnotice/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Group", response);
                        try {
                            Log.d("group", "onResponse: " +response);

                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject noticeJson = jsonArray.getJSONObject(i);
                                groupNotices = new GroupNotices(noticeJson);
                                list.add(groupNotices);
                                Log.d("group", "onResponse: "+groupNotices.getTitle());
                            }
                            fragNoticeAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("category_id",category_id);
                params.put("group_id",group_id);
                return params;
            }


        };
        AppHelper.requestQueue.add(request);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category_id = getArguments().getString("category_id");
    }
}
