package com.ydwj.MUtils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2016/2/25.
 */
public class MUtils {
    Context context;
    public MUtils(Context context){
        this.context=context;
    }
    public int getHeight(){
        DisplayMetrics displayMetrics=context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }
}
