package com.ydwj;

import com.ydwj.bean.Baoxiu;
import com.ydwj.bean.Baoxiu_List;
import com.ydwj.bean.Market;
import com.ydwj.bean.ShopCarList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mangocandy on 16-2-29.
 */
public class Ziliao {
    public static List<Baoxiu> baoxius=new ArrayList<>();
    public static List<Baoxiu_List> baoxiu_lists=new ArrayList<>();
    public Ziliao(){
    }

    public static List<Market> markets=new ArrayList<>();

    public static List<ShopCarList> shopCarLists_READY=new ArrayList<>();
    public static List<ShopCarList> shopCarLists_DONE=new ArrayList<>();
    public static List<ShopCarList> shopCarLists=new ArrayList<>();

}
