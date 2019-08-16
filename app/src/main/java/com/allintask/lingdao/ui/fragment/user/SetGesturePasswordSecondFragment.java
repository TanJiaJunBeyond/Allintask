package com.allintask.lingdao.ui.fragment.user;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.SetGesturePasswordSecondStepPresenter;
import com.allintask.lingdao.ui.activity.user.GesturePasswordActivity;
import com.allintask.lingdao.ui.fragment.BaseFragment;
import com.allintask.lingdao.view.user.ISetGesturePasswordSecondStepView;
import com.allintask.lingdao.widget.LockPatternView;
import com.sina.weibo.sdk.utils.MD5;

import java.util.List;

import butterknife.BindView;

/**
 * Created by TanJiaJun on 2018/5/25.
 */

public class SetGesturePasswordSecondFragment extends BaseFragment<ISetGesturePasswordSecondStepView, SetGesturePasswordSecondStepPresenter> implements ISetGesturePasswordSecondStepView {

    @BindView(R.id.tv_content)
    TextView contentTv;
    @BindView(R.id.lpv_gesture_password)
    LockPatternView gesturePasswordLPV;

    private String firstStepGesturePassword;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_set_gesture_password_second_step;
    }

    @Override
    protected SetGesturePasswordSecondStepPresenter CreatePresenter() {
        return new SetGesturePasswordSecondStepPresenter();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (null != bundle) {
            firstStepGesturePassword = bundle.getString(CommonConstant.EXTRA_GESTURE_PASSWORD);
        }

        initUI();
    }

    private void initUI() {
        gesturePasswordLPV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                contentTv.setTextColor(getResources().getColor(R.color.text_dark_black));
                contentTv.setText(getString(R.string.confirm_gesture_password));
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
                if (gesturePassword.equals(firstStepGesturePassword)) {
                    String MD5FirstGesturePassword = MD5.hexdigest(firstStepGesturePassword);
                    String MD5GesturePassword = MD5.hexdigest(gesturePassword);
                    mPresenter.createGesturePasswordRequest(MD5FirstGesturePassword, MD5GesturePassword);
                } else {
                    contentTv.setTextColor(getResources().getColor(R.color.text_red));
                    contentTv.setText("手势密码不一致");

                    gesturePasswordLPV.setErrorStatus();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((GesturePasswordActivity) getParentContext()).openFragment(SetGesturePasswordFirstStepFragment.class.getName(), null);
                        }
                    }, 1000);
                }
            }
        });
    }

    @Override
    public void onCreditGesturePasswordSuccess() {
        ((GesturePasswordActivity) getParentContext()).setResult(CommonConstant.SET_GESTURE_PASSWORD_RESULT_CODE);
        ((GesturePasswordActivity) getParentContext()).finish();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            contentTv.setTextColor(getResources().getColor(R.color.text_dark_black));
            contentTv.setText(getString(R.string.confirm_gesture_password));

            gesturePasswordLPV.reset();
        }
    }

}
