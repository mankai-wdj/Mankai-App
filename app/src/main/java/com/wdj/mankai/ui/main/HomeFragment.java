package com.wdj.mankai.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    private View view;
    private String URL = "https://api.mankai.shop/api";
    private ArrayList<BoardData> list = new ArrayList<BoardData>();
    private BoardData boardData;
    private BoardAdapter adapter;
    private ListView CommentList;
    private int CurrentPage = 1;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }
// 주기 우선
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getContext());

        this.GETBOARD("/board/show/전체/?page="+CurrentPage);
        CurrentPage+=1;


    }

    public void GETBOARD(String subPoint) {
//        1차 기본 보드 데이터
        StringRequest request = new StringRequest(Request.Method.POST, URL+subPoint,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray BoardJsonArray = json.getJSONArray("data");

//                          보드데이터 별로 하나씩 요청
                            for (int i = 0; i < BoardJsonArray.length(); i++) {
                                JSONObject boardJson = BoardJsonArray.getJSONObject(i);
                                boardData = new BoardData(boardJson);
//                               2차 데이터 처리
                                int finalI = i;
                                StringRequest subData = new StringRequest(Request.Method.GET,
                                        URL + "/upload_image/" + boardJson.getString("id"),
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {

                                                    JSONObject subJson = new JSONObject(response);

//                                                  사진 데이터 정리
                                                    JSONArray subArray = subJson.getJSONArray("images");
                                                    if(subArray.length()==0){
                                                        for(int i = list.size()-5 ;i<list.size();i++)
                                                        {
                                                            if(list.get(i).getId() == boardJson.getString("id")){
                                                                list.get(i).setViewType(0);
                                                                Log.d("Board", "사진 없음 = " + i);
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    else{
                                                        String  saveJson = subArray.getJSONObject(0).getString("url");
                                                        for(int i = list.size()-5 ;i<list.size();i++)
                                                        {
                                                            if(list.get(i).getId() == boardJson.getString("id")){
                                                                list.get(i).setBoardImage(saveJson);
                                                                list.get(i).setViewType(1);
                                                                Log.d("Board", "사진 있음 = " + i);
                                                                break;
                                                            }
                                                        }
                                                    }

//                                                   댓글 데이터 정리
                                                    ArrayList<String> commentText = new ArrayList<String>();
                                                    JSONArray CommentArray = subJson.getJSONArray("comments");
//                                                  댓글 배열 추출
                                                    if(CommentArray.length()>0){
                                                        for(int i = 0 ;i<CommentArray.length();i++){
                                                            JSONObject comment = CommentArray.getJSONObject(i);
                                                            commentText.add(comment.getString("user_name")+" : "+comment.getString("comment"));
                                                        }
    //                                                  추출한데이터 list에 넣고 adepter 호출
                                                        for(int i = list.size()-5 ;i< list.size();i++){
                                                            if(list.get(i).getId() == boardJson.getString("id")){
                                                                list.get(i).setComments(commentText);

                                                            }
                                                        }
                                                    }
                                                    adapter.notifyItemChanged(finalI);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, null);
                                AppHelper.requestQueue.add(subData);
                                list.add(boardData);
//                                adapter.notifyItemInserted(list.size());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },null);

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }



// UI처리
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_board, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL, false));
        adapter = new BoardAdapter(list);
        recyclerView.setAdapter(adapter);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == list.size() -1 ) {
                    GETBOARD("/board/show/전체/?page="+CurrentPage);
                    CurrentPage+=1;
                }

            }
        });
        return view;
    }

}