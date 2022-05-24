package com.wdj.mankai.ui.Group;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.wdj.mankai.R;
import com.wdj.mankai.data.GroupData;
import com.wdj.mankai.ui.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fragboard extends Fragment {
    private View view;
    private String category_id;

    public static Fragboard newInstance() {
        Fragboard fragboard = new Fragboard();
        return  fragboard;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_frg_board, container, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category_id = getArguments().getString("category_id");


        StringRequest request = new StringRequest(Request.Method.GET,
                  "https://api.mankai.shop/api/show/groupboard/"+Groupinfor.group_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("group", "SNSBoard:" + response);

                    }
                },null){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("category",category_id);
                return params;
            }
        };


    }
}
