package com.wdj.mankai.ui.chat;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.wdj.mankai.data.model.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateRoomRequest extends StringRequest {
    final static  private String URL = "https://api.mankai.shop/api/room/create";
//final static  private String URL = "http://127.0.0.1:8000/api/room/create";

    private Map<String,String> map;

    public CreateRoomRequest(ArrayList<User> users, Response.Listener<String> listener) {
        super(Method.POST,URL,listener,null);
        map = new HashMap<>();
        map.put("users", "d");


    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
