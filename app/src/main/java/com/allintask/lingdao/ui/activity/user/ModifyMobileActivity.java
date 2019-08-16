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
import com.allintask.lingdao.presenter.user.ModifyMobilePresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.utils.PhoneUtils;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.user.IModifyMobileView;
import com.allintask.lingdao.widget.EditPasswordView;
import com.allintask.lingdao.widget.EditTextWithDelete;
import com.sina.weibo.sdk.utils.MD5;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.MD5Utils;

/**
 * Created by TanJiaJun on 2018/1/26.
 */

public class ModifyMobileActivity extends BaseActivity<IModifyMobileView, ModifyMobilePresenter> implements IModifyMobileView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tv_phone_number)
    TextView phoneNumberTv;
    @BindView(R.id.epv_password)
    EditPasswordView passwordEPV;
    @BindView(R.id.ll_phone_number_home_location)
    LinearLayout phoneNumberHomeLocationLL;
    @BindView(R.id.tv_phone_number_home_location)
    TextView phoneNumberHomeLocationTv;
    @BindView(R.id.etwd_new_phone_number)
    EditTextWithDelete newPhoneNumberETWD;
    @BindView(R.id.etwd_sms_identify_code)
    EditTextWithDelete smsIdentifyCodeETWD;
    @BindView(R.id.btn_get_sms_identify_code)
    Button getSmsIdentifyCodeBtn;
    @BindView(R.id.btn_confirm_modify_phone_number)
    Button confirmModifyPhoneNumberBtn;

    private int oldMobileCountryCodeId;
    private String phoneNumber;

    private CountDownTimer countDownTimer;
    private int mobileCountryCodeId;
    private String regularEx;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_modify_mobile;
    }

    @Override
    protected ModifyMobilePresenter CreatePresenter() {
        return new ModifyMobilePresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            oldMobileCountryCodeId = intent.getIntExtra(CommonConstant.EXTRA_MOBILE_COUNTRY_CODE_ID, -1);
        }

        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.modify_mobile));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        phoneNumber = UserPreferences.getInstance().getPhoneNumber();

        if (!TextUtils.isEmpty(phoneNumber)) {
            String desensitizationPhoneNumber = PhoneUtils.maskPhone(phoneNumber);
            phoneNumberTv.setText(desensitizationPhoneNumber);
        }

        passwordEPV.getEditTextWithDel().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkConfirmModifyPhoneNumberEnable();
            }
        });

        newPhoneNumberETWD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkConfirmModifyPhoneNumberEnable();
            }
        });

        smsIdentifyCodeETWD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkConfirmModifyPhoneNumberEnable();
            }
        });

        confirmModifyPhoneNumberBtn.setEnabled(false);
        confirmModifyPhoneNumberBtn.setClickable(false);
    }

    private void initData() {
        mPresenter.fetchDefaultPhoneNumberHomeLocationRequest();
    }

    private void checkConfirmModifyPhoneNumberEnable() {
        String password = passwordEPV.getEditTextWithDel().getText().toString().trim();
        String newPhoneNumber = newPhoneNumberETWD.getText().toString().trim();
        String smsIdentifyCode = smsIdentifyCodeETWD.getText().toString().trim();

        if (oldMobileCountryCodeId != -1 && !TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(password) && mobileCountryCodeId != -1 && !TextUtils.isEmpty(newPhoneNumber) && !TextUtils.isEmpty(smsIdentifyCode)) {
            confirmModifyPhoneNumberBtn.setEnabled(true);
            confirmModifyPhoneNumberBtn.setClickable(true);
        } else {
            confirmModifyPhoneNumberBtn.setEnabled(false);
            confirmModifyPhoneNumberBtn.setClickable(false);
        }
    }

    @OnClick({R.id.ll_phone_number_home_location, R.id.btn_get_sms_identify_code, R.id.btn_confirm_modify_phone_number})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_phone_number_home_location:
                Intent phoneNumberHomeLocationIntent = new Intent(getParentContext(), PhoneNumberHomeLocationActivity.class);
                startActivityForResult(phoneNumberHomeLocationIntent, CommonConstant.REQUEST_CODE);
                break;

            case R.id.btn_get_sms_identify_code:
                String getSmsIdentifyCodePhoneNumberHomeLocation = phoneNumberHomeLocationTv.getText().toString().trim();
                String getSmsIdentifyCodePhoneNumber = newPhoneNumberETWD.getText().toString().trim();

                if (mobileCountryCodeId != 0 && !TextUtils.isEmpty(getSmsIdentifyCodePhoneNumberHomeLocation)) {
                    if (!TextUtils.isEmpty(getSmsIdentifyCodePhoneNumber)) {
                        mPresenter.sendModifyPhoneNumberSmsIdentifyCodeRequest(mobileCountryCodeId, getSmsIdentifyCodePhoneNumber);
                    } else {
                        showToast("新的手机号码不能为空");
                    }
                } else {
                    showToast("请选择手机号归属地");
                }
                break;

            case R.id.btn_confirm_modify_phone_number:
                String password = passwordEPV.getPassword();
                String MD5Password = MD5.hexdigest(password);
                String newPhoneNumber = newPhoneNumberETWD.getText().toString().trim();
                String smsIdentifyCode = smsIdentifyCodeETWD.getText().toString().trim();

                if (mobileCountryCodeId != -1) {
                    if (!TextUtils.isEmpty(MD5Password) && !TextUtils.isEmpty(newPhoneNumber) && !TextUtils.isEmpty(smsIdentifyCode)) {
                        if (TextUtils.isEmpty(regularEx)) {
                            mPresenter.modifyPhoneNumberRequest(oldMobileCountryCodeId, phoneNumber, MD5Password, mobileCountryCodeId, newPhoneNumber, smsIdentifyCode);
                        } else {
                            if (newPhoneNumber.matches(regularEx)) {
                                mPresenter.modifyPhoneNumberRequest(oldMobileCountryCodeId, phoneNumber, MD5Password, mobileCountryCodeId, newPhoneNumber, smsIdentifyCode);
                            } else {
                                showToast("手机号码格式不正确");
                            }
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
    public void onModifyPhoneNumberSuccess() {
        setResult(CommonConstant.REFRESH_RESULT_CODE);
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
