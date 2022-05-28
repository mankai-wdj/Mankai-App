package com.wdj.mankai.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.wdj.mankai.R;
import com.wdj.mankai.data.FlagClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VideoActivity extends AppCompatActivity {
    WebView mWebView;
    AppCompatButton button;
    private Map<String,String> map;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 101;
    private static final int MY_PERMISSIONS_REQUEST = 102;
    private String url;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        String token = ((FlagClass) getApplicationContext()).getLoginToken();
        askForPermissions();
        url = getIntent().getExtras().getString("url");
        mWebView = findViewById(R.id.webview);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.grant(request.getResources());
            }

        });

        mWebView.addJavascriptInterface(new WebBridge(),"Android");
        mWebView.setWebViewClient(new WebViewClient() {
            // localStorage 설정
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mWebView.evaluateJavascript("window.localStorage.setItem('auth_token','"+ token +"');", null);
                } else {
                    mWebView.loadUrl("javascript:localStorage.setItem('auth_token','"+ token +"');");

                }

            }

        });

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setDomStorageEnabled(true);

        webSettings.setMediaPlaybackRequiresUserGesture(false); // God
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true); // 웹뷰의 디버깅 모드 활성화
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }



        if(arePermissionGranted()) {
            mWebView.loadUrl(url);
        } else {
            finish();
        }

    }


    @Override
    public void onBackPressed() {
////        if (mWebView.getUrl().equalsIgnoreCase("https://mankai.shop/video")
////        ) {
////            super.onBackPressed();
////        }else if(mWebView.canGoBack()){
////            mWebView.goBack();
////        }else{
////            super.onBackPressed();
////        }
//
//        mWebView.loadUrl("javascript:leaveSession");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if (Build.VERSION.SDK_INT >= 23) {

                // requestPermission의 배열의 index가 아래 grantResults index와 매칭
                // 퍼미션이 승인되면
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG", "Permission: " + permissions[0] + "was " + grantResults[0]);
                    mWebView.loadUrl(url);
                    // TODO : 퍼미션이 승인되는 경우에 대한 코드

                }
                // 퍼미션이 승인 거부되면
                else {
                    Log.d("TAG", "Permission denied");

                    // TODO : 퍼미션이 거부되는 경우에 대한 코드
                }

        }
    }

    public void askForPermissions() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_REQUEST);
        } else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }
    }

    private boolean arePermissionGranted() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_DENIED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_DENIED);
    }
    class WebBridge {
        @JavascriptInterface
        public void leave() {
            finish();
        }
    }
}