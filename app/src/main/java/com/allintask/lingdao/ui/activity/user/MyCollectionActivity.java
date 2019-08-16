package com.allintask.lingdao.ui.activity.user;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.adapter.CommonFragmentPagerAdapter;
import com.allintask.lingdao.ui.fragment.user.MyCollectionFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by TanJiaJun on 2018/2/26.
 */

public class MyCollectionActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private int selectedTabPosition = 0;
    private List<Integer> sizeList;

    private MyCollectionFragment myCollectionServiceFragment;
    private MyCollectionFragment myCollectionDemandFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_collection;
    }

    @Override
    protected BasePresenter CreatePresenter() {
        return null;
    }

    @Override
    protected void init() {
        initToolbar();
        initUI();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.collected_service));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        sizeList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            sizeList.add(0);
        }

        CommonFragmentPagerAdapter fragmentPagerAdapter = new CommonFragmentPagerAdapter(getSupportFragmentManager());
        fragmentPagerAdapter.setTabTitles(getTabTitles());
        fragmentPagerAdapter.setFragmentList(getMyCollectionStatusFragmentList());
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager, false);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTabPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private String[] getTabTitles() {
        return new String[]{getString(R.string.service), getString(R.string.demand)};
    }

    private List<Fragment> getMyCollectionStatusFragmentList() {
        List<Fragment> myCollectionStatusFragmentList = new ArrayList<>();

        myCollectionServiceFragment = new MyCollectionFragment();
        Bundle myCollectionServiceBundle = new Bundle();
        myCollectionServiceBundle.putInt(CommonConstant.EXTRA_MY_COLLECTION_STATUS, CommonConstant.MY_COLLECTION_SERVICE);
        myCollectionServiceFragment.setArguments(myCollectionServiceBundle);
        myCollectionServiceFragment.setOnDataLoadingCompletedListener(new MyCollectionFragment.OnDataLoadingCompletedListener() {
            @Override
            public void onShowMyCollectionListSize(int size) {
                sizeList.set(0, size);

                TabLayout.Tab tab = tabLayout.getTabAt(0);

                if (null != tab) {
                    tab.setText("服务(" + String.valueOf(size) + ")");
                }
            }
        });

        myCollectionDemandFragment = new MyCollectionFragment();
        Bundle myCollectionDemandBundle = new Bundle();
        myCollectionDemandBundle.putInt(CommonConstant.EXTRA_MY_COLLECTION_STATUS, CommonConstant.MY_COLLECTION_DEMAND);
        myCollectionDemandFragment.setArguments(myCollectionDemandBundle);
        myCollectionDemandFragment.setOnDataLoadingCompletedListener(new MyCollectionFragment.OnDataLoadingCompletedListener() {
            @Override
            public void onShowMyCollectionListSize(int size) {
                sizeList.set(1, size);

                TabLayout.Tab tab = tabLayout.getTabAt(1);

                if (null != tab) {
                    tab.setText("需求(" + String.valueOf(size) + ")");
                }
            }
        });

        myCollectionStatusFragmentList.add(myCollectionServiceFragment);
        myCollectionStatusFragmentList.add(myCollectionDemandFragment);
        return myCollectionStatusFragmentList;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (selectedTabPosition == 0) {
            if (null != myCollectionServiceFragment) {
                myCollectionServiceFragment.initData();
            }
        } else if (selectedTabPosition == 1) {
            if (null != myCollectionDemandFragment) {
                myCollectionDemandFragment.initData();
            }
        }
    }

}
