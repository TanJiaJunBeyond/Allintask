package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.ui.activity.BaseFragmentActivity;
import com.allintask.lingdao.ui.fragment.user.ForgetPaymentPasswordFourthStepFragment;
import com.allintask.lingdao.ui.fragment.user.ModifyPaymentPasswordFirstStepFragment;
import com.allintask.lingdao.ui.fragment.user.PaymentPasswordUnlockFragment;
import com.allintask.lingdao.ui.fragment.user.SetPaymentPasswordFirstStepFragment;
import com.allintask.lingdao.ui.fragment.user.SetPaymentPasswordSecondStepFragment;

import butterknife.BindView;

/**
 * Created by TanJiaJun on 2018/1/27.
 */

public class PaymentPasswordActivity extends BaseFragmentActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.ll_content)
    LinearLayout contentLL;

    private int mobileCountryCodeId;
    private int paymentPasswordType = CommonConstant.PAYMENT_PASSWORD_TYPE_SET_PAYMENT_PASSWORD;
    private String smsIdentifyCode;
    private String identifyCardNumber;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_payment_password;
    }

    @Override
    protected BasePresenter CreatePresenter() {
        return null;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            mobileCountryCodeId = intent.getIntExtra(CommonConstant.EXTRA_MOBILE_COUNTRY_CODE_ID, -1);
            paymentPasswordType = intent.getIntExtra(CommonConstant.EXTRA_PAYMENT_PASSWORD_TYPE, CommonConstant.PAYMENT_PASSWORD_TYPE_SET_PAYMENT_PASSWORD);
            smsIdentifyCode = intent.getStringExtra(CommonConstant.EXTRA_SMS_IDENTIFY_CODE);
            identifyCardNumber = intent.getStringExtra(CommonConstant.EXTRA_IDENTIFY_CARD_NUMBER);
        }

        initToolbar();
        initUI();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        switch (paymentPasswordType) {
            case CommonConstant.PAYMENT_PASSWORD_TYPE_SET_PAYMENT_PASSWORD:
                titleTv.setText(getString(R.string.set_new_payment_password));
                break;

            case CommonConstant.PAYMENT_PASSWORD_TYPE_MODIFY_PAYMENT_PASSWORD:
                titleTv.setText(getString(R.string.modify_payment_password));
                break;

            case CommonConstant.PAYMENT_PASSWORD_TYPE_FORGET_PAYMENT_PASSWORD:
                titleTv.setText(getString(R.string.forget_payment_password));
                break;

            case CommonConstant.PAYMENT_PASSWORD_TYPE_PAYMENT_PASSWORD_UNLOCK:
                titleTv.setText(getString(R.string.payment_password_unlock));
                break;

            case CommonConstant.PAYMENT_PASSWORD_TYPE_DELETE_GESTURE_PASSWORD:
                titleTv.setText(getString(R.string.verify_payment_password));
                break;
        }

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConstant.EXTRA_MOBILE_COUNTRY_CODE_ID, mobileCountryCodeId);

        switch (paymentPasswordType) {
            case CommonConstant.PAYMENT_PASSWORD_TYPE_SET_PAYMENT_PASSWORD:
                openFragment(SetPaymentPasswordFirstStepFragment.class.getName(), bundle);
                break;

            case CommonConstant.PAYMENT_PASSWORD_TYPE_MODIFY_PAYMENT_PASSWORD:
                openFragment(ModifyPaymentPasswordFirstStepFragment.class.getName(), bundle);
                break;

            case CommonConstant.PAYMENT_PASSWORD_TYPE_FORGET_PAYMENT_PASSWORD:
                bundle.putString(CommonConstant.EXTRA_SMS_IDENTIFY_CODE, smsIdentifyCode);
                bundle.putString(CommonConstant.EXTRA_IDENTIFY_CARD_NUMBER, identifyCardNumber);
                openFragment(ForgetPaymentPasswordFourthStepFragment.class.getName(), bundle);
                break;

            case CommonConstant.PAYMENT_PASSWORD_TYPE_PAYMENT_PASSWORD_UNLOCK:
            case CommonConstant.PAYMENT_PASSWORD_TYPE_DELETE_GESTURE_PASSWORD:
                bundle.putInt(CommonConstant.EXTRA_PAYMENT_PASSWORD_TYPE, paymentPasswordType);
                openFragment(PaymentPasswordUnlockFragment.class.getName(), bundle);
                break;
        }
    }

}
