package com.ydwj.Service.BaoXiu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ydwj.MUtils.MUtils;
import com.ydwj.bean.Baoxiu_List;
import com.ydwj.community.R;

import java.util.List;

/**
 * Created by Administrator on 2016/2/29.
 */
public class Adapter_baoxiu_list extends RecyclerView.Adapter<Adapter_baoxiu_list.MViewHolder>  {
    List<Baoxiu_List> baoxius;
    Context context;
    public Adapter_baoxiu_list(List<Baoxiu_List> baoxius){
        this.baoxius=baoxius;
    }
    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_baoxiu_list,null);
        context=parent.getContext();
        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MViewHolder holder, int position) {
        final Baoxiu_List baoxiu=baoxius.get(position);
        holder.title.setText(baoxiu.getTitle());
        holder.time.setText(baoxiu.getTime());
        holder.text.setText(baoxiu.getText());
        for(Bitmap bitmap:baoxiu.getBitmaps()){
            ImageView imageView=new ImageView(context);
            imageView.setImageBitmap(bitmap);
            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) holder.imagegroup.getLayoutParams();
            params.weight=new MUtils(context).getWidth()/3-50;
            params.height=params.height*2/3;
            params.setMargins(5,0,5,0);
            imageView.setLayoutParams(params);
            holder.imagegroup.addView(imageView);
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Act_Baoxiu_xx.class);
                intent.putExtra("time",baoxiu.getTime());
                context.startActivity(intent);
            }
        });
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
        LinearLayout imagegroup;
        RelativeLayout button;
        public MViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);
            time=(TextView)itemView.findViewById(R.id.time);
            text=(TextView)itemView.findViewById(R.id.text);
            liuyan=(TextView)itemView.findViewById(R.id.liuyan);
            imagegroup=(LinearLayout)itemView.findViewById(R.id.imagegroup);
            button=(RelativeLayout)itemView.findViewById(R.id.button);
        }
    }
}
