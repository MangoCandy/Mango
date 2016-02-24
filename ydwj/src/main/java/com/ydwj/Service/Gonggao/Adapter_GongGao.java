package com.ydwj.Service.Gonggao;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ydwj.community.R;

/**
 * Created by Administrator on 2016/2/24.
 */
public class Adapter_GongGao extends BaseAdapter{
    String[] titles={"长沙开展禁燃区燃煤制作销售专项联合执法行动......"};
    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder{
        TextView title;
        TextView time;
        TextView text;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_gonggao,null);
            viewHolder.title=(TextView) convertView.findViewById(R.id.title);
            viewHolder.time=(TextView) convertView.findViewById(R.id.time);
            viewHolder.text=(TextView) convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(titles[position]);
        return convertView;
    }
}
