package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/5/25.
 */

public interface ISecurityManagementView extends IBaseView {

    void onShowMobileCountryCodeId(int mobileCountryCodeId);

    void onShowIsExistPaymentPassword(boolean isExistPaymentPassword);

    void onShowIsExistGesturePwd(boolean isExistGesturePwd);

    void showIsZmrzAuthSuccess(boolean isZmrzAuthSuccess);

    void onDeleteGesturePasswordSuccess();

    void onDeleteGesturePasswordFail(String errorMessage);

}
