package com.ydwj.alarm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ydwj.Login.Login;
import com.ydwj.News.Utils;
import com.ydwj.bean.Contacts;
import com.ydwj.community.R;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.List;


//
public class Act_contacts_msg extends AppCompatActivity {
    FloatingActionButton btn_addcontacts;//添加按钮
    ListView layout_show_contacts;//显示联系人容器
    List<Contacts> contactses;//存放联系人
    Context context;
    Adapter_show_contacts editAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contacts_msg);
        initActionBar();
        initDb();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        get_info_from_db();
    }

    public void initActionBar(){
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.iconfont_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitle("紧急联系人");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.clouddown:
                        cloudDown();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_contacts,menu);
        return true;
    }

    DbUtils dbUtils;
    DbUtils.DaoConfig config;
    public void initDb(){
        config=new DbUtils.DaoConfig(this);
        config.setDbName("ydwj");
        config.setDbVersion(1);
        dbUtils=DbUtils.create(config);
        try {
            dbUtils.createTableIfNotExist(Contacts.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.bo_to_top,R.anim.scale_small);
    }

    public void initView(){
        btn_addcontacts=(FloatingActionButton)findViewById(R.id.btn_add_contacts);
        btn_addcontacts.setOnClickListener(onClickListener);
        layout_show_contacts=(ListView)findViewById(R.id.show_contacts);
        layout_show_contacts.setOnItemClickListener(onItemClickListener);
    }
    AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(context,Act_show_cinfo.class);
            intent.putExtra("number", contactses.get(position).getCONTACT_NUM());
            startActivity(intent);
        }
    };
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_add_contacts:
                    edit();
                    break;
            }
        }
    };
    public void edit(){
        Intent intent=new Intent(this,Act_addcontact.class);
        startActivity(intent);
        overridePendingTransition(R.anim.bo_to_top, R.anim.alpha_lose);
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Toast.makeText(context,"加载完成",Toast.LENGTH_LONG).show();
                    askdialog.dismiss();
                    break;
                case 1:
                    askdialog.dismiss();
//                    Toast.makeText(context,"加载失败",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    AlertDialog.Builder askbuilder;
    AlertDialog askdialog;
    public void isAsking(){
        if(askbuilder==null){
            askbuilder=new AlertDialog.Builder(context);
            askbuilder.setCancelable(false);
            askbuilder.setView(R.layout.dialog_loading);
        }
        askdialog= askbuilder.show();
        askdialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
    }
//
    public void get_info_from_db(){
        try {
            contactses=dbUtils.findAll(com.lidroid.xutils.db.sqlite.Selector.from(Contacts.class));
            editAdapter=new Adapter_show_contacts(contactses,this);
            layout_show_contacts.setAdapter(editAdapter);
            editAdapter.notifyDataSetChanged();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    Utils utils=new Utils(this);
    public void cloudDown(){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("从云端下载联系人");
        builder.setMessage("可能会覆盖已存在联系人资料");
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(utils.isLogin()){
                    isAsking();//加载条
                    Utils_Contacts uc=new Utils_Contacts(context);
                    uc.Select_Contacts(handler);
                }else{
                    Intent intent=new Intent(context, Login.class);
                    startActivity(intent);
                }
            }
        });
        builder.show();
    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
