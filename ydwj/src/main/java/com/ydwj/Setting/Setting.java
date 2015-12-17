package com.ydwj.Setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ydwj.Login.Login;
import com.ydwj.News.Utils;
import com.ydwj.Utils.CircleImageView;
import com.ydwj.alarm.Utils_Contacts;
import com.ydwj.bean.MyApplication;
import com.ydwj.bean.TitleInfo;
import com.ydwj.bean.Userinfo;

import com.ydwj.community.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Setting extends Fragment {
    TextView username;
    View view;
    String usernameT;
    Utils utils;
    Userinfo userinfo;
    Context context;
    LinearLayout exit_login;//退出登录
    LinearLayout delete_cache;//清除缓存
    LinearLayout editUser;//修改用户资料
    LinearLayout editpwd;//修改密码
    LinearLayout appinfo;//查看软件信息
    CircleImageView user_img;
    String userimg;
    Bitmap headimg;
    LinearLayout loginTool;//登陆后显示的操作
    public Setting() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        utils=new Utils(context);
    }
    public void initView(){
        username=(TextView)view.findViewById(R.id.set_username);
        username.setOnClickListener(onClickListener);

        exit_login=(LinearLayout)view.findViewById(R.id.exit_login);
        exit_login.setOnClickListener(onClickListener);

        delete_cache=(LinearLayout)view.findViewById(R.id.delete_cache);
        delete_cache.setOnClickListener(onClickListener);

        user_img=(CircleImageView)view.findViewById(R.id.set_head);
        user_img.setOnClickListener(onClickListener);

        editUser=(LinearLayout) view.findViewById(R.id.editUser);
        editUser.setOnClickListener(onClickListener);

        loginTool=(LinearLayout)view.findViewById(R.id.loginTool);

        editpwd=(LinearLayout)view.findViewById(R.id.editPwd);
        editpwd.setOnClickListener(onClickListener);

        appinfo=(LinearLayout)view.findViewById(R.id.appInfo);
        appinfo.setOnClickListener(onClickListener);
    }
    public void initInfo(){
        userinfo=utils.getUserinfo();

        if(userinfo.getUSER_IMG()!=null){
            Glide.with(context).load(userinfo.getUSER_IMG()).into(user_img);
        }

        usernameT=userinfo.getUSER_NAME();
        userimg=userinfo.getUSER_IMG();
        if(userinfo.getID()!=null&&!userinfo.getID().equals("")){
            username.setText(usernameT);
            //显示登陆后操作区域
            loginTool.setVisibility(View.VISIBLE);
        }else {
            user_img.setImageBitmap(null);
            username.setText("请先登录");
            //隐藏登陆后操作区域
            loginTool.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_setting, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    public void gotoLogin(){
        Intent intent=new Intent(context, Login.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.alpha_add, R.anim.alpha_lose);
    }
    final int PICK_PHOTO=1234;
    final int PHOTO_ZOOM=1233;
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.set_username:
                    if(usernameT.equals("")){
                        gotoLogin();
                    }
                    break;
//                退出登录
                case R.id.exit_login:
                    Snackbar snackbar= Snackbar.make(view.findViewById(R.id.exit_login), "是否退出登录", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("登出", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //删除SP信息
                            utils.deleteUserinfo(context);
                            Utils_Contacts uc=new Utils_Contacts(context);
                            //清空联系人（本地）
                            uc.clear_contacts();
                            initInfo();
                        }
                    });
                    View vv=snackbar.getView();
                    TextView textView=(TextView)vv.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);

                    snackbar.show();
                    break;
//                清空缓存
                case R.id.delete_cache:
                    snackbar= Snackbar.make(view.findViewById(R.id.exit_login), "是否清空", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("清空", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DbUtils dbUtils=((MyApplication)(getActivity().getApplication())).dbUtils;
                            try {
                                dbUtils.dropTable(TitleInfo.class);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    vv=snackbar.getView();
                    textView=(TextView)vv.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                    break;
//                设置头像
                case R.id.set_head:
                    if(usernameT.equals("")||!utils.isLogin()){
                        gotoLogin();
                    }else{
                        Intent i = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
                        startActivityForResult(i,PICK_PHOTO);
                    }
                    break;

                case R.id.editUser:
                    Intent intent=new Intent(context,Act_editUser.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.top_to_bo,R.anim.alpha_lose);
                    break;
                case R.id.editPwd:
                    Intent i=new Intent(context,Act_editPwd.class);
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.top_to_bo,R.anim.alpha_lose);
                    break;
                case R.id.appInfo:
                    Intent intent1=new Intent(context,Act_AppInfo.class);
                    startActivity(intent1);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICK_PHOTO:
                switch (resultCode){
                    case Activity.RESULT_OK:
                        Uri uri=data.getData();
                        Cursor cursor=getActivity().getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        String imgPath = cursor.getString(1); // 图片文件路径
                        startPhotoZoom(imgPath);
                        break;
                }
            break;
//            裁剪结果
            case PHOTO_ZOOM:
                if(resultCode==Activity.RESULT_OK){
                    Bitmap bitmap=data.getParcelableExtra("data");
                    updataImg(bitmap);
                }
                break;
        }
    }
    String bits;
    File head_img;//头像文件
    public void startPhotoZoom(String path) {
        String sdcard = Environment.getExternalStorageDirectory().toString();
        File file = new File(sdcard + "/ydwj/HeadImg/");
        if (!file.exists()) {
            file.mkdirs();
        }
        head_img=new File(sdcard + "/ydwj/HeadImg/" + new Date().getTime() + ".png");
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(path)),"image/*");
//        intent.putExtra("output",Uri.fromFile(head_img));
        intent.putExtra("outputFormat",Bitmap.CompressFormat.PNG);
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
    public String file2String(Bitmap bitmap){
        FileInputStream inputStream=null;
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        byte[] buffer=null;
//        try {
//            inputStream=new FileInputStream(head_img);
//            buffer=new byte[inputStream.available()];
//            inputStream.read(buffer);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        buffer=baos.toByteArray();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }
    String res;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Toast.makeText(context,res,Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(context,"上传成功",Toast.LENGTH_SHORT).show();
                    user_img.setImageBitmap(headimg);
                    saveInfo(bits);
                    break;
            }
        }
    };

    //上传图片到服务器
    public void updataImg(Bitmap bitmap){
        isAsking();
        final String base64="data:image/png;base64,"+file2String(bitmap);
//        Log.i("asd",base64);
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://app.cloud-hn.net/app/factory.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                askdialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String str=jsonObject.getString("file_url").replaceAll("\\\\","");
                    saveInfo(str);
                    Glide.with(context).load(str).into(user_img);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                askdialog.dismiss();
                Toast.makeText(context,"请检查网络",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("action","update_avatar");
                params.put("users_id",utils.getUserinfo().getID());
                params.put("avatar_url",base64);
                return params;
            }
        };
        requestQueue.add(stringRequest);
        requestQueue.start();
    }
    public void saveInfo(String img){
        SharedPreferences sharedPreferences=context.getSharedPreferences("MangoWe", context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(userinfo.USER_IMG_CODE,img);
        editor.commit();
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
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
