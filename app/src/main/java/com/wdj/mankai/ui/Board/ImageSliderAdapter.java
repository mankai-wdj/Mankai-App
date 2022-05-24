package com.wdj.mankai.ui.Board;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wdj.mankai.R;
import com.wdj.mankai.data.CommentData;

import java.util.ArrayList;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ViewHolder> {
    private ArrayList<String> imagelist;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagelist);
        }
    }
    public ImageSliderAdapter(ArrayList<String> imagelist)
    {
        this.imagelist = imagelist;
    }

    @NonNull
    @Override
    public ImageSliderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.board_image,parent,false);

        ImageSliderAdapter.ViewHolder vh = new ImageSliderAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String data = imagelist.get(position);
        Glide.with(holder.imageView.getContext())
                .load(data)
                .into(holder.imageView);
        Log.d("Board", "data바뀜");

    }

    @Override
    public int getItemCount() {
        return imagelist.size();
    }
}
