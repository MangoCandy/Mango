package com.ydwj.bean;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/29.
 */
public class Baoxiu {
    String title;
    String time;
    String text;
    int dianzan;
    int liuyan;
    List<Bitmap> bitmaps=new ArrayList<>();

    public List<Bitmap> getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDianzan() {
        return dianzan;
    }

    public void setDianzan(int dianzan) {
        this.dianzan = dianzan;
    }

    public int getLiuyan() {
        return liuyan;
    }

    public void setLiuyan(int liuyan) {
        this.liuyan = liuyan;
    }
}
