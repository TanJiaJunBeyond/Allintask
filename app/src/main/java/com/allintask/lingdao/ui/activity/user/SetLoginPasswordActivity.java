package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.SetLoginPasswordPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.utils.PhoneUtils;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.user.ISetLoginPasswordView;
import com.allintask.lingdao.widget.EditPasswordView;
import com.allintask.lingdao.widget.EditTextWithDelete;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.sina.weibo.sdk.utils.MD5;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.MD5Utils;

/**
 * Created by TanJiaJun on 2018/1/27.
 */

public class SetLoginPasswordActivity extends BaseActivity<ISetLoginPasswordView, SetLoginPasswordPresenter> implements ISetLoginPasswordView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tv_phone_number)
    TextView phoneNumberTv;
    @BindView(R.id.etwd_sms_identify_code)
    EditTextWithDelete smsIdentifyCodeETWD;
    @BindView(R.id.btn_get_sms_identify_code)
    Button getSmsIdentifyCodeBtn;
    @BindView(R.id.epv_login_password)
    EditPasswordView loginPasswordEPV;
    @BindView(R.id.epv_confirm_login_password)
    EditPasswordView confirmLoginPasswordEPV;
    @BindView(R.id.btn_confirm_set)
    Button confirmSetBtn;

    private int mobileCountryCodeId = -1;

    private String phoneNumber;
    private String smsIdentifyCode;
    private String password;
    private String confirmPassword;
    private String MD5Password;

    private CountDownTimer countDownTimer;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set_login_password;
    }

    @Override
    protected SetLoginPasswordPresenter CreatePresenter() {
        return new SetLoginPasswordPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            mobileCountryCodeId = intent.getIntExtra(CommonConstant.EXTRA_MOBILE_COUNTRY_CODE_ID, -1);
        }

        initToolbar();
        initUI();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.set_login_password));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        phoneNumber = UserPreferences.getInstance().getPhoneNumber();

        if (!TextUtils.isEmpty(phoneNumber)) {
            String desensitizationPhoneNumber = PhoneUtils.maskPhone(phoneNumber);
            phoneNumberTv.setText(desensitizationPhoneNumber);
        }

        smsIdentifyCodeETWD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                smsIdentifyCode = smsIdentifyCodeETWD.getText().toString().trim();
                checkSetLoginPasswordModifyEnable();
            }
        });

        loginPasswordEPV.getEditTextWithDel().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password = loginPasswordEPV.getEditTextWithDel().getText().toString().trim();
                checkSetLoginPasswordModifyEnable();
            }
        });

        confirmLoginPasswordEPV.getEditTextWithDel().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                confirmPassword = confirmLoginPasswordEPV.getEditTextWithDel().getText().toString().trim();
                checkSetLoginPasswordModifyEnable();
            }
        });

        confirmSetBtn.setEnabled(false);
        confirmSetBtn.setClickable(false);
    }

    private void checkSetLoginPasswordModifyEnable() {
        if (mobileCountryCodeId != -1 && !TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(smsIdentifyCode) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword)) {
            confirmSetBtn.setEnabled(true);
            confirmSetBtn.setClickable(true);
        } else {
            confirmSetBtn.setEnabled(false);
            confirmSetBtn.setClickable(false);
        }
    }

    @OnClick({R.id.btn_get_sms_identify_code, R.id.btn_confirm_set})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_sms_identify_code:
                if (mobileCountryCodeId != 0) {
                    mPresenter.sendSetLoginPasswordSmsIdentifyCodeRequest(mobileCountryCodeId, phoneNumber);
                } else {
                    showToast("请选择手机号归属地");
                }
                break;

            case R.id.btn_confirm_set:
                MD5Password = MD5.hexdigest(password);
                String MD5ConfirmPassword = MD5.hexdigest(confirmPassword);

                if (!TextUtils.isEmpty(smsIdentifyCode) && !TextUtils.isEmpty(MD5Password) && !TextUtils.isEmpty(MD5ConfirmPassword)) {
                    mPresenter.setLoginPasswordRequest(smsIdentifyCode, MD5Password, MD5ConfirmPassword);
                }
                break;
        }
    }

    @Override
    public void onCountDownTimerTick(long millisUntilFinished) {
        getSmsIdentifyCodeBtn.setEnabled(false);
        getSmsIdentifyCodeBtn.setClickable(false);
        getSmsIdentifyCodeBtn.setText(String.valueOf(millisUntilFinished / 1000) + "s");
    }

    @Override
    public void onCountDownTimerFinish() {
        getSmsIdentifyCodeBtn.setEnabled(true);
        getSmsIdentifyCodeBtn.setClickable(true);
        getSmsIdentifyCodeBtn.setText("获取");
    }

    @Override
    public void onSetLoginPasswordSuccess() {
        showToast("设置登录密码成功");

        if (!TextUtils.isEmpty(MD5Password)) {
            UserPreferences.getInstance().setEMChatPassword(MD5Password);

            int userId = UserPreferences.getInstance().getUserId();
            EMClient.getInstance().login(String.valueOf(userId), MD5Password, new EMCallBack() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        }

        setResult(CommonConstant.REFRESH_RESULT_CODE);
        finish();
    }

    @Override
    public void onSendSmsIdentifyCodeSuccess() {
        showToast("发送验证码成功");
        countDownTimer = mPresenter.startCountDownTimer();
    }

    @Override
    protected void onDestroy() {
        if (null != countDownTimer) {
            countDownTimer.cancel();
        }

        super.onDestroy();
    }

}
