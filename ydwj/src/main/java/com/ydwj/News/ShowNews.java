package com.ydwj.News;

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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    float currentX=0;
    float currentY=0;
    FloatingActionButton share;
    ProgressBar progressBar;

    File file;
    TextView goback;
    ImageView cut_screen;
    ImageView src_share;
    GestureDetector gestureDetector;
    //初始化手势
    public void initGes(){
        gestureDetector=new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(Math.abs(velocityX)<30){
                    return true;
                }
                if(Math.abs(e1.getRawY()-e2.getRawY())<50){
                    if(Math.abs(e1.getRawX()-e2.getRawX())>100){
                        finish();
                        return true;
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGes();
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
//                    getBitmap();
                    break;
            }
        }
    };
    public void gotoComment(){
        Utils utils=new Utils(this);
        if((utils.getUserinfo().getUSER_NAME()).equals("")){
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

        webView.loadUrl(url);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
//        this.registerForContextMenu(webView);
        webView.setOnLongClickListener(onLongClickListener);
        webView.setOnTouchListener(onTouchListener);
    }
    android.support.v7.app.ActionBar actionBar;
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
//        actionBar.setCustomView(R.layout.action_bar_news);
//        goback=(TextView)actionBar.getCustomView().findViewById(R.id.news_back);
//        goback.setOnClickListener(onClickListener);
//        cut_screen=(ImageView)actionBar.getCustomView().findViewById(R.id.news_screen);
//        cut_screen.setOnClickListener(onClickListener);
//        src_share=(ImageView)actionBar.getCustomView().findViewById(R.id.src_share);
//        src_share.setOnClickListener(onClickListener);

        fullscreen=(RelativeLayout) findViewById(R.id.fullscreen);
    }
    Context context=this;

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
//            gestureDetector.onTouchEvent(event);
            return false;
        }
    };

    //长按保存图片
    private String imgurl = "";
    View.OnLongClickListener onLongClickListener=new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (v instanceof WebView) {
                WebView.HitTestResult result = ((WebView) v).getHitTestResult();
                if (result != null) {
                    int type = result.getType();
                    if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                        imgurl = result.getExtra();
                        initSnack("保存图片？", "保存", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new SaveImage().execute();
                            }
                        });
                    }
                }
            }
            return false;
        }
    };
    public void initSnack(String info,String actiontext,View.OnClickListener onClickListener){
        Snackbar snackbar=Snackbar.make(webView,info,Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);
        snackbar.setAction(actiontext,onClickListener).show();
    }
//
private class SaveImage extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        String result = "";
        try {
            String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
            file = new File(sdcard + "/MangoWe/Download");
            if (!file.exists()) {
                file.mkdirs();
            }
            String imgurl2="";
            int idx = imgurl.lastIndexOf("fmt=");
            imgurl2=imgurl.substring(0,imgurl.lastIndexOf("&"));
            int endx=imgurl.lastIndexOf("&");
            String ext = imgurl.substring(idx + 4, imgurl2.lastIndexOf("&tp"));
            if(ext==null){
                ext="jpg";
            }
            file = new File(sdcard + "/MangoWe/Download/" + new Date().getTime() + "."+ext);
            InputStream inputStream = null;
            URL url = new URL(imgurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(20000);
            if (conn.getResponseCode() == 200) {
                inputStream = conn.getInputStream();
            }
            byte[] buffer = new byte[400];
            int len = 0;
            FileOutputStream outStream = new FileOutputStream(file);
            while ((len = inputStream.read(buffer)) != -1) {
                outStream.write(buffer,0,len);
            }
            outStream.close();
            result = "图片已保存至：" + file.getAbsolutePath();
        } catch (Exception e) {
            result = "保存失败！" + imgurl;
        }
        freashMediaStore(Uri.fromFile(file));
        return result;
    }
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
//            MsgBox("提示", result);
        }
    }
    //        刷新媒体库
    public void freashMediaStore(Uri uri){
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
    }
    //截图
    public  Bitmap getBitmap() {
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        file = new File(sdcard + "/MangoWe/Snapshot");
        if (!file.exists()) {
            file.mkdirs();
        }
        int h = webView.getHeight();
        Bitmap bitmap = null;
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(webView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        bitmap=webView.getDrawingCache();
        String img_url=sdcard + "/MangoWe/Snapshot/"+new Date().getTime()+".jpg";
        file=new File(img_url);
        // 测试输出
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (null != out) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

                freashMediaStore(Uri.fromFile(file));
            }
        } catch (IOException e) {
            // TODO: handle exception
        }
        return bitmap;
    }
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
