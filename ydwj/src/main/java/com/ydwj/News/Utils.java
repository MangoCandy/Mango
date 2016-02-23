package com.ydwj.News;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ydwj.Service.Service_Download;
import com.ydwj.alarm.Utils_Contacts;
import com.ydwj.bean.Contacts;
import com.ydwj.bean.Userinfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;

import java.net.URL;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils{
	Context context;
	public Utils(Context context){
		this.context=context;
	}

	public void saveinfos(Userinfo userinfo){
		SharedPreferences sharedPreferences=context.getSharedPreferences("MangoWe", context.MODE_PRIVATE);
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putString(userinfo.USER_LOGINNAME_CODE,userinfo.getLogin_name());
		editor.putString(userinfo.USER_ID_CODE,userinfo.getID());
		editor.putString(userinfo.USER_EMAIL_CODE,userinfo.getEmail());
		editor.putString(userinfo.USER_ADDRESS_CODE,userinfo.getUser_address());
		editor.putString(userinfo.USER_NAME_CODE,userinfo.getUSER_NAME());
		editor.putString(userinfo.USER_IDCARD_CODE,userinfo.getCard_id());
		editor.putString(userinfo.USER_is_homeowner_CODE,userinfo.getIs_homeowner());
		editor.putString(userinfo.USER_TEL1_CODE,userinfo.getUsers_tel1());
		editor.putString(userinfo.USER_TEL2_CODE,userinfo.getUsers_tel2());
		editor.putString(userinfo.USER_PWD,userinfo.getLogin_pwd());
		editor.putString(userinfo.USER_IMG_CODE,userinfo.getUSER_IMG());
		editor.putString("pwd",userinfo.getLogin_pwd());
		editor.commit();
	}
	//读取Stream
	 public static byte[] readStream(InputStream inputStream) throws Exception {   
	        ByteArrayOutputStream bout = new ByteArrayOutputStream();   
	        byte[] buffer = new byte[1024];   
	        int len = 0;   
	        while ((len = inputStream.read(buffer)) != -1) {   
	            bout.write(buffer, 0, len);   
	        }   
	        bout.close();   
	        inputStream.close();   
	  
	        return bout.toByteArray();   
	    }
//	 获取时间
	public String gettime(){
		String time=null;
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
		time=simpleDateFormat.format(new Date());
		return time;
	}
	//获取用户信息
	public Userinfo getUserinfo(){
		Userinfo userinfo=new Userinfo();
		SharedPreferences sharedPreferences=context.getSharedPreferences("MangoWe",Context.MODE_PRIVATE);
		sharedPreferences.getString(Userinfo.USER_IMG_CODE,"");
		userinfo.setUSER_NAME(sharedPreferences.getString(Userinfo.USER_NAME_CODE, ""));
		userinfo.setCard_id(sharedPreferences.getString(Userinfo.USER_IDCARD_CODE, "未填写"));
		userinfo.setEmail(sharedPreferences.getString(Userinfo.USER_EMAIL_CODE, "未填写"));
		userinfo.setID(sharedPreferences.getString(Userinfo.USER_ID_CODE, ""));
		userinfo.setIs_homeowner(sharedPreferences.getString(Userinfo.USER_is_homeowner_CODE,""));
		userinfo.setLogin_name(sharedPreferences.getString(userinfo.USER_LOGINNAME_CODE,""));
		userinfo.setUsers_tel1(sharedPreferences.getString(userinfo.USER_TEL1_CODE,""));
		userinfo.setUsers_tel2(sharedPreferences.getString(userinfo.USER_TEL2_CODE,"未填写"));
		userinfo.setUser_address(sharedPreferences.getString(userinfo.USER_ADDRESS_CODE,"未填写"));
		userinfo.setLogin_pwd(sharedPreferences.getString(userinfo.USER_PWD,""));
		userinfo.setUSER_IMG(sharedPreferences.getString(userinfo.USER_IMG_CODE,""));
		return userinfo;
	}
	//删除用户信息
	public void deleteUserinfo(Context context){
		SharedPreferences sharedPreferences=context.getSharedPreferences("MangoWe",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}
	//    String转Bitmap
	public static Bitmap String2Bitmap(String st)
	{
		// OutputStream out;
		Bitmap bitmap = null;
		try
		{
			// out = new FileOutputStream("/sdcard/aa.jpg");
			byte[] bitmapArray;
			bitmapArray = Base64.decode(st, Base64.DEFAULT);
			bitmap =
					BitmapFactory.decodeByteArray(bitmapArray, 0,
							bitmapArray.length);
			// bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			return bitmap;
		}
		catch (Exception e)
		{
			return null;
		}
	}
	//登录
	String res=null;
	public void login(final Handler handler, final String usernameT, final String pwdT){
		RequestQueue requestQueue= Volley.newRequestQueue(context);
		StringRequest jsonObjectRequest=new StringRequest(Request.Method.POST, "http://app.cloud-hn.net/app/factory.php", new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
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
						userinfo.setLogin_pwd(pwdT);
						saveinfos(userinfo);
						//读取紧急联系人
						JSONArray jsonArray=obj.getJSONArray("emergency_contacts");
						if(jsonArray.length()>0){saveContacts(jsonArray);}
						res="登陆成功";
						editLogin(true);
					}else{
						res=obj.getString("retMessage");
						editLogin(false);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if(handler!=null){
					Message message=new Message();
					message.what=1;
					Bundle bundle=new Bundle();
					bundle.putString("res",res);
					if(res.equals("登陆成功")){
						bundle.putBoolean("login",true);
					}else{
						deleteUserinfo(context);
						bundle.putBoolean("login",false);
					}
					message.setData(bundle);
					handler.sendMessage(message);
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				editLogin(false);
				if(handler!=null){
					handler.sendEmptyMessage(2);
				}
				Toast.makeText(context,"请检查网络设置",Toast.LENGTH_SHORT).show();
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
	}
	//保存登陆返回联系人
	public void saveContacts(JSONArray jsonArray){
		try {
			JSONObject contact;
			final Utils_Contacts uc=new Utils_Contacts(context);
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
	//启动时登陆状态
	public void editLogin(Boolean bool){
		SharedPreferences sharedPreferences=context.getSharedPreferences("MangoWe",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putBoolean("islogin",bool);
		editor.commit();
	}
	//获取是否登录
	public boolean isLogin(){
		SharedPreferences sharedPreferences=context.getSharedPreferences("MangoWe",Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean("islogin",false);
	}

	//获取更新
	public void askForUpdate(final boolean silence){//是否静默检查
		RequestQueue requestQueue=Volley.newRequestQueue(context);
		StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://app.cloud-hn.net/app/factory.php", new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.i("asd",response+"123");
				try {
					JSONObject jsonObject=new JSONObject(response);
					if(jsonObject.getString("retCode").equals("01")){
						boolean b=jsonObject.getString("even_odds").equals("2")?false:true;
						updateApp(jsonObject.getString("retMessage"),jsonObject.getString("retContent"),jsonObject.getString("url"),b);
					}else{
						if(!silence){
							Toast.makeText(context,"已是最新版本",Toast.LENGTH_SHORT).show();
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.i("asd",error+"234");
				if(!silence){
					Toast.makeText(context,"请检查网络或重试",Toast.LENGTH_SHORT).show();
				}
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String>params=new HashMap<>();
				params.put("action","android_version");
				try {
					params.put("version_number",context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionCode+"");
				} catch (PackageManager.NameNotFoundException e) {
					e.printStackTrace();
				}
				return params;
			}
		};
		requestQueue.add(stringRequest);
	}
	//询问更新
	private void updateApp(String title, final String content, final String url,boolean odds){//odds是否强制更新
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent=new Intent(context, Service_Download.class);
				intent.putExtra("url",url);
				intent.putExtra("path","");
				context.startService(intent);

			}
		});
		builder.setNegativeButton("取消",null);
		if(odds){
			builder.setCancelable(false);
			builder.setNegativeButton(null,null);
		}
		builder.show();
	}
}
