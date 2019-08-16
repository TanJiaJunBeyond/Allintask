package com.allintask.lingdao.ui.fragment.user;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.ModifyPaymentPasswordThirdStepPresenter;
import com.allintask.lingdao.ui.activity.user.PaymentPasswordActivity;
import com.allintask.lingdao.ui.fragment.BaseFragment;
import com.allintask.lingdao.view.user.IModifyPaymentPasswordThirdStepView;
import com.allintask.lingdao.widget.PaymentPasswordEditText;
import com.sina.weibo.sdk.utils.MD5;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.MD5Utils;

/**
 * Created by TanJiaJun on 2018/3/22.
 */

public class ModifyPaymentPasswordThirdStepFragment extends BaseFragment<IModifyPaymentPasswordThirdStepView, ModifyPaymentPasswordThirdStepPresenter> implements IModifyPaymentPasswordThirdStepView {

    @BindView(R.id.ppet_new_payment_password)
    PaymentPasswordEditText newPaymentPasswordPPET;
    @BindView(R.id.btn_confirm)
    Button confirmBtn;

    private String oldPaymentPassword;
    private String smsIdentifyCode;

    private InputMethodManager inputMethodManager;
    private String newPaymentPassword;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_modify_payment_password_third_step;
    }

    @Override
    protected ModifyPaymentPasswordThirdStepPresenter CreatePresenter() {
        return new ModifyPaymentPasswordThirdStepPresenter();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (null != bundle) {
            oldPaymentPassword = bundle.getString(CommonConstant.EXTRA_OLD_PAYMENT_PASSWORD, "");
            smsIdentifyCode = bundle.getString(CommonConstant.EXTRA_SMS_IDENTIFY_CODE, "");
        }

        initUI();
    }

    private void initUI() {
        inputMethodManager = (InputMethodManager) getParentContext().getSystemService(Context.INPUT_METHOD_SERVICE);

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

    @OnClick({R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                if (!TextUtils.isEmpty(oldPaymentPassword) && !TextUtils.isEmpty(smsIdentifyCode) && !TextUtils.isEmpty(newPaymentPassword)) {
                    String MD5OldPaymentPassword = MD5.hexdigest(oldPaymentPassword);
                    String MD5NewPaymentPassword = MD5.hexdigest(newPaymentPassword);
                    mPresenter.modifyPaymentPasswordRequest(smsIdentifyCode, MD5OldPaymentPassword, MD5NewPaymentPassword);
                }
                break;
        }
    }

    @Override
    public void onModifyPaymentPasswordSuccess() {
        showToast("修改支付密码成功");
        ((PaymentPasswordActivity) getParentContext()).setResult(CommonConstant.REFRESH_RESULT_CODE);
        ((PaymentPasswordActivity) getParentContext()).finish();
    }

}
