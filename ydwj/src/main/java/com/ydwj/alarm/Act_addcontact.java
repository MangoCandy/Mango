package com.ydwj.alarm;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ydwj.News.Utils;
import com.ydwj.bean.Contacts;
import com.ydwj.bean.Userinfo;
import com.ydwj.community.R;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//联系人添加页面
public class Act_addcontact extends AppCompatActivity {
    EditText edit_name;
    EditText edit_num;
    EditText edit_beizhu;
    String num;
    String name;
    String beizhu;
    Button button;//确认按钮
    Context context;
    ListView show_contactses;//显示本地联系人
    List<Contacts> contactses;
    Utils utils=new Utils(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_addcontact);
        initActionBar();
        initDb();
        initView();
        context=this;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initActionBar(){
        android.support.v7.widget.Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.iconfont_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add_local_contacts:
                        showCts();
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_addcontacts,menu);
        return true;
    }

    public void initView(){
        edit_name=(EditText)findViewById(R.id.input_name);
        edit_num=(EditText)findViewById(R.id.input_num);
        edit_beizhu=(EditText)findViewById(R.id.input_beizhu);
        button=(Button)findViewById(R.id.btn_confirm);
        button.setOnClickListener(onClickListener);
        //弹窗显示联系人
        show_contactses=new ListView(this);
    }
    DbUtils dbUtils;
    DbUtils.DaoConfig config;
    public void initDb(){
        config=new DbUtils.DaoConfig(this);
        config.setDbName("ydwj");
        config.setDbVersion(1);
        dbUtils=DbUtils.create(config);
        try {
            dbUtils.createTableIfNotExist(Contacts.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_confirm:
                    if(checkinfo()&&!check_num_exist()){
                        save_contact();
                    }
                    break;
            }
        }
    };
//    保存信息
    Contacts contacts;
    public void save_contact(){
        String BEIZHU="";
        if((edit_beizhu.getText().toString().equals(""))){
            BEIZHU="无备注";
        }else{
            BEIZHU=edit_beizhu.getText().toString();
        }
        contacts=new Contacts();
        contacts.setCONTACT_NAME(edit_name.getText().toString());
        contacts.setCONTACT_NUM(edit_num.getText().toString());
        contacts.setBEIZHU(BEIZHU);
        contacts.setCID("-1");
        Utils_Contacts uc=new Utils_Contacts(context);
        if(!check_num_exist()){
            uc.Add_contacts(contacts,null);
        }

    }
    //检查数据库是否存在某号码
    public boolean check_num_exist(){
        try {
            List<Contacts> contactses=
            dbUtils.findAll(com.lidroid.xutils.db.sqlite.Selector.from(Contacts.class).where("CONTACT_NUM","=",edit_num.getText().toString()));
            if(contactses.size()>0){
                Toast.makeText(this,"号码已存在",Toast.LENGTH_SHORT).show();
                return true;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    //
//检查填写信息
    public boolean checkinfo(){
        if(TextUtils.isEmpty(edit_name.getText())){
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.indexOf(edit_name.getText().toString()," ")>=0){
            Toast.makeText(this,"姓名不能为包含空格",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.indexOf(edit_num.getText().toString()," ")>=0){
            Toast.makeText(this,"号码不能为包含空格",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(edit_num.getText())){
            Toast.makeText(this,"号码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    //获取本地联系人
    Adapter_show_contacts adapter;
    public void addContactstoview(){
        if(adapter==null){
            contactses=new ArrayList<Contacts>();
            adapter=new Adapter_show_contacts(contactses,context);
            show_contactses.setAdapter(adapter);
        }
        Thread thread=new Thread(runnable);
        thread.start();;
    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            getContacts();
        }
    };

// 获取手机联系人
    private static final String[] PHONES_PROJECTION = new String[] {
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Contacts.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID };
    /**联系人显示名称**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**电话号码**/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**头像ID**/
    private static final int PHONES_PHOTO_ID_INDEX = 2;

    /**联系人的ID**/
    private static final int PHONES_CONTACT_ID_INDEX = 3;
    public void getContacts(){
        contactses.clear();
        ContentResolver resolver = context.getContentResolver();
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,PHONES_PROJECTION, null, null, ContactsContract.CommonDataKinds.Phone.SORT_KEY_ALTERNATIVE);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                //得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;

                //得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                //得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

                //得到联系人头像ID
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

                //得到联系人头像Bitamp
                Bitmap contactPhoto = null;
                Contacts contacts=new Contacts();
                contacts.setCONTACT_NAME(contactName);
                contacts.setCONTACT_NUM(phoneNumber);
                contacts.setBEIZHU(null);
                contactses.add(contacts);
                handler.sendEmptyMessage(0);
            }
        }
        phoneCursor.close();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    //显示dialog 联系人
    AlertDialog.Builder builder=null;
    AlertDialog alertDialog=null;
    public void showCts(){
        show_contactses=new ListView(this);
        show_contactses.setAdapter(adapter);
        show_contactses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edit_name.setText(contactses.get(position).getCONTACT_NAME());
                edit_num.setText(contactses.get(position).getCONTACT_NUM());
                alertDialog.dismiss();
            }
        });
        builder =new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(show_contactses)
                .setTitle("选择本地联系人")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
        addContactstoview();
        alertDialog=builder.show();
    }
}
