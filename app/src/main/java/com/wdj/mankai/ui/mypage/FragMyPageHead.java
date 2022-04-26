package com.wdj.mankai.ui.mypage;

import static com.wdj.mankai.R.layout.frag_my_page_head;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.wdj.mankai.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragMyPageHead extends Fragment {
    private View view;
    private ViewPagerAdapter viewPagerAdapter;
    String userName;
    String userDescription;
    String userProfile;

    public static FragMyPageHead newInstance(){
        FragMyPageHead fragMyPageHead = new FragMyPageHead();

        return fragMyPageHead;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        view = inflater.inflate(R.layout.frag_my_page_head,container,false);

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
//    이거 사진 관련해서 오류뜨는데 내일 확인해보기 바람.
            }

//      이미지가 안 올라가네


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
                    if(position == 0){

                    }
                    if(position == 1){

                    }
                    if(position == 2){


                    }
                    if(position == 3){

                    }
                    if(position == 4){

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