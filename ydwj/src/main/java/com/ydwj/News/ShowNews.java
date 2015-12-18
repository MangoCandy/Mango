package com.ydwj.News;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ydwj.Comment.Comment;
import com.ydwj.Login.Login;
import com.ydwj.bean.MyApplication;
import com.ydwj.community.R;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class ShowNews extends AppCompatActivity {
    String url=null;
    String title=null;
    int id=-1;
    WebView webView=null;
    public ShowNews(){}
    FloatingActionButton share;
    ProgressBar progressBar;
    Context context=this;

    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);
        init();
        share= (FloatingActionButton) findViewById(R.id.share);
        share.setOnClickListener(onClickListener);
        MyApplication.addActivity(this);
        initActionBar();
        initProgress();
    }
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.share:
                    gotoComment();
                    break;
            }
        }
    };
    public void gotoComment(){
        Utils utils=new Utils(this);
        if(!utils.isLogin()){
            Intent intent=new Intent(this, Login.class);
            startActivity(intent);
        }else{
            Intent intent=new Intent(this, Comment.class);
            intent.putExtra("id",id);
            startActivity(intent);
        }
    }
    public void initProgress(){
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setMax(100);
    }

    public void init(){
        url= (String) getIntent().getExtras().get("url");
        Log.i("asd",url);
        title=(String) getIntent().getExtras().get("title");
        id=getIntent().getExtras().getInt("id");
        webView=(WebView)findViewById(R.id.show_news);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return false;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return super.shouldInterceptRequest(view, request);
            }
        });
        webView.setWebChromeClient(webChromeClient);
        webView.getSettings().setBlockNetworkImage(false);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.setDrawingCacheEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setSupportZoom(false);
        webView.setOnLongClickListener(onLongClickListener);
        webView.loadUrl(url);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
//        this.registerForContextMenu(webView);
        webView.setOnTouchListener(onTouchListener);
    }
    View.OnLongClickListener onLongClickListener=new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
//            if (v instanceof WebView) {
//                WebView.HitTestResult result = ((WebView) v).getHitTestResult();
//                if (result != null) {
//                    int type = result.getType();
//                    if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
//                        imgurl = result.getExtra();
//                        ImageView imageView=new ImageView(context);
//                        Glide.with(context).load(imgurl).into(imageView);
//                        PopupWindow popupWindow=new PopupWindow(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,true);
//                        popupWindow.showAtLocation(webView, Gravity.CENTER,0,0);
//                    }
//                }
//            }
            return true;
        }
    };
    public void initActionBar(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("智慧社区");
        toolbar.setNavigationIcon(R.mipmap.iconfont_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        fullscreen=(RelativeLayout) findViewById(R.id.fullscreen);
    }

    RelativeLayout fullscreen;

    WebChromeClient webChromeClient=new WebChromeClient() {
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            fullscreen.setLayoutParams(params);
            fullscreen.addView(view);
//
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
            fullscreen.removeAllViews();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        @Override
        public void onRequestFocus(WebView view) {
            super.onRequestFocus(view);
            view.requestFocus();
        }
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress < 100) {
                progressBar.setProgress(newProgress);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }


    };
    public void back(){
        onBackPressed();
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onResume() {
        super.onResume();
        if(webView!=null){
            webView.onResume();
        }
        MobclickAgent.onResume(this);
    }

    View.OnTouchListener onTouchListener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            return false;
        }
    };

    //长按保存图片
    private String imgurl = "";


    @Override
    protected void onPause() {
//        webView.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.onPause(); // 暂停网页中正在播放的视频
        }
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onDestroy() {
        ViewGroup viewGroup= (ViewGroup) webView.getParent();
        viewGroup.removeView(webView);
        webView.clearCache(true);
        webView.loadUrl("about:blank");
        webView.destroy();
        super.onDestroy();
    }
}
