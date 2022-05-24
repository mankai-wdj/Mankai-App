package com.wdj.mankai.ui.Board;

import static java.lang.Thread.*;

import android.util.Log;

import com.wdj.mankai.BuildConfig;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class PapagoTranslate{
        public static String cnt = "";
        private static String langCode;
        public static String getTranslation(String word, String target) {

            String clientId = BuildConfig.Papago_api_key;//애플리케이션 클라이언트 아이디값";
            String clientSecret = BuildConfig.Papago_secret_key;//애플리케이션 클라이언트 시크릿값";
//           키 숨길것
            new Thread() {
                public void run(){
                    try {
                        String wordSource, wordTarget;
                        String text = URLEncoder.encode(word, "UTF-8");             //word
                        wordTarget = URLEncoder.encode(target, "UTF-8");

                        String apiURL = "https://openapi.naver.com/v1/papago/detectLangs";
                        URL url = new URL(apiURL);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("POST");
                        con.setRequestProperty("X-Naver-Client-Id", clientId);
                        con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                        // post request
                        String postParams = "query=" + text;
                        con.setDoOutput(true);
                        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                        wr.writeBytes(postParams);
                        wr.flush();
                        wr.close();
                        int responseCode = con.getResponseCode();
                        BufferedReader br;
                        if (responseCode == 200) { // 정상 호출
                            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        } else {  // 에러 발생
                            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                        }
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = br.readLine()) != null) {
                            response.append(inputLine);
                        }
                        br.close();
                        //System.out.println(response.toString());
                        String s = response.toString();
                        JSONObject jsonObject = new JSONObject(s);
                        langCode = jsonObject.getString("langCode");

                        Log.d("Board", "타겟언어 " +s);

                        wordSource = URLEncoder.encode(langCode, "UTF-8");

                        apiURL = "https://openapi.naver.com/v1/papago/n2mt";
                        url = new URL(apiURL);
                        con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("POST");
                        con.setRequestProperty("X-Naver-Client-Id", clientId);
                        con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                        // post request
                        postParams = "source="+wordSource+"&target="+wordTarget+"&text=" + text;
                        con.setDoOutput(true);
                        wr = new DataOutputStream(con.getOutputStream());
                        wr.writeBytes(postParams);
                        wr.flush();
                        wr.close();
                        responseCode = con.getResponseCode();
                        if (responseCode == 200) { // 정상 호출
                            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        } else {  // 에러 발생
                            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                        }
                        response = new StringBuffer();
                        while ((inputLine = br.readLine()) != null) {
                            response.append(inputLine);
                        }
                        br.close();
                        //System.out.println(response.toString());
                        s = response.toString();
                        JSONObject translateJson = new JSONObject(s);
                        String message = translateJson.getJSONObject("message").getJSONObject("result").getString("translatedText");
                        cnt = message;
                        Log.d("Board", "cnt" + message);

                    } catch (Exception e) {
                        e.printStackTrace();
                        cnt = "";
                    }
                }
            }.start();

            try {
                sleep(1000);
                return cnt;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
}

