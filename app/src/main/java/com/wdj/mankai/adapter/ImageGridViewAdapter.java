package com.wdj.mankai.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wdj.mankai.R;

import java.util.ArrayList;

public class ImageGridViewAdapter extends BaseAdapter {
    ArrayList<String> items = new ArrayList<String>();

    @Override
    public int getCount() {
        return items.size();
    }
    public void addItem(String item) {
        items.add(item);
    }
    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        final String item = items.get(i);

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.gridview_list_item, viewGroup, false);

            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_image);

            iv_icon.setImageResource(R.drawable.ic_back);

        } else {
            View view2 = new View(context);
            view2 = (View) view;
        }

        //각 아이템 선택 event
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        return view;  //뷰 객체 반환
    }
}
