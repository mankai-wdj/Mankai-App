package com.wdj.mankai.ui.Board;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wdj.mankai.R;

import java.util.ArrayList;

public class BoardCreateAdapter extends RecyclerView.Adapter<BoardCreateAdapter.ViewHolder> {


    private ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
    private  BoardCreateAdapter boardCreateAdapter;
    public BoardCreateAdapter(ArrayList<Uri> mArrayUri){
        this.mArrayUri = mArrayUri;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ViewHolder(View itemView){
            super(itemView);
        }
        //    layout ID
        ImageView createImage = itemView.findViewById(R.id.boardCreateView);
        ImageView deleteBtn = itemView.findViewById(R.id.deleteBtn);

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.board_create_view,parent,false);
        BoardCreateAdapter.ViewHolder vh = new BoardCreateAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri mData = mArrayUri.get(position);
        holder.deleteBtn.setVisibility(View.GONE);
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Create", "Position" + position);
                BoardCreateActivity.mArrayUri.remove(position);
                BoardCreateActivity.adapter.notifyDataSetChanged();
                holder.createImage.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.DST);
                holder.deleteBtn.setVisibility(View.GONE);
                Log.d("Create", "onClick:Delete");
            }
        });
        holder.createImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.deleteBtn.getVisibility()==View.VISIBLE){
                    holder.createImage.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.DST);
                    holder.deleteBtn.setVisibility(View.GONE);
                }
                else{
                    holder.createImage.setColorFilter(Color.parseColor("#666666"), PorterDuff.Mode.MULTIPLY);
                    holder.deleteBtn.setVisibility(View.VISIBLE);
                }
                Log.d("Create", "onClick:Image");
            }
        });
        holder.createImage.setImageURI(mData);
    }

    @Override
    public int getItemCount() {
        return mArrayUri.size();
    }
}
