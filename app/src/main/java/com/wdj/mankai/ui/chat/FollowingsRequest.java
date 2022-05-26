package com.wdj.mankai.ui.chat;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FollowingsRequest extends StringRequest {
    final static  private String URL = "https://api.mankai.shop/api/follows/";

    private Map<String,String> map;

    public FollowingsRequest( String userId , Response.Listener<String> listener) {
        super(Method.GET,URL+userId,listener,null);
        map = new HashMap<>();

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}