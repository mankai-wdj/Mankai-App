package com.wdj.mankai.ui.mypage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wdj.mankai.R;
import com.wdj.mankai.ui.mypage.RecyclerView.Memo;
import com.wdj.mankai.ui.mypage.RecyclerView.MemoAdapter;

public class FragMyMemos extends Fragment implements ViewPagerData{
    private View view;

    RecyclerView memoRecyclerView;

    MemoAdapter adapter;

    public static FragMyMemos newInstance(){
        FragMyMemos fragMyMemos = new FragMyMemos();

        return fragMyMemos;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_mymemos,container,false);

        memoRecyclerView = view.findViewById(R.id.mymemo_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        memoRecyclerView.setLayoutManager(layoutManager);

        adapter = new MemoAdapter(getActivity());
        memoRecyclerView.setAdapter(adapter);

        adapter.addMemo(new Memo(43,"memo_content","memo_preview"));
        adapter.addMemo(new Memo(44,"memo_content","memo_preview"));
        adapter.addMemo(new Memo(45,"memo_content","memo_preview"));
        adapter.addMemo(new Memo(45,"memo_content","memo_preview"));
        adapter.addMemo(new Memo(45,"memo_content","memo_preview"));
        adapter.addMemo(new Memo(45,"memo_content","memo_preview"));
        adapter.addMemo(new Memo(45,"memo_content","memo_preview"));
        adapter.addMemo(new Memo(45,"memo_content","memo_preview"));
        adapter.addMemo(new Memo(45,"memo_content","memo_preview"));
        adapter.addMemo(new Memo(45,"memo_content","memo_preview"));
        adapter.addMemo(new Memo(45,"memo_content","memo_preview"));
        adapter.addMemo(new Memo(45,"memo_content","memo_preview"));

        return view;
    }

    @Override
    public void memoResponse(String response) {
        Log.i("responseInMyMemos",response);
    }
}
