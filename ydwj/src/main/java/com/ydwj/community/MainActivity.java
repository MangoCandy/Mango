package com.ydwj.community;

import android.content.Context;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ydwj.News.MangoWe;
import com.ydwj.News.Utils;
import com.ydwj.Setting.Setting;
import com.ydwj.alarm.Frg_alarm;
import com.lidroid.xutils.DbUtils;

import com.ydwj.bean.MyApplication;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {
    DbUtils dbUtils;
    Context context;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    //主要Fragment
    Fragment frg_mainnews;
    Fragment frg_setting;
    Fragment currentFrg;
    Fragment frg_alarm;
    List<ImageView> bottoms;
    //当前页按钮ID
    int currentTabId;
    //三大按钮图片
    ImageView personal;
    ImageView news_we;
    ImageView btn_alarm;
    //下面三大按钮
    LinearLayout btn_home_alrm;
    LinearLayout btn_home_personal;
    LinearLayout btn_home_news;
    List<Fragment> fragments;

    Utils utils=new Utils(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments=new ArrayList<Fragment>();
        context=this;
        dbUtils=((MyApplication)getApplication()).dbUtils;
        setContentView(R.layout.activity_main);
        MyApplication.addActivity(this);
        initBottombar();
        initFragment(savedInstanceState);
        //检查更新
        utils.askForUpdate();
    }

    //初始化Fragment
    public void initFragment(Bundle bundle){
        fragmentManager=getSupportFragmentManager();
        if(bundle==null){
            frg_alarm=new Frg_alarm();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment, frg_alarm,"alarm");
            fragmentTransaction.commit();
            Selected(R.id.alarm);
            currentFrg=frg_alarm;
        }else{
            //如果Bundle有就提取对象
            currentTabId=bundle.getInt("currentTabId");
            currentFrg=fragmentManager.getFragment(bundle, "currentfrg");
            frg_alarm=fragmentManager.getFragment(bundle, "alarm");
            frg_mainnews=fragmentManager.getFragment(bundle,"news");
            frg_setting=fragmentManager.getFragment(bundle,"setting");
            fragments=fragmentManager.getFragments();
            Selected(currentTabId);
            showFrg(currentFrg);
        }
    }
    //将Fragment添加进集合方便管理
    public void addfragList(Fragment fragment){
        if(fragment!=null){
            fragments.add(fragment);
        }
    }
    //显示传入的Fragment隐藏其他的
    public void showFrg(Fragment fragment){
        fragmentTransaction=fragmentManager.beginTransaction();
        for(Fragment frg:fragments){
            if(fragment.equals(frg)){
                fragmentTransaction.show(frg);
            }else{
                fragmentTransaction.hide(frg);
            }
        }
        fragmentTransaction.commit();
    }
    //初始化分栏按钮
    public void initBottombar(){
        btn_home_alrm=(LinearLayout)findViewById(R.id.btn_alarm);
        btn_home_alrm.setOnClickListener(onClickListener);

        btn_home_news=(LinearLayout)findViewById(R.id.btn_news_we);
        btn_home_news.setOnClickListener(onClickListener);

        btn_home_personal=(LinearLayout)findViewById(R.id.btn_personal);
        btn_home_personal.setOnClickListener(onClickListener);

        personal=(ImageView)findViewById(R.id.personl);
        personal.setOnClickListener(onClickListener);

        news_we=(ImageView)findViewById(R.id.news_we);
        news_we.setOnClickListener(onClickListener);

        btn_alarm=(ImageView)findViewById(R.id.alarm);
        btn_alarm.setOnClickListener(onClickListener);

        bottoms=new ArrayList<ImageView>();
        bottoms.add(btn_alarm);
        bottoms.add(personal);
        bottoms.add(news_we);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_alarm:case R.id.alarm:
                    showFragment(R.id.alarm, frg_alarm);
                    break;
                case R.id.btn_personal:case R.id.personl:
                   showFragment(R.id.personl,frg_setting);
                    break;
                case R.id.btn_news_we:case R.id.news_we:
                    showFragment(R.id.news_we,frg_mainnews);
                    break;
            }
        }
    };
//    显示fragment
    public void showFragment(int ID,Fragment fragment){
        fragmentTransaction=fragmentManager.beginTransaction();
        String tag="";
        if(fragment==null){
            switch (ID){
                case R.id.alarm:
                    fragment=new Frg_alarm();
                    frg_alarm=fragment;
                    tag="alarm";
                    break;
                case R.id.news_we:
                    fragment=new MangoWe();
                    frg_mainnews=fragment;
                    tag="news";
                    break;
                case R.id.personl:
                    fragment=new Setting();
                    frg_setting=fragment;
                    tag="setting";
                    break;
            }
            addfragList(fragment);
            fragmentTransaction.hide(currentFrg).add(R.id.fragment, fragment,tag).commit();
        }
        else if(!fragment.isAdded()){
            fragmentTransaction.hide(currentFrg).add(R.id.fragment, fragment).commit();
        }else{
//            showFrg(fragment);
            fragmentTransaction.hide(currentFrg).show(fragment).commit();
        }
        Selected(ID);

        currentFrg=fragment;

    }
    //输入ID以点亮按钮
    public void Selected(int ID){
        for(ImageView imageView:bottoms){
            if(imageView.getId()==ID){
                imageView.setSelected(true);
                currentTabId=ID;
            }else{
                imageView.setSelected(false);
            }
        }
    }
    //退出
    public void onBackPressed() {
        Snackbar snackbar= Snackbar.make(findViewById(R.id.weixin), "是否退出应用", Snackbar.LENGTH_SHORT);
        snackbar.setAction("退出", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
        View view=snackbar.getView();
        TextView textView=(TextView)view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        snackbar.show();
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        fragmentManager.putFragment(outState,"currentfrg",currentFrg);
        if(frg_setting!=null&&frg_setting.isAdded()){
            fragmentManager.putFragment(outState,"setting",frg_setting);
        }else{frg_setting=null;}
        if(frg_mainnews!=null&&frg_mainnews.isAdded()){
            fragmentManager.putFragment(outState,"news",frg_mainnews);
        }else{frg_mainnews=null;}
        if(frg_alarm!=null&&frg_alarm.isAdded()){
            fragmentManager.putFragment(outState,"alarm",frg_alarm);
        }else{frg_alarm=null;}

        outState.putInt("currentTabId",currentTabId);
        super.onSaveInstanceState(outState);
    }

    public void exit(){
        MyApplication.exitApp();
    }
}
