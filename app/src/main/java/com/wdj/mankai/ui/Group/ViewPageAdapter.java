package com.wdj.mankai.ui.Group;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.wdj.mankai.ui.Group.FragGroupinfor;
import com.wdj.mankai.ui.Group.Fragboard;
import com.wdj.mankai.ui.Group.Fragcate;
import com.wdj.mankai.ui.Group.Fragnotice;

public class ViewPageAdapter extends FragmentPagerAdapter {


    public ViewPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    //프래그먼트 교체를 하는 곳 (그룹 정보에 있는 곳임)
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return FragGroupinfor.newInstance();
            case 1:
                return Fragboard.newInstance();
            case 2:
                return Fragnotice.newInstance();
            case 3:
                return Fragcate.newInstance();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    //상단에 탭 레이아웃 인디케이터 쪽에 텍스트를 선언해주는 곳
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "그룹소개";
            case 1:
                return "게시판";
            case 2:
                return "공지사항";
            case 3:
                return "카테고리";
            default:
                return null;

        }
    }
}
