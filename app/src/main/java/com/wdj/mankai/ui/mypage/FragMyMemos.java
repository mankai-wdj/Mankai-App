package com.wdj.mankai.ui.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wdj.mankai.R;

public class FragMyMemos extends Fragment {
    private View view;
    private MyAdapter adapter;

    RecyclerView recycleView;

    String s1[];

//    Button sns_btn, board_btn;


    public static FragMyMemos newInstance(){
        FragMyMemos fragMyMemos = new FragMyMemos();

        return fragMyMemos;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_mymemos,container,false);

        recycleView = view.findViewById(R.id.recycleView);

        s1 = getResources().getStringArray(R.array.title);



//        MyAdapter myAdapter = new MyAdapter(this, s1);
//        recycleView.setAdapter(myAdapter);
//        recycleView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(getActivity(), s1);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleView.setAdapter(adapter);


        return view;
    }
}
