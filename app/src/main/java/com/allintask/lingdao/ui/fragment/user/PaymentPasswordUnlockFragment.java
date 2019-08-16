package com.allintask.lingdao.ui.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.PaymentPasswordUnlockPresenter;
import com.allintask.lingdao.ui.activity.user.GesturePasswordActivity;
import com.allintask.lingdao.ui.activity.user.MyAccountActivity;
import com.allintask.lingdao.ui.activity.user.PaymentPasswordActivity;
import com.allintask.lingdao.ui.fragment.BaseFragment;
import com.allintask.lingdao.view.user.IPaymentPasswordUnlockView;
import com.allintask.lingdao.widget.PaymentPasswordEditText;
import com.sina.weibo.sdk.utils.MD5;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by TanJiaJun on 2018/7/10.
 */

public class PaymentPasswordUnlockFragment extends BaseFragment<IPaymentPasswordUnlockView, PaymentPasswordUnlockPresenter> implements IPaymentPasswordUnlockView {

    @BindView(R.id.ppet_payment_password)
    PaymentPasswordEditText paymentPasswordPPET;
    @BindView(R.id.tv_error_tips)
    TextView errorTipsTv;

    private int paymentPasswordType = CommonConstant.PAYMENT_PASSWORD_TYPE_SET_PAYMENT_PASSWORD;

    private InputMethodManager inputMethodManager;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_payment_password_unlock;
    }

    @Override
    protected PaymentPasswordUnlockPresenter CreatePresenter() {
        return new PaymentPasswordUnlockPresenter();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (null != bundle) {
            paymentPasswordType = bundle.getInt(CommonConstant.EXTRA_PAYMENT_PASSWORD_TYPE, CommonConstant.PAYMENT_PASSWORD_TYPE_SET_PAYMENT_PASSWORD);
        }

        initUI();
    }

    private void initUI() {
        inputMethodManager = (InputMethodManager) getParentContext().getSystemService(INPUT_METHOD_SERVICE);

        paymentPasswordPPET.requestFocus();
        paymentPasswordPPET.requestFocusFromTouch();
        paymentPasswordPPET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String paymentPassword = paymentPasswordPPET.getText().toString().trim();
                int paymentPasswordLength = paymentPassword.length();

                if (paymentPasswordLength == 1) {
                    errorTipsTv.setVisibility(View.GONE);
                } else if (paymentPasswordLength == 6) {
                    String MD5PaymentPassword = MD5.hexdigest(paymentPassword);
                    mPresenter.checkPaymentPasswordRequest(MD5PaymentPassword);
                }
            }
        });
    }

    @OnClick({R.id.ppet_payment_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ppet_payment_password:
                paymentPasswordPPET.setFocusable(true);
                paymentPasswordPPET.setFocusableInTouchMode(true);
                paymentPasswordPPET.requestFocus();
                paymentPasswordPPET.findFocus();

                inputMethodManager.showSoftInput(paymentPasswordPPET, InputMethodManager.SHOW_FORCED);
                break;
        }
    }

    @Override
    public void onCheckPaymentPasswordSuccess() {
        paymentPasswordPPET.setFocusable(false);

        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(paymentPasswordPPET.getWindowToken(), 0);
        }

        if (paymentPasswordType == CommonConstant.PAYMENT_PASSWORD_TYPE_SET_PAYMENT_PASSWORD) {
            Intent intent = new Intent(getParentContext(), PaymentPasswordActivity.class);
            intent.putExtra(CommonConstant.EXTRA_PAYMENT_PASSWORD_TYPE, CommonConstant.EXTRA_SET_NEW_PAYMENT_PASSWORD);
            startActivity(intent);
        } else if (paymentPasswordType == CommonConstant.PAYMENT_PASSWORD_TYPE_PAYMENT_PASSWORD_UNLOCK) {
            Intent intent = new Intent(getParentContext(), MyAccountActivity.class);
            startActivity(intent);

            ((PaymentPasswordActivity) getParentContext()).finish();
        } else if (paymentPasswordType == CommonConstant.PAYMENT_PASSWORD_TYPE_SET_GESTURE_PASSWORD) {
            ((GesturePasswordActivity) getParentContext()).openFragment(SetGesturePasswordFirstStepFragment.class.getName(), null);
        } else if (paymentPasswordType == CommonConstant.PAYMENT_PASSWORD_TYPE_DELETE_GESTURE_PASSWORD) {
            ((PaymentPasswordActivity) getParentContext()).setResult(CommonConstant.DELETE_GESTURE_PASSWORD_RESULT_CODE);
            ((PaymentPasswordActivity) getParentContext()).finish();
        }
    }

    @Override
    public void onCheckPaymentPasswordFail(String errorMessage) {
        paymentPasswordPPET.reset();
        paymentPasswordPPET.setFocusable(true);
        paymentPasswordPPET.setFocusableInTouchMode(true);
        paymentPasswordPPET.requestFocus();
        paymentPasswordPPET.findFocus();

        inputMethodManager.showSoftInput(paymentPasswordPPET, InputMethodManager.SHOW_FORCED);

        errorTipsTv.setText(errorMessage);
        errorTipsTv.setVisibility(View.VISIBLE);
    }

}
