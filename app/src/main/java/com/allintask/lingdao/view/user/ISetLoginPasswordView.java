package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/1/27.
 */

public interface ISetLoginPasswordView extends IBaseView {

    void onCountDownTimerTick(long millisUntilFinished);

    void onCountDownTimerFinish();

    void onSetLoginPasswordSuccess();

    void onSendSmsIdentifyCodeSuccess();

}
