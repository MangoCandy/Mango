package com.ydwj.Service.CanYin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ydwj.bean.Market;
import com.ydwj.community.R;

import java.util.List;

/**
 * Created by Administrator on 2016/3/1.
 */
public class Adapter_canyin_list extends BaseAdapter{
    List<Market> markets;
    public Adapter_canyin_list(List<Market> markets){
        this.markets=markets;
    }

    @Override
    public int getCount() {
        return markets.size();
    }

    @Override
    public Object getItem(int position) {
        return markets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Market market= (Market) getItem(position);
        convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.single_canyin_list,null);
        TextView name=(TextView)convertView.findViewById(R.id.name);
        TextView time=(TextView)convertView.findViewById(R.id.time);
        ImageView image=(ImageView)convertView.findViewById(R.id.image);

        name.setText(market.getName());
        time.setText(market.getYytime());
        return convertView;
    }
}
