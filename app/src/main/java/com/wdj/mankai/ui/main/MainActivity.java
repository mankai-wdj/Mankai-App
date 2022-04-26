package com.wdj.mankai.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.wdj.mankai.R;
import com.wdj.mankai.ui.mypage.FragMyPageHead;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    String userName = null;
    String userDescription = null;
    String userProfile = null;
    FragMyPageHead fragMyPageHead ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//       MainActivity를 실행하는 모습을 확인하고 SharedPreferences에서 값을 갖고와서
//       Intent에 값을 넘겨주면 될 것이다.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences sharedPreferences= getSharedPreferences("login_token", MODE_PRIVATE);
        String token = sharedPreferences.getString("login_token","");
        getUser(token); // 유저 정보 받아오기

        SharedPreferences sharedPreferences2 = getSharedPreferences("user_info",MODE_PRIVATE);
        String user_info = sharedPreferences2.getString("user_info","");
//      user 정보받기
        Log.i("USER_INFO",user_info);
//      이건 String



        try {
           JSONObject obj = new JSONObject(user_info);

           userName = obj.getString("name");
           userDescription = obj.getString("description");
           userProfile = obj.getString("profile");


        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + user_info + "\"");
        }


        Button mypageButton = (Button)findViewById(R.id.mypageButton);
//      이 버튼을 MYPAGE들어가기 버튼으로 하면 될 것이다.

        mypageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


                Bundle bundle = new Bundle();
                bundle.putString("userName",userName);
                bundle.putString("userDescription",userDescription);
                bundle.putString("userProfile",userProfile);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                FragMyPageHead fragMyPageHead = new FragMyPageHead();
                fragMyPageHead.setArguments(bundle);
                transaction.replace(R.id.frame,fragMyPageHead);
                transaction.commit();
            }
        });

    }

    private void getUser(String token) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    System.out.println(response);
                    String userName = jsonObject.getString("name");
                    SharedPreferences sharedPreferences = getSharedPreferences("user_info",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_info",response);
                    editor.commit();
                    if(userName != null) {
                        System.out.println("유저 정보 받아옴");
                        Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(MainActivity.this,"토큰 만료 다시 로그인", Toast.LENGTH_SHORT).show();
                        System.out.println(response);
                    }
                } catch(JSONException err) {
                    err.printStackTrace();
                }
            }
        };
        UserRequest userRequest = new UserRequest(token,responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(userRequest);
    }
}