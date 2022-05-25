package com.wdj.mankai.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.wdj.mankai.R;
import com.wdj.mankai.data.model.Message;
import com.wdj.mankai.data.model.User;
import com.wdj.mankai.ui.chat.ChatContainerActivity;
import com.wdj.mankai.ui.chat.ChatMemoReadActivity;
import com.wdj.mankai.ui.chat.LoadImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<Message> messages = new ArrayList<>();
    ImageView imageProfile, imageMessage;
    private TextView textMessage, textFileName, textDateTime, textReceivedName, textMemoName, textVideoMessage, textVideoLink, textTransMessage;
    String roomId;
    String res;
    JSONObject currentUser;
    private View transLine;
    AppCompatImageView imageFileIcon, imageMemoIcon;
    LinearLayout fileLayout, memoLayout, videoLayout,messageBox, textMessageLayout;
    GridLayout gridLayout;
//    ImageGridViewAdapter imageGridViewAdapter;
    public static final Boolean VIEW_TYPE_SENT = true;
    public static final Boolean VIEW_TYPE_RECIEVED = false;

    public MessagesAdapter(Context mContext, JSONObject currentUser, String roomId) {
        this.mContext = mContext;
        this.currentUser = currentUser;
        this.roomId = roomId;
    }

    public void addEventMessage(Message message) {
        messages.add(messages.size()-1, message);
    }

    public void addMessage(ArrayList<Message> Arraymessages)  {

        for(int i = 0 ;i<Arraymessages.size();i++){
            messages.add(Arraymessages.get(i));
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Boolean bool = null;
            try {
                 bool = messages.get(viewType).userId.equals(currentUser.getString("id")) == VIEW_TYPE_SENT;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(bool) {
                return new SentMessageViewHolder(inflater.inflate(R.layout.chat_container_sent_message, parent, false));
            }else {
                return new ReceivedMessageViewHolder(inflater.inflate(R.layout.chat_container_received_message, parent, false));
            }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
//        imageGridViewAdapter = new ImageGridViewAdapter();

        try {
            if(message.userId.equals(currentUser.getString("id")) == VIEW_TYPE_SENT) {
                try {
                    ((SentMessageViewHolder) holder).setItem(message);
    //                ((SentMessageViewHolder) holder).gridView.setAdapter(imageGridViewAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {
                try {
                    ((ReceivedMessageViewHolder) holder).setItem(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        if(messages.get(position).userId.equals(currentUserId)) {
//            return VIEW_TYPE_SENT;
//        }else {
//            return VIEW_TYPE_RECIEVED;
//        }
//    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // current user 의 viewholder
    public class SentMessageViewHolder extends RecyclerView.ViewHolder {
//        public GridView gridView;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            messageBox = (LinearLayout) itemView.findViewById(R.id.messageBox);

//            imageProfile = itemView.findViewById(R.id.imageProfile);
            textMessageLayout = itemView.findViewById(R.id.textMessageLayout);
            textMessage = (TextView) itemView.findViewById(R.id.textMessage);
            textDateTime = (TextView) itemView.findViewById(R.id.textDateTime);
            imageMessage = (ImageView) itemView.findViewById(R.id.imageMessage);
            textTransMessage = (TextView) itemView.findViewById(R.id.textTransMessage);
            transLine = itemView.findViewById(R.id.transLine);

            // file layout
            fileLayout = (LinearLayout) itemView.findViewById(R.id.fileLayout);
            imageFileIcon = (AppCompatImageView) itemView.findViewById(R.id.imageFileIcon);
            textFileName = (TextView) itemView.findViewById(R.id.textFileName);
//            gridView = itemView.findViewById(R.id.gridView);
            gridLayout = (GridLayout) itemView.findViewById(R.id.gridLayout);

            //memo layout
            memoLayout = (LinearLayout) itemView.findViewById(R.id.memoLayout);
            imageMemoIcon = (AppCompatImageView) itemView.findViewById(R.id.imageMemoIcon);
            textMemoName = (TextView) itemView.findViewById(R.id.textMemoName);

            //video layout
            videoLayout = (LinearLayout) itemView.findViewById(R.id.videoLayout);
            textVideoMessage = (TextView) itemView.findViewById(R.id.textVideoMessage);
            textVideoLink = (TextView) itemView.findViewById(R.id.textVideoLink);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    AlertDialog.Builder downDlg = new AlertDialog.Builder(itemView.getContext());
                    if(messages.get(pos).messageType.equals("file")) {
                        downDlg.setMessage("파일을 다운로드 합니다.");
                        downDlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(itemView.getContext(), "확인 눌렀음", Toast.LENGTH_LONG).show();
                            }
                        });
                        downDlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        downDlg.show();
                    }else if (messages.get(pos).messageType.equals("memo")) {
                        Intent intent = new Intent(itemView.getContext(), ChatMemoReadActivity.class);
                        intent.putExtra("memo", messages.get(pos).message);
                        mContext.startActivity(intent);
                    }
                }
            });

            itemView.setOnTouchListener(new View.OnTouchListener() {
                GestureDetector gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public void onLongPress(MotionEvent e) {
                        Toast.makeText(itemView.getContext(), "long press", Toast.LENGTH_SHORT).show();
                        super.onLongPress(e);
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        int pos = getAdapterPosition();
                        if(messages.get(pos).messageType.equals("message")) {
                            Toast.makeText(itemView.getContext(), String.valueOf(pos), Toast.LENGTH_SHORT).show();
                            transLine.setVisibility(View.VISIBLE);
                            try {
                                translation(messages.get(pos).message, currentUser.getString("country"), itemView);
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }

                        }
                        return super.onDoubleTap(e);
                    }
                });

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    gestureDetector.onTouchEvent(motionEvent);
                    return false;
                }
            });

        }

        public void setItem(Message message) throws JSONException {  //어떻게 보여줄지 설정
            if(message.messageType.equals("file")) {
                if(message.message.startsWith("[{")) { // file 형식
                    fileLayout.setVisibility(View.VISIBLE);
                    imageMessage.setVisibility(View.GONE);
                    textMessageLayout.setVisibility(View.GONE);
                    memoLayout.setVisibility(View.GONE);
//                    gridView.setVisibility(View.GONE);
                    gridLayout.setVisibility(View.GONE);
                    videoLayout.setVisibility(View.GONE);
                    JSONArray jsonArray = new JSONArray(message.message);
                    if(jsonArray.getJSONObject(0).getString("type").equals("pptx") || jsonArray.getJSONObject(0).getString("type").equals("ppt")) {
                        imageFileIcon.setImageResource(R.drawable.file_icon_pptx);
                    }else if (jsonArray.getJSONObject(0).getString("type").equals("docx") || jsonArray.getJSONObject(0).getString("type").equals("doc")) {
                        imageFileIcon.setImageResource(R.drawable.file_icon_docx);
                    }else if(jsonArray.getJSONObject(0).getString("type").equals("xlsx") || jsonArray.getJSONObject(0).getString("type").equals("xls") ) {
                        imageFileIcon.setImageResource(R.drawable.file_icon_xlsx);
                    }else if(jsonArray.getJSONObject(0).getString("type").equals("pdf")){
                        imageFileIcon.setImageResource(R.drawable.file_icon_pdf);
                    }else {
                        imageFileIcon.setImageResource(R.drawable.file_icon_normal);
                    }

                    textFileName.setText(jsonArray.getJSONObject(0).getString("name"));
                }else if (message.message.startsWith("[")){ // 사진 여러개
//                    gridView.setVisibility(View.VISIBLE);
                    gridLayout.setVisibility(View.VISIBLE);
                    imageMessage.setVisibility(View.GONE);
                    textMessageLayout.setVisibility(View.GONE);
                    fileLayout.setVisibility(View.GONE);
                    memoLayout.setVisibility(View.GONE);
                    videoLayout.setVisibility(View.GONE);
//                    imageGridViewAdapter.addItem("ddd");
//                    imageGridViewAdapter.addItem("ddd");

//                    imageView.setImageResource(R.drawable.ic_back);
                    JSONArray jsonArray = new JSONArray(message.message);
//                    gridLayout.removeView(itemView);
                    for (int i = 0; i<jsonArray.length(); i++) {

                        LayoutInflater inflater = (LayoutInflater) itemView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        LinearLayout imageView = (LinearLayout) inflater.inflate(R.layout.gridview_list_item, null);

                        Glide.with(imageView).load(jsonArray.getString(i)).override(360, 360).centerCrop().into((ImageView) imageView.findViewById(R.id.iv_image));

                        gridLayout.addView(imageView);

                    }



                }else { // 일반 사진 1장
                    Glide.with(itemView).load(message.message).into((ImageView) itemView.findViewById(R.id.imageMessage));
                    imageMessage.setVisibility(View.VISIBLE);
                    textMessageLayout.setVisibility(View.GONE);
                    fileLayout.setVisibility(View.GONE);
                    memoLayout.setVisibility(View.GONE);
//                    gridView.setVisibility(View.GONE);
                    gridLayout.setVisibility(View.GONE);
                    videoLayout.setVisibility(View.GONE);
                }

            }else if (message.messageType.equals("memo")) {
                memoLayout.setVisibility(View.VISIBLE);
                fileLayout.setVisibility(View.GONE);
                imageMessage.setVisibility(View.GONE);
                textMessageLayout.setVisibility(View.GONE);
//                gridView.setVisibility(View.GONE);
                gridLayout.setVisibility(View.GONE);
                videoLayout.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(message.message);
                textMemoName.setText(jsonObject.getString("memo_title"));
                imageMemoIcon.setImageResource(R.drawable.memo_icon);

            }else if(message.messageType.equals("message")){  // message
                textMessageLayout.setVisibility(View.VISIBLE);
                textMessage.setText(message.message);
                imageMessage.setVisibility(View.GONE);
                fileLayout.setVisibility(View.GONE);
                memoLayout.setVisibility(View.GONE);
//                gridView.setVisibility(View.GONE);
                gridLayout.setVisibility(View.GONE);
                videoLayout.setVisibility(View.GONE);
            }else if(message.messageType.equals("video")){ // video
                textMessage.setText(message.message);
                videoLayout.setVisibility(View.VISIBLE);
                textMessageLayout.setVisibility(View.GONE);
                imageMessage.setVisibility(View.GONE);
                fileLayout.setVisibility(View.GONE);
                memoLayout.setVisibility(View.GONE);
//                gridView.setVisibility(View.GONE);
                gridLayout.setVisibility(View.GONE);
                textVideoMessage.setText(message.message);
                textVideoLink.setText("https://mankai.shop/"+roomId);
            }
            textDateTime.setText(message.created_at);

        }
    }

    // 다른 유저의 viewholder
    public class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            textMessageLayout = itemView.findViewById(R.id.textMessageLayout);
            textMessage = (TextView) itemView.findViewById(R.id.textMessage);
            textDateTime = (TextView) itemView.findViewById(R.id.textDateTime);
            textReceivedName = (TextView) itemView.findViewById(R.id.text_received_name);
            imageMessage = (ImageView) itemView.findViewById(R.id.imageMessage);
            textTransMessage = (TextView) itemView.findViewById(R.id.textTransMessage);
            transLine = itemView.findViewById(R.id.transLine);

            // file layout
            fileLayout = (LinearLayout) itemView.findViewById(R.id.fileLayout);
            imageFileIcon = (AppCompatImageView) itemView.findViewById(R.id.imageFileIcon);
            textFileName = (TextView) itemView.findViewById(R.id.textFileName);
            gridLayout = (GridLayout) itemView.findViewById(R.id.gridLayout);

            //memo layout
            memoLayout = (LinearLayout) itemView.findViewById(R.id.memoLayout);
            imageMemoIcon = (AppCompatImageView) itemView.findViewById(R.id.imageMemoIcon);
            textMemoName = (TextView) itemView.findViewById(R.id.textMemoName);

            //video layout
            videoLayout = (LinearLayout) itemView.findViewById(R.id.videoLayout);
            textVideoMessage = (TextView) itemView.findViewById(R.id.textVideoMessage);
            textVideoLink = (TextView) itemView.findViewById(R.id.textVideoLink);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    AlertDialog.Builder downDlg = new AlertDialog.Builder(itemView.getContext());
                    if(messages.get(pos).messageType.equals("file")) {
                        downDlg.setTitle("download");
                        downDlg.setMessage("파일을 다운로드 합니다.");
                        downDlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(itemView.getContext(), "확인 눌렀음", Toast.LENGTH_LONG).show();
                            }
                        });
                        downDlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        downDlg.show();
                    }else if (messages.get(pos).messageType.equals("memo")) {
                        Intent intent = new Intent(itemView.getContext(), ChatMemoReadActivity.class);
                        intent.putExtra("memo", messages.get(pos).message);
                        mContext.startActivity(intent);
                    }


                }
            });
            itemView.setOnTouchListener(new View.OnTouchListener() {
                GestureDetector gestureDetector = new GestureDetector(itemView.getContext(), new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public void onLongPress(MotionEvent e) {
                        Toast.makeText(itemView.getContext(), "long press", Toast.LENGTH_SHORT).show();
                        super.onLongPress(e);
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        int pos = getAdapterPosition();
                        if(messages.get(pos).messageType.equals("message")) {
                            Toast.makeText(itemView.getContext(), String.valueOf(pos), Toast.LENGTH_SHORT).show();
                            transLine.setVisibility(View.VISIBLE);
                            try {
                                translation(messages.get(pos).message, currentUser.getString("country"), itemView);
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }

                        }
                        return super.onDoubleTap(e);
                    }
                });

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    gestureDetector.onTouchEvent(motionEvent);
                    return false;
                }
            });

        }

        public void setItem(Message message) throws JSONException {  //어떻게 보여줄지 설정
            if(message.messageType.equals("file")) {
                if(message.message.startsWith("[{")) { // file 형식
                    fileLayout.setVisibility(View.VISIBLE);
                    imageMessage.setVisibility(View.GONE);
                    textMessageLayout.setVisibility(View.GONE);
                    memoLayout.setVisibility(View.GONE);
//                    gridView.setVisibility(View.GONE);
                    gridLayout.setVisibility(View.GONE);
                    videoLayout.setVisibility(View.GONE);
                    JSONArray jsonArray = new JSONArray(message.message);
                    if(jsonArray.getJSONObject(0).getString("type").equals("pptx") || jsonArray.getJSONObject(0).getString("type").equals("ppt")) {
                        imageFileIcon.setImageResource(R.drawable.file_icon_pptx);
                    }else if (jsonArray.getJSONObject(0).getString("type").equals("docx") || jsonArray.getJSONObject(0).getString("type").equals("doc")) {
                        imageFileIcon.setImageResource(R.drawable.file_icon_docx);
                    }else if(jsonArray.getJSONObject(0).getString("type").equals("xlsx") || jsonArray.getJSONObject(0).getString("type").equals("xls") ) {
                        imageFileIcon.setImageResource(R.drawable.file_icon_xlsx);
                    }else if(jsonArray.getJSONObject(0).getString("type").equals("pdf")){
                        imageFileIcon.setImageResource(R.drawable.file_icon_pdf);
                    }else {
                        imageFileIcon.setImageResource(R.drawable.file_icon_normal);
                    }

                    textFileName.setText(jsonArray.getJSONObject(0).getString("name"));
                }else if (message.message.startsWith("[")){ // 사진 여러개
//                    gridView.setVisibility(View.VISIBLE);
                    gridLayout.setVisibility(View.VISIBLE);
                    imageMessage.setVisibility(View.GONE);
                    textMessageLayout.setVisibility(View.GONE);
                    fileLayout.setVisibility(View.GONE);
                    memoLayout.setVisibility(View.GONE);
                    videoLayout.setVisibility(View.GONE);
//                    imageGridViewAdapter.addItem("ddd");
//                    imageGridViewAdapter.addItem("ddd");

//                    imageView.setImageResource(R.drawable.ic_back);
                    JSONArray jsonArray = new JSONArray(message.message);
//                    gridLayout.removeView(itemView);
                    for (int i = 0; i<jsonArray.length(); i++) {

                        LayoutInflater inflater = (LayoutInflater) itemView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        LinearLayout imageView = (LinearLayout) inflater.inflate(R.layout.gridview_list_item, null);

                        Glide.with(imageView).load(jsonArray.getString(i)).override(360, 360).centerCrop().into((ImageView) imageView.findViewById(R.id.iv_image));

                        gridLayout.addView(imageView);

                    }



                }else { // 일반 사진 1장
                    Glide.with(itemView).load(message.message).into((ImageView) itemView.findViewById(R.id.imageMessage));
                    imageMessage.setVisibility(View.VISIBLE);
                    textMessageLayout.setVisibility(View.GONE);
                    fileLayout.setVisibility(View.GONE);
                    memoLayout.setVisibility(View.GONE);
//                    gridView.setVisibility(View.GONE);
                    gridLayout.setVisibility(View.GONE);
                    videoLayout.setVisibility(View.GONE);
                }

            }else if (message.messageType.equals("memo")) {
                memoLayout.setVisibility(View.VISIBLE);
                fileLayout.setVisibility(View.GONE);
                imageMessage.setVisibility(View.GONE);
                textMessageLayout.setVisibility(View.GONE);
//                gridView.setVisibility(View.GONE);
                gridLayout.setVisibility(View.GONE);
                videoLayout.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(message.message);
                textMemoName.setText(jsonObject.getString("memo_title"));
                imageMemoIcon.setImageResource(R.drawable.memo_icon);

            }else if(message.messageType.equals("message")){  // message
                textMessage.setText(message.message);
                textMessageLayout.setVisibility(View.VISIBLE);
                imageMessage.setVisibility(View.GONE);
                fileLayout.setVisibility(View.GONE);
                memoLayout.setVisibility(View.GONE);
//                gridView.setVisibility(View.GONE);
                gridLayout.setVisibility(View.GONE);
                videoLayout.setVisibility(View.GONE);
            }else if(message.messageType.equals("video")){ // video
                textMessage.setText(message.message);
                videoLayout.setVisibility(View.VISIBLE);
                textMessageLayout.setVisibility(View.GONE);
                imageMessage.setVisibility(View.GONE);
                fileLayout.setVisibility(View.GONE);
                memoLayout.setVisibility(View.GONE);
//                gridView.setVisibility(View.GONE);
                gridLayout.setVisibility(View.GONE);
                textVideoMessage.setText(message.message);
                textVideoLink.setText("https://mankai.shop/"+roomId);
            }
            textReceivedName.setText(new JSONObject(message.user).getString("name"));
            Glide.with(itemView.getContext()).load(new JSONObject(message.user).getString("profile")).into((ImageView) itemView.findViewById(R.id.imageProfile));
            textDateTime.setText(message.created_at);

        }
    }
    private String translation (String message, String country, View itemView) {

        if(message.equals("")) {
            return "";
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
                            textTransMessage.setVisibility(View.VISIBLE);
                            textTransMessage.setText(response);

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
            Volley.newRequestQueue(itemView.getContext()).add(stringRequest);

        }
        return res;
    }

}
