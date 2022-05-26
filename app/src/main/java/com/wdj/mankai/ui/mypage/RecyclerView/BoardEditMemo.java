package com.wdj.mankai.ui.mypage.RecyclerView;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.wdj.mankai.R;
import com.wdj.mankai.ui.main.MyPageFragment;
import com.wdj.mankai.ui.mypage.FragMyMemos;
import com.wdj.mankai.ui.mypage.ViewPagerAdapter;

public class BoardEditMemo extends Activity {

    WebView editMemoWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boardeditmemo);
        int memoId = getIntent().getIntExtra("memoId",0);


        editMemoWebView = findViewById(R.id.board_memo_edit_webview);
        WebSettings settings = editMemoWebView.getSettings();
        settings.setDomStorageEnabled(true);
        editMemoWebView.setWebViewClient(new WebViewClient(){
           public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;//응용프로그램이 직접 url를 처리함
            }
        });
        editMemoWebView.getSettings().setJavaScriptEnabled(true);
        editMemoWebView.setWebChromeClient(new WebChromeClient());
        editMemoWebView.loadUrl("http://localhost:3000/boardmemoedit/"+memoId);
        /*?!스크롤 손 좀 봐야겠다..*/
        editMemoWebView.addJavascriptInterface(new WebBridge(),"BRIDGE");
        /*웹의 수정하기 버튼을 눌렀을 때 WebBridge내부의 함수를 실행한다.*/

    }


    class WebBridge{
        @JavascriptInterface
        public void editclick(){
              finish();
        }
        @JavascriptInterface
        public void deleteclick(){
            finish();
        }
    }
    }



