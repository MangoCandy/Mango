package com.ydwj.Setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.ydwj.Login.Login;
import com.ydwj.News.Utils;
import com.ydwj.bean.Userinfo;
import com.ydwj.community.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Act_editPwd extends AppCompatActivity {
    Context context=this;
    Utils utils=new Utils(context);
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //如果未登录 跳转至登录
        if(!utils.isLogin()){
            Intent intent=new Intent(this, Login.class);
            startActivity(intent);
            this.finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_edit_pwd);
        initView();
        inittoolbar();
    }
    MaterialEditText oldPwd;
    MaterialEditText pwd;
    MaterialEditText pwd1;
    FloatingActionButton fb;

    String oldPwdT;
    String pwdT;
    String pwd1T;
    public void inittoolbar(){
        toolbar=(Toolbar)findViewById(R.id.toolbar);
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
    public void initView(){
        oldPwd=(MaterialEditText)findViewById(R.id.oldPwd);
        pwd=(MaterialEditText)findViewById(R.id.pwd);
        pwd1=(MaterialEditText)findViewById(R.id.pwd1);
        fb=(FloatingActionButton)findViewById(R.id.confirm);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkinfo();
            }
        });
    }

    public void checkinfo(){
        oldPwdT=oldPwd.getText().toString();
        pwdT=pwd.getText().toString();
        pwd1T=pwd1.getText().toString();
        if(oldPwdT.trim().isEmpty()&&pwdT.trim().isEmpty()&&pwd1T.trim().isEmpty()){
            Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
        }else{
            {
                if(pwd1T.equals(pwdT)){
                    post();
                }else{
                    Toast.makeText(this,"修改密码不一致",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public void post(){
        isAsking();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://app.cloud-hn.net/app/factory.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    askdialog.dismiss();
                    JSONObject object=new JSONObject(response);
                    Toast.makeText(context,object.getString("retMessage"),Toast.LENGTH_SHORT).show();
                    if(object.getString("retCode").equals("00")){
                        Userinfo userinfo=utils.getUserinfo();
                        userinfo.setLogin_pwd(pwd1T);
                        utils.saveinfos(userinfo);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("asd",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                askdialog.dismiss();
                Toast.makeText(context,"请检查网络连接,稍后重试",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("action","pwd_update");
                params.put("old_pwd",oldPwdT);
                params.put("loginPwd",pwd1T);
                params.put("usersId",(new Utils(context).getUserinfo()).getID());
                return params;
            }
        };
        requestQueue.add(stringRequest);
        requestQueue.start();
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
}
