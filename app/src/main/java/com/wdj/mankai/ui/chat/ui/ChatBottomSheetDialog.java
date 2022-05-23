package com.wdj.mankai.ui.chat.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wdj.mankai.R;
import com.wdj.mankai.data.model.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatBottomSheetDialog extends BottomSheetDialogFragment{
    private View view;
    private BottomSheetListener mListener;
    private LinearLayout filePhotoLayout, sttLayout;
    private TextView tvSttTitle;
    private GridLayout gridSttLayout;
    private Room room;
    private JSONObject user;
    Intent intent;
    SpeechRecognizer speechRecognizer;
    final int PERMISSION = 1;	//permission 변수
    boolean recording = false;  //현재 녹음중인지 여부
    String sttContents = "";

    public ChatBottomSheetDialog(Room room, JSONObject user) {
        this.room = room;
        this.user = user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chat_bottom_sheet_layout, container, false);
        CheckPermission();
        mListener = (BottomSheetListener) getContext();
        filePhotoLayout = view.findViewById(R.id.filePhotoLayout);
        sttLayout = view.findViewById(R.id.sttLayout);
        tvSttTitle = view.findViewById(R.id.tvSttTitle);
        gridSttLayout = view.findViewById(R.id.gridSttLayout);


        //RecognizerIntent 객체 생성
        intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getActivity().getPackageName());
        try {
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,user.getString("country"));   //한국어
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(room.users);
            for(int i = 0; i< jsonArray.length(); i++) {
                if(!jsonArray.getJSONObject(i).getString("user_id").equals(user.getString("id")) && !jsonArray.getJSONObject(i).getString("country").equals("null")) {
                    LayoutInflater inflater2 = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    LinearLayout gridView = (LinearLayout) inflater2.inflate(R.layout.grid_layout_stt, null);
                    Button country = gridView.findViewById(R.id.btCountry);
                    country.setText(jsonArray.getJSONObject(i).getString("country"));
                    String currentCountry = (String) country.getText();
                    country.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(!recording) {
                                StartRecord();
                                country.setText("STOP");
                                Toast.makeText(getContext(), "음성인식중", Toast.LENGTH_SHORT).show();
                            }else {
                                country.setText(currentCountry);
                                StopRecord(currentCountry);

                            }

                        }
                    });
                    gridSttLayout.addView(gridView);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




        filePhotoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("file choose go");
                dismiss();
            }
        });

        sttLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("stt choose go");
                filePhotoLayout.setVisibility(View.GONE);
                sttLayout.setVisibility(View.GONE);
                tvSttTitle.setVisibility(View.VISIBLE);
                gridSttLayout.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }
    //녹음 시작
    void StartRecord() {
        recording = true;
        sttContents = "";
        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(getActivity());
        speechRecognizer.setRecognitionListener(listener);
        speechRecognizer.startListening(intent);
    }

    //녹음 중지
    void StopRecord(String currentCountry) {
        recording = false;

        speechRecognizer.stopListening();   //녹음 중지
        System.out.println(sttContents);
        System.out.println(currentCountry);
        translation(sttContents, currentCountry);
    }
    //퍼미션 체크
    void CheckPermission() {
        //안드로이드 버전이 6.0 이상
        System.out.println(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET) );
        if ( Build.VERSION.SDK_INT >= 23 ){
            //인터넷이나 녹음 권한이 없으면 권한 요청
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.INTERNET,
                                Manifest.permission.RECORD_AUDIO},PERMISSION);
        }
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {

        }

        @Override
        public void onBeginningOfSpeech() {
            //사용자가 말하기 시작
        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {
            //사용자가 말을 멈추면 호출
            //인식 결과에 따라 onError나 onResults가 호출됨
        }

        @Override
        public void onError(int error) {    //토스트 메세지로 에러 출력
            String message;
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    //message = "클라이언트 에러";
                    //speechRecognizer.stopListening()을 호출하면 발생하는 에러
                    return; //토스트 메세지 출력 X
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    //message = "찾을 수 없음";
                    //녹음을 오래하거나 speechRecognizer.stopListening()을 호출하면 발생하는 에러
                    //speechRecognizer를 다시 생성하여 녹음 재개
                    if (recording)
                        StartRecord();
                    return; //토스트 메세지 출력 X
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }
            Toast.makeText(getContext(), "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
        }

        //인식 결과가 준비되면 호출
        @Override
        public void onResults(Bundle bundle) {
            ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);	//인식 결과를 담은 ArrayList
            String originText = sttContents;  //기존 text

            //인식 결과
            String newText="";
            for (int i = 0; i < matches.size() ; i++) {
                newText += matches.get(i);
            }

            sttContents = originText + newText + " ";	//기존의 text에 인식 결과를 이어붙임
            speechRecognizer.startListening(intent);    //녹음버튼을 누를 때까지 계속 녹음해야 하므로 녹음 재개
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    };

    private void translation (String message, String country) {

        if(message.equals("")) {
        }else {
            JSONObject reqJsonObject = new JSONObject(); //JSON 객체를 생성
            try {
                reqJsonObject.put("text", message);
                reqJsonObject.put("country", country);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(reqJsonObject);
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    "https://api.mankai.shop/api/translate/text",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("TAG", "성공");
                            System.out.println(response);
                            sendMessage(response, "message");

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //통신 ERROR
                            System.out.println(error);
                        }
                    }
            ) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    return reqJsonObject.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            Volley.newRequestQueue(getActivity()).add(stringRequest);

        }
    }

    // 메세지 보내기
    private void sendMessage(String message, String type) {
        JSONObject reqJsonObject = new JSONObject(); //JSON 객체를 생성
        JSONArray toUsers = null;
        JSONArray toUsers2 = new JSONArray();
        try {
            toUsers = new JSONArray(room.users);
            for(int i = 0; i < toUsers.length(); i++) {
                toUsers2.put(toUsers.getJSONObject(i).getString("user_id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            reqJsonObject.put("message", message);
            reqJsonObject.put("room_id", room.id);
            reqJsonObject.put("to_users", toUsers2);
            reqJsonObject.put("user_id", user.getString("id"));
            reqJsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                "https://api.mankai.shop/api/message/send",
                reqJsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", "성공");

                        if(type.equals("message")) {
                            try {
                                transBotChat(user.getString("name"), message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //통신 ERROR
                    }
                }
        );
        Volley.newRequestQueue(getActivity()).add(jsonObjectRequest);

    }

    // 채팅 봇 채팅
    private void transBotChat(String name, String message) throws JSONException {
        System.out.println(name);
        JSONObject reqJsonObject = new JSONObject(); //JSON 객체를 생성
        JSONArray toUsers = new JSONArray(room.users);
        JSONArray toUsers2 = new JSONArray();
        for(int i = 0; i < toUsers.length(); i++) {
            toUsers2.put(toUsers.getJSONObject(i).getString("user_id"));
        }
        for(int i=0; i< toUsers.length(); i++) {
            if(toUsers.getJSONObject(i).getString("position").equals("official")) {
                System.out.println("official 있음");
                try {
                    reqJsonObject.put("id", room.id);
                    reqJsonObject.put("message", message);
                    reqJsonObject.put("room_id", room.id);
                    reqJsonObject.put("to_users", toUsers2);
                    reqJsonObject.put("user_id", toUsers.getJSONObject(i).getString("user_id"));
                    reqJsonObject.put("type", "message");
                    reqJsonObject.put("name", name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        "https://api.mankai.shop/api/messageBot/send",
                        reqJsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("TAG", "성공 봇");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //통신 ERROR
                            }
                        }
                );
                Volley.newRequestQueue(getActivity()).add(jsonObjectRequest);
            }
        }
    }
}
