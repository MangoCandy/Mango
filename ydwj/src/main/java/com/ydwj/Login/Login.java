package com.ydwj.Login;

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;

import android.os.Handler;
import android.os.Message;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ydwj.News.ShowNews;
import com.ydwj.News.Utils;

import com.ydwj.alarm.Utils_Contacts;
import com.ydwj.bean.Contacts;
import com.ydwj.bean.Userinfo;
import com.ydwj.community.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {
    EditText username;
    EditText pwd;
    Button login_btn;
    Button reg_btn;
    String usernameT;
    String pwdT;
    Utils utils;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        utils=new Utils(context);
        initView();
        initActionBar();
        Intent intent=getIntent();
        if(intent.getExtras()!=null){
           username.setText(intent.getExtras().getString("name"));
            pwd.setText(intent.getExtras().getString("pwd"));
            checkinfo();
        }
    }

    public void initView(){
        username=(EditText)findViewById(R.id.username_l);
        pwd=(EditText)findViewById(R.id.pwd_l);
        login_btn=(Button)findViewById(R.id.btn_login);
        login_btn.setOnClickListener(onClickListener);
        reg_btn=(Button)findViewById(R.id.btn_regs);
        reg_btn.setOnClickListener(onClickListener);
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
        toolbar.setTitle("登录");
    }
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_login:
                    checkinfo();
                    break;
                case R.id.btn_regs:
                    Intent intent=new Intent(context,Register.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.alpha_add, R.anim.alpha_lose);
                    finish();
                    break;
            }
        }
    };
    public void checkinfo(){
        usernameT=username.getText().toString();
        pwdT=pwd.getText().toString();
        try {
            usernameT= URLEncoder.encode(usernameT,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(!usernameT.equals("")&&!pwdT.equals("")){
            isAsking();
            utils.login(handler,usernameT,pwdT);
        }else{
            Toast.makeText(this,"信息不能为空",Toast.LENGTH_SHORT).show();
        }
    }

    public void fogetPwd(View view){
        Intent intent=new Intent(this, ShowNews.class);
        intent.putExtra("url","http://app.cloud-hn.net/app/forget_password.show.php");
        startActivity(intent);
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:

                    break;
                case 1:
                    askdialog.dismiss();
                    Bundle bundle=msg.getData();
                    Toast.makeText(context,bundle.getString("res"),Toast.LENGTH_SHORT).show();
                    if(bundle.getBoolean("login")){
                        finish();
                    }
                    break;
                case 2:
                    askdialog.dismiss();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        if(askdialog==null||!askdialog.isShowing()){
            super.onBackPressed();
        }
    }

    //请求网络时显示加载圈
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
    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }
}
