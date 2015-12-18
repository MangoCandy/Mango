package com.ydwj.alarm;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ydwj.Utils.CircleImageView;
import com.ydwj.bean.Contacts;
import com.ydwj.community.R;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2015/10/30.
 */
//联系人列表适配器
public class Adapter_show_contacts extends BaseAdapter {
    List<Contacts> contactses;
    Context context;
    int[] colors=new int[]{
            R.color.contacts1,
            R.color.contacts2,
            R.color.contacts3,
            R.color.contacts4,
            R.color.contacts5,
    };
    Random random=new Random(9);
    public Adapter_show_contacts(List<Contacts> contactses, Context context){
        this.contactses=contactses;
        this.context=context;
    }
    @Override
    public int getCount() {
        return contactses.size();

    }

    @Override
    public Object getItem(int position) {
        return contactses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder{
        TextView name;
        TextView beizhu;
        CircleImageView imageView;
        TextView name_head;
        TextView num;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        String BEIZHU="无备注";
        final Contacts contacts= (Contacts) getItem(position);
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.single_edit_contacts,null);
            viewHolder.name=(TextView)convertView.findViewById(R.id.single_contact_name);
            viewHolder.beizhu=(TextView)convertView.findViewById(R.id.single_contact_beizhu);
            viewHolder.imageView=(CircleImageView)convertView.findViewById(R.id.name_head_bg);
            viewHolder.name_head=(TextView)convertView.findViewById(R.id.name_head_text);
            viewHolder.num=(TextView)convertView.findViewById(R.id.single_contact_num);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        BEIZHU=contacts.getBEIZHU();
        if(BEIZHU==null||BEIZHU.equals("")){
            BEIZHU="无备注";
        }
        //颜色代码
        String name=contacts.getCONTACT_NAME();
        viewHolder.beizhu.setText(BEIZHU);
        viewHolder.name.setText(name);
        viewHolder.name_head.setText(name.charAt(0)+"");

        viewHolder.imageView.setImageResource(R.color.contacts5);//预留随机颜色
        viewHolder.num.setText(contacts.getCONTACT_NUM());
        return convertView;
    }
}
