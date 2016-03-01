package com.ydwj.Service.CanYin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ydwj.Service.Shopping.Act_Shopcar;
import com.ydwj.Ziliao;
import com.ydwj.bean.ShopCarList;
import com.ydwj.community.R;

public class Act_WMXQ extends AppCompatActivity {
    EditText num;
    TextView name;
    TextView price;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wmxq);
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
        imageView=(ImageView)findViewById(R.id.img);
    }

    public void tianjia(View view) {
        ShopCarList shopCarList=new ShopCarList();
        shopCarList.setBitmap(imageView.getDrawable());
        shopCarList.setNum(num.getText().toString());
        shopCarList.setName(name.getText().toString());
        shopCarList.setPrice(price.getText().toString().replaceAll("￥",""));
        Ziliao.shopCarLists.add(shopCarList);
        Toast.makeText(this,"成功添加到购物车",Toast.LENGTH_SHORT).show();
    }

    public void gouwu(View view) {
        ShopCarList shopCarList=new ShopCarList();
        shopCarList.setNum(num.getText().toString());
        shopCarList.setBitmap(imageView.getDrawable());
        shopCarList.setName("");
        shopCarList.setPrice(price.getText().toString().replaceAll("￥",""));
        shopCarList.setName(name.getText().toString());
        Ziliao.shopCarLists.add(shopCarList);

        Intent intent=new Intent(this,Act_Shopcar.class);
        startActivity(intent);
    }
}
