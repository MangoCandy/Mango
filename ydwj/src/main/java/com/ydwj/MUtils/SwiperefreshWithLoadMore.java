package com.ydwj.MUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.ydwj.community.R;

/**
 * Created by Administrator on 2015/12/16.
 */
public class SwiperefreshWithLoadMore extends SwipeRefreshLayout implements View.OnTouchListener {
    Context context;
    View footview;
    public SwiperefreshWithLoadMore(Context context) {
        super(context);
    }

    public SwiperefreshWithLoadMore(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        footview=LayoutInflater.from(context).inflate(R.layout.foot_layout,null);
        mTouchSlop= 200;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    ListView listView;
    int my=0;
    int y=0;
    int mTouchSlop=0;
    public void getListview(){
        for(int i=0;i<this.getChildCount();i++){
            View view=getChildAt(i);
            if(view instanceof ListView){
                listView=(ListView)view;
                listView.setOnTouchListener(this);
            }
        }
    }

    public void loaddate(){
        setLoadingMore(true);
        onLoadListener.load();
    }
    public boolean canLoad(){
        return isBottom()&&!isloading&&(y-my)>=mTouchSlop;
    }
    boolean isloading=false;
    public void setLoadingMore(boolean bool){
        isloading=bool;
        if(bool){
            listView.addFooterView(footview);
        }else{
            listView.removeFooterView(footview);
        }
    }
    OnLoadListener onLoadListener;
    public void setOnLoadListener(OnLoadListener onLoadListener){
        getListview();
        this.onLoadListener=onLoadListener;
    }
    public boolean isBottom(){
        if(listView!=null){
            return listView.getLastVisiblePosition()>=listView.getAdapter().getCount()-1;
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action=event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                y=(int)event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                my=(int)event.getRawY();
                if(isBottom()&&listView.getFooterViewsCount()<1){
                    listView.addFooterView(footview);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(canLoad()){
                    if(onLoadListener!=null){
                        loaddate();
                    }
                }else{
                    listView.removeFooterView(footview);
                }
                break;
        }
        return false;
    }

    public static interface OnLoadListener{
        public void load();
    }
}
