package com.ydwj.Service.BaoXiu;






import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.ydwj.News.ShowTitle;
import com.ydwj.bean.NewsType;

import java.util.List;

/**
 * Created by MangoCandy on 2015/9/20.
 */
public class Adapter_Baoxiu extends FragmentPagerAdapter {
    String[] types={"公共报修","我的报修"};
    Fragment fragment;
    Context context;
    public Adapter_Baoxiu(Context context,FragmentManager manager) {
        super(manager);
        this.context=context;
    }
    @Override
    public Fragment getItem(int position) {
            fragment=Frg_BaoXiu.newInstance(types[position]);
            return fragment;
    }

    @Override
    public int getCount() {
        return types.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return types[position];
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
