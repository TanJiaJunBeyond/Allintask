package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

import java.util.Map;

/**
 * Created by TanJiaJun on 2018/1/26.
 */

public interface ISettingView extends IBaseView {

    void onShowMobileCountryCodeId(int mobileCountryCodeId);

    void onShowPhoneNumber(String phoneNumber);

    void onShowIsExistGesturePwd(boolean isExistGesturePwd);

//    void onShowMailbox(String mailbox);
//
//    void onShowIsExistLoginPassword(boolean isExistLoginPassword);
//
//    void onShowIsExistPaymentPassword(boolean isExistPaymentPassword);

    void onShowBankId(Integer bankId);

    void onShowBankCardName(String bankCardName);

    void onShowQQName(String qqName);

    void onShowWechatName(String wechatName);

    void onWechatUMAuthComplete(Map<String, String> map);

    void onWechatUMAuthError();

    void onQQUMAuthComplete(Map<String, String> map);

    void onQQUMAuthError();

    void onBindWechatSuccess();

    void onBindQQSuccess();

    void onUnbindWechatSuccess();

    void onUnbindQQSuccess();

    void onLogoutSuccess();

}
