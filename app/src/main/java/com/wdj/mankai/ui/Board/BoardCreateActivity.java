package com.wdj.mankai.ui.Board;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.wdj.mankai.R;
import com.wdj.mankai.data.CommentData;
import com.wdj.mankai.ui.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BoardCreateActivity extends AppCompatActivity {
    private TextView image_btn;
    private final int GET_GALLERY_IMAGE = 200;
    private RecyclerView boardImage;
    private TextView write_btn;
    private EditText boardContent;
    private ImageView back_image;
    public static ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    public static BoardCreateAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_create);

        boardContent = findViewById(R.id.content_edit);
        back_image = findViewById(R.id.backImage);
        image_btn = findViewById(R.id.inputImage);
        boardImage = findViewById(R.id.createRecycler);
        boardImage.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        adapter = new BoardCreateAdapter(mArrayUri);
        write_btn = findViewById(R.id.WriteBtn);
        boardImage.setAdapter(adapter);


        boardContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               if(charSequence.toString().length()==0)
               {
                   write_btn.setEnabled(false);
                   write_btn.setTextColor(Color.parseColor("#FFFFFF"));
                   write_btn.setBackgroundColor(getColor(R.color.black));
                   write_btn.setText("하고 싶은 말을 써주세요");
               }
               else{
                   write_btn.setEnabled(true);
                   write_btn.setTextColor(Color.parseColor("#FFFFFF"));
                   write_btn.setBackgroundColor(getColor(R.color.mankai_logo));
                   write_btn.setText("작성 완료!");
               }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_wait,R.anim.slide_out_right);
            }
        });

        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), GET_GALLERY_IMAGE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Create", "first:"+ data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null) {
            if(data.getData()!=null){
                mArrayUri.add(data.getData());
                adapter.notifyDataSetChanged();
                Log.d("Create", "onActivityResult:"+ data.getData());
            }
            else if(data.getClipData()!=null){
                ClipData mClipData = data.getClipData();
                for(int i = 0 ; i <mClipData.getItemCount();i++){
                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri uri = item.getUri();
                    mArrayUri.add(uri);
                }
                adapter.notifyDataSetChanged();
                for(int i =0;i<mArrayUri.size();i++){
                    Log.d("Create", "for loop = "+mArrayUri.get(i));
                }
            }

        }


        StringRequest request = new StringRequest(Request.Method.POST, "https://api.mankai.shop/api/upload_image",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },null);
    }
}