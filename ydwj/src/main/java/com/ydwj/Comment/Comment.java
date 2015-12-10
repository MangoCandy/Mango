package com.ydwj.Comment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ydwj.bean.CommentBean;
import com.ydwj.News.Utils;
import com.ydwj.community.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class Comment extends Activity {
    EditText input;
    ImageView btn_post;
    Context context;
    Utils utils;
    int id;
    List<CommentBean> commentBeanList;
    CommentBean commentBean;
    int currentPage=1;
    ListView showCom;
    ComAdapter adapter;
    //初始化手势
    GestureDetector gestureDetector;
    boolean isLoad=false;
    public void initGes(){
        gestureDetector=new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(Math.abs(velocityX)<30){
                    return true;
                }
                if(Math.abs(e1.getRawY()-e2.getRawY())<50){
                    if(Math.abs(e1.getRawX()-e2.getRawX())>100){
                        finish();
                        return true;
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }
    View.OnTouchListener onTouchListener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commentBeanList=new ArrayList<CommentBean>();
        id=getIntent().getExtras().getInt("id");
        context=this;
        utils=new Utils(context);
        setContentView(R.layout.activity_comment);
        inittools();
        initview();
        getComments(true);
    }
    public void initview(){
        initGes();
        showCom=(ListView)findViewById(R.id.com_container);
        showCom.setOnTouchListener(onTouchListener);
        adapter=new ComAdapter(commentBeanList,context);
        showCom.setAdapter(adapter);
        showCom.addFooterView(getLayoutInflater().inflate(R.layout.foot_layout,null));
        showCom.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(view.getLastVisiblePosition()>=view.getCount()-1){
                    if(!isLoad){
                        isLoad=true;
                        getComments(false);
                    }
                }
            }
        });
    }
    public void inittools(){
        input=(EditText)findViewById(R.id.input_Comment);
        input.addTextChangedListener(textWatcher);
        btn_post=(ImageView)findViewById(R.id.btn_postComment);
        btn_post.setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_postComment:
                    if(input.getText().toString().equals("")){
                        Toast.makeText(context,"未填写",Toast.LENGTH_SHORT).show();
                    }else{
                        if(textnums<=80){
                            post();
                        }else{
                            Toast.makeText(context,"字数不能超过80",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
            }
        }
    };
    int textnums;
    TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            textnums=s.length();
            if(textnums>80){
                input.setTextColor(Color.RED);
            }else{
                input.setTextColor(Color.BLACK);
            }
        }
    };
    String item;

//    提交
public void post(){
    RequestQueue requestQueue= Volley.newRequestQueue(this);
    StringRequest jsonObjectRequest=new StringRequest(Request.Method.POST,"http://app.cloud-hn.net/app/factory.php", new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Toast.makeText(context,"提交成功",Toast.LENGTH_SHORT).show();
            try {
                JSONObject jsonObject=new JSONObject(response);
                if(jsonObject.getString("retCode").equals("00")){
                    input.setText("");
                    currentPage=1;
                    input.clearFocus();
                    getComments(true);
                }
                Toast.makeText(context,jsonObject.getString("retMessage"),Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("asd", response+"asd");
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(context,"提交失败",Toast.LENGTH_SHORT).show();
        }
    }){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String> params=new HashMap<String,String>();
            params.put("action","add_comment");
            params.put("users_id",utils.getUserinfo().getID());
            params.put("news_id",id+"");
            params.put("comment_contents",input.getText().toString());
            return params;
        }
    };
    requestQueue.add(jsonObjectRequest);
    requestQueue.start();
}
    public void getComments(final boolean isFresh){
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest=new StringRequest(Request.Method.POST, "http://app.cloud-hn.net/app/factory.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("asd",response+"123");
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("retCode").equals("00")){
                        currentPage++;
                        JSONArray jsonArray=jsonObject.getJSONArray("retData");
                        if(isFresh){commentBeanList.clear();}//如果是刷新就清空
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object=jsonArray.getJSONObject(i);
                            CommentBean commentBean=new CommentBean();
                            commentBean.setDate(object.getString("comment_time"));
                            commentBean.setName(object.getString("commentators"));
                            commentBean.setImg(object.getString("avatar_url").replaceAll("\\\\", ""));
                            commentBean.setText(object.getString("comment_contents"));
                            commentBeanList.add(commentBean);
                        }
                        adapter.notifyDataSetChanged();
                        isLoad=false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "获取失败", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("action","comment_list");
                params.put("news_id",id+"");
                params.put("page",currentPage+"");
                params.put("pageSize","5");
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
        requestQueue.start();
    }
    //    数据添加到List
    public void RefreshList(JSONObject json,boolean isfresh){
        try {
            if(isfresh){
                commentBeanList.clear();
            }
            JSONArray jsonArray=json.getJSONObject("showapi_res_body").getJSONObject("pagebean").getJSONArray("contentlist");
            JSONObject jsonObject;
            for(int i=0;i<jsonArray.length();i++){
                try {
                    jsonObject=jsonArray.getJSONObject(i);
                    commentBean=new CommentBean();
                    commentBean.setDate(jsonObject.getString("date"));
                    commentBean.setUrl(jsonObject.getString("url"));
                    commentBean.setText(jsonObject.getString("text"));
                    commentBean.setName(jsonObject.getString("name"));
                    commentBean.setImg(jsonObject.getString("img"));
                    commentBeanList.add(commentBean);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            handler.sendEmptyMessage(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    adapter.notifyDataSetChanged();
                    isLoad=false;
                    break;
            }
        }
    };
}
