package com.allintask.lingdao.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.utils.WindowUtils;

/**
 * Created by TanJiaJun on 2018/5/9.
 */

public class PayDialog extends Dialog implements View.OnClickListener {

    private boolean canInput;
    private int amount;
    private String title;
    private String tip;

    private PaymentPasswordEditText paymentPasswordPPET;
    private LinearLayout tipLL;
    private TextView tipTv;

    private OnPayDialogListener onPayDialogListener;

    public PayDialog(@NonNull Context context, boolean canInput, int amount, String title, String tip) {
        super(context, R.style.PayDialogStyle);

        this.canInput = canInput;
        this.amount = amount;
        this.title = title;
        this.tip = tip;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();

        if (null != window) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(WindowUtils.getScreenWidth(getContext()), -2);
        View contentView = View.inflate(getContext(), R.layout.dialog_pay, null);
        setContentView(contentView, layoutParams);

        ImageView closeIv = contentView.findViewById(R.id.iv_close);
        TextView amountTv = contentView.findViewById(R.id.tv_amount);
        TextView titleTv = contentView.findViewById(R.id.tv_title);
        paymentPasswordPPET = contentView.findViewById(R.id.ppet_payment_password);
        tipLL = contentView.findViewById(R.id.ll_tip);
        tipTv = contentView.findViewById(R.id.tv_tip);
        TextView forgetPasswordTv = contentView.findViewById(R.id.tv_forget_password);

        StringBuilder stringBuilder = new StringBuilder(String.valueOf(amount)).append("元");
        amountTv.setText(stringBuilder);

        titleTv.setText(title);

        if (canInput) {
            tipLL.setVisibility(View.GONE);
        } else {
            tipTv.setText(tip);
            tipLL.setVisibility(View.VISIBLE);
        }

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

                if (canInput) {
                    int paymentPasswordLength = paymentPassword.length();

                    if (null != onPayDialogListener && paymentPasswordLength == 6) {
                        onPayDialogListener.onInputted(paymentPassword);
                    }
                } else {
                    Editable editable = paymentPasswordPPET.getText();
                    editable.delete(0, paymentPassword.length());
                }
            }
        });

        closeIv.setOnClickListener(this);
        forgetPasswordTv.setOnClickListener(this);
    }

    public void setCanInput(boolean canInput) {
        this.canInput = canInput;
    }

    public void setTip(String tip) {
        this.tip = tip;

        if (null != tipLL && null != tipTv) {
            if (!TextUtils.isEmpty(tip)) {
                tipTv.setText(tip);
                tipLL.setVisibility(View.VISIBLE);
            } else {
                tipLL.setVisibility(View.GONE);
            }
        }
    }

    public void resetPaymentPasswordEditText() {
        if (null != paymentPasswordPPET) {
            paymentPasswordPPET.reset();
        }
    }

    public interface OnPayDialogListener {

        void onInputted(String paymentPassword);

        void onForgetPasswordClick();

    }

    public void setOnInputtedListener(OnPayDialogListener onPayDialogListener) {
        this.onPayDialogListener = onPayDialogListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                if (isShowing()) {
                    dismiss();
                }
                break;

            case R.id.tv_forget_password:
                if (null != onPayDialogListener) {
                    onPayDialogListener.onForgetPasswordClick();
                }
                break;
        }
    }

}
