package com.ydwj.alarm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ydwj.bean.Contacts;
import com.ydwj.community.R;

import java.util.List;

/**
 * Created by Administrator on 2015/10/30.
 */
//弹出联系人适配器
public class Adapter_contacts extends BaseAdapter {
    List<Contacts> contactses;
    Context context;

    public Adapter_contacts(List<Contacts> contactses, Context context) {
        this.contactses = contactses;
        this.context = context;
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
        return position;
    }

    public class ViewHolder {
        TextView name;
        ImageView call;
        ImageView bg;
        TextView num;
        RelativeLayout relativeLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final Contacts contacts = (Contacts) getItem(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.single_pop_contacts, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.single_contact_name);
            viewHolder.call = (ImageView) convertView.findViewById(R.id.single_contact_call);
            viewHolder.bg = (ImageView) convertView.findViewById(R.id.single_contact_bg);
            viewHolder.num=(TextView)convertView.findViewById(R.id.single_contact_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.bg.setImageResource(R.drawable.btn_single_contacts);
        viewHolder.name.setText(contacts.getCONTACT_NAME());
        viewHolder.num.setText(contacts.getCONTACT_NUM());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + contacts.getCONTACT_NUM()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
