package com.ydwj.Service;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;

import com.ydwj.community.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/2/23.
 */
public class Frg_sqservice extends Fragment {
    View view;
    RecyclerView buttongroup;
    Adapter_buttongroup adapter;
    private String texts[] = null;
    private int images[] = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_sqservice,null);
        return view;
    }

    @Override
    public void onResume() {
        initView();
        super.onResume();
    }

    public void initView(){
        buttongroup=(RecyclerView) view.findViewById(R.id.buttongroup);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),3);
        buttongroup.setLayoutManager(gridLayoutManager);
        adapter=new Adapter_buttongroup();
        buttongroup.setFocusable(false);
        buttongroup.setAdapter(adapter);
    }
}
