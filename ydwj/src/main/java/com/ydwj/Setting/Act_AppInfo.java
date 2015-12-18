package com.ydwj.Setting;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ydwj.News.Utils;
import com.ydwj.community.R;

public class Act_AppInfo extends AppCompatActivity {
    TextView versionText;
    LinearLayout checkupdate;
    Utils utils=new Utils(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act__app_info);
        initView();
        inittoolbar();
    }

    public void inittoolbar(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
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
        PackageManager manager=getPackageManager();
        versionText=(TextView)findViewById(R.id.version);
        try {
            versionText.setText(manager.getApplicationLabel(getApplicationInfo())+": "+manager.getPackageInfo(getPackageName(),0).versionName+"");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void checkupdate(View view){
        utils.askForUpdate(false);
    }
}
