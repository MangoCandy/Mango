package com.ydwj.Login;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ydwj.Utils.CircleImageView;
import com.ydwj.News.Utils;
import com.ydwj.Utils.ProgressWheel;
import com.ydwj.Utils.Utils_user;
import com.ydwj.bean.Userinfo;

import com.ydwj.community.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {
    ActionBar actionBar;
    String res;
    EditText username;//用户名
    EditText pwd;//密码
    EditText num;//电话
    EditText num2;//紧急联系人
    String num2T;
    EditText pwd2;//确认密码
    EditText id;//身份证号码
    EditText homeaddr;//家庭住址
    String homeaddrT;
    EditText email;//邮箱
    String emailT;
    Button register;
    Button login;
    Context context;
    String usernameT;
    String pwdT;
    String pwd2T;
    Utils utils;
    CircleImageView setImg;
    LinearLayout btn_isChecked;
    ImageView haschecked;

    Utils_user uu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context=this;
        initActionBar();
        initTool();
        utils=new Utils(context);
        uu=new Utils_user(context);
    }
    public void initActionBar(){
        actionBar=getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.action_bar_back);
        TextView mtitle= (TextView) actionBar.getCustomView().findViewById(R.id.mtitle);
        actionBar.getCustomView().findViewById(R.id.go_back).setOnClickListener(onClickListener);
        mtitle.setText("注册帐号");
    }
    public void initTool(){
//        setImg=(CircleImageView)findViewById(R.id.set_img);
//        setImg.setOnClickListener(onClickListener);
        register=(Button)findViewById(R.id.btn_reg);
        register.setOnClickListener(onClickListener);
        pwd=(EditText)findViewById(R.id.password_r);
        pwd2=(EditText)findViewById(R.id.password2_r);
        username=(EditText)findViewById(R.id.username_r);
        login=(Button)findViewById(R.id.btn_log);
        login.setOnClickListener(onClickListener);
        btn_isChecked=(LinearLayout)findViewById(R.id.isChecked);
        btn_isChecked.setOnClickListener(onClickListener);
        haschecked=(ImageView)findViewById(R.id.checkbox_hasread);
        num=(EditText)findViewById(R.id.login_name);
        id=(EditText)findViewById(R.id.id_card);
        num.setFocusable(true);
        num.setFocusableInTouchMode(true);
        num.requestFocus();
        homeaddr=(EditText)findViewById(R.id.homeaddrest);
        email=(EditText)findViewById(R.id.email);
        num2=(EditText)findViewById(R.id.num2);

    }
    boolean isChoice=false;
    public void showChoice(View view){
        if(isChoice==false){
            ((TextView)view.findViewById(R.id.choiceText)).setText("收起可选项");
//            view.setVisibility(View.GONE);
            findViewById(R.id.choice).setVisibility(View.VISIBLE);
            findViewById(R.id.choice).setAnimation(AnimationUtils.loadAnimation(context,R.anim.alpha_add));
            isChoice=true;
        }else{
//            view.setVisibility(View.GONE);
            ((TextView)view.findViewById(R.id.choiceText)).setText("展开可选项");
            findViewById(R.id.choice).setVisibility(View.GONE);
            findViewById(R.id.choice).setAnimation(AnimationUtils.loadAnimation(context,R.anim.alpha_lose));
            isChoice=false;
        }
    }
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_reg:
                    checkinfo();
                    break;
                case R.id.btn_log:
                    Intent intent=new Intent(context,Login.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.alpha_add, R.anim.alpha_lose);
                    finish();
                    break;
                case R.id.isChecked:
                    if(haschecked.isSelected()){
                        haschecked.setSelected(false);
                    }else{
                        haschecked.setSelected(true);
                    }
                    break;
                case R.id.go_back:
                    onBackPressed();
                    break;
            }
        }
    };
//    String usernamestr;
    String numt;
    String idt;
    //检查注册信息
    public void checkinfo(){
        idt=id.getText().toString();
        numt=num.getText().toString();
        pwdT=pwd.getText().toString();
        pwd2T=pwd2.getText().toString();
        usernameT=username.getText().toString();
//        usernamestr=usernameT;
        if(uu.checknum(numt)){
            if(uu.checkname(usernameT)){
                if(!username.equals("")&&!pwdT.equals("")&&!pwd2T.equals("")){
                    if(pwd2T.equals(pwdT)){
                        if(haschecked.isSelected()){
                            if(isChoice&&checkother()){
                                register();
                            }else if(!isChoice){
                                register();
                            }
                        }else{
                            Toast.makeText(this,"请仔细阅读服务协议并勾选",Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(this,"两次密码输入不一致，请检查",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,"信息填写不完整",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    //检测非必选信息
    public boolean checkother(){
        if(num2.getText().toString()!=null&&!num2.getText().toString().equals("")){
            if(!uu.checknum(num2.getText().toString())){
                num2T=null;
                return false;
            }else{
                num2T=num2.getText().toString();
            }
        }
        if(id.getText().toString()!=null&&!id.getText().toString().equals("")){
            if(!uu.checkid(idt)){
                idt=null;
                return false;
            }else{
                idt=id.getText().toString();
            }
        }
        if(homeaddr.getText().toString()!=null&&!homeaddr.getText().toString().equals("")){
            homeaddrT=homeaddr.getText().toString();
        }else{
            homeaddrT=null;
        }
        if(email.getText().toString()!=null&&!email.getText().toString().equals("")){
            if(!uu.checkEmail(email.getText().toString())){
                emailT=null;
                return false;
            }else{
                emailT=email.getText().toString();
            }
        }
        return true;
    }
   //检测用户名是否存在
   JSONObject jsonObject;
    public void register(){
        isAsking();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest jsonObjectRequest=new StringRequest(Request.Method.POST, "http://app.cloud-hn.net/app/factory.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                askdialog.dismiss();
                try {
                    jsonObject=new JSONObject(response);
                    String rectcode=jsonObject.getString("retCode");
                    if(rectcode.equals("00")){
                        Toast.makeText(context,"注册成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(context,Login.class);
                        intent.putExtra("name",numt);
                        intent.putExtra("pwd",pwdT);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(context,jsonObject.getString("retMessage"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                handler.sendEmptyMessage(0);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (askdialog != null && askdialog.isShowing()) {
                    askdialog.dismiss();
                }
                res = "注册失败，网络连接超时" + error;
                handler.sendEmptyMessage(0);
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params=new HashMap<String,String>();
                params.put("action","register");
                params.put("loginName",numt);
                params.put("loginPwd",pwdT);
                params.put("users_tel1",numt);
                params.put("user_name",usernameT);
                if(isChoice){
                    if(homeaddrT!=null){
                        params.put("users_address",homeaddrT);
                    }
                    if(num2T!=null){
                        params.put("users_tel2",num2T);
                    }
                    if(idt!=null){
                        params.put("card_id",idt);
                    }
                    if(emailT!=null){
                        params.put("users_email",emailT);
                    }
                }
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
        requestQueue.start();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Toast.makeText(context,res,Toast.LENGTH_LONG).show();
                    break;
                case 1:

            }
        }
    };
    String imgStr="";
    public boolean iskit(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            return true;
        }
        return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICK_PHOTO:
                switch (resultCode){
                    case Activity.RESULT_OK:
                        Uri uri=data.getData();
                        Cursor cursor=getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        String imgPath = cursor.getString(1); // 图片文件路径
                        startPhotoZoom(imgPath);
                        break;
                }
                break;
//            裁剪结果
            case PHOTO_ZOOM:
                if(resultCode==Activity.RESULT_OK){
                    Bitmap bitmap=null;
                    if(iskit()){
                        bitmap=BitmapFactory.decodeFile(head_img.getPath());
                    }else{
                        bitmap=data.getParcelableExtra("data");
                    }
                    setImg.setImageBitmap(bitmap);
                    imgStr=bitmap2String(bitmap);
                }
                break;
        }
    }
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }
    File head_img;
    final int PICK_PHOTO=1234;
    final int PHOTO_ZOOM=1233;
    public void startPhotoZoom(String path) {
        String sdcard = Environment.getExternalStorageDirectory().toString();
        File file = new File(sdcard + "/MangoWe/HeadImg");
        if (!file.exists()) {
            file.mkdirs();
        }
        head_img=new File(sdcard + "/MangoWe/HeadImg/" + new Date().getTime() + ".jpg");
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(path)),"image/*");
        intent.putExtra("output",Uri.fromFile(head_img));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scaleUpIfNeeded", true);// 如果小于要求输出大小，就放大
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX",300);
        intent.putExtra("outputY",300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, PHOTO_ZOOM);
    }
    //Bitmap转String
    public String bitmap2String(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imgbyte = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(imgbyte, Base64.DEFAULT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

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
