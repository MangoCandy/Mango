package com.ydwj.Setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ydwj.Login.Login;
import com.ydwj.News.Utils;
import com.ydwj.alarm.Act_addcontact;
import com.ydwj.bean.Userinfo;
import com.ydwj.community.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Act_editUser extends AppCompatActivity {
    Utils utils=new Utils(this);
    Userinfo userinfo;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        //如果未登录 跳转至登录
        if(!utils.isLogin()){
            Intent intent=new Intent(this, Login.class);
            startActivity(intent);
            this.finish();
        }
        setContentView(R.layout.activity_act_edit_user);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        initToolbar();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,Act_editUserInfo.class);
                startActivity(intent);
                overridePendingTransition(R.anim.top_to_bo, R.anim.alpha_lose);
            }
        });
    }
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        userinfo=utils.getUserinfo();//初始化用户信息
        initView();
    }

    public void initToolbar(){
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.iconfont_back);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.alpha_add, R.anim.alpha_lose);
    }

    TextView username;
    TextView address;
    TextView idCard;
    TextView tel1;
    TextView tel2;
    TextView email;
    TextView loginname;
    public void initView(){
        loginname=(TextView)findViewById(R.id.loginname);
        loginname.setText(userinfo.getLogin_name());

        username=(TextView) findViewById(R.id.username);
        username.setText(userinfo.getUSER_NAME());

        address=(TextView)findViewById(R.id.address);
        address.setText(userinfo.getUser_address());

        idCard=(TextView)findViewById(R.id.id_card);
        idCard.setText(userinfo.getCard_id());

        tel1=(TextView)findViewById(R.id.tel1);
        tel1.setText(userinfo.getUsers_tel1());
        tel2=(TextView)findViewById(R.id.tel2);
        tel2.setText(userinfo.getUsers_tel2());

        email=(TextView)findViewById(R.id.email);
        email.setText(userinfo.getEmail());
    }
}
