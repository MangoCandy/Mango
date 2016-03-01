package com.ydwj.bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2016/3/1.
 */
public class Goods {
    String price;
    String name;
    Bitmap bitmap;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
