package com.ydwj.Service.Shopping;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ydwj.MUtils.MUtils;
import com.ydwj.Ziliao;
import com.ydwj.bean.ShopCarList;
import com.ydwj.community.R;

public class Act_SPXQ extends AppCompatActivity {
    EditText num;
    TextView name;
    TextView price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__spxq);
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
        num=(EditText)findViewById(R.id.num);
        name=(TextView)findViewById(R.id.name);
        price=(TextView)findViewById(R.id.price);
    }

    public void tianjia(View view) {
        ShopCarList shopCarList=new ShopCarList();
        shopCarList.setNum(num.getText().toString());
        shopCarList.setName(name.getText().toString());
        shopCarList.setPrice(price.getText().toString().replaceAll("￥",""));
        Ziliao.shopCarLists.add(shopCarList);
        Toast.makeText(this,"成功添加到购物车",Toast.LENGTH_SHORT).show();
    }

    public void gouwu(View view) {
        ShopCarList shopCarList=new ShopCarList();
        shopCarList.setNum(num.getText().toString());
        shopCarList.setName("");
        shopCarList.setPrice(price.getText().toString().replaceAll("￥",""));
        shopCarList.setName(name.getText().toString());
        Ziliao.shopCarLists.add(shopCarList);

        Intent intent=new Intent(this,Act_Shopcar.class);
        startActivity(intent);
    }
}
