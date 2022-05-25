package com.wdj.mankai.ui.mypage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdj.mankai.R;
import com.wdj.mankai.data.BoardData;
import com.wdj.mankai.data.model.AppHelper;
import com.wdj.mankai.ui.Board.BoardAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragMyPosts extends Fragment{
    public static int currentPage = 0;
    private View view;
    public static String category;
    public static ArrayList<BoardData> list = new ArrayList<BoardData>();
    public static BoardAdapter adapter;
    private static BoardData boardData;
    private static String URL = "https://api.mankai.shop/api";
    private String userId;
    private String userName;

    private ListView CommentList;

    public FragMyPosts(){

    }

    public static FragMyPosts newInstance(){
        FragMyPosts fragMyPosts = new FragMyPosts();

        return fragMyPosts;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getContext());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_myposts,container,false);


        RecyclerView recyclerView = view.findViewById(R.id.BoardRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));

        adapter = new BoardAdapter(list);
        recyclerView.setAdapter(adapter);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String user_info = sharedPreferences.getString("user_info", "");

        Log.d("jitor", user_info);



        try {
            JSONObject object = new JSONObject(user_info);
            userId = object.getString("id");
            userName = object.getString("name");

            Log.d("jitortext", userId +" : " + userName);
        } catch (Throwable t) {

        }

        GETBOARD("/myposts/"+userId);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == list.size()-1) {
                    GETBOARD("/myposts/"+userId);
                }
            }
        });
        return view;
    }
    public static void GETBOARD(String subPoint) {
//        1차 기본 보드 데이터
        currentPage+=1;
        Log.d("Board", "GETBOARD: "+ currentPage);
        StringRequest request = new StringRequest(Request.Method.GET, URL+subPoint+"?page="+currentPage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("MYPOST", response);
                            adapter.notifyDataSetChanged();
                            JSONObject json = new JSONObject(response);
                            JSONArray BoardJsonArray = json.getJSONArray("data");

                            if(BoardJsonArray.length() ==0){
                                Log.d("Board", "빈값임 ㅋㅋ");
                                JSONObject ch = new JSONObject();
                                ch.put("viewType",2);
                            }
//                          보드데이터 별로 하나씩 요청
                            for (int i = 0; i < BoardJsonArray.length(); i++) {
                                Log.d("jitor", String.valueOf(BoardJsonArray.length()));
                                JSONObject boardJson = BoardJsonArray.getJSONObject(i);
                                boardData = new BoardData(boardJson);
//                               2차 데이터 처리
                                int finalI = i;
                                StringRequest subData = new StringRequest(Request.Method.GET,
                                        URL + "/upload_image/" + boardJson.getString("id"),
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                ArrayList<String> cntString = new ArrayList<String>();
                                                try {
                                                    Log.d("JJJBoard",response );
                                                    JSONObject subJson = new JSONObject(response);

//                                                  사진 데이터 정리
                                                    JSONArray subArray = subJson.getJSONArray("images");
                                                    if(subArray.length()==0){
                                                        for(int i = 0 ;i<list.size();i++)
                                                        {
                                                            if(list.get(i).getId() == boardJson.getString("id")){
                                                                list.get(i).setViewType(0);
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    else{
                                                        for(int i = 0 ; i< subArray.length();i++){
                                                            cntString.add(subArray.getJSONObject(i).getString("url"));
                                                            Log.d("Image 갯수", boardJson.getString("id")+" = "+cntString.get(i));
                                                        }
                                                        for(int i = 0 ;i<list.size();i++)
                                                        {
                                                            if(list.get(i).getId() == boardJson.getString("id")){
                                                                list.get(i).setBoardImage(cntString);
                                                                list.get(i).setViewType(1);

                                                                Log.d("Image", "get " + list.get(i).getBoardImage());
                                                                break;
                                                            }
                                                        }
                                                    }

//                                                  댓글 데이터 정리
                                                    ArrayList<String> commentText = new ArrayList<String>();
                                                    JSONArray CommentArray = subJson.getJSONArray("comments");

//                                                  댓글 1개 이상
                                                    if(CommentArray.length()>0){
                                                        for(int i = 0 ;i<CommentArray.length();i++){
                                                            JSONObject comment = CommentArray.getJSONObject(i);
                                                            commentText.add(comment.getString("user_name")+" : "+comment.getString("comment"));
                                                        }
                                                        //추출한데이터 list에 넣고 adepter 호출
                                                        for(int i = 0 ;i< list.size();i++){
                                                            if(list.get(i).getId() == boardJson.getString("id")){
                                                                list.get(i).setComments(commentText);
                                                                list.get(i).setComment_length(subJson.getString("comment_length"));
                                                            }
                                                        }
                                                    }
//                                                    댓글 0개일떄
                                                    else {
                                                        for(int i = 0 ;i< list.size();i++){
                                                            if(list.get(i).getId() == boardJson.getString("id")){
                                                                list.get(i).setComment_length("0");
                                                            }
                                                        }

                                                    }
//                                                  좋아요 데이터 처리
                                                    JSONArray likeArray = subJson.getJSONArray("likes");
                                                    if(likeArray.length()>0){
                                                        for (int i =0;i < list.size();i++){
                                                            if(list.get(i).getId() == boardJson.getString("id")){
                                                                list.get(i).setLike_length(likeArray.length()+"");
                                                                for(int j = 0; j < likeArray.length() ; j++) {
                                                                    Log.d("like", list.get(i).getId() + likeArray.getString(j));
                                                                }
                                                                break;
                                                            }
                                                        }
                                                    }

                                                    Log.d("Board", "filalI?"+finalI);
                                                    adapter.notifyItemChanged(finalI);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, null);
                                AppHelper.requestQueue.add(subData);
                                list.add(boardData);
//                              adapter.notifyItemInserted(list.size());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },null);

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }
}
