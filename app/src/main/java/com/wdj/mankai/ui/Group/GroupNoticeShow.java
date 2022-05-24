package com.wdj.mankai.ui.Group;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wdj.mankai.R;


public class GroupNoticeShow extends AppCompatActivity {
    private WebView wb;
    private String notice_id;
    private ImageView back_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_notice_show);
        wb = findViewById(R.id.notice_webview);
        back_btn = findViewById(R.id.backBtn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_wait,R.anim.slide_out_right);
            }
        });

        notice_id = getIntent().getStringExtra("id");
        wb.setWebViewClient(new WebViewClient());
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setDomStorageEnabled(true);
        wb.loadUrl("https://mankai.shop/groupboard/webview/"+notice_id);



    }
}
