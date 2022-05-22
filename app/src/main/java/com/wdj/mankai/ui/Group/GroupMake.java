package com.wdj.mankai.ui.Group;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wdj.mankai.R;
import com.wdj.mankai.data.model.AppHelper;
import com.wdj.mankai.ui.main.MainActivity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GroupMake extends AppCompatActivity {

    private final int GALLERY_REQ_CODE = 1000;

    private final int GET_GALLERY_IMAGE = 200;

    private static final String URL = "https://api.mankai.shop/api";
    ImageView groupimagebtn, groupimage; //그룹 이미지
    LinearLayout grouplayout;
    String Category, password; //그룹 카테고리, 그룹 비번
    EditText grouponelineintro, groupname; //그룹 한줄 소개, 그룹 이름
    ImageButton btnback; //이전 페이지 이동
    TextView text_sheef; //그룹 비번 설정 모달
    Button group_submit; //그룹 저장(DB로 저장)


    // 그룹 생성(이름, 한줄소개, 비번설정 등등)
    @Override
    protected void onCreate(Bundle savedInstanceState) {    // 액티비티 시작시 처음으로 실행
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmake);
        grouplayout = findViewById(R.id.groupmake_layout);
        groupimage = findViewById(R.id.groupmake_image); //여기에 갤러리에서 받아온 이미지가 출력됨
        groupimage.setVisibility(View.GONE);

        grouponelineintro = findViewById(R.id.grouponelineintro); //한줄소개
        groupname = findViewById(R.id.MakeGroupname); //그룹 이름

        btnback = findViewById(R.id.groupback);//이전 페이지 이동
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //카테고리 선택 한 값 받아오는 곳
        Intent intent = getIntent();
        Category = intent.getExtras().getString("Category");

        //비번 설정 창
        text_sheef = findViewById(R.id.groupmake_sheef);
        final BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(getApplicationContext());
        text_sheef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });

        //그룹 생성 정보들을 이제 DB로 저장하는 것
        group_submit = findViewById(R.id.group_make_submit);
        group_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SharedPreferences sharedPreferences = getSharedPreferences("password", MODE_PRIVATE);
//                password = sharedPreferences.getString("PasswordValue", "");
                //commitGroup();

            }
        });

        //그룹 이미지 설정
        groupimagebtn = findViewById(R.id.testimage);
        groupimagebtn.setOnClickListener(new View.OnClickListener() {
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            Log.d("Create", "onActivityResult:"+ data.getData());
            grouplayout.setVisibility(View.GONE);
            groupimage.setVisibility(View.VISIBLE);
            groupimage.setImageURI(selectedImageUri);

        }
    }


//    public void commitGroup() {
//
//        final String name = groupname.getText().toString().trim();
//        final String oneline = grouponelineintro.getText().toString().trim();
//
//        SharedPreferences sharedPreferences = getSharedPreferences("password", MODE_PRIVATE);
//        password = sharedPreferences.getString("PasswordValue", "");
//        StringRequest request = new StringRequest(Request.Method.POST, URL + "/post/group",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//
//                        }
//                    }
//                }, null) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("name", name);
//                params.put("onelineintro", oneline);
//                params.put("category", Category);
//                params.put("password", password);
//                return params;
//            }
//    };
//        request.setShouldCache(false);
//        AppHelper.requestQueue.add(request);
//    }
}
