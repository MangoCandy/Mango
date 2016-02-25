package com.ydwj.alarm;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ydwj.Login.Login;
import com.ydwj.MUtils.MUtils;
import com.ydwj.News.Utils;
import com.ydwj.bean.Contacts;
import com.ydwj.bean.Userinfo;
import com.ydwj.community.R;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frg_alarm extends Fragment {
    ImageView btn_alarm;
    ImageView circle_ready0;
    View view;
    Context context;
    RelativeLayout gotocontacts;//修改信息
    ImageView img_anim;
    Utils utils;
    Userinfo userinfo;
    TextView toptext;//顶部字
    Utils_Contacts uc;
    List<Contacts> noup_contactses;//未上传或修改联系人
    public Frg_alarm() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        utils=new Utils(context);
        userinfo=utils.getUserinfo();
        uc=new Utils_Contacts(context);
        initDb();
    }

    //短信相关
    public void initBroad(){
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context,
                                "短信发送成功", Toast.LENGTH_SHORT)
                                .show();
                        if(edit_send!=null){
                            edit_send.setText("");
                            layout_send.setVisibility(View.GONE);
                            layout_caontacts.setVisibility(View.VISIBLE);
                        }
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context,
                                "短信发送失败", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context,
                                "短信发送失败，请检查手机是否开启飞行模式或者已欠费", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context,
                                "短信发送失败", Toast.LENGTH_SHORT)
                                .show();
                        break;
                }
            }
        }, new IntentFilter("SENT_SMS_ACTION"));
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context,
                                "短信接收成功", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case Activity.RESULT_CANCELED:

                        break;
                }
            }
        }, new IntentFilter("DELIVERED_SMS_ACTION"));
    }


    public void initView(){
        img_anim=(ImageView)view.findViewById(R.id.img_anim);
        img_anim.setAnimation(AnimationUtils.loadAnimation(context, R.anim.alarm_anim));
        //报警按钮
        btn_alarm=(ImageView)view.findViewById(R.id.btn_if_alarm);
//        circle_ready0=(ImageView)view.findViewById(R.id.ready0);
        btn_alarm.setOnLongClickListener(onLongClickListener);
        gotocontacts=(RelativeLayout)view.findViewById(R.id.go_to_editcontacts);
        gotocontacts.setOnClickListener(onClickListener);
        //顶部提醒
        topLayout= (RelativeLayout) view.findViewById(R.id.toplayout);
        topLayout.setOnClickListener(onClickListener);
        toptext=(TextView)view.findViewById(R.id.toptext);
    }
    View.OnLongClickListener onLongClickListener=new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()){
                case R.id.btn_if_alarm:
                    if(islogin){
//                        sendMsg(0,"");
                        initcontacts_pop();
                    }else{
                        gotologin();
                    }
                    break;
            }
            return false;
        }
    };
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    size--;
                    toptext.setText("还有"+size+"位联系人未同步");
                    if(size<=0){
                        askdialog.dismiss();
                        checknoup();
                    }
                    break;
                case 1:
                    askdialog.dismiss();
                    break;
            }
        }
    };
    //初始化popupwindow显示联系人
    FloatingActionButton dismiss_cpop;
    PopupWindow contacts_pop;
    View cview;//popupwindow view
    List<Contacts> contactses;//联系人对象列表
    ListView showContacts;//显示联系人
    RelativeLayout layout_send;//发送框布局
    RelativeLayout layout_caontacts;//显示联系人布局
    FloatingActionButton send_all;
    EditText edit_send;//短信编辑框
    Button btn_send;//发送按钮
    int SEND_MODE_ALL=0;//群发模式
    int SEND_MODE_SINGLE=1;//单人模式
    int SEND_MODE=-1;

    public void initcontacts_pop(){
        contactses=getlist();
        if(contacts_pop==null){
            cview=LayoutInflater.from(context).inflate(R.layout.pop_contacts,null);//整个pop view
            dismiss_cpop=(FloatingActionButton)cview.findViewById(R.id.btn_dismiss_cpop);//dismiss按钮
            dismiss_cpop.setOnClickListener(onClickListener);

            contacts_pop=new PopupWindow(cview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);

            showContacts=(ListView)cview.findViewById(R.id.pop_showcontact);

            contacts_pop.setAnimationStyle(R.style.popwin_alarm_style);
            layout_caontacts=(RelativeLayout)cview.findViewById(R.id.layout_showcontacts);

            cview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(layout_send!=null&&layout_send.isShown()){
//                        dismissInput();
                        layout_send.setVisibility(View.GONE);
                        layout_caontacts.setVisibility(View.VISIBLE);
                        contacts_pop.dismiss();
                        contacts_pop.showAtLocation(view,Gravity.CENTER_VERTICAL,0,0);
                    }
                    return false;
                }
            });
        }
        showContacts.setAdapter(new Adapter_contacts(contactses,context));

        cview.setAnimation(AnimationUtils.loadAnimation(context, R.anim.alpha_add));

//        contacts_pop.showAtLocation(getView(), Gravity.CENTER, 0, 0);
        contacts_pop.setHeight((new MUtils(context).getHeight())*9/11);
        contacts_pop.showAtLocation(view,Gravity.BOTTOM,0,0);
        btn_alarm.setAnimation(AnimationUtils.loadAnimation(context, R.anim.pop_contacts));
    }
    public void dismissInput(){//收起键盘
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,imm.HIDE_NOT_ALWAYS);

    }
    //获取联系人
    public List<Contacts> getlist(){
        try {
            contactses=dbUtils.findAll(Contacts.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return contactses;
    }

    int size;//同步至服务器人数数量
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_dismiss_cpop://隐藏拨号界面
                    contacts_pop.dismiss();
                    break;
                case R.id.btn_send://群发按钮
                    if(!edit_send.getText().toString().equals("")){
                        if(SEND_MODE==SEND_MODE_ALL){
                            sendMsg(0,edit_send.getText().toString());
                        }
                    }else{
                        Toast.makeText(context,"信息不能为空",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.go_to_editcontacts://跳转联系人界面
                    if(islogin){
                        Intent intent=new Intent(context,Act_contacts_msg.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.top_to_bo,R.anim.scale_small);
                    }else{
                        gotologin();
                    }
                    break;
                case R.id.toplayout://顶部通知 未登录跳转登录 登录提示联系人位未 同步
                    if(utils.isLogin()){
//                        topLayout.setVisibility(View.GONE);
                        size=noup_contactses.size();
                        isAsking();
                        for(Contacts contacts:noup_contactses){
                            if(contacts.getCID()==null||contacts.getCID().equals("-1")){
                                uc.Add_contacts(contacts,handler);

                            }else{
                                uc.Update_contacts(contacts,handler);
                            }
                        }
                    }else{
                        gotologin();
                    }
                    break;
            }
        }
    };

    //发送短信
    public void sendMsg(int num,String msg){
        Intent sentIntent = new Intent("SENT_SMS_ACTION");
        Intent deliverIntent = new Intent("DELIVERED_SMS_ACTION");
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentIntent, 0);
        PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0,
                deliverIntent, 0);
        String content=null;
        initBroad();
        if(msg.equals("")){
            content = "紧急按钮启动 有危险情况  by-智慧社区";//短信内容
        }else{
            content=msg;
        }
        for(Contacts contacts:getlist()){
            String phone = contacts.getCONTACT_NUM()+"";//电话号码
            android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
            java.util.List<String> texts = smsManager.divideMessage(content);
//发送之前检查短信内容是否为空
            for (String text :texts) {
                smsManager.sendTextMessage(phone, null, text, sentPI, deliverPI);
            }
        }
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
    //判断是否登录
    boolean islogin=false;
    public void checkisLogin(){
        userinfo=utils.getUserinfo();
        if(userinfo.getID()==null||userinfo.getID().equals("")){
            islogin=false;
            toptext.setText("请先登录");
            topLayout.setVisibility(View.VISIBLE);
        }else{
            islogin=true;
            topLayout.setVisibility(View.GONE);
        }
    }
    public void gotologin(){
        Intent intent=new Intent(context, Login.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkisLogin();
        checknoup();
    }
    public void checknoup(){
        //检测未上传联系人
        noup_contactses=uc.check_no_update();
        if(noup_contactses!=null){
            if(islogin&&noup_contactses.size()>0){
                topLayout.setVisibility(View.VISIBLE);
                topLayout.setBackgroundColor(Color.RED);
                toptext.setText("目前有"+noup_contactses.size()+"位联系人更新未同步至服务器");
            }else if(islogin&&noup_contactses.size()==0){
                topLayout.setVisibility(View.GONE);
            }
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            checkisLogin();
            checknoup();
        }
    }

    RelativeLayout topLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_frg_alarm, container, false);
        initView();
        return view;
    }

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
