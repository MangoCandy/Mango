package com.ydwj.Utils;

import android.content.Context;
import android.widget.Toast;

import com.ydwj.News.Utils;

/**
 * Created by Administrator on 2015/12/2.
 */
public class Utils_user {
    Context context;
    public Utils_user(Context context){
        this.context =context;
    }
    public boolean checkEmail(String email){
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        if(email.matches(str)){
            return true;
        }else{
            Toast.makeText(context,"邮箱格式错误",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    //check号码
    public boolean checknum(String num){
        String telRegex = "[1][358]\\d{9}";
        if(num.matches(telRegex)){
            return true;
        }else{
            Toast.makeText(context,"请输入正确手机号码",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    //用户名检查
    public boolean checkname(String name){
        if(name==null||name.contains(" ")){
            Toast.makeText(context,"昵称不能为空或包含空格",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }
    //身份证验证
    public boolean checkid(String idt){
        if(idt==null){
            return true;
        }
        else{
            String idRegex="(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";
            if(idt.matches(idRegex)){

                return true;
            }else{
                Toast.makeText(context,"请输入正确身份证号码",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }
}
