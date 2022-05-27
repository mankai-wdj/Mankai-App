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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.wdj.mankai.R;
import com.wdj.mankai.data.FlagClass;
import com.wdj.mankai.data.model.AppHelper;
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
    int userId;
    boolean first = true;
    String url;




    public static MyPageFragment newInstance(){
        MyPageFragment fragMyPageHead = new MyPageFragment();

        return fragMyPageHead;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_my_page,container,false);


        url = "https://api.mankai.shop/api/";
        AppHelper.requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        TextView userNameView = (TextView) view.findViewById(R.id.userName);
        TextView userDescriptionView = (TextView) view.findViewById(R.id.userDescription);
        CircleImageView userProfileView = (CircleImageView) view.findViewById(R.id.userProfile);

            userName =  ((FlagClass) getActivity().getApplicationContext()).getUserName();;
            userDescription =  ((FlagClass) getActivity().getApplicationContext()).getUserDescription();;
            userProfile =  ((FlagClass) getActivity().getApplicationContext()).getUserProfile();;
            userId =  ((FlagClass) getActivity().getApplicationContext()).getUserId();
            Log.e("USER" ,userName);
            userNameView.setText(userName);
            userDescriptionView.setText(userDescription);
            Glide.with(this).load(userProfile).placeholder(R.drawable.ic_launcher_foreground).dontAnimate().into(userProfileView);


//      뷰페이저 세팅
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(String.valueOf(userId),getActivity().getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);





        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if(position == 0 && first){
                    onPageSelected(0);
                    first = false;
                }
                /* 최초에 1회만 실시(요청의 낭비를 막기 위해서 onPageSelected에서 요청하기로
                *  했는데 처음 페이지에서는 onPageSelected를 호출하지 않아 최초에 한번만
                *  onPageScrolled가 처음에도 호출되는 점을 이용*/

            }
//            onPageScrolled는 페이지를 잡고 드래그 할 때 조그마한 움직임이라도 있으면 계속 호출한다.


            /*오직 상태바 변경을 위해서*/
            @Override
            public void onPageSelected(int position) {
                if(position == 0||position == 1||position == 2||position ==3){
                    FragMyMemoExceptToolbar fragMyMemoExceptToolbar = new FragMyMemoExceptToolbar();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.my_page_head,fragMyMemoExceptToolbar).commit();
                }else{
                    FragMyMemoToolbar fragMyMemoToolbar = new FragMyMemoToolbar();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.my_page_head,fragMyMemoToolbar).commit();
                }
            }


            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("onPageScrollStateChanged","onPageScrollStateChanged"+state);
            }
        });

        return view;
    }


}