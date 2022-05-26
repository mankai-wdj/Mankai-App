package com.wdj.mankai.ui.chat;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ChatRoomListRequest extends StringRequest {
    final static  private String URL = "https://api.mankai.shop/api/rooms/";

    private Map<String,String> map;

    public ChatRoomListRequest(String ACCESS_TOKEN, int userId , Response.Listener<String> listener) {
        super(Method.GET,URL+userId,listener,null);
        map = new HashMap<>();
        map.put("Authorization", "Bearer "+ACCESS_TOKEN);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
