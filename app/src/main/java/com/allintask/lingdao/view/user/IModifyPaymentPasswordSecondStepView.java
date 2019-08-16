package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/3/22.
 */

public interface IModifyPaymentPasswordSecondStepView extends IBaseView {

    void onCountDownTimerTick(long millisUntilFinished);

    void onCountDownTimerFinish();

    void onCheckSmsIdentifyCodeSuccess();

    void onSendSmsIdentifyCodeSuccess();

}
