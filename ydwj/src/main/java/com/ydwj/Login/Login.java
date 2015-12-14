package com.ydwj.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ydwj.News.ShowNews;
import com.ydwj.News.Utils;

import com.ydwj.alarm.Utils_Contacts;
import com.ydwj.bean.Contacts;
import com.ydwj.bean.Userinfo;
import com.ydwj.community.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
            loginA();
        }else{
            Toast.makeText(this,"信息不能为空",Toast.LENGTH_SHORT).show();
        }
    }

    String res;

    public void loginA(){
        isAsking();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest=new StringRequest(Request.Method.POST, "http://app.cloud-hn.net/app/factory.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                askdialog.dismiss();
                JSONObject obj=null;
                try {
                    obj=new JSONObject(response);
                    Log.i("asd",response);
                    if(obj.getString("retCode").equals("00")){
                        JSONObject jsonObject=obj.getJSONObject("userinfo");
                        Userinfo userinfo=new Userinfo();
                        userinfo.setCard_id(jsonObject.getString(userinfo.USER_IDCARD_CODE));
                        userinfo.setEmail(jsonObject.getString(userinfo.USER_EMAIL_CODE));
                        userinfo.setID(jsonObject.getString(userinfo.USER_ID_CODE));
                        userinfo.setIs_homeowner(jsonObject.getString(userinfo.USER_is_homeowner_CODE));
                        userinfo.setLogin_name(jsonObject.getString(userinfo.USER_LOGINNAME_CODE));
                        userinfo.setUser_address(jsonObject.getString(userinfo.USER_ADDRESS_CODE));
                        userinfo.setUSER_NAME(jsonObject.getString(userinfo.USER_NAME_CODE));
                        userinfo.setUsers_tel1(jsonObject.getString(userinfo.USER_TEL1_CODE));
                        userinfo.setUsers_tel2(jsonObject.getString(userinfo.USER_TEL2_CODE));
                        userinfo.setLogin_pwd(jsonObject.getString(userinfo.USER_PWD));
                        userinfo.setUSER_IMG((jsonObject.getString(userinfo.USER_IMG_CODE)).replaceAll("\\\\",""));
                        utils.saveinfos(userinfo);
                        //读取紧急联系人
                        JSONArray jsonArray=obj.getJSONArray("emergency_contacts");
                        if(jsonArray.length()>0){saveContacts(jsonArray);}
                        res="登陆成功";
                        finish();
                    }else if(obj.getString("retCode").equals("01")){
                        res=obj.getString("retMessage");
                    }else{
                        res="密码错误";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                askdialog.dismiss();
                res = "" + error;
                System.out.println(error.getCause());
                handler.sendEmptyMessage(0);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("action","login");
                params.put("loginName",usernameT);
                params.put("loginPwd",pwdT);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
        requestQueue.start();
    }
    public void saveContacts(JSONArray jsonArray){
        try {
            JSONObject contact;
            final Utils_Contacts uc=new Utils_Contacts(this);
            for(int i=0;i<jsonArray.length();i++) {
                contact = jsonArray.getJSONObject(i);
                Contacts contacts = new Contacts();
                contacts.setCID(contact.getString("person_id"));
                contacts.setCONTACT_NUM(contact.getString("person_tel1"));
                contacts.setCONTACT_NAME(contact.getString("person_name"));
                contacts.setBEIZHU(contact.getString("person_beizhu"));
                uc.save(contacts);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
                    Toast.makeText(context,res,Toast.LENGTH_SHORT).show();
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
