package com.ydwj.Service.BaoXiu;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ydwj.community.R;

public class Act_BaoXiu extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_xiu);
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
        tabLayout=(TabLayout)findViewById(R.id.tabs);
        viewPager=(ViewPager)findViewById(R.id.viewpger);
        Adapter_Baoxiu adapter_baoxiu=new Adapter_Baoxiu(this,getSupportFragmentManager());
        viewPager.setAdapter(adapter_baoxiu);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void addbaoxiu(View view) {
        Intent intent=new Intent(this,Act_AddBaoxiu.class);
        startActivity(intent);
    }
}
