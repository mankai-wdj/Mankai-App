package com.wdj.mankai.ui.Group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wdj.mankai.R;

public class Fragcate extends Fragment {
    private View view;


    public static Fragcate newInstance() {
        Fragcate fragcate = new Fragcate();
        return  fragcate;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_frg_cate, container, false);
        return view;
    }
}
