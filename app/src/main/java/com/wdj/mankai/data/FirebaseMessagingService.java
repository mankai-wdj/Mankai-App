package com.wdj.mankai.data;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.wdj.mankai.R;
import com.wdj.mankai.data.model.Room;
import com.wdj.mankai.ui.chat.FCMChatContainer;
import com.wdj.mankai.ui.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    public Room room;
    public String roomId;
    public String roomUsers;
    public String currentRoomId;
    String type;
    String users;
    String updated_at;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        System.out.println("ddddddddddddddddddddd" + message);
        try {
            JSONObject jsonObject = new JSONObject(message.getData().get("room"));
            roomId = jsonObject.getString("id");
            roomUsers = jsonObject.getString("users");
            currentRoomId =((FlagClass) getApplicationContext()).getCurrentRoomId();
            System.out.println("Ddddddddddd"+roomId);
            System.out.println(jsonObject.getString("id").equals(currentRoomId));
            if(!jsonObject.getString("id").equals(currentRoomId)) {
                System.out.println("Ddddddddddd "+jsonObject.getString("id").equals(currentRoomId));
                String msg = message.getNotification().getBody();
                type = message.getData().get("type");
                users = message.getData().get("users");
                updated_at = message.getData().get("updated_at");
                if(message.getData().get("type").equals("memo")) {
                    msg = "메모가 도착했습니다";
                }else if(message.getData().get("type").equals("file")) {
                    if(message.getNotification().getBody().startsWith("[{")) {
                        msg = "파일이 도착했습니다";
                    }else {
                        msg = "사진이 도착했습니다";
                    }
                }
                showNotification(message.getNotification().getTitle(), msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    private RemoteViews getCustomDesign(String title, String message) {
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.fcm_notification_layout);
        remoteViews.setTextViewText(R.id.noti_title, title);
        remoteViews.setTextViewText(R.id.noti_message, message);
        remoteViews.setImageViewResource(R.id.logo, R.drawable.ic_chat_info);
        return remoteViews;
    }

    public void showNotification(String title, String message) {
        //팝업 터치시 이동할 액티비티를 지정합니다.
        Intent intent = new Intent(this, FCMChatContainer.class);
        room = new Room(roomId,"","", "","","");
        ((FlagClass) getApplicationContext()).setRoomId(roomId);
        ((FlagClass) getApplicationContext()).setRoomUsers(roomUsers);
        //알림 채널 아이디 : 본인 하고싶으신대로...
        String channel_id = "CHN_ID";
        Log.e("Room",roomId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        //기본 사운드로 알림음 설정. 커스텀하려면 소리 파일의 uri 입력
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                .setSmallIcon(R.drawable.ic_chat_info)
                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000}) //알림시 진동 설정 : 1초 진동, 1초 쉬고, 1초 진동
                .setOnlyAlertOnce(false) //동일한 알림은 한번만.. : 확인 하면 다시 울림
                .setOngoing(true)
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) { //안드로이드 버전이 커스텀 알림을 불러올 수 있는 버전이면
            //커스텀 레이아웃 호출
            builder = builder.setContent(getCustomDesign(title, message));
        } else { //아니면 기본 레이아웃 호출
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_chat_info); //커스텀 레이아웃에 사용된 로고 파일과 동일하게..
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //알림 채널이 필요한 안드로이드 버전을 위한 코드
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "CHN_NAME", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri, null);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        //알림 표시 !
        notificationManager.notify(0, builder.build());
    }
}
