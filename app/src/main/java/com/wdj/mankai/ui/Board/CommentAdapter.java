package com.wdj.mankai.ui.Board;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wdj.mankai.R;
import com.wdj.mankai.data.CommentData;
import com.wdj.mankai.ui.main.MainActivity;
import com.wdj.mankai.ui.mypage.YouPage;

import org.w3c.dom.Comment;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class CommentAdapter  extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<CommentData> commentData = null;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        TextView commentName = itemView.findViewById(R.id.snsCommentName);
        TextView commentComment = itemView.findViewById(R.id.snsCommentContent);
        ImageView commentProfile = itemView.findViewById(R.id.snsCommentUserImage);
        ImageView trans_btn = itemView.findViewById(R.id.translate_btn);
        TextView translate_text = itemView.findViewById(R.id.translate_text);
        LinearLayout layout = itemView.findViewById(R.id.linear);
    }
    public CommentAdapter(ArrayList<CommentData> commentData)
    {
        this.commentData = commentData;
        Log.d("Board", "값넣음");

    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.comment_view,parent,false);
        CommentAdapter.ViewHolder vh = new CommentAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentData data = commentData.get(position);
        Log.d("Board", "data바뀜");
        holder.commentComment.setText(data.getComment());
        holder.commentName.setText(data.getName());

        if(data.getProfile().equals("null")){
            holder.commentProfile.setImageResource(R.drawable.profileimage);
        }
        Glide.with(holder.itemView.getContext())
                .load(data.getProfile())
                .into(holder.commentProfile);



        holder.translate_text.setText("");
        holder.translate_text.setVisibility(View.GONE);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), YouPage.class);
                intent.putExtra("youURL",data.getUser_id());
                Bundle bundle = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.slide_in_right, R.anim.slide_wait).toBundle();
                view.getContext().startActivity(intent, bundle);

            }
        });

        holder.trans_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String translate;
                translate = PapagoTranslate.getTranslation(data.getComment(),"ko");
                holder.translate_text.setText(translate);
                holder.translate_text.setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    public int getItemCount() {
        return commentData.size();
    }
}
