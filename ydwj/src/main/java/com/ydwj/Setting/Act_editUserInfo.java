package com.ydwj.Setting;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.ydwj.News.Utils;
import com.ydwj.MUtils.Utils_user;
import com.ydwj.bean.Userinfo;
import com.ydwj.community.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Act_editUserInfo extends AppCompatActivity {
    Utils_user uu;
    FloatingActionButton fb;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_act_edit_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.iconfont_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitleTextColor(Color.WHITE);
        uu=new Utils_user(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        userinfo=(new Utils(this)).getUserinfo();
        initView();
    }
    Userinfo userinfo;
    MaterialEditText username;
    MaterialEditText tel1;
    MaterialEditText tel2;
    MaterialEditText email;
    MaterialEditText address;

    public void initView(){
        fb=(FloatingActionButton)findViewById(R.id.confirm);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(checkinfo()){
                            editUser();
                        }
                    }
                });
                builder.setMessage("是否更新修改信息");
                builder.setNegativeButton("取消",null);
                builder.show();

            }
        });
        username=(MaterialEditText)findViewById(R.id.username);
        username.setText(userinfo.getUSER_NAME());

        tel1=(MaterialEditText)findViewById(R.id.tel1);
        tel1.setText(userinfo.getUsers_tel1());

        tel2=(MaterialEditText)findViewById(R.id.tel2);
        tel2.setText(userinfo.getUsers_tel2());

        email=(MaterialEditText)findViewById(R.id.email);
        email.setText(userinfo.getEmail());

        address=(MaterialEditText)findViewById(R.id.address);
        address.setText(userinfo.getUser_address());
    }

    //检查信息
    String usernameT;
    String tel1T;
    String tel2T;
    String emailT;
    String addressT;
    public boolean checkinfo(){
        usernameT=username.getText().toString();
        tel1T=tel1.getText().toString();
        tel2T=tel2.getText().toString();
        emailT=email.getText().toString();
        addressT=address.getText().toString();
        //联系人1
        if(!tel1T.equals(userinfo.getUsers_tel1())){
            if(!uu.checknum(tel1T)){
                return false;
            };
        }
        //联系人2
        if(!tel2T.equals(userinfo.getUsers_tel2())){
            if(!uu.checknum(tel2T)){
                return false;
            };
        }
        //邮箱
        if(!emailT.equals(userinfo.getEmail())){
            if(!uu.checkEmail(emailT)){
                return false;
            }
        }
        //住址
        if(!addressT.equals(userinfo.getUser_address())){
            if(addressT==null&&addressT.equals("")){
                Toast.makeText(this,"住址不能为空",Toast.LENGTH_SHORT).show();
            }
        }
        //昵称
        if(!usernameT.equals(userinfo.getUSER_NAME())){
            if(usernameT==null&&usernameT.equals("")){
                Toast.makeText(this,"昵称不能为空",Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    public void editUser(){
        isAsking();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest=new StringRequest(Request.Method.POST, "http://app.cloud-hn.net/app/factory.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                askdialog.dismiss();
                Log.i("asd", response);
                JSONObject jsonObject=null;
                try {
                    jsonObject=new JSONObject(response);
                    if(jsonObject.getString("retCode").equals("00")){
                        Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT).show();
                        userinfo.setUSER_NAME(usernameT);
                        userinfo.setEmail(emailT);
                        userinfo.setUser_address(addressT);
                        userinfo.setUsers_tel1(tel1T);
                        userinfo.setUsers_tel2(tel2T);
                        (new Utils(context)).saveinfos(userinfo);
                        finish();
                    }else{
                        Toast.makeText(context,jsonObject.getString("retMessage")+"",Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("asd", error+"");
                Toast.makeText(context,"请检查网络连接,稍后重试",Toast.LENGTH_SHORT).show();
                askdialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("action","users_update");
                params.put("users_name",usernameT);
                params.put("usersId",userinfo.getID());
                params.put("usersTel1",tel1T);
                params.put("usersEmail",emailT);
                params.put("usersTel2",tel2T);
                params.put("usersAddress",addressT);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
        requestQueue.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.top_to_bo, R.anim.alpha_lose);
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
