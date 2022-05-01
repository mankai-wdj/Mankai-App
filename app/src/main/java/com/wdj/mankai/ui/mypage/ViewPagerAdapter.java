package com.wdj.mankai.ui.mypage;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wdj.mankai.data.model.AppHelper;

import org.json.JSONObject;



public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    String userId;
    String url = "https://api.mankai.shop/api/";
    ViewPagerData viewPagerData;

    public ViewPagerAdapter(String userId,@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.userId = userId;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                myPageRequest("followers");
                return new FragMyFollowers();
            case 1 :
                myPageRequest("follows");
                return new FragMyFollowings();
            case 2 :
                myPageRequest("myposts");
                return new FragMyPosts();
            case 3 :
                myPageRequest("show/mygroup");
                return new FragMyGroups();
            case 4 :
                myPageRequest("memo");
                return new FragMyMemos();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
       String title = "";
       switch (position){
           case 0 :
               title = "Followers";
               break;
           case 1 :
               title = "Followings";
               break;
           case 2 :
               title = "MyPosts";
               break;
           case 3 :
               title = "MyGroups";
               break;
           case 4 :
               title = "MyMemos";
               break;
       }

        return title;
    }
    /**/

    public void myPageRequest(String type){
        StringRequest myPageStringRequest = new StringRequest(
                Request.Method.GET,
                url+type+"/"+userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (type){
                            case "followers":
                                viewPagerData = new FragMyFollowers();
                                viewPagerData.memoResponse(response);
                                break;
                            case "follows":
                                viewPagerData = new FragMyFollowings();
                                viewPagerData.memoResponse(response);
                                break;
                            case "myposts":
                                viewPagerData = new FragMyPosts();
                                viewPagerData.memoResponse(response);
                                break;
                            case "show/mygroup":
                                viewPagerData = new FragMyGroups();
                                viewPagerData.memoResponse(response);
                                break;
                            case "memo":
                                viewPagerData = new FragMyMemos();
                                viewPagerData.memoResponse(response);
                                break;
                            default:
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error",""+error);
                    }
                });

        myPageStringRequest.setShouldCache(false);
        AppHelper.requestQueue.add(myPageStringRequest);
    }
}
