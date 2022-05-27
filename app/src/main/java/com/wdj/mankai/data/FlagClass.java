package com.wdj.mankai.data;

import android.app.Application;

import org.json.JSONObject;

public class FlagClass extends Application {

    private static String roomId; //글로벌로 사용할 변수를 선언합니다.
    private static String roomUsers;
    private static String fcmToken;
    private static String loginToken;
    private static JSONObject userObject;
    private static String userInfo;
    private static String userProfile;
    private static String userDescription;
    private static String userName;
    private static String userCountry;
    private static int userId;
    private static String currentRoomId;
    @Override   //오버라이딩 해서 onCreate()를 만들어 줍니다. 여느 클래스와 똑같이요 ㅎㅎ
    public void onCreate() {
        super.onCreate();


    }

    @Override  //이건 선택사항인데 일단 추가해 줍니다. 데이터 공간 낭비를 방지하기 위해 추가하고 나중에 필요하면 선언하세요.
    public void onTerminate() {
        super.onTerminate();
        Object instance = null;
    }


    // 초기화 함수입니다. 처음 선언을 해주면 안정적으로 초기화 되서 변수가 안정적입니다.
    public void Init() {

    }

    //클래스를 선언한 뒤, 다른 액티비티에서 사용될 함수 입니다. 이건 verdiosn이라는 글로벌 변수에 flag값을 넣게다는 뜻입니다.
//다른 액티비티에서 선언 방법은 밑에 써드릴게요
    public void setRoomId(String flag){roomId = flag;}

    //이것은 저장된 값을 불러오는 함수입니다ㅏ.
    public String  getRoomId(){return roomId;}

    public void setRoomUsers(String users){roomUsers = users;}

    //이것은 저장된 값을 불러오는 함수입니다ㅏ.
    public String  getRoomUsers(){return roomUsers;}

    public void setFcmToken(String token){fcmToken = token;}

    //이것은 저장된 값을 불러오는 함수입니다ㅏ.
    public String  getFcmToken(){return fcmToken;}


    public void setLoginToken(String token){loginToken = token;}

    //이것은 저장된 값을 불러오는 함수입니다ㅏ.
    public String  getLoginToken(){return loginToken;}


    public void setUserObject(JSONObject user){userObject = user;}

    //이것은 저장된 값을 불러오는 함수입니다ㅏ.
    public JSONObject  getUserObject(){return userObject;}

    public void setUserInfo(String user){userInfo = user;}

    //이것은 저장된 값을 불러오는 함수입니다ㅏ.
    public String  getUserInfo(){return userInfo;}

    public void setUserId(int user){userId = user;}

    //이것은 저장된 값을 불러오는 함수입니다ㅏ.
    public int  getUserId(){return userId;}

    public void setUserName(String user){userName = user;}

    //이것은 저장된 값을 불러오는 함수입니다ㅏ.
    public String  getUserName(){return userName;}

    public void setUserProfile(String user){userProfile = user;}

    //이것은 저장된 값을 불러오는 함수입니다ㅏ.
    public String  getUserProfile(){return userProfile;}

    public void setUserDescription(String user){userDescription = user;}

    //이것은 저장된 값을 불러오는 함수입니다ㅏ.
    public String  getUserDescription(){return userDescription;}

    public void setUserCountry(String user){userCountry = user;}

    //이것은 저장된 값을 불러오는 함수입니다ㅏ.
    public String  getUserCountry(){return userCountry;}

    public void setCurrentRoomId(String roomId){currentRoomId = roomId;}

    //이것은 저장된 값을 불러오는 함수입니다ㅏ.
    public String  getCurrentRoomId(){return currentRoomId;}
}