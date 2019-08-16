package com.allintask.lingdao.ui.fragment.user;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.ModifyPaymentPasswordFirstStepPresenter;
import com.allintask.lingdao.ui.activity.user.PaymentPasswordActivity;
import com.allintask.lingdao.ui.fragment.BaseFragment;
import com.allintask.lingdao.view.user.IModifyPaymentPasswordFirstStepView;
import com.allintask.lingdao.widget.PaymentPasswordEditText;
import com.sina.weibo.sdk.utils.MD5;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/3/22.
 */

public class ModifyPaymentPasswordFirstStepFragment extends BaseFragment<IModifyPaymentPasswordFirstStepView, ModifyPaymentPasswordFirstStepPresenter> implements IModifyPaymentPasswordFirstStepView {

    @BindView(R.id.ppet_old_payment_password)
    PaymentPasswordEditText oldPaymentPasswordPPET;
    @BindView(R.id.btn_confirm)
    Button confirmBtn;

    private int mobileCountryCodeId;

    private InputMethodManager inputMethodManager;
    private String oldPaymentPassword;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_modify_payment_password_first_step;
    }

    @Override
    protected ModifyPaymentPasswordFirstStepPresenter CreatePresenter() {
        return new ModifyPaymentPasswordFirstStepPresenter();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (null != bundle) {
            mobileCountryCodeId = bundle.getInt(CommonConstant.EXTRA_MOBILE_COUNTRY_CODE_ID, -1);
        }

        initUI();
    }

    private void initUI() {
        inputMethodManager = (InputMethodManager) getParentContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        oldPaymentPasswordPPET.requestFocus();
        oldPaymentPasswordPPET.requestFocusFromTouch();
        oldPaymentPasswordPPET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                oldPaymentPassword = oldPaymentPasswordPPET.getText().toString().trim();

                if (oldPaymentPassword.length() == 6) {
                    oldPaymentPasswordPPET.setFocusable(false);

                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(oldPaymentPasswordPPET.getWindowToken(), 0);
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

    @OnClick({R.id.ppet_old_payment_password, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ppet_old_payment_password:
                oldPaymentPasswordPPET.setFocusable(true);
                oldPaymentPasswordPPET.setFocusableInTouchMode(true);
                oldPaymentPasswordPPET.requestFocus();
                oldPaymentPasswordPPET.findFocus();

                inputMethodManager.showSoftInput(oldPaymentPasswordPPET, InputMethodManager.SHOW_FORCED);
                break;

            case R.id.btn_confirm:
                String MD5OldPaymentPassword = MD5.hexdigest(oldPaymentPassword);
                mPresenter.checkOldPaymentPasswordRequest(MD5OldPaymentPassword);
                break;
        }
    }

    @Override
    public void onCheckOldPaymentPasswordSuccess() {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConstant.EXTRA_MOBILE_COUNTRY_CODE_ID, mobileCountryCodeId);
        bundle.putString(CommonConstant.EXTRA_OLD_PAYMENT_PASSWORD, oldPaymentPassword);
        ((PaymentPasswordActivity) getParentContext()).openFragment(ModifyPaymentPasswordSecondStepFragment.class.getName(), bundle);
    }

    @Override
    public void onCheckOldPaymentPasswordError() {
        showToast("支付密码错误");

        oldPaymentPasswordPPET.reset();
        oldPaymentPasswordPPET.setFocusable(true);
        oldPaymentPasswordPPET.setFocusableInTouchMode(true);
        oldPaymentPasswordPPET.requestFocus();
        oldPaymentPasswordPPET.findFocus();

        inputMethodManager.showSoftInput(oldPaymentPasswordPPET, InputMethodManager.SHOW_FORCED);
    }

    @Override
    public void onCheckOldPaymentPasswordFail() {
        showToast("请求失败");

        oldPaymentPasswordPPET.reset();
        oldPaymentPasswordPPET.setFocusable(true);
        oldPaymentPasswordPPET.setFocusableInTouchMode(true);
        oldPaymentPasswordPPET.requestFocus();
        oldPaymentPasswordPPET.findFocus();

        inputMethodManager.showSoftInput(oldPaymentPasswordPPET, InputMethodManager.SHOW_FORCED);
    }

}
