package com.wdj.mankai.ui.mypage.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.wdj.mankai.R;
import com.wdj.mankai.data.model.AppHelper;
import com.wdj.mankai.ui.mypage.FragMyMemos;

import java.util.ArrayList;
import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Memo> memos = new ArrayList<>();
    Context context;

    public MemoAdapter (ArrayList<Memo> memos){
        this.memos = memos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType == Code.ViewType.SNS_VIEWTYPE)
        {
            view = inflater.inflate(R.layout.sns_memo_item,parent,false);
            return new SNSViewHolder(context,view);
        }

        else if(viewType == Code.ViewType.BOARD_VIEWTYPE)
        {
            view = inflater.inflate(R.layout.board_memo_item,parent,false);
            return new BOARDViewHolder(context,view);
        }

        return null;
        /* 아이템하나(memo_item)에 대한 xml을 ViewHolder에 준다. 그리고 ViewHolder에 가면 itemView내에 있는 view들을
        *  itemView에 할당시킨다.
        * */
    }

    @Override
    public int getItemViewType(int position) {
        return memos.get(position).getViewType();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Memo memo = memos.get(position);
        boolean isVisible = memo.visibility;
        if(holder instanceof SNSViewHolder)
        {

            ((SNSViewHolder)holder).content_title.setText(memo.getMemo_title());
            ((SNSViewHolder)holder).content_text.setText(memo.getMemo_content_text());
            ((SNSViewHolder)holder).content_text_detail.setText(memo.getMemo_content_text());
            List<SlideModel> slideModels = new ArrayList<>();
            ArrayList<String> imageUrl = memo.getImageUrl();
            if(imageUrl.size() > 0) {
                ((SNSViewHolder)holder).imageSlider.setVisibility(View.VISIBLE);
                for (int i = 0; i < imageUrl.size(); i++) {
                    slideModels.add(new SlideModel(imageUrl.get(i)));
                }
            }
            else{
                ((SNSViewHolder)holder).imageSlider.setVisibility(View.GONE);
            }
            ((SNSViewHolder)holder).imageSlider.setImageList(slideModels,true);
            ((SNSViewHolder)holder).id = (memo.getMemo_id());



            ((SNSViewHolder)holder).expandedLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);

            ((SNSViewHolder)holder).editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(((SNSViewHolder) holder).forContext, SNSEditMemo.class);
                    intent.putExtra("memoId",((SNSViewHolder) holder).id);
                    intent.putExtra("memoTitle",memo.getMemo_title());
                    intent.putExtra("memoContentText",memo.getMemo_content_text());

                    ((SNSViewHolder) holder).forContext.startActivity(intent);
                }
            });


        }
        else if(holder instanceof BOARDViewHolder)
        {
            ((BOARDViewHolder)holder).content_title.setText(memo.getMemo_title());
            ((BOARDViewHolder)holder).content_text.setText("상세보기");
            ((BOARDViewHolder)holder).id = (memo.getMemo_id());
            ((BOARDViewHolder)holder).webView.loadData(memo.getMemo_content_text(),"text/html","UTF-8");

            WebSettings settings = ((BOARDViewHolder)holder).webView.getSettings();
            settings.setDomStorageEnabled(true);

            ((BOARDViewHolder)holder).webView.getSettings().setJavaScriptEnabled(true);
            ((BOARDViewHolder)holder).webView.loadUrl("http://localhost:3000/boardmemo/"+memo.getMemo_id());
            ((BOARDViewHolder)holder).webView.setWebChromeClient(new WebChromeClient());
            ((BOARDViewHolder)holder).webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Log.d("check URL",url);
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    ((BOARDViewHolder)holder).webViewProgress.setVisibility(View.VISIBLE);
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    ((BOARDViewHolder)holder).webViewProgress.setVisibility(View.GONE);
                    super.onPageFinished(view, url);
                }
            });





            /*부모스크롤에게 WebView가 스크롤 권한을 안 뺏기기 위함.*/
            ((BOARDViewHolder)holder).webView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    view.getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            ((BOARDViewHolder)holder).webView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                        Log.i("ScollChange","onSchollChange");
                }
            });



            /*BOARD게시판 수정*/
            ((BOARDViewHolder)holder).editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(((BOARDViewHolder) holder).forContext,BoardEditMemo.class);
                    intent.putExtra("memoId",((BOARDViewHolder) holder).id);
                    ((BOARDViewHolder) holder).forContext.startActivity(intent);
                }
            });


            ((BOARDViewHolder)holder).expandedLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return memos.size();
    }

    public void addMemo(Memo memo){
        memos.add(memo);
    }

    class SNSViewHolder extends RecyclerView.ViewHolder{

        TextView content_title;
        TextView content_text;
        TextView content_text_detail;
        ImageSlider imageSlider;
        FrameLayout memo_container;
        ConstraintLayout expandedLayout;
        Button editButton;
        Button deleteButton;
        Context forContext;
        int id;

        public SNSViewHolder(Context context,@NonNull View itemView) {
            super(itemView);
            forContext = context;
            content_title = itemView.findViewById(R.id.sns_memo_title);
            content_text = itemView.findViewById(R.id.sns_memo_content_text_preview);
            content_text_detail = itemView.findViewById(R.id.sns_memo_content_text_detail);
            imageSlider = itemView.findViewById(R.id.sns_memo_slider);
            memo_container = itemView.findViewById(R.id.sns_memo_container);
            expandedLayout = itemView.findViewById(R.id.sns_memo_detail);
            editButton = itemView.findViewById(R.id.sns_memo_edit_button);
            deleteButton = itemView.findViewById(R.id.sns_memo_delete_button);

            /*데이터바인딩 뿐만아니라 눌렀을 때의 조작 등 해당뷰에 접근하려면
            * View 할당까지 해야한다. 단순히 반복해서 보이기만 해도 되면
            * View 할당과정을 거치지 않아도 된다. */

            memo_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Memo memo = memos.get(getAdapterPosition());
                    memo.setVisibility(!memo.isVisibility());
                    notifyItemChanged(getAdapterPosition());
                }
            });



            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Memo memo = memos.get(getAdapterPosition());
                    snsMemoDelete(memo);
                }
            });
        }

        public void snsMemoDelete(Memo memo){
            StringRequest snsMemoDeleteRequest = new StringRequest(
                    Request.Method.POST,
                    "https://api.mankai.shop/api/deletememos/" + memo.memo_id,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("delete됨","");
                            memos.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                        }
                    }
                    ,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("delete오류","");
                        }
                    }
            );
            snsMemoDeleteRequest.setShouldCache(false);
            AppHelper.requestQueue.add(snsMemoDeleteRequest);
        }

    }

    class BOARDViewHolder extends RecyclerView.ViewHolder{
        TextView content_title;
        TextView content_text;
        WebView webView;
        ProgressBar webViewProgress;
        FrameLayout memoContainer;
        ConstraintLayout expandedLayout;
        Button editButton;
        Button deleteButton;
        Context forContext;
        int id;

        public BOARDViewHolder(Context context,@NonNull View itemView) {
            super(itemView);
            forContext = context;
            webViewProgress = itemView.findViewById(R.id.webview_progressbar);
            content_title = itemView.findViewById(R.id.board_memo_title);
            content_text = itemView.findViewById(R.id.board_memo_content_text_preview);
            webView = itemView.findViewById(R.id.board_memo_webview);
            memoContainer = itemView.findViewById(R.id.board_memo_container);
            expandedLayout = itemView.findViewById(R.id.board_memo_detail);
            deleteButton = itemView.findViewById(R.id.board_memo_delete_button);
            editButton = itemView.findViewById(R.id.board_memo_edit_button);

            memoContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Memo memo = memos.get(getAdapterPosition());
                    memo.setVisibility(!memo.isVisibility());
                    notifyItemChanged(getAdapterPosition());
                }
            });




            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Memo memo = memos.get(getAdapterPosition());
                    boardMemoDelete(memo);
                    /*?!DB에서 삭제해야 됨*/
                }
            });


        }


        public void boardMemoDelete(Memo memo){
            StringRequest boardMemoDeleteRequest = new StringRequest(
                    Request.Method.POST,
                    "https://api.mankai.shop/api/deletememos/" + memo.memo_id,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("delete됨","");
                            memos.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                        }
                    }
                    ,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("delete오류","");
                        }
                    }
            );
            boardMemoDeleteRequest.setShouldCache(false);
            AppHelper.requestQueue.add(boardMemoDeleteRequest);
        }


    }


}

