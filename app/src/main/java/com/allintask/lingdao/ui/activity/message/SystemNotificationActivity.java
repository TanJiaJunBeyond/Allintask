package com.allintask.lingdao.ui.activity.message;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.presenter.message.SystemNotificationPresenter;
import com.allintask.lingdao.ui.activity.BaseSwipeRefreshActivity;
import com.allintask.lingdao.ui.adapter.message.SystemNotificationAdapter;
import com.allintask.lingdao.view.message.ISystemNotificationView;

import butterknife.BindView;

/**
 * Created by TanJiaJun on 2018/1/12.
 */

public class SystemNotificationActivity extends BaseSwipeRefreshActivity<ISystemNotificationView, SystemNotificationPresenter> implements ISystemNotificationView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;

    private SystemNotificationAdapter systemNotificationAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_system_notification;
    }

    @Override
    protected SystemNotificationPresenter CreatePresenter() {
        return new SystemNotificationPresenter();
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

        titleTv.setText(getString(R.string.system_notification));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        systemNotificationAdapter = new SystemNotificationAdapter(getParentContext());
        recycler_view.setAdapter(systemNotificationAdapter);
    }

    @Override
    protected void onLoadMore() {

    }

    @Override
    protected void onSwipeRefresh() {

    }

}
