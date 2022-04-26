package com.wdj.mankai.ui.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wdj.mankai.R;

public class FragMyMemos extends Fragment {
    private View view;

    public static FragMyMemos newInstance(){
        FragMyMemos fragMyMemos = new FragMyMemos();

        return fragMyMemos;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_mymemos,container,false);

        return view;
    }
}
