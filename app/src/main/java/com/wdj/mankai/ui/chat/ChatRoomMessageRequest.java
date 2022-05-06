package com.wdj.mankai.ui.chat;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class ChatRoomMessageRequest extends StringRequest {
    final static  private String URL = "https://api.mankai.shop/api/messages/";

    private Map<String,String> map;

    public ChatRoomMessageRequest(String ACCESS_TOKEN, String roomId, String userId , Response.Listener<String> listener) {
        super(Method.GET,URL+roomId+'/'+userId,listener,null);
        map = new HashMap<>();
        map.put("Authorization", "Bearer "+ACCESS_TOKEN);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}