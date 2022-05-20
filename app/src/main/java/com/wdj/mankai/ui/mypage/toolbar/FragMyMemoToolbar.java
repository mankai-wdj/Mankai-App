package com.wdj.mankai.ui.mypage.toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wdj.mankai.R;

public class FragMyMemoToolbar extends Fragment {
    private View view;
    ImageView plusMemoIv;

    public static FragMyMemoToolbar newInstance(){
        FragMyMemoToolbar fragMyMemoToolbar = new FragMyMemoToolbar();

        return fragMyMemoToolbar;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_my_memo_toolbar,container,false);
        plusMemoIv = view.findViewById(R.id.myMemoAddButton);

        plusMemoIv.setOnClickListener                                                                                                                                   (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("메모추가버튼 클릭","");
            }
        });

        return view;
    }
}
