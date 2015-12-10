package com.ydwj.Comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ydwj.bean.CommentBean;
import com.ydwj.News.Utils;
import com.ydwj.community.R;


import java.util.List;

/**
 * Created by MangoCandy on 2015/10/19.
 */
public class ComAdapter extends BaseAdapter {
    List<CommentBean> commentBeans;
    Context context;
    CommentBean bean;
    Utils utils;
    public ComAdapter(List<CommentBean> commentBeans,Context context){
        this.commentBeans=commentBeans;
        utils=new Utils(context);
        this.context=context;
    }

    @Override
    public int getCount() {
        return commentBeans.size();
    }

    @Override
    public CommentBean getItem(int position) {
        return commentBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{
        TextView text;
        TextView name;
        TextView date;
        ImageView img;
    }
    String txt;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            LayoutInflater inflater=LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.single_comment, null);
            viewHolder.text=(TextView)convertView.findViewById(R.id.show_comText);
            viewHolder.name=(TextView)convertView.findViewById(R.id.show_comName);
            viewHolder.date=(TextView)convertView.findViewById(R.id.show_comDate);
            viewHolder.img=(ImageView)convertView.findViewById(R.id.com_img);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        bean=getItem(position);
        viewHolder.text.setText(bean.getText());
        viewHolder.date.setText(bean.getDate());
        viewHolder.name.setText(bean.getName());
        viewHolder.img.setImageBitmap(utils.String2Bitmap(bean.getImg()));
        Glide.with(context).load(bean.getImg()).into(viewHolder.img);
        return convertView;
    }
}
