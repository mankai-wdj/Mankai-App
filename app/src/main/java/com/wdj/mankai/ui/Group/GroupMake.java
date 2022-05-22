package com.wdj.mankai.ui.Group;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wdj.mankai.R;

import java.util.Locale;

public class GroupMake extends AppCompatActivity {

    private final int GALLERY_REQ_CODE = 1000;

    ImageView testimage, groupimage;
    LinearLayout grouplayout;
    String Category, imageURl;
    ImageButton btnback;
    TextView text_sheef;
    Button group_submit;
    String password;

    // 그룹 생성(이름, 한줄소개, 비번설정 등등)
    @Override
    protected void onCreate(Bundle savedInstanceState) {    // 액티비티 시작시 처음으로 실행
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmake);
        grouplayout = findViewById(R.id.groupmake_layout);
        groupimage = findViewById(R.id.groupmake_image);
        groupimage.setVisibility(View.GONE);

        btnback = findViewById(R.id.groupback);
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
                SharedPreferences sharedPreferences = getSharedPreferences("password", MODE_PRIVATE);
                password = sharedPreferences.getString("PasswordValue", "");

            }
        });

        //그룹 이미지 설정
        testimage = findViewById(R.id.testimage);
        testimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
                
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            if(requestCode==GALLERY_REQ_CODE) {
                //for gallery
                grouplayout.setVisibility(View.GONE);
                groupimage.setVisibility(View.VISIBLE);
                groupimage.setImageURI(data.getData());

            }
        }
    }
}
