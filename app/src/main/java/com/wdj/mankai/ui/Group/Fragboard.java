package com.wdj.mankai.ui.Group;

import static org.webrtc.ContextUtils.getApplicationContext;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdj.mankai.R;
import com.wdj.mankai.data.BoardData;
import com.wdj.mankai.data.GroupData;
import com.wdj.mankai.data.model.AppHelper;
import com.wdj.mankai.ui.Board.BoardAdapter;
import com.wdj.mankai.ui.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Fragboard extends Fragment {
    private View view;
    private String category_id;
    private BoardData boardData;
    private RecyclerView recyclerView;
    private ArrayList<BoardData> list = new ArrayList<BoardData>();
    public static BoardAdapter adapter;
    private int currentPage = 1;
    private StringRequest request;

    public static Fragboard newInstance() {
        Fragboard fragboard = new Fragboard();
        return  fragboard;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_frg_board, container, false);
        recyclerView = view.findViewById(R.id.BoardRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));
        adapter = new BoardAdapter(list);
        recyclerView.setAdapter(adapter);

        GetBoard();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == list.size()-1) {

                    GetBoard();
                }
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category_id = getArguments().getString("category_id");

    }

    public void GetBoard(){
        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());

        request = new StringRequest(Request.Method.POST,
                "https://api.mankai.shop/api/show/groupboard/"+Groupinfor.group_id+"?page="+currentPage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        currentPage+=1;
                        Log.d("Group", "onResponse: " + currentPage);
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray BoardJsonArray = json.getJSONArray("data");

                            if(BoardJsonArray.length() ==0){
                                Log.d("Board", "빈값임 ㅋㅋ");
                                JSONObject ch = new JSONObject();
                                ch.put("viewType",2);
                            }
//                          보드데이터 별로 하나씩 요청
                            for (int i = 0; i < BoardJsonArray.length(); i++) {
                                JSONObject boardJson = BoardJsonArray.getJSONObject(i);
                                boardData = new BoardData(boardJson);
//                               2차 데이터 처리
                                int finalI = i;

                                StringRequest subData = new StringRequest(Request.Method.GET,
                                        "https://api.mankai.shop/api/show/groupdata/" + boardJson.getString("id"),
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                ArrayList<String> cntString = new ArrayList<String>();
                                                try {
                                                    Log.d("Board",response );
                                                    JSONObject subJson = new JSONObject(response);

//                                                  사진 데이터 정리
                                                    JSONArray subArray = subJson.getJSONArray("images");
                                                    if(subArray.length()==0){
                                                        for(int i = list.size()-5 ;i<list.size();i++)
                                                        {
                                                            if(list.get(i).getId() == boardJson.getString("id")){
                                                                list.get(i).setViewType(0);
                                                                list.get(i).setIsGroup("Group");
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    else{
                                                        for(int i = 0 ; i< subArray.length();i++){
                                                            cntString.add(subArray.getJSONObject(i).getString("url"));
                                                            Log.d("Image 갯수", boardJson.getString("id")+" = "+cntString.get(i));
                                                        }
                                                        for(int i = list.size()-5 ;i<list.size();i++)
                                                        {
                                                            if(list.get(i).getId() == boardJson.getString("id")){
                                                                list.get(i).setBoardImage(cntString);
                                                                list.get(i).setViewType(1);
                                                                list.get(i).setIsGroup("Group");
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
                                                        for(int i = list.size()-5 ;i< list.size();i++){
                                                            if(list.get(i).getId() == boardJson.getString("id")){
                                                                list.get(i).setComments(commentText);
                                                                list.get(i).setComment_length(subJson.getString("comment_length"));
                                                            }
                                                        }
                                                    }
//                                                    댓글 0개일떄
                                                    else {
                                                        for(int i = list.size()-5 ;i< list.size();i++){
                                                            if(list.get(i).getId() == boardJson.getString("id")){
                                                                list.get(i).setComment_length("0");
                                                            }
                                                        }

                                                    }
//                                                  좋아요 데이터 처리
                                                    JSONArray likeArray = subJson.getJSONArray("likes");
                                                    if(likeArray.length()>0){
                                                        for (int i = list.size()-5 ;i < list.size();i++){
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
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },null){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("category",category_id);
                return params;
            }
        };
        AppHelper.requestQueue.add(request);
    }
}