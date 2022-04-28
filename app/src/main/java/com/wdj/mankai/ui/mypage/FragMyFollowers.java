package com.wdj.mankai.ui.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabItem;
import com.wdj.mankai.R;

public class FragMyFollowers extends Fragment {
    private View view;

    public static FragMyFollowers newInstance(){
        FragMyFollowers fragFollowers = new FragMyFollowers();

        return fragFollowers;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_myfollowers,container,false);
        return view;
    }
}
