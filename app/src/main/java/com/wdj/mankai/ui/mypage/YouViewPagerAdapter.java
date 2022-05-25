package com.wdj.mankai.ui.mypage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class YouViewPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> items = new ArrayList<Fragment>();

    public YouViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
//        items.add(new FragYouFollowers());
//        items.add(new FragYouFollowings());
//        items.add(new FragYouPosts());
//        items.add(new FragYouGroups());
    }

    public void addItem(Fragment item){
        items.add(item);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                return new FragYouFollowers();
            case 1 :
                return new FragYouFollowings();
            case 2 :
                return new FragYouPosts();
            case 3 :
                return new FragYouGroups();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Followers";
            case 1:
                return "Followings";
            case 2:
                return "Posts";
            case 3:
                return "Groups";
            default:
                return null;
        }
    }
}
