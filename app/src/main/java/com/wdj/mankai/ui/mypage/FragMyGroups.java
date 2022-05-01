package com.wdj.mankai.ui.mypage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wdj.mankai.R;

public class FragMyGroups extends Fragment implements ViewPagerData{
    private View view;

    public static FragMyGroups newInstance(){
        FragMyGroups fragMyGroups = new FragMyGroups();

        return fragMyGroups;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_mygroups,container,false);

        return view;
    }

    @Override
    public void memoResponse(String response) {
        Log.i("responseInGroups",response);
    }
}
