package com.ydwj.Service.BaoXiu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ydwj.MUtils.MUtils;
import com.ydwj.Ziliao;
import com.ydwj.bean.Baoxiu;
import com.ydwj.bean.Baoxiu_List;
import com.ydwj.community.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Act_AddBaoxiu extends AppCompatActivity {
    LinearLayout imagegroup;
    ImageView addbutton;
    Context context=this;

    RadioGroup radioGroup;
    RadioButton personbutton;
    int type;
    EditText xiaoqu;
    EditText dizhi;
    EditText num;
    EditText text;
    List<Bitmap> bitmaps=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_baoxiu);

        initToolbar();
        initView();
    }

    public void initToolbar(){
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.iconfont_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void initView(){
        imagegroup=(LinearLayout)findViewById(R.id.imagegroup);
        addbutton=(ImageView)findViewById(R.id.image);
        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) addbutton.getLayoutParams();
        layoutParams.width=(new MUtils(this).getWidth()/3)-40;
        addbutton.setLayoutParams(layoutParams);

        radioGroup=(RadioGroup)findViewById(R.id.radiogroup);
        xiaoqu=(EditText)findViewById(R.id.xiaoqu);
        dizhi=(EditText)findViewById(R.id.dizhi);
        num=(EditText)findViewById(R.id.num);
        text=(EditText)findViewById(R.id.text);
        personbutton=(RadioButton)findViewById(R.id.personweixiu);
    }

    public void baoxiu(View view) {
        if(checkDate()){
            Date date=new Date();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日 HH:mm ss");

            Baoxiu baoxiu=new Baoxiu();
            baoxiu.setXiaoqu(xiaoqu.getText().toString());
            baoxiu.setDizhi(dizhi.getText().toString());
            baoxiu.setNum(num.getText().toString());
            baoxiu.setType(type);
            baoxiu.setText(text.getText().toString());
            baoxiu.setDate(simpleDateFormat.format(date));
            baoxiu.setBitmaps(bitmaps);
            Ziliao.baoxius.add(baoxiu);

            Baoxiu_List baoxiu_list=new Baoxiu_List();
            baoxiu_list.setType(type);
            baoxiu_list.setLiuyan(0);
            baoxiu_list.setText(baoxiu.getText());
            baoxiu_list.setTitle(baoxiu.getDizhi());
            baoxiu_list.setBitmaps(bitmaps);
            baoxiu_list.setTime(simpleDateFormat.format(date));
            Ziliao.baoxiu_lists.add(baoxiu_list);
            Toast.makeText(context,"提交成功",Toast.LENGTH_SHORT).show();
            finish();

        }else{
            Toast.makeText(context,"信息填写不全",Toast.LENGTH_SHORT).show();
        }

    }

    public boolean checkDate(){
        if(personbutton.isChecked()){
            type=1;
        }else{
            type=0;
        }
        if(xiaoqu.getText().length()<1){
            return false;
        }
        if(dizhi.getText().length()<1){
            return false;
        }
        if(num.getText().length()<1){
            return false;
        }
        if(text.getText().length()<1){
            return false;
        }
        return true;
    }
    public void addimage(View view) {
        getBycamera();
    }

    int TAKE=1;
    int RETAKE=2;
    public void getBycamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, TAKE);
    }
    public void getBycamera(int type){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, type);
    }
    public void checkImage(){
        if(imagegroup.getChildCount()>3){
            addbutton.setVisibility(View.GONE);
        }else{
            addbutton.setVisibility(View.VISIBLE);
        }
    }

    AlertDialog alertDialog;
    ImageView editimageview;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            Bundle bundle=data.getExtras();
            final Bitmap bitmap= (Bitmap) bundle.get("data");

            if(requestCode == TAKE){
                bitmaps.add(bitmap);

                final ImageView imageView=new ImageView(this);
                imageView.setImageBitmap(bitmap);
                imageView.setBackgroundResource(R.drawable.input_backgroud);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) addbutton.getLayoutParams();
                layoutParams.setMargins(5,0,5,0);
                imageView.setLayoutParams(layoutParams);
                imagegroup.addView(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View view= LayoutInflater.from(context).inflate(R.layout.choise_image,null);
                        LinearLayout takephoto=(LinearLayout)view.findViewById(R.id.takephoto);
                        LinearLayout delete=(LinearLayout)view.findViewById(R.id.delete);

                        View.OnClickListener myonclick=new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (v.getId()){
                                    case R.id.takephoto:
                                        editimageview=imageView;
                                        getBycamera(RETAKE);
                                        alertDialog.dismiss();
                                        break;
                                    case R.id.delete:
                                        bitmaps.remove(bitmap);
                                        imagegroup.removeView(imageView);
                                        alertDialog.dismiss();
                                        checkImage();
                                        break;
                                }
                            }
                        };

                        takephoto.setOnClickListener(myonclick);
                        delete.setOnClickListener(myonclick);

                        alertDialog=new AlertDialog.Builder(context)
                                .setView(view)
                                .show();
                    }
                });
            }else if(requestCode == RETAKE){
                editimageview.setImageBitmap(bitmap);
            }
            checkImage();
        }

    }
}
