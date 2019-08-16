package com.allintask.lingdao.ui.fragment.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.SetPaymentPasswordSecondStepPresenter;
import com.allintask.lingdao.ui.activity.user.PaymentPasswordActivity;
import com.allintask.lingdao.ui.fragment.BaseFragment;
import com.allintask.lingdao.view.user.ISetPaymentPasswordSecondStepView;
import com.allintask.lingdao.widget.PaymentPasswordEditText;
import com.sina.weibo.sdk.utils.MD5;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by TanJiaJun on 2018/1/27.
 */

public class SetPaymentPasswordSecondStepFragment extends BaseFragment<ISetPaymentPasswordSecondStepView, SetPaymentPasswordSecondStepPresenter> implements ISetPaymentPasswordSecondStepView {

    @BindView(R.id.ppet_new_payment_password)
    PaymentPasswordEditText newPaymentPasswordPPET;
    @BindView(R.id.btn_confirm)
    Button confirmBtn;

    private String smsIdentifyCode;

    private InputMethodManager inputMethodManager;
    private String newPaymentPassword;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_set_payment_password_second_step;
    }

    @Override
    protected SetPaymentPasswordSecondStepPresenter CreatePresenter() {
        return new SetPaymentPasswordSecondStepPresenter();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (null != bundle) {
            smsIdentifyCode = bundle.getString(CommonConstant.EXTRA_SMS_IDENTIFY_CODE, "");
        }

        initUI();
    }

    private void initUI() {
        inputMethodManager = (InputMethodManager) getParentContext().getSystemService(INPUT_METHOD_SERVICE);

        newPaymentPasswordPPET.requestFocus();
        newPaymentPasswordPPET.requestFocusFromTouch();
        newPaymentPasswordPPET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newPaymentPassword = newPaymentPasswordPPET.getText().toString().trim();

                if (newPaymentPassword.length() == 6) {
                    newPaymentPasswordPPET.setFocusable(false);

                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(newPaymentPasswordPPET.getWindowToken(), 0);
                    }

                    confirmBtn.setEnabled(true);
                    confirmBtn.setClickable(true);
                } else {
                    confirmBtn.setEnabled(false);
                    confirmBtn.setClickable(false);
                }
            }
        });

        confirmBtn.setEnabled(false);
        confirmBtn.setClickable(false);
    }

    @OnClick({R.id.ppet_new_payment_password, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ppet_new_payment_password:
                newPaymentPasswordPPET.setFocusable(true);
                newPaymentPasswordPPET.setFocusableInTouchMode(true);
                newPaymentPasswordPPET.requestFocus();
                newPaymentPasswordPPET.findFocus();

                inputMethodManager.showSoftInput(newPaymentPasswordPPET, InputMethodManager.SHOW_FORCED);
                break;

            case R.id.btn_confirm:
                if (!TextUtils.isEmpty(smsIdentifyCode)) {
                    String MD5NewPaymentPassword = MD5.hexdigest(newPaymentPassword);
                    mPresenter.setPaymentPasswordRequest(smsIdentifyCode, MD5NewPaymentPassword);
                }
                break;
        }
    }

    @Override
    public void onSetPaymentPasswordSuccess() {
        showToast("设置支付密码成功");
        ((PaymentPasswordActivity) getParentContext()).setResult(CommonConstant.REFRESH_RESULT_CODE);
        ((PaymentPasswordActivity) getParentContext()).finish();
    }

    @Override
    public void onSetPaymentPasswordFail() {
        showToast("设置支付密码失败");

        newPaymentPasswordPPET.reset();
        newPaymentPasswordPPET.setFocusable(true);
        newPaymentPasswordPPET.setFocusableInTouchMode(true);
        newPaymentPasswordPPET.requestFocus();
        newPaymentPasswordPPET.findFocus();

        inputMethodManager.showSoftInput(newPaymentPasswordPPET, InputMethodManager.SHOW_FORCED);
    }

}
