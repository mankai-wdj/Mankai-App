package com.wdj.mankai.ui.main;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.wdj.mankai.R;
import com.wdj.mankai.ui.mypage.ViewPagerAdapter;
import com.wdj.mankai.ui.mypage.toolbar.FragMyMemoExceptToolbar;
import com.wdj.mankai.ui.mypage.toolbar.FragMyMemoToolbar;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPageFragment extends Fragment {
    private View view;
    private ViewPagerAdapter viewPagerAdapter;
    String userName;
    String userDescription;
    String userProfile;

    public static MyPageFragment newInstance(){
        MyPageFragment fragMyPageHead = new MyPageFragment();

        return fragMyPageHead;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        view = inflater.inflate(R.layout.fragment_my_page,container,false);

        TextView userNameView = (TextView) view.findViewById(R.id.userName);
        TextView userDescriptionView = (TextView) view.findViewById(R.id.userDescription);
        CircleImageView userProfileView = (CircleImageView) view.findViewById(R.id.userProfile);

        if(getArguments() != null){

            userName = getArguments().getString("userName");
            Log.i("Test:",userName);
            userDescription = getArguments().getString("userDescription");
            userProfile = getArguments().getString("userProfile");

            userNameView.setText(userName);
            userDescriptionView.setText(userDescription);
            Glide.with(this).load(userProfile).placeholder(R.drawable.ic_launcher_foreground).dontAnimate().into(userProfileView);
//    ?! 이거 사진 관련해서 오류뜨는데 내일 확인해보기 바람.
        }



//      뷰페이저 세팅
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == 0 || position == 1 || position == 2 || position == 3 ){
                    FragMyMemoExceptToolbar fragMyMemoExceptToolbar = new FragMyMemoExceptToolbar();
                    getParentFragmentManager().beginTransaction().replace(R.id.my_page_head,fragMyMemoExceptToolbar).commit();
                }
                if(position == 4){
                    FragMyMemoToolbar fragMyMemoToolbar = new FragMyMemoToolbar();
                    getParentFragmentManager().beginTransaction().replace(R.id.my_page_head,fragMyMemoToolbar);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//    ?! 이걸로 Page의 상태에 따라 상태바를 다르게 하면 된다.
        return view;
    }
}