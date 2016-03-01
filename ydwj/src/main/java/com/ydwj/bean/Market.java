package com.ydwj.bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/3/1.
 */
public class Market {
    String name;
    int star;
    String yytime;
    String jieshao;
    Bitmap bitmap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getYytime() {
        return yytime;
    }

    public void setYytime(String yytime) {
        this.yytime = yytime;
    }

    public String getJieshao() {
        return jieshao;
    }

    public void setJieshao(String jieshao) {
        this.jieshao = jieshao;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
