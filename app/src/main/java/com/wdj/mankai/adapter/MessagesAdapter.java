package com.wdj.mankai.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
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

import com.bumptech.glide.Glide;
import com.wdj.mankai.R;
import com.wdj.mankai.data.model.Message;
import com.wdj.mankai.ui.chat.LoadImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<Message> messages = new ArrayList<>();
    ImageView imageProfile, imageMessage;
    TextView textMessage, textFileName, textDateTime, textReceivedName, textMemoName, textVideoMessage, textVideoLink;
    String currentUserId, roomId;
    AppCompatImageView imageFileIcon, imageMemoIcon;
    LinearLayout fileLayout, memoLayout, videoLayout,messageBox;
    GridLayout gridLayout;
//    ImageGridViewAdapter imageGridViewAdapter;
    public static final Boolean VIEW_TYPE_SENT = true;
    public static final Boolean VIEW_TYPE_RECIEVED = false;

    public MessagesAdapter(Context mContext, String currentUserId, String roomId) {
        this.mContext = mContext;
        this.currentUserId = currentUserId;
        this.roomId = roomId;
    }

    public void addMessage(Message message) {
        messages.add(0, message);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(messages.get(viewType).userId.equals(currentUserId) == VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(inflater.inflate(R.layout.chat_container_sent_message, parent, false));
        }else {
            return new ReceivedMessageViewHolder(inflater.inflate(R.layout.chat_container_received_message, parent, false));
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
//        imageGridViewAdapter = new ImageGridViewAdapter();

        if(message.userId.equals(currentUserId) == VIEW_TYPE_SENT) {
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
            textMessage = (TextView) itemView.findViewById(R.id.textMessage);
            textDateTime = (TextView) itemView.findViewById(R.id.textDateTime);
            imageMessage = (ImageView) itemView.findViewById(R.id.imageMessage);

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
                        Toast.makeText(itemView.getContext(), "memo 보여주기", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

        public void setItem(Message message) throws JSONException {  //어떻게 보여줄지 설정
            if(message.messageType.equals("file")) {
                if(message.message.startsWith("[{")) { // file 형식
                    fileLayout.setVisibility(View.VISIBLE);
                    imageMessage.setVisibility(View.GONE);
                    textMessage.setVisibility(View.GONE);
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
                    textMessage.setVisibility(View.GONE);
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
                    textMessage.setVisibility(View.GONE);
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
                textMessage.setVisibility(View.GONE);
//                gridView.setVisibility(View.GONE);
                gridLayout.setVisibility(View.GONE);
                videoLayout.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(message.message);
                textMemoName.setText(jsonObject.getString("memo_title"));
                imageMemoIcon.setImageResource(R.drawable.memo_icon);

            }else if(message.messageType.equals("message")){  // message
                textMessage.setText(message.message);
                textMessage.setVisibility(View.VISIBLE);
                imageMessage.setVisibility(View.GONE);
                fileLayout.setVisibility(View.GONE);
                memoLayout.setVisibility(View.GONE);
//                gridView.setVisibility(View.GONE);
                gridLayout.setVisibility(View.GONE);
                videoLayout.setVisibility(View.GONE);
            }else if(message.messageType.equals("video")){ // video
                textMessage.setText(message.message);
                videoLayout.setVisibility(View.VISIBLE);
                textMessage.setVisibility(View.GONE);
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
            textMessage = (TextView) itemView.findViewById(R.id.textMessage);
            textDateTime = (TextView) itemView.findViewById(R.id.textDateTime);
            textReceivedName = (TextView) itemView.findViewById(R.id.text_received_name);
            imageMessage = (ImageView) itemView.findViewById(R.id.imageMessage);

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
                        Toast.makeText(itemView.getContext(), "memo 보여주기", Toast.LENGTH_LONG).show();
                    }


                }
            });

        }

        public void setItem(Message message) throws JSONException {  //어떻게 보여줄지 설정
            if(message.messageType.equals("file")) {
                if(message.message.startsWith("[{")) { // file 형식
                    fileLayout.setVisibility(View.VISIBLE);
                    imageMessage.setVisibility(View.GONE);
                    textMessage.setVisibility(View.GONE);
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
                    textMessage.setVisibility(View.GONE);
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
                    textMessage.setVisibility(View.GONE);
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
                textMessage.setVisibility(View.GONE);
//                gridView.setVisibility(View.GONE);
                gridLayout.setVisibility(View.GONE);
                videoLayout.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(message.message);
                textMemoName.setText(jsonObject.getString("memo_title"));
                imageMemoIcon.setImageResource(R.drawable.memo_icon);

            }else if(message.messageType.equals("message")){  // message
                textMessage.setText(message.message);
                textMessage.setVisibility(View.VISIBLE);
                imageMessage.setVisibility(View.GONE);
                fileLayout.setVisibility(View.GONE);
                memoLayout.setVisibility(View.GONE);
//                gridView.setVisibility(View.GONE);
                gridLayout.setVisibility(View.GONE);
                videoLayout.setVisibility(View.GONE);
            }else if(message.messageType.equals("video")){ // video
                textMessage.setText(message.message);
                videoLayout.setVisibility(View.VISIBLE);
                textMessage.setVisibility(View.GONE);
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


}
