package com.ydwj.News;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.core.BitmapSize;
import com.ydwj.bean.TitleInfo;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;
import com.ydwj.community.R;
/**
 * Created by MangoCandy on 2015/9/21.
 */
public class TitleAdapter extends BaseAdapter {
    List<TitleInfo> titleInfos;
    Context context;
    TitleInfo titleInfo;
    Bitmap bitmap=null;
    ImageRequest imageRequest=null;
    RequestQueue requestQueue=null;
    BitmapUtils utils;
    public TitleAdapter(List<TitleInfo> titleInfos,Context context){
        this.titleInfos=titleInfos;
        this.context=context;
        requestQueue= Volley.newRequestQueue(context);
    }
    @Override
    public int getCount() {
        return titleInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return titleInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder{
        TextView title;
        TextView name;
        ImageView img1;
        TextView url;
    }

    public Bitmap getBitmap(String url){
        if(url!=null){
            imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    bitmap=response;
                }
            }, 200, 200, ImageView.ScaleType.MATRIX, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(imageRequest);
            requestQueue.start();
        }
        return bitmap;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            LayoutInflater inflater=LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.single_title, null);
            viewHolder.img1=(ImageView)convertView.findViewById(R.id.show_title_img);
            viewHolder.title=(TextView)convertView.findViewById(R.id.show_title);
            viewHolder.name=(TextView)convertView.findViewById(R.id.show_name);
            viewHolder.url=(TextView)convertView.findViewById(R.id.show_url);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        titleInfo=(TitleInfo) getItem(position);
        if(titleInfo!=null&&titleInfo.getContentimg()!=null){
            Glide.with(context).load(titleInfo.getContentimg()).error(R.color.colorPrimary).into(viewHolder.img1);
        }
        viewHolder.title.setText(titleInfo.getTitle());
        viewHolder.name.setText(titleInfo.getUsername());
        viewHolder.url.setText(titleInfo.getUrl());
//        convertView.setAnimation(AnimationUtils.loadAnimation(context,R.anim.alpha_add_news));
        return convertView;
    }
}
