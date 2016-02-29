package com.ydwj.Service.BaoXiu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ydwj.bean.Baoxiu_List;
import com.ydwj.community.R;

import java.util.List;

/**
 * Created by Administrator on 2016/2/29.
 */
public class Adapter_baoxiu_list extends RecyclerView.Adapter<Adapter_baoxiu_list.MViewHolder>  {
    List<Baoxiu_List> baoxius;

    public Adapter_baoxiu_list(List<Baoxiu_List> baoxius){
        this.baoxius=baoxius;
    }
    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_baoxiu_list,null);
        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MViewHolder holder, int position) {
        Baoxiu_List baoxiu=baoxius.get(position);
        holder.title.setText(baoxiu.getTitle());
        holder.time.setText(baoxiu.getTime());
        holder.text.setText(baoxiu.getText());
//        holder.liuyan.setText(baoxiu.getLiuyan());
    }

    @Override
    public int getItemCount() {
        return baoxius.size();
    }

    class MViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView time;
        TextView text;
        TextView dianzan;
        TextView liuyan;
        public MViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);
            time=(TextView)itemView.findViewById(R.id.time);
            text=(TextView)itemView.findViewById(R.id.text);
            liuyan=(TextView)itemView.findViewById(R.id.liuyan);
        }
    }
}
