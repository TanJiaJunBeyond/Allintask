package com.allintask.lingdao.ui.fragment.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.ui.activity.user.PaymentPasswordActivity;
import com.allintask.lingdao.ui.fragment.BaseFragment;
import com.allintask.lingdao.widget.PaymentPasswordEditText;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by TanJiaJun on 2018/7/9.
 */

public class ForgetPaymentPasswordFourthStepFragment extends BaseFragment {

    @BindView(R.id.ppet_set_new_payment_password)
    PaymentPasswordEditText setNewPaymentPasswordPPET;

    private String smsIdentifyCode;
    private String identifyCardNumber;

    private InputMethodManager inputMethodManager;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_forget_payment_password_fourth_step;
    }

    @Override
    protected BasePresenter CreatePresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (null != bundle) {
            smsIdentifyCode = bundle.getString(CommonConstant.EXTRA_SMS_IDENTIFY_CODE);
            identifyCardNumber = bundle.getString(CommonConstant.EXTRA_IDENTIFY_CARD_NUMBER);
        }

        initUI();
    }

    private void initUI() {
        inputMethodManager = (InputMethodManager) getParentContext().getSystemService(INPUT_METHOD_SERVICE);

        setNewPaymentPasswordPPET.requestFocus();
        setNewPaymentPasswordPPET.requestFocusFromTouch();
        setNewPaymentPasswordPPET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String setNewPaymentPassword = setNewPaymentPasswordPPET.getText().toString().trim();
                int setNewPaymentPasswordLength = setNewPaymentPassword.length();

                if (setNewPaymentPasswordLength == 6) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CommonConstant.EXTRA_SMS_IDENTIFY_CODE, smsIdentifyCode);
                    bundle.putString(CommonConstant.EXTRA_IDENTIFY_CARD_NUMBER, identifyCardNumber);
                    bundle.putString(CommonConstant.EXTRA_SET_NEW_PAYMENT_PASSWORD, setNewPaymentPassword);
                    ((PaymentPasswordActivity) getParentContext()).openFragment(ForgetPaymentPasswordFifthStepFragment.class.getName(), bundle);
                }
            }
        });
    }

    @OnClick({R.id.ppet_set_new_payment_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ppet_set_new_payment_password:
                setNewPaymentPasswordPPET.setFocusable(true);
                setNewPaymentPasswordPPET.setFocusableInTouchMode(true);
                setNewPaymentPasswordPPET.requestFocus();
                setNewPaymentPasswordPPET.findFocus();

                inputMethodManager.showSoftInput(setNewPaymentPasswordPPET, InputMethodManager.SHOW_FORCED);
                break;
        }
    }

}
