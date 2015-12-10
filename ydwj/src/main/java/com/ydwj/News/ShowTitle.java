package com.ydwj.News;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.method.Touch;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.ydwj.bean.TitleInfo;

import com.ydwj.community.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MangoCandy on 2015/9/20.
 */
public class ShowTitle extends Fragment {
    Context context;

    boolean isfreashing=false;//是否正处于刷新状态
    private static final String TYPE_ID = "param1";
    int currentlist=0;
    View view;
    private int typeID;//请求类型
    int currentPage=1;//当前页
    String URL;
    Utils utils=new Utils(context);
    ListView listView;
    List<TitleInfo> titleInfos;//标题集合
    TitleInfo titleInfo;
    TitleAdapter adapter;
    RequestQueue requestQueue=null;
    ShowNews showNews;
    SwipeRefreshLayout swipeRefreshLayout=null;
    ImageView loading;
    Intent intent;
    DbUtils dbUtils;
    FloatingActionButton gototop;

    String what=null;//alert提示信息内容
    boolean freshed=false;//是否刷新


    final int ASK_REFRESH=1;
    final int PULL_ADD=2;
    int ASK_MODE=-1;

    public static ShowTitle newInstance(int typeID,Context c) {
        ShowTitle fragment = new ShowTitle();
        Bundle args = new Bundle();
        args.putInt(TYPE_ID, typeID);
        fragment.setArguments(args);
        return fragment;
    }
    public ShowTitle(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        initDb();
    }
    boolean isHiden=true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isHiden=false;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        isHiden=hidden;
        super.onHiddenChanged(hidden);
    }

    //加载按钮旋转
//    public void startloading(){
//        loading.setVisibility(view.VISIBLE);
//        loading.startAnimation(AnimationUtils.loadAnimation(context, R.anim.loading));
//    }
//    public void stoploading(){
//        loading.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alpha_lose));
//        loading.clearAnimation();
//        loading.setVisibility(view.GONE);
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fmt_somenews, container, false);
        init();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    DbUtils.DaoConfig config;
    public void initDb(){
        config=new DbUtils.DaoConfig(context);
        config.setDbName("ydwj");
        config.setDbVersion(1);
        dbUtils=DbUtils.create(config);
        try {
            dbUtils.createTableIfNotExist(TitleInfo.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    boolean topIsShow=false;//返回顶部按钮是否已经显示
    public void init(){
        this.typeID=getArguments().getInt(TYPE_ID);
        listView= (ListView) view.findViewById(R.id.title_List);
        loading=(ImageView)view.findViewById(R.id.loading);
        listView.setOnItemClickListener(itemClickListener);
        initSwipeRefresh();
//        滚动监听刷新
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //满足一定条件显示滚回顶部按钮
                if(topIsShow==false&&view.getFirstVisiblePosition()>15){
                    topIsShow=true;
                    gototop.startAnimation(AnimationUtils.loadAnimation(context,R.anim.gotop));
                    gototop.show();
                }else if(topIsShow==true&&view.getFirstVisiblePosition()<15){
                    topIsShow=false;
                    gototop.hide();
                }

                if (scrollState == SCROLL_STATE_IDLE) {
                    currentlist = view.getFirstVisiblePosition();
                }
                if(scrollState==SCROLL_STATE_IDLE){
                    if(view.getLastVisiblePosition()==view.getCount()-1){
                        isBottom=true;
                        setLoad();
                    }else{
                        isBottom=false;
                        listView.setOnTouchListener(null);
                    }
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        initTitle();
        initGototop();
    }
    //加载更多
    float y2;
    float y;
    boolean isBottom=false;
    public void setLoad(){
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isBottom && !isfreashing) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            y = event.getY();
                            Log.i("asd", y + "");
                            break;
                        case MotionEvent.ACTION_MOVE:
                            y2 = event.getY();
                            Log.i("asd", y2 + "");
                            break;
                    }
                    if (y - y2 > 200) {
                        isfreashing = true;
                        ASK_MODE = PULL_ADD;
                        askForJson(typeID);
                    }
                }
                return false;
            }
        });
    }

    //    返回顶部
    public void initGototop(){
        gototop=(FloatingActionButton) view.findViewById(R.id.gototop);
        gototop.hide();
        gototop.setOnClickListener(onClickListener);
    }

    //点击监听
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.gototop:
                    listView.smoothScrollToPosition(0);
                    break;
            }
        }
    };

    //从缓存读取
    public void initTitle(){
        if(typeID>=0){
            if (true) {
                try {
                    //读取缓存
                    titleInfos=dbUtils.findAll(Selector.from(TitleInfo.class).where(TitleInfo.TITLE_TYPEID,"=",typeID));

                    if(titleInfos!=null&&titleInfos.size()>0){

                    }else{
                        askForJson(typeID);
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
                adapter=new TitleAdapter(titleInfos,context);
                listView.setAdapter(adapter);
                handler.sendEmptyMessage(0);
            }
        }
        listView.setSelection(currentlist);
    }
    public void initSwipeRefresh(){
        swipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.Swipe_fresh);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener=new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if(isfreashing==false){
                isfreashing=true;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ASK_MODE=ASK_REFRESH;
                        currentPage = 1;
                        askForJson(typeID);
                    }
                });
            }
        }

    };


    AdapterView.OnItemClickListener itemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView textView= (TextView) view.findViewById(R.id.show_url);
            TextView ti= (TextView) view.findViewById(R.id.show_title);
            intent = new Intent(context, ShowNews.class);
            intent.putExtra("url", textView.getText().toString());
            intent.putExtra("id",titleInfos.get(position).getId());
            intent.putExtra("title",ti.getText().toString());
            startActivity(intent);
        }
    };

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    isfreashing=false;
                    break;
                case 1:
                    titleInfo = (TitleInfo)msg.obj;
                    titleInfos.add(titleInfo);
                    break;
                case 2:
                    for(TitleInfo titleInfo:titleInfos){
                        try {
                            dbUtils.save(titleInfo);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    public void alert(){
        builder=new AlertDialog.Builder(context);
        builder.setMessage(what);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("重试", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                swipeRefreshLayout.setRefreshing(true);
                askForJson(typeID);
            }
        });
        alertDialog=builder.show();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(!freshed){
            ASK_MODE=ASK_REFRESH;
            askForJson(typeID);
        }
    }

    //请求数据
    public void askForJson(final int typeID){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                showRefresh(true);
                requestQueue= Volley.newRequestQueue(context);
                StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://app.cloud-hn.net/app/factory.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            showRefresh(false);
                            Log.i("asd",response);
                            isfreashing=false;
                            RefreshList(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"网络请求失败，请重试",Toast.LENGTH_SHORT).show();
                        isfreashing=false;
                        showRefresh(false);
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<String,String>();
                        params.put("action","newsList");
                        params.put("newsType",typeID+"");
                        params.put("pageSize","20");
                        params.put("page",currentPage+"");
                        Log.i("asd",currentPage+"");
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
                requestQueue.start();
            }
        };
        runnable.run();
    }
    //删除缓存
    public void deleteCache(){
        titleInfos.clear();
        try {
            dbUtils.delete(TitleInfo.class, WhereBuilder.b("typeid", "=", typeID));
        } catch (DbException e) {
            e.printStackTrace();
        }
        listView.smoothScrollToPosition(0);
    }
    //    数据添加到List
    public void RefreshList(JSONObject json){
        try {
            if(json.getString("retCode").equals("00")){
                currentPage++;
                if(ASK_MODE==ASK_REFRESH){
                    deleteCache();
                }
                JSONArray jsonArray=json.getJSONArray("retData");
                JSONObject jsonObject;
                for(int i=0;i<jsonArray.length();i++){
                    try {
                        jsonObject=jsonArray.getJSONObject(i);
                        Log.i("asd",jsonObject+"");
                        titleInfo=new TitleInfo();
                        titleInfo.setTitle(jsonObject.getString(TitleInfo.TITLE_TITLENAME));
                        titleInfo.setContentimg((jsonObject.getString("uploadFile")).replaceAll("\\\\", ""));
                        titleInfo.setUsername(jsonObject.getString("createDate"));
                        titleInfo.setTypeid(typeID);
                        titleInfo.setId(Integer.parseInt(jsonObject.getString("id")));
                        titleInfo.setUrl("http://app.cloud-hn.net/app/showNews.php?id="+jsonObject.getString("id"));

                        titleInfos.add(titleInfo);
                        dbUtils.save(titleInfo);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
                handler.sendEmptyMessage(0);
            }else if(json.getString("retCode").equals("01")){
                if(!isHiden){

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showRefresh(final boolean boo){
        int time;
        time=boo?0:500;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(boo);
            }
        },time);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        view=null;
        titleInfos=null;
        listView=null;
        showNews=null;

        System.gc();
    }
}
