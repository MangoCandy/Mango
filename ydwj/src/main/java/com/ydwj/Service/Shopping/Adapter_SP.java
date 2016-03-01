package com.ydwj.Service.Shopping;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ydwj.community.R;

/**
 * Created by Administrator on 2016/3/1.
 */
public class Adapter_SP extends RecyclerView.Adapter<Adapter_SP.mViewHolder>{
    Context context;
    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_goods,null);
        context=parent.getContext();
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, int position) {
        holder.imageView.setImageResource(R.mipmap.cs_cz);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Act_SPXQ.class);
                context.startActivity(intent);
            }
        });
        holder.textView.setText("￥55");
        holder.name.setText("纯甄好牛奶");
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
