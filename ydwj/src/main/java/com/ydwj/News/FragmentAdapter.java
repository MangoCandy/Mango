package com.ydwj.News;






import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.ydwj.bean.NewsType;

import java.util.List;
import java.util.Map;

/**
 * Created by MangoCandy on 2015/9/20.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments;
    List<NewsType> typeMaps;
    NewsType newsType;
    Fragment fragment;
    Context context;
    public FragmentAdapter(Context context,FragmentManager fm, List<Fragment> fragments, List<NewsType> newsTypes) {
        super(fm);
        this.context=context;
        this.typeMaps=newsTypes;
        this.fragments=fragments;
    }
    @Override
    public Fragment getItem(int position) {
            if(fragments.get(position)==null){
                fragment= ShowTitle.newInstance(typeMaps.get(position).getTypeId(),context);
            }
            return fragment;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        newsType=typeMaps.get(position);
        if(newsType==null){
            return "";
        }else{
            return newsType.getTypeName();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}
