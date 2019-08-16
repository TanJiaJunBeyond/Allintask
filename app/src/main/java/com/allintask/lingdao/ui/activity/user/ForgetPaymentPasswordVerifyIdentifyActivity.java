package com.allintask.lingdao.ui.activity.user;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.ForgetPaymentPasswordVerifyIdentifyPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.view.user.IForgetPaymentPasswordVerifyIdentifyView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/7/5.
 */

public class ForgetPaymentPasswordVerifyIdentifyActivity extends BaseActivity<IForgetPaymentPasswordVerifyIdentifyView, ForgetPaymentPasswordVerifyIdentifyPresenter> implements IForgetPaymentPasswordVerifyIdentifyView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.ll_identify_card_name)
    LinearLayout identifyCardNameLL;
    @BindView(R.id.tv_identify_card_name)
    TextView identifyCardNameTv;
    @BindView(R.id.et_identify_card_number)
    EditText identifyCardNumberET;
    @BindView(R.id.btn_next_step)
    Button nextStepBtn;

    private String smsIdentifyCode;

    private String identifyCardNumber;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_forget_payment_password_verify_identify;
    }

    @Override
    protected ForgetPaymentPasswordVerifyIdentifyPresenter CreatePresenter() {
        return new ForgetPaymentPasswordVerifyIdentifyPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            smsIdentifyCode = intent.getStringExtra(CommonConstant.EXTRA_SMS_IDENTIFY_CODE);
        }

        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setTitle("");

        titleTv.setText(getString(R.string.verify_identify_card));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        identifyCardNumberET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkNextStepEnable();
            }
        });

        nextStepBtn.setEnabled(false);
        nextStepBtn.setClickable(false);
    }

    private void initData() {
        mPresenter.getHiddenUserDataRequest();
    }

    private void checkNextStepEnable() {
        String identifyCardNumber = identifyCardNumberET.getText().toString().trim();

        if (!TextUtils.isEmpty(identifyCardNumber)) {
            nextStepBtn.setEnabled(true);
            nextStepBtn.setClickable(true);
        } else {
            nextStepBtn.setEnabled(false);
            nextStepBtn.setClickable(false);
        }
    }

    @OnClick({R.id.btn_next_step})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next_step:
                identifyCardNumber = identifyCardNumberET.getText().toString().trim();

                if (!TextUtils.isEmpty(identifyCardNumber)) {
                    if (identifyCardNumber.matches("^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$")) {
                        mPresenter.checkIdentifyCardRequest(identifyCardNumber);
                    } else {
                        showToast("身份证号码不正确");
                    }
                }
                break;
        }
    }

    @Override
    public void onShowHiddenName(String hiddenName) {
        identifyCardNameTv.setText(hiddenName + "的身份证号");
        identifyCardNameLL.setVisibility(View.VISIBLE);
    }

    @Override
    public void onShowVerifyIdentifyCardSuccess() {
        Intent intent = new Intent(getParentContext(), PaymentPasswordActivity.class);
        intent.putExtra(CommonConstant.EXTRA_PAYMENT_PASSWORD_TYPE, CommonConstant.PAYMENT_PASSWORD_TYPE_FORGET_PAYMENT_PASSWORD);
        intent.putExtra(CommonConstant.EXTRA_SMS_IDENTIFY_CODE, smsIdentifyCode);
        intent.putExtra(CommonConstant.EXTRA_IDENTIFY_CARD_NUMBER, identifyCardNumber);
        startActivity(intent);

        finish();
    }

    @Override
    public void onShowVerifyIdentifyCardFail(String errorMessage) {
        showToast(errorMessage);
        identifyCardNumberET.setText("");
    }

}
