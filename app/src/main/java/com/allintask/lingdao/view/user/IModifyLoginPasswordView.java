package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/1/27.
 */

public interface IModifyLoginPasswordView extends IBaseView {


    void onCountDownTimerTick(long millisUntilFinished);

    void onCountDownTimerFinish();

    void onModifyLoginPasswordSuccess();

    void onSendSmsIdentifyCodeSuccess();

}
