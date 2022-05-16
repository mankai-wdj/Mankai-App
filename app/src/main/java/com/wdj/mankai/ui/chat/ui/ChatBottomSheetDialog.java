package com.wdj.mankai.ui.chat.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wdj.mankai.R;
import com.wdj.mankai.data.model.Room;
import com.wdj.mankai.ui.chat.ChatContainerActivity;

import org.json.JSONArray;
import org.json.JSONException;

public class ChatBottomSheetDialog extends BottomSheetDialogFragment{
    private View view;
    private BottomSheetListener mListener;
    private LinearLayout filePhotoLayout, sttLayout;
    private TextView tvSttTitle;
    private GridLayout gridSttLayout;
    private Room room;

    public ChatBottomSheetDialog(Room room) {
        this.room = room;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chat_bottom_sheet_layout, container, false);
        mListener = (BottomSheetListener) getContext();
        filePhotoLayout = view.findViewById(R.id.filePhotoLayout);
        sttLayout = view.findViewById(R.id.sttLayout);
        tvSttTitle = view.findViewById(R.id.tvSttTitle);
        gridSttLayout = view.findViewById(R.id.gridSttLayout);

        try {
            JSONArray jsonArray = new JSONArray(room.users);
            System.out.println(jsonArray.length());
            for(int i = 0; i< jsonArray.length(); i++) {
                LayoutInflater inflater2 = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LinearLayout imageView = (LinearLayout) inflater2.inflate(R.layout.grid_layout_stt, null);
                Button country = imageView.findViewById(R.id.btCountry);

                country.setText(jsonArray.getJSONObject(i).getString("country"));
                gridSttLayout.addView(imageView);
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


    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }
}
