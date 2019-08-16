package com.allintask.lingdao.ui.activity.user;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.ApplyForWithdrawDepositPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.utils.AmountUtils;
import com.allintask.lingdao.view.user.IApplyForWithdrawDepositView;
import com.allintask.lingdao.widget.PayDialog;
import com.sina.weibo.sdk.utils.MD5;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/5/3.
 */

public class ApplyForWithdrawDepositActivity extends BaseActivity<IApplyForWithdrawDepositView, ApplyForWithdrawDepositPresenter> implements IApplyForWithdrawDepositView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tv_bank_card)
    TextView bankCardTv;
    @BindView(R.id.tv_withdraw_deposit_rate)
    TextView withdrawDepositRateTv;
    @BindView(R.id.et_withdraw_deposit_amount)
    EditText withdrawDepositAmountEt;
    @BindView(R.id.tv_withdraw_deposit_amount)
    TextView withdrawDepositAmountTv;
    @BindView(R.id.tv_withdraw_deposit_minimum_amount)
    TextView withdrawDepositMinimumAmountTv;
    @BindView(R.id.ll_withdraw_deposit_service_charge)
    LinearLayout withdrawDepositServiceChargeLL;
    @BindView(R.id.tv_withdraw_deposit_service_charge)
    TextView withdrawDepositServiceChargeTv;
    @BindView(R.id.btn_withdraw_deposit)
    Button withdrawDepositBtn;

    private int bankCardId = -1;
    private double withdrawRate;
    private double canWithdraw;
    private int withdrawLowPrice;
    private boolean isCollectWithdrawPoundage;
    private String mWithdrawRateTip;
    private Integer surplusPayPwdInputCount;
    private String payPwdInputErrorTip = null;

    private PayDialog payDialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_apply_for_withdraw_deposit;
    }

    @Override
    protected ApplyForWithdrawDepositPresenter CreatePresenter() {
        return new ApplyForWithdrawDepositPresenter();
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

        titleTv.setText(getString(R.string.withdraw_deposit));

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        withdrawDepositAmountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String withdrawDepositAmountString = withdrawDepositAmountEt.getText().toString().trim();

                if (!TextUtils.isEmpty(withdrawDepositAmountString)) {
                    double withdrawDepositAmount = Double.valueOf(withdrawDepositAmountString);
                    double withdrawDepositServiceCharge = AmountUtils.round(withdrawDepositAmount * withdrawRate, 2);
                    String withdrawRateTip = mWithdrawRateTip.replace(CommonConstant.POUNDAGE, String.valueOf(withdrawDepositServiceCharge));
                    withdrawDepositServiceChargeTv.setText(withdrawRateTip);
                    withdrawDepositServiceChargeLL.setVisibility(View.VISIBLE);

                    withdrawDepositBtn.setEnabled(true);
                    withdrawDepositBtn.setClickable(true);
                } else {
                    withdrawDepositServiceChargeLL.setVisibility(View.GONE);

                    withdrawDepositBtn.setEnabled(false);
                    withdrawDepositBtn.setClickable(true);
                }
            }
        });

        if (!isCollectWithdrawPoundage) {
            withdrawDepositServiceChargeTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        withdrawDepositBtn.setEnabled(false);
        withdrawDepositBtn.setClickable(false);
    }

    private void initData() {
        mPresenter.fetchWithdrawDetailsRequest();
    }

    private void showPayDialog(boolean canInput, final int amount, String title, String tip) {
        payDialog = new PayDialog(getParentContext(), canInput, amount, title, tip);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;

        Window window = payDialog.getWindow();

        if (null != window) {
            window.setAttributes(layoutParams);
        }

        payDialog.show();
        payDialog.setOnInputtedListener(new PayDialog.OnPayDialogListener() {
            @Override
            public void onInputted(String paymentPassword) {
                String MD5PaymentPassword = MD5.hexdigest(paymentPassword);

                if (bankCardId != -1 && amount != 0 && !TextUtils.isEmpty(MD5PaymentPassword)) {
                    if (ContextCompat.checkSelfPermission(getParentContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        TelephonyManager telephonyManager = (TelephonyManager) getParentContext().getSystemService(TELEPHONY_SERVICE);
                        String deviceId = telephonyManager.getDeviceId();
                        long timestamp = System.currentTimeMillis();
                        String withdrawLogId = deviceId + String.valueOf(timestamp);
                        mPresenter.withdrawDepositRequest(bankCardId, amount, MD5PaymentPassword, withdrawLogId);
                    }
                }
            }

            @Override
            public void onForgetPasswordClick() {

            }
        });
    }

    @OnClick({R.id.btn_withdraw_deposit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_withdraw_deposit:
                String withdrawDepositAmountString = withdrawDepositAmountEt.getText().toString().trim();

                if (!TextUtils.isEmpty(withdrawDepositAmountString)) {
                    int withdrawDepositAmount = Integer.valueOf(withdrawDepositAmountString);

                    if (withdrawDepositAmount <= canWithdraw) {
                        if (withdrawDepositAmount >= withdrawLowPrice) {
                            double tempWithdrawDepositRate = withdrawRate * 100D;
                            double withdrawDepositRate = AmountUtils.round(tempWithdrawDepositRate, 2);

                            if (null == surplusPayPwdInputCount || surplusPayPwdInputCount == 0) {
                                showPayDialog(false, withdrawDepositAmount, "额外扣除￥" + String.valueOf(withdrawDepositRate) + "手续费", payPwdInputErrorTip);
                            } else {
                                showPayDialog(true, withdrawDepositAmount, "额外扣除￥" + String.valueOf(withdrawDepositRate) + "手续费", null);
                            }
                        } else {
                            StringBuilder withdrawDepositMinimumAmountSB = new StringBuilder(String.valueOf(withdrawLowPrice)).append("元以上才能提现");
                            showToast(withdrawDepositMinimumAmountSB);
                        }
                    } else {
                        showToast("提现金额不能大于可提现金额");
                    }
                }
                break;
        }
    }

    @Override
    public void onShowWithdrawDetailsData(String realName, int bankCardId, String bankCardDetails, double withdrawRate, double canWithdraw, int withdrawLowPrice, boolean isCollectWithdrawPoundage, String withdrawRateTip, Integer surplusPayPwdInputCount, String payPwdInputErrorTip) {
        this.bankCardId = bankCardId;
        this.withdrawRate = withdrawRate;
        this.canWithdraw = canWithdraw;
        this.withdrawLowPrice = withdrawLowPrice;
        this.isCollectWithdrawPoundage = isCollectWithdrawPoundage;
        mWithdrawRateTip = withdrawRateTip;
        this.surplusPayPwdInputCount = surplusPayPwdInputCount;
        this.payPwdInputErrorTip = payPwdInputErrorTip;

        bankCardTv.setText(bankCardDetails);

        double tempWithdrawDepositRate = withdrawRate * 100D;
        double withdrawDepositRate = AmountUtils.round(tempWithdrawDepositRate, 2);

        StringBuilder withdrawDepositRateSB = new StringBuilder("提现手续费为").append(String.valueOf(withdrawDepositRate)).append("%");
        withdrawDepositRateTv.setText(withdrawDepositRateSB);

        double withdrawDepositAmount = AmountUtils.round(canWithdraw, 2);
        StringBuilder withdrawDepositAmountSB = new StringBuilder(getString(R.string.withdraw_deposit_amount)).append(String.valueOf(withdrawDepositAmount)).append("元");
        withdrawDepositAmountTv.setText(withdrawDepositAmountSB);

        StringBuilder withdrawDepositMinimumAmountSB = new StringBuilder(String.valueOf(withdrawLowPrice)).append("元以上才能提现");
        withdrawDepositMinimumAmountTv.setText(withdrawDepositMinimumAmountSB);
    }

    @Override
    public void onWithdrawDepositSuccess() {
        if (null != payDialog && payDialog.isShowing()) {
            payDialog.dismiss();
        }

        setResult(CommonConstant.REFRESH_RESULT_CODE);
        finish();
    }

    @Override
    public void onWithdrawDepositFail(String errorMessage) {
        if (null != payDialog) {
            payDialog.resetPaymentPasswordEditText();
            payDialog.setCanInput(true);
            payDialog.setTip(errorMessage);
        }
    }

}
