package com.ydwj.News;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.lidroid.xutils.DbUtils;
import com.ydwj.bean.NewsType;

import com.ydwj.community.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MangoWe extends Fragment {

    ViewPager viewPager;
    View view;
    List<Fragment> fragmentList=new ArrayList<Fragment>();

    TabLayout tabLayout;
    List<NewsType> newsTypes=new ArrayList<NewsType>();
    DbUtils dbUtils;
    Context context;
    private OnFragmentInteractionListener mListener;

    public MangoWe() {
        // Required empty public constructor
    }

    ImageView loading;
    public void initLoading(){
        loading=(ImageView)view.findViewById(R.id.loading);
        loading.setAnimation(AnimationUtils.loadAnimation(context, R.anim.loading));
        loading.setImageResource(R.mipmap.loading);
        loading.setVisibility(View.VISIBLE);
    }
    public void stoploading(){
        loading.clearAnimation();
        loading.setVisibility(view.GONE);
        loading.setImageBitmap(null);
    }
    Handler handler=new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context=getActivity();
    }
    //出现刷新标题按钮
    LinearLayout btn_asktitle;
    public void askTitleAgain(){
        tabLayout.setVisibility(View.GONE);
        if(btn_asktitle==null){
            btn_asktitle=(LinearLayout)view.findViewById(R.id.askTitleAgain);
            btn_asktitle.setOnClickListener(onClickListener);
        }
        btn_asktitle.setVisibility(View.VISIBLE);
        btn_asktitle.setAnimation(AnimationUtils.loadAnimation(context,R.anim.alpha_add));
    }
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.askTitleAgain:
                    getNewstype();
                    initLoading();
                    v.setVisibility(View.GONE);
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_mango_we, container, false);

        initLoading();

        tabLayout= (TabLayout)view.findViewById(R.id.show_types);
        Thread thread=new Thread(runnable);
        thread.start();
        return view;
    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            getNewstype();
        }
    };
    Snackbar snackbar;
    FragmentAdapter fragmentAdapter;
    public void initT(){
        initFrg();
        fragmentAdapter =new FragmentAdapter(context,this.getChildFragmentManager(),fragmentList, newsTypes);
        viewPager= (ViewPager)view.findViewById(R.id.main_title);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
        initTypes();
    }
    public void initFrg(){
        for(int i=0;i<newsTypes.size();i++){
            Fragment fragment=null;
            fragmentList.add(fragment);
        }
    }
    public void initTypes(){
        tabLayout.setVisibility(View.VISIBLE);
        if(tabLayout!=null){
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//            tabLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.alpha_add));
        }
        //得到type数组
    }

    //
    String json = "";
    public void getNewstype() {
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://app.cloud-hn.net/app/factory.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("asd",response);
                getTypes(response);
                stoploading();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                askTitleAgain();
                Toast.makeText(context,"网络连接失败,请重试",Toast.LENGTH_SHORT).show();
                stoploading();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("action","newsType");

                return params;
            }
        };
        requestQueue.add(stringRequest);
        requestQueue.start();
    }
    public void getTypes(String json) {
        JSONArray jArray;
        NewsType newstype=null;
        try {
            JSONObject jObject = new JSONObject(json);
            if(jObject.getString("retCode").equals("00")){
                jArray = jObject.getJSONArray("retData");
                for (int i = 0; i < jArray.length(); i++) {
                    newstype = new NewsType();
                    newstype.setTypeName(jArray.getJSONObject(i).getString("typeName"));
                    newstype.setTypeId(jArray.getJSONObject(i).getInt("typeId"));
                    newsTypes.add(newstype);
                }
                initT();
            }else{
                Toast.makeText(context,json,Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
