package com.allintask.lingdao.ui.fragment.user;

import android.os.Bundle;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.presenter.user.SetGesturePasswordFirstStepPresenter;
import com.allintask.lingdao.ui.activity.user.GesturePasswordActivity;
import com.allintask.lingdao.ui.fragment.BaseFragment;
import com.allintask.lingdao.view.user.ISetGesturePasswordFirstStepView;
import com.allintask.lingdao.widget.LockPatternView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by TanJiaJun on 2018/5/25.
 */

public class SetGesturePasswordFirstStepFragment extends BaseFragment<ISetGesturePasswordFirstStepView, SetGesturePasswordFirstStepPresenter> implements ISetGesturePasswordFirstStepView {

    @BindView(R.id.lpv_gesture_password)
    LockPatternView gesturePasswordLPV;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_set_gesture_password_first_step;
    }

    @Override
    protected SetGesturePasswordFirstStepPresenter CreatePresenter() {
        return new SetGesturePasswordFirstStepPresenter();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ((GesturePasswordActivity) getParentContext()).setTitle(getString(R.string.gesture_password));
        initUI();
    }

    private void initUI() {
        gesturePasswordLPV.setOnPatternListener(new LockPatternView.OnPatternListener() {
            @Override
            public void onPatternStart() {

            }

            @Override
            public void onPatternCleared() {

            }

            @Override
            public void onPatternComplete(List<Integer> indexList, String gesturePassword) {
                Bundle bundle = new Bundle();
                bundle.putString(CommonConstant.EXTRA_GESTURE_PASSWORD, gesturePassword);
                ((GesturePasswordActivity) getParentContext()).openFragment(SetGesturePasswordSecondFragment.class.getName(), bundle);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            gesturePasswordLPV.reset();
        }
    }

}
