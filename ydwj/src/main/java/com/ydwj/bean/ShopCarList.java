package com.ydwj.bean;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by mangocandy on 16-3-1.
 */
public class ShopCarList {
    String shopname;
    String price;
    String danwei;
    String num;
    String name;
    Drawable bitmap;

    public Drawable getBitmap() {
        return bitmap;
    }

    public void setBitmap(Drawable bitmap) {
        this.bitmap = bitmap;
    }

    boolean xuanzhong = false;

    public boolean isXuanzhong() {
        return xuanzhong;
    }

    public void setXuanzhong(boolean xuanzhong) {
        this.xuanzhong = xuanzhong;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDanwei() {
        return danwei;
    }

    public void setDanwei(String danwei) {
        this.danwei = danwei;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
