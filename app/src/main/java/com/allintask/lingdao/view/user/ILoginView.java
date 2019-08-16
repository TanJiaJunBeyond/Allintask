package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

import java.util.Map;

/**
 * Created by TanJiaJun on 2017/12/28.
 */

public interface ILoginView extends IBaseView {

    void onShowPhoneNumberHomeLocationMobileCountryCodeId(int mobileCountryCodeId);

    void onShowPhoneNumberHomeLocationValue(String value);

    void onShowPhoneNumberHomeLocationRegularEx(String regularEx);

    void onCountDownTimerTick(long millisUntilFinished);

    void onCountDownTimerFinish();

    void onWechatUMAuthComplete(Map<String, String> map);

    void onWechatUMAuthError(Throwable throwable);

    void onQQUMAuthComplete(Map<String, String> map);

    void onQQUMAuthError(Throwable throwable);

    void onLoginSuccess();

    void onWechatLoginIsBinding();

    void onWechatLoginUnbind();

    void onQQLoginIsBinding();

    void onQQLoginUnbind();

    void onCheckNeedGuideSuccess(boolean isNeedGuide);

    void onSendSmsIdentifyCodeSuccess();

}
