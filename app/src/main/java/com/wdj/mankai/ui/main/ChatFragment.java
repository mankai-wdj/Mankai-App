package com.wdj.mankai.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.wdj.mankai.R;
import com.wdj.mankai.adapter.ChatViewPagerAdapter;
import com.wdj.mankai.adapter.RoomsAdapter;
import com.wdj.mankai.data.model.Room;
import com.wdj.mankai.ui.chat.ChatContainerActivity;
import com.wdj.mankai.ui.chat.ChatCreateActivity;
import com.wdj.mankai.ui.chat.ChatDMListFragment;
import com.wdj.mankai.ui.chat.ChatGroupListFragment;
import com.wdj.mankai.ui.chat.ChatRoomListRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ChatFragment extends Fragment {
    TabLayout chat_room_tabs;
    private ViewPager2 viewPager2;

    private AppBarLayout appBarLayout;
    ImageView imageCreateChat;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_chat, container, false);

        chat_room_tabs = rootView.findViewById(R.id.chat_room_tabs);
        appBarLayout = rootView.findViewById(R.id.appBarLayout);

        viewPager2 = rootView.findViewById(R.id.viewPager2);
        imageCreateChat = rootView.findViewById(R.id.imageCreateChat);

        imageCreateChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("클릭됨");
                Intent intent = new Intent(view.getContext(), ChatCreateActivity.class);
                startActivity(intent);


            }
        });


        ChatViewPagerAdapter adapter = new ChatViewPagerAdapter(getActivity());
        viewPager2.setAdapter(adapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        new TabLayoutMediator(chat_room_tabs, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 1:
                        tab.setText("GROUP");
                        break;
                    default:
                        tab.setText("DM");
                        break;
                }
            }
        }).attach();
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override public void onPageSelected(int position) {
                super.onPageSelected(position);
                appBarLayout.setExpanded(true);
            }
        });

        return rootView;
    }


}