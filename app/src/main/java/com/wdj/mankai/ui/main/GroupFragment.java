package com.wdj.mankai.ui.main;

import static org.webrtc.ContextUtils.getApplicationContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wdj.mankai.R;
import com.wdj.mankai.data.GroupData;
import com.wdj.mankai.data.model.AppHelper;
import com.wdj.mankai.ui.Group.GroupAdapter;
import com.wdj.mankai.ui.Group.GroupMake2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroupFragment extends Fragment {

    private View view;

    TextView groupmake;
    ImageView groupsearch_btn;
    EditText GroupSearch;

    //리사이클1 : 로그인 한 ID의 가입 그룹 리스트
    //리사이클2 : 전체 그룹 리스트
    private RecyclerView recyclerView;
    private String LoginUserId, GroupName;
    private GridLayoutManager gridLayoutManager;
    private TextView statusText;
    private GroupAdapter adapter;
    private ArrayList<GroupData> list = new ArrayList<>();
    private GroupData groupData;
    private String URL = "https://api.mankai.shop/api";

    String userId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_group, container, false);

        GroupSearch = (EditText)view.findViewById(R.id.GroupSearch);
        statusText= view.findViewById(R.id.StatusText);


        // 그룹 생성 페이지 이동
        groupmake = (TextView) view.findViewById(R.id.groupmake);
        groupmake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GroupMake2.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.group_recycler_view);
        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(this.getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.scrollToPosition(0);
        adapter = new GroupAdapter(getContext(),list);
        recyclerView.setAdapter(adapter);

        if (AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getContext());

        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("userId", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId","");

        request1();
        GroupSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN)
                {
                    statusText.setText("검색 결과");
                    String Test = GroupSearch.getText().toString();
                    Log.d("Test", "onEditorAction: " + Test);
                    request2(Test);
                }
                return true;
            }
        });

        GroupSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(GroupSearch.getText().length()==0)
                {

                    statusText.setText("가입 그룹 리스트");
                    request1();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        return view;
    }


    public void request1() {
        list.clear();
        adapter.notifyDataSetChanged();
        StringRequest request = new StringRequest(Request.Method.GET,
                URL + "/show/mygroup/"+MainActivity.userId,
                //url 끌고와서 다시 배열로 제작후 각각의 함수에 넣어 줌
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Group", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject groupJSON = jsonArray.getJSONObject(i);
                                groupData = new GroupData(groupJSON);
                                list.add(groupData);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },null);
        AppHelper.requestQueue.add(request);
    }

    public void request2(String test) {
        list.clear();
        adapter.notifyDataSetChanged();
        GroupName = GroupSearch.getText().toString();
        StringRequest request = new StringRequest(Request.Method.GET,
                URL + "/show/group/"+GroupName,
                //url 끌고와서 다시 배열로 제작후 각각의 함수에 넣어 줌
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Group", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject groupJSON = jsonArray.getJSONObject(i);
                                groupData = new GroupData(groupJSON);
                                list.add(groupData);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },null);
        AppHelper.requestQueue.add(request);
    }
}

