package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.presenter.user.SelectResetPaymentPasswordWayPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.view.user.ISelectResetPaymentPasswordWayView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/7/5.
 */

public class SelectResetPaymentPasswordWayActivity extends BaseActivity<ISelectResetPaymentPasswordWayView, SelectResetPaymentPasswordWayPresenter> implements ISelectResetPaymentPasswordWayView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.rl_sms_identify_code_and_identify_card_number)
    RelativeLayout smsIdentifyCodeAndIdentifyCardNumberRL;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_select_reset_payment_password_way;
    }

    @Override
    protected SelectResetPaymentPasswordWayPresenter CreatePresenter() {
        return new SelectResetPaymentPasswordWayPresenter();
    }

    @Override
    protected void init() {
        initToolbar();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.select_reset_way));

        setSupportActionBar(toolbar);
    }

    @OnClick({R.id.rl_sms_identify_code_and_identify_card_number})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_sms_identify_code_and_identify_card_number:
                Intent intent = new Intent(getParentContext(), ForgetPaymentPasswordVerifyMobileActivity.class);
                startActivity(intent);

                finish();
                break;
        }
    }

}
