package com.ydwj.Service.Shopping;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ydwj.Ziliao;
import com.ydwj.bean.ShopCarList;
import com.ydwj.community.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Act_Shopcar extends AppCompatActivity {
    RecyclerView listview;
    Adapter_shopcar_list adapter;
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopcar);
        initToolbar();
        initView();
    }

    public void initToolbar(){
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.iconfont_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void initView(){
        listview=(RecyclerView)findViewById(R.id.listview);
        listview.setLayoutManager(new LinearLayoutManager(this));

        adapter=new Adapter_shopcar_list(Ziliao.shopCarLists);
        listview.setAdapter(adapter);
    }

    public void jiesuan(View view) {
        Map<Integer,Boolean> bools=adapter.getBools();
        for(int i=0;i<Ziliao.shopCarLists.size();i++){
            if(bools.get(i)!=null){
                if(bools.get(i)){
                    Ziliao.shopCarLists_READY.add(Ziliao.shopCarLists.get(i));
                }
            }
        }
        if(Ziliao.shopCarLists_READY.size()<1){
            Toast.makeText(context,"未选择商品",Toast.LENGTH_SHORT).show();
            return;
        }
        int price=0;
        for(ShopCarList shopCarList:Ziliao.shopCarLists_READY){
            int p=Integer.parseInt(shopCarList.getPrice());
            p=Integer.parseInt(shopCarList.getNum())*p;
            price+=p;
        }

        new AlertDialog.Builder(this).setTitle("确认付款")
                .setMessage("总计"+price+"元")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,"付款成功",Toast.LENGTH_SHORT).show();
                        for(ShopCarList shopCarList:Ziliao.shopCarLists_READY){
                            Ziliao.shopCarLists_DONE.add(shopCarList);
                            Ziliao.shopCarLists.remove(shopCarList);
                        }
                        Ziliao.shopCarLists_READY.clear();
                        initView();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Ziliao.shopCarLists_READY.clear();
            }
        }).show();
    }
}
