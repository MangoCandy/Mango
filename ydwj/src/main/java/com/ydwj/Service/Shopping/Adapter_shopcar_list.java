package com.ydwj.Service.Shopping;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ydwj.bean.ShopCarList;
import com.ydwj.community.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mangocandy on 16-3-1.
 */
public class Adapter_shopcar_list extends RecyclerView.Adapter<Adapter_shopcar_list.MViewholder> {
    List<ShopCarList> shopCarListList;
    Context context;
    public Adapter_shopcar_list(List<ShopCarList> shopCarListList){
        this.shopCarListList=shopCarListList;
    }
    Map<Integer,Boolean> bools=new HashMap<>();

    public Map<Integer, Boolean> getBools() {
        return bools;
    }

    public void setBools(Map<Integer, Boolean> bools) {
        this.bools = bools;
    }

    @Override
    public MViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_shopcar_list,null);

        return new MViewholder(view);
    }

    @Override
    public void onBindViewHolder(MViewholder holder, final int position) {
        final ShopCarList shopCarList=shopCarListList.get(position);
        holder.num.setText("*"+shopCarList.getNum());
        holder.price.setText("￥"+shopCarList.getPrice());
        holder.name.setText(shopCarList.getName());
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shopCarList.isXuanzhong()==false){
                    shopCarList.setXuanzhong(true);
                    bools.put(position,true);
                }else{
                    shopCarList.setXuanzhong(false);
                    bools.put(position,false);
                }
            }
        });
        if(shopCarList.getBitmap()!=null){
            holder.imageView.setImageDrawable(shopCarList.getBitmap());
        }
        holder.check.setChecked(shopCarList.isXuanzhong());
    }

    @Override
    public int getItemCount() {
        return shopCarListList.size();
    }

    class MViewholder extends RecyclerView.ViewHolder{
        TextView name;
        TextView danwei;
        TextView price;
        TextView num;
        CheckBox check;
        ImageView imageView;
        public MViewholder(View itemView) {
            super(itemView);
            name=(TextView) itemView.findViewById(R.id.name);
//            danwei=(TextView) itemView.findViewById(R.id.danwei);
            price=(TextView) itemView.findViewById(R.id.price);
            num=(TextView) itemView.findViewById(R.id.num);
            check=(CheckBox) itemView.findViewById(R.id.checkbox);
            imageView=(ImageView) itemView.findViewById(R.id.image);
        }
    }
}
