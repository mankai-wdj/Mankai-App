package com.wdj.mankai.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.wdj.mankai.ui.chat.ChatDMListFragment;
import com.wdj.mankai.ui.chat.ChatGroupListFragment;

import java.util.ArrayList;
import java.util.List;

public class ChatViewPagerAdapter extends FragmentStateAdapter {

    private final int TYPE_DM = 0;
    private final int TYPE_GROUP = 1;
    private List<Integer> listType = new ArrayList<>();


    public ChatViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        listType.add(TYPE_DM);
        listType.add(TYPE_GROUP);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case TYPE_GROUP:
                return new ChatGroupListFragment();
            default:
                return new ChatDMListFragment();
        }

    }

    @Override
    public int getItemCount() {
        return listType.size();
    }
}
