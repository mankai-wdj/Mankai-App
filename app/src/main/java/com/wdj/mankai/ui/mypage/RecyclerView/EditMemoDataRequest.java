package com.wdj.mankai.ui.mypage.RecyclerView;

import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditMemoDataRequest extends VolleyMultipartRequest{

    private Map<String,String> stringMap;
    private Map<String,DataPart> newImageMap;
    private String uriString;

    public EditMemoDataRequest(int method, String url, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener, int memoId, String memoTitle,String memoContentText, ArrayList<Uri> uri) {
        super(method, url, listener, errorListener);
        stringMap = new HashMap<>();
        uriString = "";
        stringMap.put("memo_id",String.valueOf(memoId));
        stringMap.put("memo_title",memoTitle);
        stringMap.put("content_text",memoContentText);
        for(int i = 0 ; i < uri.size() ; i++) {
            if (uri.get(i).toString().startsWith("com")) {
                uriString = uriString + "," + uri.get(i);
            }
        }
        stringMap.put("url_images",uriString);

        newImageMap = new HashMap<>();
        /*uri이미지를 drawable에 bit맵으로 저장한뒤에 다시 넣어야 되는 것?*/

    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return stringMap;
    }

    @Override
    protected Map<String, DataPart> getByteData() throws AuthFailureError {
        return newImageMap;
    }
}
