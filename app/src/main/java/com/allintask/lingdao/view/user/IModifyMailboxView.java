package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/1/26.
 */

public interface IModifyMailboxView extends IBaseView {

    void onCountDownTimerTick(long millisUntilFinished);

    void onCountDownTimerFinish();

    void onModifyMailboxSuccess();

    void onSendSmsIdentifyCodeSuccess();

}
