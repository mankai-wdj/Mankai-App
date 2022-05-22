package com.wdj.mankai.ui.Group;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPageAdapter extends FragmentPagerAdapter {



    private ArrayList<String> categoryList,categoryType,categoryId = new ArrayList<String>();
    public ViewPageAdapter(@NonNull FragmentManager fm ,ArrayList<String> categoryList, ArrayList<String> categoryType,ArrayList<String> categoryId) {
        super(fm);
        this.categoryList = categoryList;
        this.categoryType = categoryType;
        this.categoryId = categoryId;
    }
    //프래그먼트 교체를 하는 곳 (그룹 정보에 있는 곳임)
    @NonNull
    @Override
    public Fragment getItem(int position) {

            if(position == 0)
                return FragGroupinfor.newInstance();
            else
            {
                Bundle bundle = new Bundle();
                bundle.putString("category_id",categoryId.get(position-1));
                if(this.categoryType.get(position-1).equals("BOARD")) {
                    Fragnotice fn = Fragnotice.newInstance();
                    fn.setArguments(bundle);
                    return fn;
                }
                else if(this.categoryType.get(position-1).equals("SNS")) {
                    Fragboard fn = Fragboard.newInstance();
                    fn.setArguments(bundle);
                    return fn;
                }
            }

            return Fragcate.newInstance();
    }

    @Override
    public int getCount() {
        return categoryList.size()+1;
    }

    //상단에 탭 레이아웃 인디케이터 쪽에 텍스트를 선언해주는 곳
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0)
        {
            return "그룹 소개";
        }
        else{
            return categoryList.get(position-1);
        }
    }
}
