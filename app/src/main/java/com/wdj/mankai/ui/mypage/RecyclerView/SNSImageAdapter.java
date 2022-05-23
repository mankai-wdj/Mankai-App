package com.wdj.mankai.ui.mypage.RecyclerView;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wdj.mankai.R;

import java.util.ArrayList;

public class SNSImageAdapter extends RecyclerView.Adapter<SNSImageAdapter.ViewHolder> {

    private ArrayList<Uri> uriArrayList;

    public SNSImageAdapter(ArrayList<Uri> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }

    /*xml을 ViewHolder에 준다.*/
    @NonNull
    @Override
    public SNSImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        Context context = parent.getContext();
        View view = inflater.inflate(R.layout.sns_single_image,parent,false);

        return new ViewHolder(context,view);
    }

    /*데이터를 뷰에 바인딩한다.*/
    @Override
    public void onBindViewHolder(@NonNull SNSImageAdapter.ViewHolder holder, int position) {
        if(uriArrayList.get(position).toString().substring(0,6) == "content"){
            holder.imageView.setImageURI(uriArrayList.get(position));
        }
        else{
            Glide.with(holder.forContext).load(uriArrayList.get(position)).dontAnimate().into(holder.imageView);
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uriArrayList.remove(uriArrayList.get(position));
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,getItemCount());
            }
        });
    }

    /*item의 크기*/
    @Override
    public int getItemCount() {
    return uriArrayList.size();

    }

    /*View에 id를 할당한다.*/
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView,delete;
        Context forContext;

        public ViewHolder(Context context, @NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            delete = itemView.findViewById(R.id.delete);
            forContext = context;
        }
    }
}
