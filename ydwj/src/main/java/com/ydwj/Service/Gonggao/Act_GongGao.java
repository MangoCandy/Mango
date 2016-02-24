package com.ydwj.Service.Gonggao;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ydwj.News.ShowNews;
import com.ydwj.community.R;

public class Act_GongGao extends AppCompatActivity {
    ListView listView;//标题展示
    Adapter_GongGao adapter;
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gong_gao);
        initToolbar();
        initView();
    }

    public void initToolbar(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.iconfont_back);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void initView(){
        listView=(ListView)findViewById(R.id.listview);
        adapter=new Adapter_GongGao();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context, ShowNews.class);
                intent.putExtra("url","http://m.cssqt.com/80292.shtml");
                startActivity(intent);
            }
        });
    }
}
