package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/7/5.
 */

public interface IForgetPaymentPasswordVerifyMobileView extends IBaseView {

    void onShowHiddenMobile(String hiddenMobile);

    void onSendForgetPaymentPasswordIdentifyCodeSuccess();

    void onCountDownTimerTick(long millisUntilFinished);

    void onCountDownTimerFinish();

    void onCheckForgetPaymentPasswordIdentifyCodeSuccess();

    void onCheckForgetPaymentPasswordIdentifyCodeFail(String errorMessage);

}
