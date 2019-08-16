package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/5/25.
 */

public interface IGesturePasswordUnlockView extends IBaseView {

    void onCheckGesturePasswordSuccess();

    void onCheckGesturePasswordFail(String errorMessage);

}
