package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.ForgetPasswordPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.view.user.IForgetPasswordView;
import com.allintask.lingdao.widget.EditPasswordView;
import com.allintask.lingdao.widget.EditTextWithDelete;
import com.sina.weibo.sdk.utils.MD5;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.MD5Utils;

/**
 * Created by TanJiaJun on 2017/12/29.
 */

public class ForgetPasswordActivity extends BaseActivity<IForgetPasswordView, ForgetPasswordPresenter> implements IForgetPasswordView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.ll_phone_number_home_location)
    LinearLayout phoneNumberHomeLocationLL;
    @BindView(R.id.tv_phone_number_home_location)
    TextView phoneNumberHomeLocationTv;
    @BindView(R.id.etwd_phone_number)
    EditTextWithDelete phoneNumberETWD;
    @BindView(R.id.etwd_sms_identify_code)
    EditTextWithDelete smsIdentifyCodeETWD;
    @BindView(R.id.btn_get_sms_identify_code)
    Button getSmsIdentifyCodeBtn;
    @BindView(R.id.epwv_set_new_password)
    EditPasswordView setNewPasswordEPWV;
    @BindView(R.id.epwv_confirm_new_password)
    EditPasswordView confirmNewPasswordEPWV;
    @BindView(R.id.btn_confirm_set)
    Button confirmSetBtn;

    private CountDownTimer countDownTimer;
    private int mobileCountryCodeId;
    private String regularEx;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected ForgetPasswordPresenter CreatePresenter() {
        return new ForgetPasswordPresenter();
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

        titleTv.setText(getString(R.string.forget_password));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkConfirmEnable();
            }
        };

        phoneNumberETWD.addTextChangedListener(textWatcher);
        smsIdentifyCodeETWD.addTextChangedListener(textWatcher);
        setNewPasswordEPWV.getEditTextWithDel().addTextChangedListener(textWatcher);
        confirmNewPasswordEPWV.getEditTextWithDel().addTextChangedListener(textWatcher);

        confirmSetBtn.setEnabled(false);
        confirmSetBtn.setClickable(false);
    }

    private void initData() {
        mPresenter.fetchDefaultPhoneNumberHomeLocationRequest();
    }

    private void checkConfirmEnable() {
        String phoneNumber = phoneNumberETWD.getText().toString().trim();
        String smsIdentifyCode = smsIdentifyCodeETWD.getText().toString().trim();
        String setNewPassword = setNewPasswordEPWV.getPassword();
        String confirmNewPassword = setNewPasswordEPWV.getPassword();

        if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(smsIdentifyCode) && !TextUtils.isEmpty(setNewPassword) && !TextUtils.isEmpty(confirmNewPassword)) {
            confirmSetBtn.setEnabled(true);
            confirmSetBtn.setClickable(true);
        } else {
            confirmSetBtn.setEnabled(false);
            confirmSetBtn.setClickable(false);
        }
    }

    @OnClick({R.id.ll_phone_number_home_location, R.id.btn_get_sms_identify_code, R.id.btn_confirm_set})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_phone_number_home_location:
                Intent phoneNumberHomeLocationIntent = new Intent(getParentContext(), PhoneNumberHomeLocationActivity.class);
                startActivityForResult(phoneNumberHomeLocationIntent, CommonConstant.REQUEST_CODE);
                break;

            case R.id.btn_get_sms_identify_code:
                String getSmsIdentifyCodePhoneNumber = phoneNumberETWD.getText().toString().trim();

                if (mobileCountryCodeId != 0) {
                    if (!TextUtils.isEmpty(getSmsIdentifyCodePhoneNumber)) {
                        if (TextUtils.isEmpty(regularEx)) {
                            mPresenter.sendForgetPasswordSmsIdentifyCodeRequest(mobileCountryCodeId, getSmsIdentifyCodePhoneNumber);
                        } else if (getSmsIdentifyCodePhoneNumber.matches(regularEx)) {
                            mPresenter.sendForgetPasswordSmsIdentifyCodeRequest(mobileCountryCodeId, getSmsIdentifyCodePhoneNumber);
                        } else {
                            showToast("手机号码格式不正确");
                        }
                    } else {
                        showToast("请填写手机号码");
                    }
                } else {
                    showToast("请选择手机号归属地");
                }
                break;

            case R.id.btn_confirm_set:
                String phoneNumber = phoneNumberETWD.getText().toString().trim();
                String identifyCode = smsIdentifyCodeETWD.getText().toString().trim();
                String setNewPassword = setNewPasswordEPWV.getPassword();
                String confirmNewPassword = confirmNewPasswordEPWV.getPassword();

                String MD5SetNewPassword = MD5.hexdigest(setNewPassword);
                String MD5ConfirmNewPassword = MD5.hexdigest(confirmNewPassword);

                if (mobileCountryCodeId != -1) {
                    if (TextUtils.isEmpty(regularEx)) {
                        if (setNewPassword.equals(confirmNewPassword)) {
                            int setNewPasswordLength = setNewPassword.length();

                            if (setNewPasswordLength >= 6) {
                                mPresenter.resetLoginPasswordRequest(mobileCountryCodeId, phoneNumber, identifyCode, MD5SetNewPassword, MD5ConfirmNewPassword);
                            } else {
                                showToast("密码长度必须大于6位、小于16位");
                            }
                        } else {
                            showToast("新密码和确认密码不一致");
                        }
                    } else {
                        if (phoneNumber.matches(regularEx)) {
                            if (setNewPassword.equals(confirmNewPassword)) {
                                int setNewPasswordLength = setNewPassword.length();

                                if (setNewPasswordLength >= 6) {
                                    mPresenter.resetLoginPasswordRequest(mobileCountryCodeId, phoneNumber, identifyCode, MD5SetNewPassword, MD5ConfirmNewPassword);
                                } else {
                                    showToast("密码长度必须大于6位、小于16位");
                                }
                            } else {
                                showToast("新密码和确认密码不一致");
                            }
                        } else {
                            showToast("手机号码格式不正确");
                        }
                    }
                } else {
                    showToast("请选择手机号归属地");
                }
                break;
        }
    }

    @Override
    public void onShowPhoneNumberHomeLocationMobileCountryCodeId(int mobileCountryCodeId) {
        this.mobileCountryCodeId = mobileCountryCodeId;
    }

    @Override
    public void onShowPhoneNumberHomeLocationValue(String value) {
        phoneNumberHomeLocationTv.setText(value);
    }

    @Override
    public void onShowPhoneNumberHomeLocationRegularEx(String regularEx) {
        this.regularEx = regularEx;
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
    public void onResetLoginPasswordSuccess() {
        finish();
    }

    @Override
    public void onSendSmsIdentifyCodeSuccess() {
        showToast("发送验证码成功");
        countDownTimer = mPresenter.startCountDownTimer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CommonConstant.REQUEST_CODE && resultCode == CommonConstant.RESULT_CODE && null != data) {
            mobileCountryCodeId = data.getIntExtra(CommonConstant.EXTRA_PHONE_NUMBER_HOME_LOCATION_MOBILE_COUNTRY_CODE_ID, 0);
            String value = data.getStringExtra(CommonConstant.EXTRA_PHONE_NUMBER_HOME_LOCATION_VALUE);
            regularEx = data.getStringExtra(CommonConstant.EXTRA_PHONE_NUMBER_HOME_LOCATION_REGULAR_EX);

            phoneNumberHomeLocationTv.setText(value);
        }
    }

    @Override
    protected void onDestroy() {
        if (null != countDownTimer) {
            countDownTimer.cancel();
        }

        super.onDestroy();
    }

}
