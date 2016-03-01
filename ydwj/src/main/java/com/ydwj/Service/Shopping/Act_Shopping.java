package com.ydwj.Service.Shopping;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ydwj.bean.Market;
import com.ydwj.community.R;

import java.util.ArrayList;
import java.util.List;

public class Act_Shopping extends AppCompatActivity {
    ListView listView;
    Adapter_shoping_list adapter;
    List<Market> markets=new ArrayList<>();
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__shopping);
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
        setDate();
        listView=(ListView)findViewById(R.id.listview);
        adapter=new Adapter_shoping_list(markets);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context,Act_ShopSp.class);
                intent.putExtra("name",markets.get(position).getName());
                startActivity(intent);
            }
        });
    }

    public void setDate(){
        String[] chaoname={"家得利生活超市","步步高超市","芙蓉兴盛","兴港超市"};
        for(int i=0;i<chaoname.length;i++){
            Market market=new Market();
            market.setName(chaoname[i]);
            market.setYytime("营业时间：8点~23点");
            markets.add(market);
        }
    }
}
