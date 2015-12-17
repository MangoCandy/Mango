package com.ydwj.Service;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Administrator on 2015/12/17.
 */
public class Service_Download extends Service {
    String url;
    String path;
    File file;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)  {
        url=intent.getExtras().getString("url");
        path=intent.getExtras().getString("path");

        startdownload();
        IntentFilter filter=new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getExtras().getLong(DownloadManager.EXTRA_DOWNLOAD_ID)==id){
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    Uri downloadFileUri = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                        downloadFileUri = downloadManager.getUriForDownloadedFile(id);
                        install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");

                        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(install);
                        stopService(new Intent(context,Service_Download.class));
                    }
                }
            }
        };
        registerReceiver(broadcastReceiver,filter);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    Long id;
    DownloadManager downloadManager=null;
    protected void startdownload(){
        file=new File(Environment.getExternalStorageDirectory().getPath()+"/ydwj/Apk");
        if(!file.exists()){
            file.mkdirs();
        }
        file=new File(file.getAbsoluteFile()+"/智慧社区.apk");
        if(downloadManager==null){
            downloadManager=(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        }
        Uri uri=Uri.parse(url);
//        Uri uri=Uri.parse("http://api.gfan.com/market/api/apk?type=WAP&cid=99&uid=-1&pid=CNxraEHB/54w1sz/058EAQ==&sid=x69B3MmOtYHz3gPmEXsXTw==");
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setDescription("正在更新...");
        request.setTitle("智慧社区");
        request.setDestinationUri(Uri.fromFile(file));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }

//        request.setDestinationInExternalFilesDir(this,file.getAbsolutePath(),"asd.apk");

//        try {
//            id=downloadManager.enqueue(request);
//        }catch (SecurityException e){
//
//        }
        id=downloadManager.enqueue(request);
    }


}
