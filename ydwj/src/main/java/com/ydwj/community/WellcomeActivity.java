package com.ydwj.community;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.ydwj.Login.Login;
import com.ydwj.News.Utils;
import com.ydwj.bean.MyApplication;
import com.ydwj.bean.Userinfo;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WellcomeActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;


    private boolean mVisible;
    Context context=this;
    Utils utils=new Utils(context);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_wellcome);
        MyApplication.addActivity(this);
        mVisible = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                login();
            }
        },1500);

    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:

                    jump();
                    break;
                case 1:
                    Bundle bundle=msg.getData();
                    Toast.makeText(context,bundle.getString("res"),Toast.LENGTH_SHORT).show();
                    if(bundle.getBoolean("login")){
                        jump();
                    }else{
                        jump();
                        Intent intent=new Intent(context, Login.class);
                        startActivity(intent);
                        finish();
                        }
                    break;
                case 2:
                    Toast.makeText(context,"登陆失败,稍后请自行登录",Toast.LENGTH_SHORT).show();
                    jump();
                    break;
            }
        }
    };
    public void jump(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.alpha_add,R.anim.alpha_lose);
        this.finish();
    }
    //在此静默登录 在需要登录操作的地方调用 utils.islogin 判断是否登录 未登录则跳转至登陆界面
    public void login(){
        Userinfo userinfo=utils.getUserinfo();
        if(userinfo.getID()!=null&&!userinfo.getID().equals("")){
            utils.login(handler,userinfo.getLogin_name(),userinfo.getLogin_pwd());
        }else{
            jump();
        }

    }
}
