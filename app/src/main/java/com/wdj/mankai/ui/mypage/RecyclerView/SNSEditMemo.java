package com.wdj.mankai.ui.mypage.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdj.mankai.R;
import com.wdj.mankai.data.model.AppHelper;
import com.wdj.mankai.ui.login.LoginActivity;
import com.wdj.mankai.ui.login.LoginRequest;
import com.wdj.mankai.ui.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;

public class SNSEditMemo extends Activity {

    RecyclerView recyclerView;
    Button pick;
    int memoId;

    EditText memoTitleEt;
    EditText memoContentTextEt;
    String memoTitle;
    String memoContentText;
    Button editButton;
    Button deleteButton;

    ArrayList<Uri> uri = new ArrayList<>();
    SNSImageAdapter adapter;

    private static final  int Read_Permission = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snseditmemo);

        Intent intent = getIntent();
        memoId = intent.getExtras().getInt("memoId");

        memoTitleEt = findViewById(R.id.sns_edit_memo_title);
        memoContentTextEt = findViewById(R.id.sns_edit_memo_text);
        editButton = findViewById(R.id.sns_edit_memo_edit_btn);
        deleteButton = findViewById(R.id.sns_edit_memo_delete_btn);

        Log.i("memoTitle",intent.getStringExtra("memoTitle"));

        memoTitle = intent.getStringExtra("memoTitle");
        memoContentText = intent.getStringExtra("memoContentText");

        memoTitleEt.setText(memoTitle);
        memoContentTextEt.setText(memoContentText);

        recyclerView = findViewById(R.id.recyclerview_gallery_images);
        pick = findViewById(R.id.pick_image);

        adapter = new SNSImageAdapter(uri);
        recyclerView.setLayoutManager(new GridLayoutManager(SNSEditMemo.this,3));
        recyclerView.setAdapter(adapter);

        getImageFromDB(memoId);

        /*gallery에 접근할 때 권한허용요청 Dialog*/
        if(ContextCompat.checkSelfPermission(SNSEditMemo.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SNSEditMemo.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Read_Permission);
        }

        /*수정버튼*/
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("uri",""+uri);
//                EditMemoDataRequest loginRequest = new EditMemoDataRequest(1,"https://api.mankai.shop/api/updatememo",snsEditMemoData,null,memoId,memoTitle,memoContentText,uri);
//                AppHelper.requestQueue.add(loginRequest);
            }
        });



        /*삭제버튼*/
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRequest(memoId);
            }
        });

        /*이미지를 가져오기 위해 갤러리에 접근한다.*/
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                }
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });

    }

    Response.Listener<NetworkResponse> snsEditMemoData = new Response.Listener<NetworkResponse>() {
        @Override
        public void onResponse(NetworkResponse response) {
            Log.i("SNSMemoEdit성공","");
        }
    };

    /*갤러리에서 사진 선택후 데이터를 Intent data에서 uri를 받는다.*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK){
            /*여러장일 경우*/
            if(data.getClipData() != null){
                int x = data.getClipData().getItemCount();

                for(int i = 0;i<x;i++){
                    uri.add(data.getClipData().getItemAt(i).getUri());
                }
                adapter.notifyDataSetChanged();
            }

            /*한장일 경우*/
            else if (data.getData() != null){
                    Log.i("data.getData()",""+data.getData());
                    uri.add(data.getData());
                    adapter.notifyDataSetChanged();
            }

        }
    }

    /*삭제요청*/
    public void deleteRequest(int memoId){
        StringRequest SNSDeleteRequest = new StringRequest(
                Request.Method.POST,
                "https://api.mankai.shop/api/deletememos/" + memoId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        finish();
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("delete오류","");
                    }
                }

        );
        SNSDeleteRequest.setShouldCache(false);
        AppHelper.requestQueue.add(SNSDeleteRequest);
    }






    public void getImageFromDB(int memoId){

        StringRequest getImageRequest = new StringRequest(
                Request.Method.GET,
                "https://api.mankai.shop/api/getmemoimages/" + memoId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("사진url",response);
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            for(int i = 0 ; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String jsonObjectURL = jsonObject.getString("url");
                                uri.add(Uri.parse(jsonObjectURL));
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
                        Log.i("사진을 못 받아옴","");
                    }
                }
        );

        getImageRequest.setShouldCache(false);
        AppHelper.requestQueue.add(getImageRequest);
    }
}
