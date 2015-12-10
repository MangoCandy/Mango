package com.ydwj.bean;

import android.app.Activity;
import android.app.Application;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MangoCandy on 2015/10/6.
 */
public class MyApplication extends Application {
    public static List<Activity> activities=new ArrayList<Activity>();
    public static DbUtils dbUtils;
    DbUtils.DaoConfig config;

    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void exitApp(){
        for(Activity activity:activities){
            activity.finish();
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        initDb();
    }

    public void initDb(){
        config=new DbUtils.DaoConfig(this);
        config.setDbName("ydwj");
        config.setDbVersion(1);
        dbUtils=DbUtils.create(config);
        try {
            dbUtils.createTableIfNotExist(TitleInfo.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        dbUtils.close();
        exitApp();
    }
}
