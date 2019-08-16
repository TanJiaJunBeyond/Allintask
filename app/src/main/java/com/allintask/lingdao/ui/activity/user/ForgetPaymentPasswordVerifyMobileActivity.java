package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.ForgetPaymentPasswordVerifyMobilePresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.view.user.IForgetPaymentPasswordVerifyMobileView;
import com.allintask.lingdao.widget.TransactPasswordEditText;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/7/6.
 */

public class ForgetPaymentPasswordVerifyMobileActivity extends BaseActivity<IForgetPaymentPasswordVerifyMobileView, ForgetPaymentPasswordVerifyMobilePresenter> implements IForgetPaymentPasswordVerifyMobileView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tv_phone_number)
    TextView phoneNumberTv;
    @BindView(R.id.tpet_identify_code)
    TransactPasswordEditText identifyCodeTPET;
    @BindView(R.id.tv_send_identify_code)
    TextView sendIdentifyCodeTv;

    private InputMethodManager inputMethodManager;
    private CountDownTimer countDownTimer;
    private String identifyCode;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_forget_payment_password_verify_mobile;
    }

    @Override
    protected ForgetPaymentPasswordVerifyMobilePresenter CreatePresenter() {
        return new ForgetPaymentPasswordVerifyMobilePresenter();
    }

    @Override
    protected void init() {
        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.verify_mobile));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        inputMethodManager = (InputMethodManager) getParentContext().getSystemService(INPUT_METHOD_SERVICE);

        identifyCodeTPET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                identifyCode = identifyCodeTPET.getText().toString().trim();
                int identifyCodeLength = identifyCode.length();

                if (identifyCodeLength == 4) {
                    mPresenter.checkForgetPaymentPasswordIdentifyCodeRequest(identifyCode);
                }
            }
        });
    }

    private void initData() {
        mPresenter.getHiddenUserDataRequest();
        mPresenter.sendForgetPaymentPasswordIdentifyCodeRequest();
    }

    @OnClick({R.id.tv_send_identify_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_identify_code:
                String sendIdentifyCode = sendIdentifyCodeTv.getText().toString().trim();

                if (!sendIdentifyCode.equals(getString(R.string.resend_identify_code))) {
                    mPresenter.sendForgetPaymentPasswordIdentifyCodeRequest();
                }
                break;
        }
    }

    @Override
    public void onShowHiddenMobile(String hiddenMobile) {
        phoneNumberTv.setText(hiddenMobile);
        phoneNumberTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSendForgetPaymentPasswordIdentifyCodeSuccess() {
        showToast("发送验证码成功");
        countDownTimer = mPresenter.startCountDownTimer();
    }

    @Override
    public void onCountDownTimerTick(long millisUntilFinished) {
        sendIdentifyCodeTv.setText(String.valueOf(millisUntilFinished / 1000) + "秒后重发");
    }

    @Override
    public void onCountDownTimerFinish() {
        sendIdentifyCodeTv.setText(getString(R.string.resend_identify_code));
    }

    @Override
    public void onCheckForgetPaymentPasswordIdentifyCodeSuccess() {
        Intent intent = new Intent(getParentContext(), ForgetPaymentPasswordVerifyIdentifyActivity.class);
        intent.putExtra(CommonConstant.EXTRA_SMS_IDENTIFY_CODE, identifyCode);
        startActivity(intent);

        finish();
    }

    @Override
    public void onCheckForgetPaymentPasswordIdentifyCodeFail(String errorMessage) {
        showToast(errorMessage);
        inputMethodManager.showSoftInput(identifyCodeTPET, InputMethodManager.SHOW_FORCED);
        identifyCodeTPET.reset();
    }

    @Override
    protected void onDestroy() {
        if (null != countDownTimer) {
            countDownTimer.cancel();
        }

        super.onDestroy();
    }

}
