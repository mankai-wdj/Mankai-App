package com.wdj.mankai.ui.Board;


import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.wdj.mankai.R;
import com.wdj.mankai.data.BoardData;
import com.wdj.mankai.ui.main.HomeFragment;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {

    private ArrayList<BoardData> mData=null;
    private String TransData;
    private String TransId;

    @Override
    public int getItemViewType(int position) {
        try {
            return mData.get(position).getViewType();
        } catch (JSONException e) {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ViewHolder(View itemView){
            super(itemView);
        }
        //    layout ID
        TextView snsName = itemView.findViewById(R.id.snsName);
        ImageView snsUserImage = itemView.findViewById(R.id.snsUserImage);
        TextView snsContent = itemView.findViewById(R.id.snsContent);
        ViewPager2 viewPager2 = itemView.findViewById(R.id.snsMainImage);
        CircleIndicator3 indicator = itemView.findViewById(R.id.indicator);
        TextView likeCount = itemView.findViewById(R.id.likeCnt);
//      ImageView snsMainImage = itemView.findViewById(R.id.snsMainImage);
        TextView snsComment = itemView.findViewById(R.id.snsComment);
        ImageView commentBtn = itemView.findViewById(R.id.commentBtn);
        TextView commentCount = itemView.findViewById(R.id.CommentCount);
        ImageView translateBtn = itemView.findViewById(R.id.translate_btn);
        TextView translateText = itemView.findViewById(R.id.translate_text);
        ImageView likeBtn = itemView.findViewById(R.id.like_btn);
    }

    public BoardAdapter(ArrayList<BoardData> list){
        mData = list;
    }
    //    아이템뷰를 위한 뷰홀더 객체 생성하여 리턴

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
//        멀티 뷰타입 분리
        if(viewType == 0)
            view = inflater.inflate(R.layout.board_normal_view,parent,false);
        else if(viewType ==1)
            view = inflater.inflate(R.layout.board_image_view,parent,false);

        BoardAdapter.ViewHolder vh = new BoardAdapter.ViewHolder(view);

        return vh;
    }

    //    position에 해당하는 데이터를 뷰홀더 아이템에 표시
    @Override
    public void onBindViewHolder(BoardAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position){
        BoardData snsdata = mData.get(position);

        try {
            if (snsdata.getViewType()==1){
                holder.viewPager2.setOffscreenPageLimit(1);
                holder.viewPager2.setAdapter(new ImageSliderAdapter(snsdata.getBoardImage()));
                holder.indicator.setViewPager(holder.viewPager2);
                holder.indicator.createIndicators(snsdata.getBoardImage().size(),0);
            }
            holder.snsContent.setText(snsdata.getContent_text());
            holder.snsName.setText(snsdata.getName());
            Glide.with(holder.itemView.getContext())
                    .load(snsdata.getProfile())
                    .into(holder.snsUserImage);

            if(snsdata.getComments()!=null) {
                String ch ="";
                for (int i = 0; i <  snsdata.getComments().size(); i++) {
                    ch +=  snsdata.getComments().get(i)+"\n"  ;
                }
                holder.snsComment.setText(ch);
            }

            holder.likeCount.setText(snsdata.getLike_length());

            if(snsdata.getTranslateText().equals("")){
                holder.translateText.setText("");
                holder.translateText.setTextSize(0);
            }
            else {
                holder.translateText.setText(snsdata.getTranslateText());
                holder.translateText.setTextSize(18);
            }
            holder.translateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String translate = null;
                    try {
                        translate = PapagoTranslate.getTranslation(snsdata.getContent_text(),"ko");
                        Log.d("Board", "position"+HomeFragment.list.get(position).getContent_text());

                        HomeFragment.list.get(position).setTranslateText(translate);
                        holder.translateText.setTextSize(18);
                        holder.translateText.setText(HomeFragment.list.get(position).getTranslateText());
                        Log.d("Board", "translate = "+translate);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            holder.likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Log.d("Board", "Changed"+position+"?"+HomeFragment.list.get(position).getTranslateText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            holder.commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),BoardCommentActivity.class);
                    try {
                        intent.putExtra("id",snsdata.getId());
                        intent.putExtra("content",snsdata.getContent_text());
                        intent.putExtra("name",snsdata.getName());
                        intent.putExtra("profile",snsdata.getProfile());
                        intent.putExtra("like_count",snsdata.getLike_length());
                        intent.putExtra("board_count",position);
                        intent.putExtra("isGroup",snsdata.getIsGroup());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Bundle bundle = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.slide_in_right,R.anim.slide_wait).toBundle();
                    view.getContext().startActivity(intent, bundle);
                }
            });
            holder.commentCount.setText(snsdata.getComment_length());

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }



}
