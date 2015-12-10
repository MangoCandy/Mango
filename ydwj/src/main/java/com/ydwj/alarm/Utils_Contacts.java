package com.ydwj.alarm;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.ydwj.News.Utils;
import com.ydwj.bean.Contacts;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MangoCandy on 2015/11/28.
 */
public class Utils_Contacts {
    Context context;
    Utils utils;
    List<Contacts> contactses;
    public Utils_Contacts(Context context){
        this.context=context;
        utils=new Utils(context);
    }
    public void Select_Contacts(final Handler handler){
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest=new StringRequest(Request.Method.POST, "http://app.cloud-hn.net/app/factory.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("asd", response);
                JSONObject jsonObject=null;
                try {
                    jsonObject=new JSONObject(response);
                    if(jsonObject.getString("retCode").equals("00")){
                        JSONArray jsonArray=jsonObject.getJSONArray("retData");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object=jsonArray.getJSONObject(i);
                            Contacts contacts=new Contacts();
                            contacts.setCID(object.getString("id"));
                            contacts.setBEIZHU(object.getString("person_beizhu"));
                            contacts.setCONTACT_NAME(object.getString("person_name"));
                            contacts.setCONTACT_NUM(object.getString("person_tel1"));
//                            本地列表不存在则保存
                            save(contacts);
                            if(context.getClass()==Act_contacts_msg.class){
                                ((Act_contacts_msg)context).get_info_from_db();
                            }
                        }

                        if(handler!=null){
                            handler.sendEmptyMessage(0);
                        }
                    }else{
                        Toast.makeText(context,jsonObject.getString("error_msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"请检查网络", Toast.LENGTH_SHORT).show();
                Log.i("asd", error+"");
                if(handler!=null){
                    handler.sendEmptyMessage(1);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("action","select_person");
                params.put("users_id",utils.getUserinfo().getID());
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
        requestQueue.start();
    }
    public boolean check_num_existBytel(String num){
        if(dbUtils==null){initDb();}
        try {
            List<Contacts> contactses=
                    dbUtils.findAll(com.lidroid.xutils.db.sqlite.Selector.from(Contacts.class).where("CONTACT_NUM","=",num));
            if(contactses.size()>0){

                return true;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }
    //新增联系人
    boolean isSuccess=false;
    public void Add_contacts(final Contacts cts, final Handler handler){
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest=new StringRequest(Request.Method.POST, "http://app.cloud-hn.net/app/factory.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //handler发送成功信息
                if(handler!=null){
                    handler.sendEmptyMessage(0);
                }

                Log.i("asd", response);
                JSONObject jsonObject=null;
                try {
                    jsonObject=new JSONObject(response);
                    if(jsonObject.getString("retCode").equals("00")){
                        cts.setCID(jsonObject.getString("ret_id"));
                        cts.setIsUpdate("0");

                    }else if(jsonObject.getString("retCode").equals("01")){
                        cts.setCID(jsonObject.getString("ret_id"));

                    }else{

                    }
                    //如果号码存在则修改
                    if(!check_num_existBytel(cts.getCONTACT_NUM())){
                        save(cts);
                    }else{
                        update(cts);
                    }

                    if(context.getClass()==Act_addcontact.class){
                        ((Activity)context).finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cts.setCID("-1");
                Log.i("asd", error+"");
                //如果号码已存在则修改 不然则保存
                if(!check_num_existBytel(cts.getCONTACT_NUM())){
                    save(cts);
                }else{
                    update(cts);
                }
                if(context.getClass()==Act_addcontact.class){
                    ((Activity)context).finish();
                }

                if(handler!=null){
                    handler.sendEmptyMessage(1);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("action","add_person");
                params.put("users_id",utils.getUserinfo().getID());
                params.put("person_name",cts.getCONTACT_NAME());
                params.put("person_tel1",cts.getCONTACT_NUM());
                params.put("person_beizhu",cts.getBEIZHU());
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
        requestQueue.start();
    }

    DbUtils dbUtils;
    DbUtils.DaoConfig config;
    public void initDb(){
        config=new DbUtils.DaoConfig(context);
        config.setDbName("ydwj");
        config.setDbVersion(1);
        dbUtils=DbUtils.create(config);
        try {
            dbUtils.createTableIfNotExist(Contacts.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    public void save(Contacts contacts){
        try {
            if(dbUtils==null){
                initDb();
            }
            if(!check_num_exist_byTel(contacts)){
                dbUtils.save(contacts);
            }else{
                dbUtils.update(contacts);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    public void update(Contacts contacts){
        try {
            if(dbUtils==null){
                initDb();
            }
            dbUtils.update(contacts);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    //清除联系人表
    public void clear_contacts(){
        if(dbUtils==null){
            initDb();
        }
        try {
            dbUtils.dropTable(Contacts.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    //删除联系人
    //删除数据库
    public void Delete_contacts(final Contacts contacts){
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        StringRequest request=new StringRequest(Request.Method.POST, "http://app.cloud-hn.net/app/factory.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("asd",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("asd",error+"");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("action","delete_person");
                params.put("users_id",utils.getUserinfo().getID());
                params.put("person_id",contacts.getCID());
                return params;

            }
        };
        requestQueue.add(request);
        requestQueue.start();
    }
//    检测本地数据库是否存在联系人
    public boolean check_num_exist(String CID){
        try {
            if(dbUtils==null){
                initDb();
            }
            List<Contacts> contactses=
                    dbUtils.findAll(com.lidroid.xutils.db.sqlite.Selector.from(Contacts.class).where("CID","=",CID));
            if(contactses!=null&&contactses.size()>0){

                return true;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean check_num_exist_byTel(Contacts contacts){
        try {
            if(dbUtils==null){
                initDb();
            }
            List<Contacts> contactses=
                    dbUtils.findAll(com.lidroid.xutils.db.sqlite.Selector.from(Contacts.class).where("CONTACT_NUM","=",contacts.getCONTACT_NUM()));
            if(contactses!=null&&contactses.size()>0){
                if(contactses.size()==1&&contactses.get(0).getID()==contacts.getID()){
                    return false;
                }
                return true;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }
    //修改联系人
    public void updateByDb(Contacts contacts){
        Contacts cts=contacts;
        if(!cts.getCID().equals("-1")){
            cts.setIsUpdate("-1");
        }
        update(cts);
    }
    public void Update_contacts(final Contacts contacts,final Handler handler){
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        StringRequest request=new StringRequest(Request.Method.POST, "http://app.cloud-hn.net/app/factory.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("asd",response);
                contacts.setIsUpdate("0");
                update(contacts);

                if(handler!=null){
                    handler.sendEmptyMessage(0);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                updateByDb(contacts);
                Log.i("asd",error+"");
                if(handler!=null){
                    handler.sendEmptyMessage(1);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("action","update_person");
                params.put("users_id",utils.getUserinfo().getID());
                params.put("person_id",contacts.getCID());
                params.put("person_beizhu",contacts.getBEIZHU());
                params.put("person_name",contacts.getCONTACT_NAME());
                params.put("person_tel1",contacts.getCONTACT_NUM());
                return params;

            }
        };
        requestQueue.add(request);
        requestQueue.start();
    }
    //检测未上传联系人
    public List<Contacts> check_no_update(){
        if(dbUtils==null){initDb();}
        List<Contacts> contactses=null;
        try {
            contactses=
                    dbUtils.findAll(com.lidroid.xutils.db.sqlite.Selector.from(Contacts.class).where("CID","=","-1").or("IS_UPDATE","=","-1"));
        } catch (DbException e) {
            e.printStackTrace();
        }
        return contactses;
    }
}
