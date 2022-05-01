package com.wdj.mankai.ui.mypage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabItem;
import com.wdj.mankai.R;
import com.wdj.mankai.ui.mypage.RecyclerView.Followers;
import com.wdj.mankai.ui.mypage.RecyclerView.FollowersAdapter;
import com.wdj.mankai.ui.mypage.RecyclerView.Memo;

public class FragMyFollowers extends Fragment implements ViewPagerData{
    private View view;
    private String myFollowersData;

    RecyclerView myFollowers_recyclerview;
    FollowersAdapter adapter;

    public static FragMyFollowers newInstance(String myFollowersData){
        FragMyFollowers fragFollowers = new FragMyFollowers();
        return fragFollowers;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_myfollowers,container,false);

        myFollowers_recyclerview = view.findViewById(R.id.myFollowers_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        myFollowers_recyclerview.setLayoutManager(layoutManager);

        adapter = new FollowersAdapter(getActivity());
        myFollowers_recyclerview.setAdapter(adapter);


        adapter.addFollowers(new Followers(43,"memo_content","@drawable/a"));

        return view;
    }

    @Override
    public void memoResponse(String response) {
        Log.i("responseInFollowers",response);



    }
}
