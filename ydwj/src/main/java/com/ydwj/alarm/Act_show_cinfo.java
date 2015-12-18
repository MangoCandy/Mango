package com.ydwj.alarm;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.ydwj.News.Utils;
import com.ydwj.bean.Contacts;
import com.ydwj.community.R;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Act_show_cinfo extends AppCompatActivity {
    String num;
    Contacts contacts;
    String name;
    String cid;
    String beizhu;
    TextView view_name;
    TextView view_num;
    TextView view_beizhu;
    Button delete;
    Context context = this;
    LinearLayout layout_num;
    LinearLayout layout_beizhu;
    LinearLayout layout_confirm;//确认
    LinearLayout layout_delete;//删除

    LinearLayout tools;//电话 短信
    EditText edit_num;
    EditText edit_beizhu;
    EditText edit_name;
    Button btn_cancel;
    Button btn_confirm;
    ImageView btn_call;
    ImageView btn_msg;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_show_cinfo);
        initToolbar();
        Intent intent = getIntent();
        num = intent.getExtras().getString("number");
        initDb();
        findNum();
        initView();
        setInfo();
    }

    public void initToolbar(){
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.iconfont_back);
        setSupportActionBar(toolbar);
        toolbar.setTitle(null);
        toolbar.setTitleTextColor(Color.TRANSPARENT);
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
                    case R.id.edit_local_contacts:
                        isEdit(true);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editcontacts,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void initView() {
        view_name = (TextView) findViewById(R.id.show_name);
        view_num = (TextView) findViewById(R.id.show_num);
        view_num.setOnClickListener(onClickListener);

        tools=(LinearLayout)findViewById(R.id.tools);

        view_beizhu = (TextView) findViewById(R.id.show_beizhu);
        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(onClickListener);

        edit_num = (EditText) findViewById(R.id.edit_num);
        edit_beizhu = (EditText) findViewById(R.id.edit_beizhu);

        layout_num = (LinearLayout) findViewById(R.id.btn_edit_num);
        layout_num.setOnClickListener(onClickListener);

        layout_beizhu = (LinearLayout) findViewById(R.id.btn_edit_beizhu);
        layout_beizhu.setOnClickListener(onClickListener);

        layout_confirm = (LinearLayout) findViewById(R.id.layout_confirm);
        layout_delete = (LinearLayout) findViewById(R.id.layout_delete);

        btn_cancel = (Button) layout_confirm.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(onClickListener);

        btn_confirm = (Button) layout_confirm.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(onClickListener);

        view_name.setOnClickListener(onClickListener);
        edit_name = (EditText) findViewById(R.id.edit_name);

        btn_call = (ImageView) findViewById(R.id.btn_call);
        btn_call.setOnClickListener(onClickListener);

        btn_msg=(ImageView)findViewById(R.id.btn_msg);
        btn_msg.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_call:
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + contacts.getCONTACT_NUM()));
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);
                    break;
                case R.id.btn_msg:
                    Intent msgintent=new Intent(Intent.ACTION_SENDTO);
                    msgintent.setData(Uri.parse("smsto:"+contacts.getCONTACT_NUM()));
                    startActivity(msgintent);
                    break;
                case R.id.show_num:
                    isEdit(true);
                    break;
                case R.id.delete:
                    delete();
                    break;
                case R.id.btn_edit_num:
                    isEdit(true);
                    break;
                case R.id.btn_edit_beizhu:
                    isEdit(true);

                    break;
                case R.id.btn_cancel:
                    isEdit(false);
                    break;
                case R.id.show_name:
                    isEdit(true);
                    break;
                case R.id.btn_confirm:
                    Utils_Contacts uc=new Utils_Contacts(context);
                    if(checkinfo()){
                       if(!uc.check_num_exist_byTel(contacts)){
                           isEdit(false);
                           try {
                               editContact();
                               contacts.setIsUpdate("-1");
                               dbUtils.update(contacts);
                               if(contacts.getCID().equals("-1")){
                                   uc.Add_contacts(contacts,null);
                               }else{
                                   uc.Update_contacts(contacts,null);
                               }
                               setInfo();
                           } catch (DbException e) {
                               e.printStackTrace();
                           }
                       }else{
                           Toast.makeText(context,"号码已存在",Toast.LENGTH_SHORT).show();
                       }
                    }
                    break;
            }
        }
    };
    public void editContact(){
        contacts.setBEIZHU(edit_beizhu.getText().toString());
        contacts.setCONTACT_NUM(edit_num.getText().toString());
        contacts.setCONTACT_NAME(edit_name.getText().toString());
    }
    public void isEdit(boolean o){
        if(!o){
            //确认修改按钮栏
            layout_confirm.setVisibility(View.GONE);
            layout_delete.setVisibility(View.VISIBLE);
            //修改号码备注姓名
            view_num.setVisibility(View.VISIBLE);
            edit_num.setVisibility(View.GONE);

            view_beizhu.setVisibility(View.VISIBLE);
            edit_beizhu.setVisibility(View.GONE);

            view_name.setVisibility(View.VISIBLE);
            edit_name.setVisibility(View.GONE);
            //拨号按钮显示
            tools.setVisibility(View.VISIBLE);
        }else{
            //确认修改按钮栏
            layout_confirm.setVisibility(View.VISIBLE);
            layout_delete.setVisibility(View.GONE);
            //修改号码备注姓名
            view_num.setVisibility(View.GONE);
            edit_num.setVisibility(View.VISIBLE);

            view_beizhu.setVisibility(View.GONE);
            edit_beizhu.setVisibility(View.VISIBLE);

            view_name.setVisibility(View.GONE);
            edit_name.setVisibility(View.VISIBLE);
            //添加信息
            edit_name.setText(contacts.getCONTACT_NAME());
            edit_num.setText(contacts.getCONTACT_NUM());
            edit_beizhu.setText(contacts.getBEIZHU());
            //拨号按钮隐藏
            tools.setVisibility(View.GONE);
        }
    }
    public void delete(){
        Snackbar snackbar=Snackbar.make(view_num,"确认删除？",Snackbar.LENGTH_LONG);
        snackbar.setAction("删除", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dbUtils.delete(Contacts.class, WhereBuilder.b("ID", "=", contacts.getID()));
                    Utils_Contacts uc=new Utils_Contacts(context);
                    uc.Delete_contacts(contacts);
                    finish();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
            }
        });
        snackbar.show();
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
    Utils utils=new Utils(this);


    //从数据库找号码
    public void findNum(){
        try {
            List<Contacts> contactses=dbUtils.findAll(Selector.from(Contacts.class).where("CONTACT_NUM","=", num+""));
            if(contactses.size()<1){
                Toast.makeText(this,"号码不存在",Toast.LENGTH_SHORT).show();
                finish();
            }
            contacts=contactses.get(0);
            name=contacts.getCONTACT_NAME();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
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
    public void setInfo(){
        view_name.setText(contacts.getCONTACT_NAME());
        view_num.setText(contacts.getCONTACT_NUM());
        view_beizhu.setText(contacts.getBEIZHU());
    }
}
