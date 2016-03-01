package com.ydwj.Service.CanYin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ydwj.Service.Shopping.Act_SPXQ;
import com.ydwj.community.R;

/**
 * Created by Administrator on 2016/3/1.
 */
public class Adapter_canyin_SP extends RecyclerView.Adapter<Adapter_canyin_SP.mViewHolder>{
    Context context;
    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_foods,null);
        context=parent.getContext();
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, int position) {
        holder.imageView.setImageResource(R.drawable.cs_jd);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Act_WMXQ.class);
                context.startActivity(intent);
            }
        });
        holder.textView.setText("￥15");
        holder.name.setText("宫保鸡丁");
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class mViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        TextView name;
        public mViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView) itemView.findViewById(R.id.image);
            textView=(TextView)itemView.findViewById(R.id.price);
            name=(TextView)itemView.findViewById(R.id.name);
        }
    }
}
