package com.ydwj.Service;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ydwj.Service.Gonggao.Act_GongGao;
import com.ydwj.community.R;

/**
 * Created by Administrator on 2016/2/24.
 */
public class Adapter_buttongroup extends RecyclerView.Adapter<Adapter_buttongroup.mViewHolder> {
    String[] buttontexts={"社区公告","物业报修","商超服务","社区医疗","餐饮服务","敬请期待"};
    int[] images={R.mipmap.bt_gg,R.mipmap.bt_bx,R.mipmap.bt_sc,R.mipmap.bt_yl,R.mipmap.bt_cy,0};
    Context context;
    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.single_9button,null);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, final int position) {
        holder.textView.setText(buttontexts[position]);
        holder.imageView.setImageResource(images[position]);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null;
                switch (position){
                    case 0://公告
                        intent=new Intent(context, Act_GongGao.class);
                        break;
                    case 1:

                        break;
                    case 3://公告
                        intent=new Intent(context, Act_GongGao.class);
                        break;
                    case 4://公告
                        intent=new Intent(context, Act_GongGao.class);
                        break;
                    case 5://公告
                        intent=new Intent(context, Act_GongGao.class);
                        break;
                    case 6://公告
                        intent=new Intent(context, Act_GongGao.class);
                        break;

                }
                if(position==buttontexts.length-1){
                    Toast.makeText(context,"敬请期待",Toast.LENGTH_SHORT).show();
                }else{
                    context.startActivity(intent);
                }

            }
        });
    }
    @Override
    public int getItemCount() {
        return buttontexts.length;
    }

    public static class mViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        LinearLayout button;
        public mViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView) itemView.findViewById(R.id.myimage);
            textView=(TextView)itemView.findViewById(R.id.mytext);
            button=(LinearLayout)itemView.findViewById(R.id.button);
        }
    }
}