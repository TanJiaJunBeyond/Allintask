package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2017/12/29.
 */

public interface IForgetPasswordView extends IBaseView {

    void onShowPhoneNumberHomeLocationMobileCountryCodeId(int mobileCountryCodeId);

    void onShowPhoneNumberHomeLocationValue(String value);

    void onShowPhoneNumberHomeLocationRegularEx(String regularEx);

    void onCountDownTimerTick(long millisUntilFinished);

    void onCountDownTimerFinish();

    void onResetLoginPasswordSuccess();

    void onSendSmsIdentifyCodeSuccess();

}
