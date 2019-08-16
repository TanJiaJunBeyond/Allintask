package com.allintask.lingdao.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.List;

/**
 * Created by TanJiaJun on 2017/4/15 0015.
 */

public class CommonFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles;

    private List<Fragment> fragmentList;

    public CommonFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return (null == fragmentList) ? 0 : fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String pageTitle = "";

        if (null != tabTitles) {
            if (0 <= position && position < tabTitles.length) {
                pageTitle = tabTitles[position];
            }
        }
        return pageTitle;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        if (null != fragmentList) {
            if (0 <= position && position < fragmentList.size()) {
                fragment = fragmentList.get(position);
            }
        }
        return fragment;
    }

    public void setTabTitles(String[] tabTitleArray) {
        this.tabTitles = tabTitleArray;
    }

    public void setFragmentList(List<Fragment> fragmentList) {
        this.fragmentList = fragmentList;
    }

}
