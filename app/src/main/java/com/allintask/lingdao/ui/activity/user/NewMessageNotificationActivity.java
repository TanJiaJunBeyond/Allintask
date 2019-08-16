package com.allintask.lingdao.ui.activity.user;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.presenter.user.NewMessageNotificationPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.view.user.INewMessageNotificationView;

import butterknife.BindView;

/**
 * Created by TanJiaJun on 2018/1/27.
 */

public class NewMessageNotificationActivity extends BaseActivity<INewMessageNotificationView, NewMessageNotificationPresenter> implements INewMessageNotificationView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_new_message_notification;
    }

    @Override
    protected NewMessageNotificationPresenter CreatePresenter() {
        return new NewMessageNotificationPresenter();
    }

    @Override
    protected void init() {
        initToolbar();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.new_message_notification));

        setSupportActionBar(toolbar);
    }

}
