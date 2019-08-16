package com.allintask.lingdao.ui.fragment.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.service.ServiceManagementHeaderBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.main.ServiceManagementPresenter;
import com.allintask.lingdao.ui.activity.main.MainActivity;
import com.allintask.lingdao.ui.activity.main.PublishServiceActivity;
import com.allintask.lingdao.ui.activity.service.MyServiceActivity;
import com.allintask.lingdao.ui.adapter.CommonFragmentPagerAdapter;
import com.allintask.lingdao.ui.adapter.main.ServiceManagementHeaderAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.RecyclerItemClickListener;
import com.allintask.lingdao.ui.fragment.BaseFragment;
import com.allintask.lingdao.ui.fragment.service.ServiceStatusFragment;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.main.IServiceManagementView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/1/3.
 */

public class ServiceManagementFragment extends BaseFragment<IServiceManagementView, ServiceManagementPresenter> implements IServiceManagementView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tv_right_first)
    TextView rightFirstTv;
    @BindView(R.id.rl_service_management_first_enter)
    RelativeLayout serviceManagementFirstEnterRL;
    @BindView(R.id.rl_service_management)
    RelativeLayout serviceManagementRL;
    @BindView(R.id.btn_know)
    Button knowBtn;
    //    @BindView(R.id.header_recycler_view)
//    RecyclerView headerRecyclerView;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.rl_compile_service)
    RelativeLayout compileServiceRL;

    private ServiceManagementFragmentBroadcastReceiver serviceManagementFragmentBroadcastReceiver;
    private boolean isFirstEnterServiceManagement = true;
    private int serviceStatus = CommonConstant.SERVICE_STATUS_WAIT_BID;
    private int selectedTabPosition = 0;

//    private ServiceManagementHeaderAdapter serviceManagementHeaderAdapter;

    private ServiceStatusFragment serviceWaitBidFragment;
    private ServiceStatusFragment serviceHasBidFragment;
    private ServiceStatusFragment serviceUnderwayFragment;
    private ServiceStatusFragment serviceCompletedFragment;
    private ServiceStatusFragment serviceExpiredFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_service_management;
    }

    @Override
    protected ServiceManagementPresenter CreatePresenter() {
        return new ServiceManagementPresenter();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (null != bundle) {
            serviceStatus = bundle.getInt(CommonConstant.EXTRA_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_WAIT_BID);
        }

        Window window = ((MainActivity) getParentContext()).getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(getResources().getColor(R.color.white));

            ((MainActivity) getParentContext()).MIUISetStatusBarLightMode(((MainActivity) getParentContext()).getWindow(), true);
            ((MainActivity) getParentContext()).FlymeSetStatusBarLightMode(((MainActivity) getParentContext()).getWindow(), true);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.white));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        isFirstEnterServiceManagement = UserPreferences.getInstance().getIsFirstEnterServiceManagement();

        initToolbar();
        initUI();
//        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.service_management));
        rightFirstTv.setText(getString(R.string.my_service));
        rightFirstTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getParentContext(), MyServiceActivity.class);
                startActivity(intent);
            }
        });

        ((MainActivity) getParentContext()).setSupportActionBar(toolbar);
    }

    private void initUI() {
        registerServiceManagementFragmentBroadcastReceiver();

        if (isFirstEnterServiceManagement) {
            serviceManagementFirstEnterRL.setVisibility(View.VISIBLE);
            serviceManagementRL.setVisibility(View.GONE);
        } else {
            serviceManagementFirstEnterRL.setVisibility(View.GONE);
            serviceManagementRL.setVisibility(View.VISIBLE);
        }

//        initHeaderRecyclerView();

        CommonFragmentPagerAdapter fragmentPagerAdapter = new CommonFragmentPagerAdapter(getChildFragmentManager());
        fragmentPagerAdapter.setTabTitles(getTabTitles());
        fragmentPagerAdapter.setFragmentList(getServiceStatusFragmentList());
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setCurrentItem(serviceStatus);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager, false);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTabPosition = tab.getPosition();

                switch (selectedTabPosition) {
                    case 0:
                        serviceStatus = CommonConstant.SERVICE_STATUS_WAIT_BID;

                        if (null != serviceWaitBidFragment) {
                            serviceWaitBidFragment.initData();
                        }
                        break;

                    case 1:
                        serviceStatus = CommonConstant.SERVICE_STATUS_HAS_BID;

                        if (null != serviceHasBidFragment) {
                            serviceHasBidFragment.initData();
                        }
                        break;

                    case 2:
                        serviceStatus = CommonConstant.SERVICE_STATUS_UNDERWAY;

                        if (null != serviceUnderwayFragment) {
                            serviceUnderwayFragment.initData();
                        }
                        break;

                    case 3:
                        serviceStatus = CommonConstant.SERVICE_STATUS_COMPLETED;

                        if (null != serviceCompletedFragment) {
                            serviceCompletedFragment.initData();
                        }
                        break;

                    case 4:
                        serviceStatus = CommonConstant.SERVICE_STATUS_EXPIRED;

                        if (null != serviceExpiredFragment) {
                            serviceExpiredFragment.initData();
                        }
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        boolean isFirstShowCompileServiceLayout = UserPreferences.getInstance().getIsFirstShowCompileServiceLayout();

        if (isFirstShowCompileServiceLayout) {
            compileServiceRL.setVisibility(View.VISIBLE);
        } else {
            compileServiceRL.setVisibility(View.GONE);
        }
    }

//    private void initData() {
//        List<ServiceManagementHeaderBean> serviceManagementHeaderList = new ArrayList<>();
//
//        for (int i = 0; i < 4; i++) {
//            ServiceManagementHeaderBean serviceManagementHeaderBean = new ServiceManagementHeaderBean();
//
//            switch (i) {
//                case 0:
//                    serviceManagementHeaderBean.imageUrl = "drawable://" + R.mipmap.ic_service_management_my_service;
//                    serviceManagementHeaderBean.title = getString(R.string.my_service);
//                    break;
//
//                case 1:
//                    serviceManagementHeaderBean.imageUrl = "drawable://" + R.mipmap.ic_service_management_my_photo_album;
//                    serviceManagementHeaderBean.title = getString(R.string.my_photo_album);
//                    break;
//
//                case 2:
//                    serviceManagementHeaderBean.imageUrl = "drawable://" + R.mipmap.ic_service_management_authentication_center;
//                    serviceManagementHeaderBean.title = getString(R.string.authentication_center);
//                    break;
//
//                case 3:
//                    serviceManagementHeaderBean.imageUrl = "drawable://" + R.mipmap.ic_service_management_strategies_to_make_money;
//                    serviceManagementHeaderBean.title = getString(R.string.strategies_to_make_money);
//                    break;
//            }
//
//            serviceManagementHeaderList.add(serviceManagementHeaderBean);
//        }
//
//        serviceManagementHeaderAdapter.setDateList(serviceManagementHeaderList);
//    }

//    private void initHeaderRecyclerView() {
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getParentContext(), LinearLayoutManager.HORIZONTAL, false);
//        headerRecyclerView.setLayoutManager(linearLayoutManager);
//
//        serviceManagementHeaderAdapter = new ServiceManagementHeaderAdapter(getParentContext());
//        headerRecyclerView.setAdapter(serviceManagementHeaderAdapter);
//
//        serviceManagementHeaderAdapter.setOnItemClickListener(new RecyclerItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                switch (position) {
//                    case 0:
//                        Intent intent = new Intent(getParentContext(), MyServiceActivity.class);
//                        startActivity(intent);
//                        break;
//                }
//            }
//        });
//    }

    private String[] getTabTitles() {
        return new String[]{getString(R.string.wait_bid), getString(R.string.has_bid), getString(R.string.underway), getString(R.string.completed), getString(R.string.expired)};
    }

    private List<Fragment> getServiceStatusFragmentList() {
        List<Fragment> serviceStatusFragmentList = new ArrayList<>();

        serviceWaitBidFragment = new ServiceStatusFragment();
        Bundle serviceWaitBidBundle = new Bundle();
        serviceWaitBidBundle.putInt(CommonConstant.EXTRA_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_WAIT_BID);
        serviceWaitBidFragment.setArguments(serviceWaitBidBundle);

        serviceHasBidFragment = new ServiceStatusFragment();
        Bundle serviceHasBidBundle = new Bundle();
        serviceHasBidBundle.putInt(CommonConstant.EXTRA_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_HAS_BID);
        serviceHasBidFragment.setArguments(serviceHasBidBundle);

        serviceUnderwayFragment = new ServiceStatusFragment();
        Bundle serviceUnderwayBundle = new Bundle();
        serviceUnderwayBundle.putInt(CommonConstant.EXTRA_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_UNDERWAY);
        serviceUnderwayFragment.setArguments(serviceUnderwayBundle);

        serviceCompletedFragment = new ServiceStatusFragment();
        Bundle serviceCompletedBundle = new Bundle();
        serviceCompletedBundle.putInt(CommonConstant.EXTRA_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_COMPLETED);
        serviceCompletedFragment.setArguments(serviceCompletedBundle);

        serviceExpiredFragment = new ServiceStatusFragment();
        Bundle serviceExpiredBundle = new Bundle();
        serviceExpiredBundle.putInt(CommonConstant.EXTRA_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_EXPIRED);
        serviceExpiredFragment.setArguments(serviceExpiredBundle);

        serviceStatusFragmentList.add(serviceWaitBidFragment);
        serviceStatusFragmentList.add(serviceHasBidFragment);
        serviceStatusFragmentList.add(serviceUnderwayFragment);
        serviceStatusFragmentList.add(serviceCompletedFragment);
        serviceStatusFragmentList.add(serviceExpiredFragment);
        return serviceStatusFragmentList;
    }

    public void setViewPagerCurrentItem(int item) {
        if (item == serviceStatus) {
            switch (item) {
                case 0:
                    if (null != serviceWaitBidFragment) {
                        serviceWaitBidFragment.initData();
                    }
                    break;

                case 1:
                    if (null != serviceHasBidFragment) {
                        serviceHasBidFragment.initData();
                    }
                    break;

                case 2:
                    if (null != serviceUnderwayFragment) {
                        serviceUnderwayFragment.initData();
                    }
                    break;

                case 3:
                    if (null != serviceCompletedFragment) {
                        serviceCompletedFragment.initData();
                    }
                    break;

                case 4:
                    if (null != serviceExpiredFragment) {
                        serviceExpiredFragment.initData();
                    }
                    break;
            }
        } else if (null != viewPager) {
            viewPager.setCurrentItem(item, true);
        }
    }

    private void registerServiceManagementFragmentBroadcastReceiver() {
        serviceManagementFragmentBroadcastReceiver = new ServiceManagementFragmentBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommonConstant.ACTION_SERVICE_STATUS);
        getParentContext().registerReceiver(serviceManagementFragmentBroadcastReceiver, intentFilter);
    }

    private class ServiceManagementFragmentBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent) {
                String action = intent.getAction();

                if (!TextUtils.isEmpty(action) && action.equals(CommonConstant.ACTION_SERVICE_STATUS)) {
                    int serviceStatus = intent.getIntExtra(CommonConstant.EXTRA_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_WAIT_BID);

                    switch (serviceStatus) {
                        case CommonConstant.SERVICE_STATUS_WAIT_BID:
                            setViewPagerCurrentItem(0);
                            break;

                        case CommonConstant.SERVICE_STATUS_HAS_BID:
                            setViewPagerCurrentItem(1);
                            break;

                        case CommonConstant.SERVICE_STATUS_UNDERWAY:
                            setViewPagerCurrentItem(2);
                            break;

                        case CommonConstant.SERVICE_STATUS_COMPLETED:
                            setViewPagerCurrentItem(3);
                            break;

                        case CommonConstant.SERVICE_STATUS_EXPIRED:
                            setViewPagerCurrentItem(4);
                            break;
                    }
                }
            }
        }

    }

    @OnClick({R.id.btn_know, R.id.rl_compile_service})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_know:
                serviceManagementFirstEnterRL.setVisibility(View.GONE);
                serviceManagementRL.setVisibility(View.VISIBLE);

                UserPreferences.getInstance().setIsFirstEnterServiceManagement(false);
                break;

            case R.id.rl_compile_service:
                UserPreferences.getInstance().setIsFirstShowCompileServiceLayout(false);
                compileServiceRL.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            Window window = ((MainActivity) getParentContext()).getWindow();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                window.setStatusBarColor(getResources().getColor(R.color.white));

                ((MainActivity) getParentContext()).MIUISetStatusBarLightMode(((MainActivity) getParentContext()).getWindow(), true);
                ((MainActivity) getParentContext()).FlymeSetStatusBarLightMode(((MainActivity) getParentContext()).getWindow(), true);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.white));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

            Bundle bundle = getArguments();

            if (null != bundle) {
                serviceStatus = bundle.getInt(CommonConstant.EXTRA_SERVICE_STATUS, CommonConstant.SERVICE_STATUS_WAIT_BID);
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (null != serviceManagementFragmentBroadcastReceiver) {
            getParentContext().unregisterReceiver(serviceManagementFragmentBroadcastReceiver);
        }

        super.onDestroyView();
    }

}
