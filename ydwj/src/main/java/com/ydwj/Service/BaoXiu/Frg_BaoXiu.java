package com.ydwj.Service.BaoXiu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ydwj.Ziliao;
import com.ydwj.bean.Baoxiu;
import com.ydwj.bean.Baoxiu_List;
import com.ydwj.community.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/29.
 */
public class Frg_BaoXiu extends Fragment {
    String type=null;
    View view;
    RecyclerView listview;
    Adapter_baoxiu_list adapter_baoxiu_list;
    List<Baoxiu_List> baoxius=new ArrayList<>();
    Context context=getContext();
    TextView nothing;
    LinearLayout imagegroup;
    int tp;
    public static Frg_BaoXiu newInstance(String type){
        Frg_BaoXiu frg_baoXiu=new Frg_BaoXiu();
        Bundle bundle=new Bundle();
        bundle.putString("type",type);
        frg_baoXiu.setArguments(bundle);
        return frg_baoXiu;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle=getArguments();
        type=bundle.getString("type");
        if(type.endsWith("公共报修")){
            tp=0;
        }else{
            tp=1;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_baoxiu,null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        baoxius.clear();
        initView();
    }

    public void initView(){
        nothing=(TextView)view.findViewById(R.id.nothing);

        listview=(RecyclerView)view.findViewById(R.id.listview);
        listview.setLayoutManager(new LinearLayoutManager(context));
        adapter_baoxiu_list=new Adapter_baoxiu_list(baoxius);
        for(Baoxiu_List baoxiu_list:Ziliao.baoxiu_lists){
            if(baoxiu_list.getType()==tp){
                baoxius.add(baoxiu_list);
            }
        }
        listview.setAdapter(adapter_baoxiu_list);
        notifylist();
    }

    public void notifylist(){
        if(baoxius.size()>0){
            nothing.setVisibility(View.GONE);
        }else{
            nothing.setVisibility(View.VISIBLE);
        }
        adapter_baoxiu_list.notifyDataSetChanged();
    }
}
