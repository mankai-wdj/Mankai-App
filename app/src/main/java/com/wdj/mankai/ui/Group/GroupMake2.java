package com.wdj.mankai.ui.Group;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.wdj.mankai.R;

public class GroupMake2 extends AppCompatActivity {

    public static final int REQUEST_CODE = 1000;
    ImageButton groupback;
    LinearLayout gcate1,gcate2,gcate3,gcate4,gcate5,gcate6,gcate7,gcate8;
    String Category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {    // 액티비티 시작시 처음으로 실행
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmake2);
        gcate1 = findViewById(R.id.Gcate1);
        gcate2 = findViewById(R.id.Gcate2);
        gcate3 = findViewById(R.id.Gcate3);
        gcate4 = findViewById(R.id.Gcate4);
        gcate5 = findViewById(R.id.Gcate5);
        gcate6 = findViewById(R.id.Gcate6);
        gcate7 = findViewById(R.id.Gcate7);
        gcate8 = findViewById(R.id.Gcate8);


        gcate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GroupMake.class);
                intent.putExtra("Category","IT");
                startActivity(intent);
            }
        });

        gcate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GroupMake.class);
                intent.putExtra("Category","자유");
                startActivity(intent);
            }
        });

        gcate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GroupMake.class);
                intent.putExtra("Category","스터디");
                startActivity(intent);
            }
        });

        gcate4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GroupMake.class);
                intent.putExtra("Category","게임");
                startActivity(intent);
            }
        });

        gcate5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GroupMake.class);
                intent.putExtra("Category","취업");
                startActivity(intent);
            }
        });

        gcate6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GroupMake.class);
                intent.putExtra("Category","운동");
                startActivity(intent);
            }
        });

        gcate7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GroupMake.class);
                intent.putExtra("Category","정치");
                startActivity(intent);
            }
        });

        gcate8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GroupMake.class);
                intent.putExtra("Category","투자");
                startActivity(intent);
            }
        });

        groupback = findViewById(R.id.groupback);
        groupback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
