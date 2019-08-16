package com.allintask.lingdao.ui.fragment.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.GesturePasswordUnlockPresenter;
import com.allintask.lingdao.ui.activity.user.GesturePasswordActivity;
import com.allintask.lingdao.ui.activity.user.MyAccountActivity;
import com.allintask.lingdao.ui.activity.user.PaymentPasswordActivity;
import com.allintask.lingdao.ui.fragment.BaseFragment;
import com.allintask.lingdao.view.user.IGesturePasswordUnlockView;
import com.allintask.lingdao.widget.LockPatternView;
import com.sina.weibo.sdk.utils.MD5;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by TanJiaJun on 2018/5/25.
 */

public class GesturePasswordUnlockFragment extends BaseFragment<IGesturePasswordUnlockView, GesturePasswordUnlockPresenter> implements IGesturePasswordUnlockView {

    @BindView(R.id.tv_content)
    TextView contentTv;
    @BindView(R.id.lpv_gesture_password)
    LockPatternView gesturePasswordLPV;
    @BindView(R.id.tv_payment_password_unlock)
    TextView paymentPasswordUnlockTv;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_gesture_password_unlock;
    }

    @Override
    protected GesturePasswordUnlockPresenter CreatePresenter() {
        return new GesturePasswordUnlockPresenter();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initUI();
    }

    private void initUI() {
        gesturePasswordLPV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                contentTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                contentTv.setText(getString(R.string.please_input_gesture_password));
                return false;
            }
        });

        gesturePasswordLPV.setOnPatternListener(new LockPatternView.OnPatternListener() {
            @Override
            public void onPatternStart() {

            }

            @Override
            public void onPatternCleared() {

            }

            @Override
            public void onPatternComplete(List<Integer> indexList, String gesturePassword) {
                String MD5GesturePassword = MD5.hexdigest(gesturePassword);
                mPresenter.checkGesturePasswordRequest(MD5GesturePassword);
            }
        });
    }

    @OnClick({R.id.tv_payment_password_unlock})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_payment_password_unlock:
                Intent intent = new Intent(getParentContext(), PaymentPasswordActivity.class);
                intent.putExtra(CommonConstant.EXTRA_PAYMENT_PASSWORD_TYPE, CommonConstant.PAYMENT_PASSWORD_TYPE_PAYMENT_PASSWORD_UNLOCK);
                startActivity(intent);

                ((GesturePasswordActivity) getParentContext()).finish();
                break;
        }
    }

    @Override
    public void onCheckGesturePasswordSuccess() {
        Intent myAccountIntent = new Intent(getParentContext(), MyAccountActivity.class);
        startActivity(myAccountIntent);

        ((GesturePasswordActivity) getParentContext()).finish();
    }

    @Override
    public void onCheckGesturePasswordFail(String errorMessage) {
        contentTv.setTextColor(getResources().getColor(R.color.text_red));
        contentTv.setText(errorMessage);

        gesturePasswordLPV.setErrorStatus();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                contentTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                contentTv.setText(getString(R.string.please_input_gesture_password));

                gesturePasswordLPV.reset();
            }
        }, 1000);
    }

}
