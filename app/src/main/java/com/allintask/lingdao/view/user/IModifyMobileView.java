package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/1/26.
 */

public interface IModifyMobileView extends IBaseView {

    void onShowPhoneNumberHomeLocationMobileCountryCodeId(int mobileCountryCodeId);

    void onShowPhoneNumberHomeLocationValue(String value);

    void onShowPhoneNumberHomeLocationRegularEx(String regularEx);

    void onCountDownTimerTick(long millisUntilFinished);

    void onCountDownTimerFinish();

    void onModifyPhoneNumberSuccess();

    void onSendSmsIdentifyCodeSuccess();

}
