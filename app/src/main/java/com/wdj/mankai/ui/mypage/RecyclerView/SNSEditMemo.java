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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wdj.mankai.R;
import com.wdj.mankai.data.model.AppHelper;
import com.wdj.mankai.ui.main.MainActivity;

import java.util.ArrayList;

public class SNSEditMemo extends Activity {

    RecyclerView recyclerView;
    Button pick;
    int memoId;

    EditText memoTitleEt;
    EditText memoContentTextEt;
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

        memoTitleEt.setText(intent.getStringExtra("memoTitle"));
        memoContentTextEt.setText(intent.getStringExtra("memoContentText"));

        recyclerView = findViewById(R.id.recyclerview_gallery_images);
        pick = findViewById(R.id.pick_image);

        adapter = new SNSImageAdapter(uri);
        recyclerView.setLayoutManager(new GridLayoutManager(SNSEditMemo.this,3));
        recyclerView.setAdapter(adapter);

        /*gallery에 접근할 때 권한허용요청 Dialog*/
        if(ContextCompat.checkSelfPermission(SNSEditMemo.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SNSEditMemo.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Read_Permission);
        }

        /*수정버튼*/
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("일단보류","");
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
}
