package com.allintask.lingdao.ui.fragment.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.ForgetPaymentPasswordFifthStepPresenter;
import com.allintask.lingdao.ui.activity.user.PaymentPasswordActivity;
import com.allintask.lingdao.ui.fragment.BaseFragment;
import com.allintask.lingdao.view.user.IForgetPaymentPasswordFifthStepView;
import com.allintask.lingdao.widget.PaymentPasswordEditText;
import com.sina.weibo.sdk.utils.MD5;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by TanJiaJun on 2018/7/9.
 */

public class ForgetPaymentPasswordFifthStepFragment extends BaseFragment<IForgetPaymentPasswordFifthStepView, ForgetPaymentPasswordFifthStepPresenter> implements IForgetPaymentPasswordFifthStepView {

    @BindView(R.id.ppet_confirm_new_payment_password)
    PaymentPasswordEditText confirmNewPaymentPasswordPPET;

    private String smsIdentifyCode;
    private String identifyCardNumber;
    private String setNewPaymentPassword;

    private InputMethodManager inputMethodManager;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_forget_payment_password_fifth_step;
    }

    @Override
    protected ForgetPaymentPasswordFifthStepPresenter CreatePresenter() {
        return new ForgetPaymentPasswordFifthStepPresenter();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (null != bundle) {
            smsIdentifyCode = bundle.getString(CommonConstant.EXTRA_SMS_IDENTIFY_CODE);
            identifyCardNumber = bundle.getString(CommonConstant.EXTRA_IDENTIFY_CARD_NUMBER);
            setNewPaymentPassword = bundle.getString(CommonConstant.EXTRA_SET_NEW_PAYMENT_PASSWORD);
        }

        initUI();
    }

    private void initUI() {
        inputMethodManager = (InputMethodManager) getParentContext().getSystemService(INPUT_METHOD_SERVICE);

        confirmNewPaymentPasswordPPET.requestFocus();
        confirmNewPaymentPasswordPPET.requestFocusFromTouch();
        confirmNewPaymentPasswordPPET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String confirmNewPaymentPassword = confirmNewPaymentPasswordPPET.getText().toString().trim();
                int confirmNewPaymentPasswordLength = confirmNewPaymentPassword.length();

                if (confirmNewPaymentPasswordLength == 6) {
                    confirmNewPaymentPasswordPPET.setFocusable(false);

                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(confirmNewPaymentPasswordPPET.getWindowToken(), 0);
                    }

                    if (setNewPaymentPassword.equals(confirmNewPaymentPassword)) {
                        String MD5SetNewPaymentPassword = MD5.hexdigest(setNewPaymentPassword);
                        String MD5ConfirmNewPaymentPassword = MD5.hexdigest(confirmNewPaymentPassword);
                        mPresenter.resetPaymentPasswordRequest(smsIdentifyCode, identifyCardNumber, MD5SetNewPaymentPassword, MD5ConfirmNewPaymentPassword);
                    } else {
                        showToast("支付密码不一致");
                        ((PaymentPasswordActivity) getParentContext()).openFragment(ForgetPaymentPasswordFourthStepFragment.class.getName(), null);
                    }
                }
            }
        });
    }

    @OnClick({R.id.ppet_confirm_new_payment_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ppet_confirm_new_payment_password:
                confirmNewPaymentPasswordPPET.setFocusable(true);
                confirmNewPaymentPasswordPPET.setFocusableInTouchMode(true);
                confirmNewPaymentPasswordPPET.requestFocus();
                confirmNewPaymentPasswordPPET.findFocus();

                inputMethodManager.showSoftInput(confirmNewPaymentPasswordPPET, InputMethodManager.SHOW_FORCED);
                break;
        }
    }

    @Override
    public void onResetPaymentPasswordSuccess() {
        showToast("重置支付密码成功");
        ((PaymentPasswordActivity) getParentContext()).finish();
    }

    @Override
    public void onResetPaymentPasswordFail(String errorMessage) {
        showToast(errorMessage);
        ((PaymentPasswordActivity) getParentContext()).openFragment(ForgetPaymentPasswordFourthStepFragment.class.getName(), null);
    }

}
