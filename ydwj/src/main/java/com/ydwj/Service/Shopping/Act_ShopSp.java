package com.ydwj.Service.Shopping;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.ydwj.community.R;

public class Act_ShopSp extends AppCompatActivity {
    String name;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_xq);

        Bundle bundle=getIntent().getExtras();
        name=bundle.getString("name");
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
        ImageView imageView=(ImageView)findViewById(R.id.image);
        imageView.requestFocus();

        recyclerView=(RecyclerView)findViewById(R.id.goods);
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(new Adapter_SP());
    }

    public void call(View view) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+1511122554));
        startActivity(intent);
    }
}
