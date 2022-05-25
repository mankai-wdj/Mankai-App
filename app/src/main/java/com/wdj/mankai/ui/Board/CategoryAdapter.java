package com.wdj.mankai.ui.Board;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.divider.MaterialDivider;
import com.wdj.mankai.R;
import com.wdj.mankai.ui.main.HomeFragment;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {


    private String[] CategoryList;
    private int[] logo_list;


    public class ViewHolder extends RecyclerView.ViewHolder{
        ViewHolder(View itemView){
            super(itemView);
        }
        //    layout ID
        Button categoryName = itemView.findViewById(R.id.category_name);
        MaterialDivider divider = itemView.findViewById(R.id.categorydivier);
        ImageView category_logo = itemView.findViewById(R.id.category_logo);

    }
    public CategoryAdapter(String[] list,int[] logo_list){
        this.logo_list = logo_list;
        this.CategoryList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.board_category_view,parent,false);
        CategoryAdapter.ViewHolder vh = new CategoryAdapter.ViewHolder(view);

        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String data = CategoryList[position];
        holder.categoryName.setText(data);

        holder.category_logo.setImageResource(logo_list[position]);

        if(position%2==1){
            holder.divider.setVisibility(View.GONE);
        }
        holder.categoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment.category = holder.categoryName.getText().toString();
                HomeFragment.category_text.setText(HomeFragment.category);
                HomeFragment.list.clear();
                HomeFragment.GETBOARD("/board/show/"+HomeFragment.category+"/?page=1");
                HomeFragment.CurrentPage = 2;
                BoardCategoryActivity.CategoryActivity.finish();
                Log.d("Board", "버튼누름 " + HomeFragment.list);
            }
        });
    }

    @Override
    public int getItemCount() {
        return CategoryList.length;
    }
}
