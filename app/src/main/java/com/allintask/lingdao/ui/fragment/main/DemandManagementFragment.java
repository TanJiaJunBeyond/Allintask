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
import com.allintask.lingdao.bean.user.CheckBasicPersonalInformationBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.main.DemandManagementPresenter;
import com.allintask.lingdao.ui.activity.main.MainActivity;
import com.allintask.lingdao.ui.activity.main.PublishDemandActivity;
import com.allintask.lingdao.ui.activity.user.CompletePersonalInformationActivity;
import com.allintask.lingdao.ui.adapter.CommonFragmentPagerAdapter;
import com.allintask.lingdao.ui.fragment.BaseFragment;
import com.allintask.lingdao.ui.fragment.demand.DemandStatusFragment;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.main.IDemandManagementView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/1/14.
 */

public class DemandManagementFragment extends BaseFragment<IDemandManagementView, DemandManagementPresenter> implements IDemandManagementView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tv_right_first)
    TextView rightFirstTv;
    @BindView(R.id.rl_demand_management_first_enter)
    RelativeLayout demandManagementFirstEnterRL;
    @BindView(R.id.ll_demand_management)
    LinearLayout demandManagementLL;
    @BindView(R.id.btn_go_to_publish_demand)
    Button goToPublishDemandBtn;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private int demandStatus = CommonConstant.DEMAND_STATUS_IN_THE_BIDDING;

    private DemandManagementFragmentBroadcastReceiver demandManagementFragmentBroadcastReceiver;
    private boolean isFirstEnterDemandFragment = true;
    private List<Integer> sizeList;
    private int selectedTabPosition = 0;

    private DemandStatusFragment demandInTheBiddingFragment;
    private DemandStatusFragment demandUnderwayFragment;
    private DemandStatusFragment demandCompletedFragment;
    private DemandStatusFragment demandExpiredFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_demand_management;
    }

    @Override
    protected DemandManagementPresenter CreatePresenter() {
        return new DemandManagementPresenter();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (null != bundle) {
            demandStatus = bundle.getInt(CommonConstant.EXTRA_DEMAND_STATUS, CommonConstant.DEMAND_STATUS_IN_THE_BIDDING);
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

        isFirstEnterDemandFragment = UserPreferences.getInstance().getIsFirstEnterDemandManagement();
        sizeList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            sizeList.add(0);
        }

        initToolbar();
        initUI();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.demand_management));
        rightFirstTv.setText(getString(R.string.publish_demand));
        rightFirstTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.checkBasicPersonalInformationWholeRequest();
            }
        });

        ((MainActivity) getParentContext()).setSupportActionBar(toolbar);
    }

    private void initUI() {
        registerDemandManagementFragmentBroadcastReceiver();

        if (isFirstEnterDemandFragment) {
            demandManagementFirstEnterRL.setVisibility(View.VISIBLE);
            demandManagementLL.setVisibility(View.GONE);
        } else {
            demandManagementFirstEnterRL.setVisibility(View.GONE);
            demandManagementLL.setVisibility(View.VISIBLE);
        }

        final CommonFragmentPagerAdapter fragmentPagerAdapter = new CommonFragmentPagerAdapter(getChildFragmentManager());
        fragmentPagerAdapter.setTabTitles(getTabTitles());
        fragmentPagerAdapter.setFragmentList(getDemandStatusFragmentList());
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(demandStatus);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager, true);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTabPosition = tab.getPosition();
                int size = sizeList.get(selectedTabPosition);

                switch (selectedTabPosition) {
                    case 0:
                        demandStatus = CommonConstant.DEMAND_STATUS_IN_THE_BIDDING;

                        tab.setText("竞标中(" + String.valueOf(size) + ")");

                        tabLayout.getTabAt(1).setText("进行中");
                        tabLayout.getTabAt(2).setText("已完成");
                        tabLayout.getTabAt(3).setText("已过期");

                        if (null != demandInTheBiddingFragment) {
                            demandInTheBiddingFragment.initData();
                        }
                        break;

                    case 1:
                        demandStatus = CommonConstant.DEMAND_STATUS_UNDERWAY;

                        tab.setText("进行中(" + String.valueOf(size) + ")");

                        tabLayout.getTabAt(0).setText("竞标中");
                        tabLayout.getTabAt(2).setText("已完成");
                        tabLayout.getTabAt(3).setText("已过期");

                        if (null != demandUnderwayFragment) {
                            demandUnderwayFragment.initData();
                        }
                        break;

                    case 2:
                        demandStatus = CommonConstant.DEMAND_STATUS_COMPLETED;

                        tab.setText("已完成(" + String.valueOf(size) + ")");

                        tabLayout.getTabAt(0).setText("竞标中");
                        tabLayout.getTabAt(1).setText("进行中");
                        tabLayout.getTabAt(3).setText("已过期");

                        if (null != demandCompletedFragment) {
                            demandCompletedFragment.initData();
                        }
                        break;

                    case 3:
                        demandStatus = CommonConstant.DEMAND_STATUS_EXPIRED;

                        tab.setText("已过期(" + String.valueOf(size) + ")");

                        tabLayout.getTabAt(0).setText("竞标中");
                        tabLayout.getTabAt(1).setText("进行中");
                        tabLayout.getTabAt(2).setText("已完成");

                        if (null != demandExpiredFragment) {
                            demandExpiredFragment.initData();
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
    }

    private String[] getTabTitles() {
        return new String[]{"竞标中(0)", "进行中", "已完成", "已过期"};
    }

    private List<Fragment> getDemandStatusFragmentList() {
        List<Fragment> demandStatusFragmentList = new ArrayList<>();

        demandInTheBiddingFragment = new DemandStatusFragment();
        Bundle demandInTheBiddingBundle = new Bundle();
        demandInTheBiddingBundle.putInt(CommonConstant.EXTRA_DEMAND_STATUS, CommonConstant.DEMAND_STATUS_IN_THE_BIDDING);
        demandInTheBiddingFragment.setArguments(demandInTheBiddingBundle);
        demandInTheBiddingFragment.setOnDataLoadingCompletedListener(new DemandStatusFragment.OnDataLoadingCompletedListener() {
            @Override
            public void onShowDemandStatusListSize(int size) {
                sizeList.set(0, size);

                if (selectedTabPosition == 0) {
                    tabLayout.getTabAt(0).setText("竞标中(" + String.valueOf(size) + ")");
                }
            }
        });

        demandUnderwayFragment = new DemandStatusFragment();
        Bundle demandUnderwayBundle = new Bundle();
        demandUnderwayBundle.putInt(CommonConstant.EXTRA_DEMAND_STATUS, CommonConstant.DEMAND_STATUS_UNDERWAY);
        demandUnderwayFragment.setArguments(demandUnderwayBundle);
        demandUnderwayFragment.setOnDataLoadingCompletedListener(new DemandStatusFragment.OnDataLoadingCompletedListener() {
            @Override
            public void onShowDemandStatusListSize(int size) {
                sizeList.set(1, size);

                if (selectedTabPosition == 1) {
                    tabLayout.getTabAt(1).setText("进行中(" + String.valueOf(size) + ")");
                }
            }
        });

        demandCompletedFragment = new DemandStatusFragment();
        Bundle demandCompletedBundle = new Bundle();
        demandCompletedBundle.putInt(CommonConstant.EXTRA_DEMAND_STATUS, CommonConstant.DEMAND_STATUS_COMPLETED);
        demandCompletedFragment.setArguments(demandCompletedBundle);
        demandCompletedFragment.setOnDataLoadingCompletedListener(new DemandStatusFragment.OnDataLoadingCompletedListener() {
            @Override
            public void onShowDemandStatusListSize(int size) {
                sizeList.set(2, size);

                if (selectedTabPosition == 2) {
                    tabLayout.getTabAt(2).setText("已完成(" + String.valueOf(size) + ")");
                }
            }
        });

        demandExpiredFragment = new DemandStatusFragment();
        Bundle demandExpiredBundle = new Bundle();
        demandExpiredBundle.putInt(CommonConstant.EXTRA_DEMAND_STATUS, CommonConstant.DEMAND_STATUS_EXPIRED);
        demandExpiredFragment.setArguments(demandExpiredBundle);
        demandExpiredFragment.setOnDataLoadingCompletedListener(new DemandStatusFragment.OnDataLoadingCompletedListener() {
            @Override
            public void onShowDemandStatusListSize(int size) {
                sizeList.set(3, size);

                if (selectedTabPosition == 3) {
                    tabLayout.getTabAt(3).setText("已过期(" + String.valueOf(size) + ")");
                }
            }
        });

        demandStatusFragmentList.add(demandInTheBiddingFragment);
        demandStatusFragmentList.add(demandUnderwayFragment);
        demandStatusFragmentList.add(demandCompletedFragment);
        demandStatusFragmentList.add(demandExpiredFragment);
        return demandStatusFragmentList;
    }

    public void setViewPagerCurrentItem(int item) {
        if (item == demandStatus) {
            int size = sizeList.get(item);

            switch (item) {
                case 0:
                    tabLayout.getTabAt(0).setText("竞标中(" + String.valueOf(size) + ")");
                    tabLayout.getTabAt(1).setText("进行中");
                    tabLayout.getTabAt(2).setText("已完成");
                    tabLayout.getTabAt(3).setText("已过期");

                    if (null != demandInTheBiddingFragment) {
                        demandInTheBiddingFragment.initData();
                    }
                    break;

                case 1:
                    tabLayout.getTabAt(0).setText("竞标中");
                    tabLayout.getTabAt(1).setText("进行中(" + String.valueOf(size) + ")");
                    tabLayout.getTabAt(2).setText("已完成");
                    tabLayout.getTabAt(3).setText("已过期");

                    if (null != demandUnderwayFragment) {
                        demandUnderwayFragment.initData();
                    }
                    break;

                case 2:
                    tabLayout.getTabAt(0).setText("竞标中");
                    tabLayout.getTabAt(1).setText("进行中");
                    tabLayout.getTabAt(2).setText("已完成(" + String.valueOf(size) + ")");
                    tabLayout.getTabAt(3).setText("已过期");

                    if (null != demandCompletedFragment) {
                        demandCompletedFragment.initData();
                    }
                    break;

                case 3:
                    tabLayout.getTabAt(0).setText("竞标中");
                    tabLayout.getTabAt(1).setText("进行中");
                    tabLayout.getTabAt(2).setText("已完成");
                    tabLayout.getTabAt(3).setText("已过期(" + String.valueOf(size) + ")");

                    if (null != demandExpiredFragment) {
                        demandExpiredFragment.initData();
                    }
                    break;
            }
        } else if (null != viewPager) {
            viewPager.setCurrentItem(item, true);
        }
    }

    private void registerDemandManagementFragmentBroadcastReceiver() {
        demandManagementFragmentBroadcastReceiver = new DemandManagementFragmentBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommonConstant.ACTION_DEMAND_STATUS);
        getParentContext().registerReceiver(demandManagementFragmentBroadcastReceiver, intentFilter);
    }

    private class DemandManagementFragmentBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent) {
                String action = intent.getAction();

                if (!TextUtils.isEmpty(action) && action.equals(CommonConstant.ACTION_DEMAND_STATUS)) {
                    int serviceStatus = intent.getIntExtra(CommonConstant.EXTRA_DEMAND_STATUS, CommonConstant.DEMAND_STATUS_IN_THE_BIDDING);

                    switch (serviceStatus) {
                        case CommonConstant.DEMAND_STATUS_IN_THE_BIDDING:
                            setViewPagerCurrentItem(0);
                            break;

                        case CommonConstant.DEMAND_STATUS_UNDERWAY:
                            setViewPagerCurrentItem(1);
                            break;

                        case CommonConstant.DEMAND_STATUS_COMPLETED:
                            setViewPagerCurrentItem(2);
                            break;

                        case CommonConstant.DEMAND_STATUS_EXPIRED:
                            setViewPagerCurrentItem(3);
                            break;
                    }
                }
            }
        }
    }

    @OnClick({R.id.btn_go_to_publish_demand})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_go_to_publish_demand:
                demandManagementFirstEnterRL.setVisibility(View.GONE);
                demandManagementLL.setVisibility(View.VISIBLE);

                UserPreferences.getInstance().setIsFirstEnterDemandManagement(false);
                break;
        }
    }

    @Override
    public void onShowCompletedBasicPersonalInformation() {
        Intent intent = new Intent(getParentContext(), PublishDemandActivity.class);
        startActivity(intent);
    }

    @Override
    public void onShowBasicPersonalInformationBean(CheckBasicPersonalInformationBean checkBasicPersonalInformationBean) {
        Intent intent = new Intent(getParentContext(), CompletePersonalInformationActivity.class);
        intent.putExtra(CommonConstant.EXTRA_COMPLETE_PERSONAL_INFORMATION_TYPE, CommonConstant.COMPLETE_PERSONAL_INFORMATION_TYPE_COMPLETE);
        intent.putExtra(CommonConstant.EXTRA_PUBLISH_TYPE, CommonConstant.PUBLISH_TYPE_PUBLISH_DEMAND);
        intent.putExtra(CommonConstant.EXTRA_CHECK_BASIC_PERSONAL_INFORMATION_BEAN, checkBasicPersonalInformationBean);
        startActivity(intent);
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
                demandStatus = bundle.getInt(CommonConstant.EXTRA_DEMAND_STATUS, CommonConstant.DEMAND_STATUS_IN_THE_BIDDING);
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (null != demandManagementFragmentBroadcastReceiver) {
            getParentContext().unregisterReceiver(demandManagementFragmentBroadcastReceiver);
        }

        super.onDestroyView();
    }

}
