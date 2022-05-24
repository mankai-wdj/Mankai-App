package com.wdj.mankai.ui.Group;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wdj.mankai.R;


public class GroupNoticeShow extends AppCompatActivity {
    private WebView wb;
    private String notice_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_notice_show);
        wb = findViewById(R.id.notice_webview);
        notice_id = getIntent().getStringExtra("id");

        wb.setWebViewClient(new WebViewClient());
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setDomStorageEnabled(true);
        wb.loadUrl("https://mankai.shop/groupboard/webview/"+notice_id);

    }
}
