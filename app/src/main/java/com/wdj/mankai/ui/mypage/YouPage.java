package com.wdj.mankai.ui.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.wdj.mankai.R;

public class YouPage extends AppCompatActivity {

    TextView test ;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_page);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("youURL");

        test = findViewById(R.id.test);
        test.setText(userId);

        url = "https://api.mankai.shop/api/followers/";


    }
}