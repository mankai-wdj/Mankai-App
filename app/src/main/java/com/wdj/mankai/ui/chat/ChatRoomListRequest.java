package com.wdj.mankai.ui.chat;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ChatRoomListRequest extends StringRequest {
    final static  private String URL = "https://api.mankai.shop/api/rooms/"+8;

    private Map<String,String> map;

    public ChatRoomListRequest(int userId , Response.Listener<String> listener) {
        super(Method.GET,URL,listener,null);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
