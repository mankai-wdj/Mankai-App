package com.wdj.mankai.ui.Group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wdj.mankai.R;

public class Fragnotice extends Fragment {
    private View view;


    public static Fragnotice newInstance() {
        Fragnotice fragnotice = new Fragnotice();
        return  fragnotice;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_frg_notice, container, false);
        return view;
    }
}
