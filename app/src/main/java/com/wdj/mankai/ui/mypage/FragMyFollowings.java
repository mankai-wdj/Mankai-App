package com.wdj.mankai.ui.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wdj.mankai.R;

public class FragMyFollowings extends Fragment {
    private View view;

    public static FragMyFollowings newInstance(){
        FragMyFollowings fragFollowings = new FragMyFollowings();

        return fragFollowings;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_myfollowings,container,false);

        return view;
    }
}
