package com.allintask.lingdao.ui.fragment.user;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.ModifyPaymentPasswordSecondStepPresenter;
import com.allintask.lingdao.ui.activity.user.PaymentPasswordActivity;
import com.allintask.lingdao.ui.fragment.BaseFragment;
import com.allintask.lingdao.utils.PhoneUtils;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.user.IModifyPaymentPasswordSecondStepView;
import com.allintask.lingdao.widget.EditTextWithDelete;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/3/22.
 */

public class ModifyPaymentPasswordSecondStepFragment extends BaseFragment<IModifyPaymentPasswordSecondStepView, ModifyPaymentPasswordSecondStepPresenter> implements IModifyPaymentPasswordSecondStepView {

    @BindView(R.id.tv_phone_number)
    TextView phoneNumberTv;
    @BindView(R.id.etwd_sms_identify_code)
    EditTextWithDelete smsIdentifyCodeETWD;
    @BindView(R.id.btn_get_sms_identify_code)
    Button getSmsIdentifyCodeBtn;
    @BindView(R.id.btn_next_step)
    Button nextStepBtn;

    private int mobileCountryCodeId = -1;
    private String oldPaymentPassword;

    private String phoneNumber;
    private String smsIdentifyCode;

    private CountDownTimer countDownTimer;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_modify_payment_password_second_step;
    }

    @Override
    protected ModifyPaymentPasswordSecondStepPresenter CreatePresenter() {
        return new ModifyPaymentPasswordSecondStepPresenter();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (null != bundle) {
            mobileCountryCodeId = bundle.getInt(CommonConstant.EXTRA_MOBILE_COUNTRY_CODE_ID, -1);
            oldPaymentPassword = bundle.getString(CommonConstant.EXTRA_OLD_PAYMENT_PASSWORD);
        }

        initUI();
    }

    private void initUI() {
        phoneNumber = UserPreferences.getInstance().getPhoneNumber();

        if (!TextUtils.isEmpty(phoneNumber)) {
            String desensitizationPhoneNumber = PhoneUtils.maskPhone(phoneNumber);
            phoneNumberTv.setText(desensitizationPhoneNumber);
        }

        smsIdentifyCodeETWD.requestFocus();
        smsIdentifyCodeETWD.requestFocusFromTouch();
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

                if (!TextUtils.isEmpty(smsIdentifyCode)) {
                    nextStepBtn.setEnabled(true);
                    nextStepBtn.setClickable(true);
                }
            }
        });

        nextStepBtn.setEnabled(false);
        nextStepBtn.setClickable(false);
    }

    @OnClick({R.id.btn_get_sms_identify_code, R.id.btn_next_step})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_sms_identify_code:
                if (mobileCountryCodeId != -1) {
                    if (!TextUtils.isEmpty(phoneNumber)) {
                        mPresenter.sendModifyPaymentPasswordSmsIdentifyCodeRequest(mobileCountryCodeId, phoneNumber);
                    }
                } else {
                    showToast("请选择手机号码归属地");
                }
                break;

            case R.id.btn_next_step:
                mPresenter.checkModifyPaymentPasswordSmsIdentifyCodeRequest(smsIdentifyCode);
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
    public void onCheckSmsIdentifyCodeSuccess() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConstant.EXTRA_OLD_PAYMENT_PASSWORD, oldPaymentPassword);
        bundle.putString(CommonConstant.EXTRA_SMS_IDENTIFY_CODE, smsIdentifyCode);
        ((PaymentPasswordActivity) getParentContext()).openFragment(ModifyPaymentPasswordThirdStepFragment.class.getName(), bundle);
    }

    @Override
    public void onSendSmsIdentifyCodeSuccess() {
        showToast("发送验证码成功");
        countDownTimer = mPresenter.startCountDownTimer();
    }

    @Override
    public void onDestroy() {
        if (null != countDownTimer) {
            countDownTimer.cancel();
        }

        super.onDestroy();
    }

}
