package com.ydwj.Service.BaoXiu;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ydwj.MUtils.MUtils;
import com.ydwj.Ziliao;
import com.ydwj.bean.Baoxiu;
import com.ydwj.community.R;

import java.util.List;

public class Act_Baoxiu_xx extends AppCompatActivity {
    TextView dizhi;
    TextView xiaoqu;
    TextView time;
    TextView text;
    List<Bitmap> bitmaps;
    Baoxiu baoxiu;
    String date;
    LinearLayout group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baoxiu_xx);
        initToolbar();
        Bundle bundle=getIntent().getExtras();
        date=bundle.getString("time");
        init();
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

    public void init(){
        group=(LinearLayout)findViewById(R.id.group);
        dizhi=(TextView)findViewById(R.id.dizhi);
        xiaoqu=(TextView)findViewById(R.id.xiaoqu);
        time=(TextView)findViewById(R.id.time);
        text=(TextView)findViewById(R.id.text);

        for(Baoxiu baoxiu:Ziliao.baoxius){
            if(date.equals(baoxiu.getDate())){
                this.baoxiu=baoxiu;
            }
        }
        dizhi.setText("详细地址："+baoxiu.getDizhi());
        xiaoqu.setText("小区名："+baoxiu.getXiaoqu());
        time.setText("报修日期："+baoxiu.getDate());
        text.setText("详情描述："+baoxiu.getText());

        int width=new MUtils(this).getWidth();
        int height=width*2/3;
        for(Bitmap bitmap:baoxiu.getBitmaps()){
            ImageView imageView=new ImageView(this);
            imageView.setImageBitmap(bitmap);
            LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(width,height);
            params.setMargins(5,10,5,0);
            imageView.setLayoutParams(params);
            group.addView(imageView);
        }

    }
}
