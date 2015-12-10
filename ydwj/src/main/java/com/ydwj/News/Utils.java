package com.ydwj.News;

import android.content.Context;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.ydwj.bean.Userinfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;

import java.net.URL;
import java.text.SimpleDateFormat;

import java.util.Date;

import org.json.JSONObject;

public class Utils{
	Context context;
	public Utils(Context context){
		this.context=context;
	}
	public JSONObject getNewsType(String path){
		String string=null;
		JSONObject jsonObject=null;
		try {
			URL url=new URL(path);
			HttpURLConnection hConnection=(HttpURLConnection) url.openConnection();
			hConnection.setConnectTimeout(5000);
			hConnection.connect();
			InputStream inputStream=hConnection.getInputStream();
			byte[] buffer=readStream(inputStream);
			string=new String(buffer);
			hConnection.disconnect();
			inputStream.close();
			buffer=null;
			jsonObject=new JSONObject(string);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}
	public void saveinfos(Userinfo userinfo){
		SharedPreferences sharedPreferences=context.getSharedPreferences("MangoWe", context.MODE_PRIVATE);
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putString("login_name",userinfo.getLogin_name());
		editor.putString("id",userinfo.getID());
		editor.putString("users_email",userinfo.getEmail());
		editor.putString("users_address",userinfo.getUser_address());
		editor.putString("users_name",userinfo.getUSER_NAME());
		editor.putString("card_id",userinfo.getCard_id());
		editor.putString("is_homeowner",userinfo.getIs_homeowner());
		editor.putString(userinfo.USER_TEL1_CODE,userinfo.getUsers_tel1());
		editor.putString(userinfo.USER_TEL2_CODE,userinfo.getUsers_tel2());
		editor.putString(userinfo.USER_PWD,userinfo.getLogin_pwd());
		editor.putString(userinfo.USER_IMG_CODE,userinfo.getUSER_IMG());
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
}
