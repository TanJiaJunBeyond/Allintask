package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.ui.activity.BaseFragmentActivity;
import com.allintask.lingdao.ui.fragment.user.GesturePasswordUnlockFragment;
import com.allintask.lingdao.ui.fragment.user.PaymentPasswordUnlockFragment;
import com.allintask.lingdao.ui.fragment.user.SetGesturePasswordFirstStepFragment;

import butterknife.BindView;

/**
 * Created by TanJiaJun on 2018/5/24.
 */

public class GesturePasswordActivity extends BaseFragmentActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;

    private int type;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_gesture_password;
    }

    @Override
    protected BasePresenter CreatePresenter() {
        return null;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            type = intent.getIntExtra(CommonConstant.EXTRA_GESTURE_PASSWORD_TYPE, CommonConstant.SET_GESTURE_PASSWORD);
        }

        initToolbar();
        initUI();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        if (type == CommonConstant.SET_GESTURE_PASSWORD) {
            titleTv.setText(getString(R.string.verify_payment_password));
        } else {
            titleTv.setText(getString(R.string.gesture_password));
        }

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        switch (type) {
            case CommonConstant.SET_GESTURE_PASSWORD:
                Bundle bundle = new Bundle();
                bundle.putInt(CommonConstant.EXTRA_PAYMENT_PASSWORD_TYPE, CommonConstant.PAYMENT_PASSWORD_TYPE_SET_GESTURE_PASSWORD);
                openFragment(PaymentPasswordUnlockFragment.class.getName(), bundle);
                break;

            case CommonConstant.MODIFY_GESTURE_PASSWORD:

                break;

            case CommonConstant.GESTURE_PASSWORD_LOCK:
                openFragment(GesturePasswordUnlockFragment.class.getName(), null);
                break;
        }
    }

    public void setTitle(String title) {
        titleTv.setText(title);
    }

}
