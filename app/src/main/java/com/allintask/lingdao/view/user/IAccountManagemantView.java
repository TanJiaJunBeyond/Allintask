package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/5/24.
 */

public interface IAccountManagemantView extends IBaseView {

    void onShowMobileCountryCodeId(int mobileCountryCodeId);

    void onShowPhoneNumber(String phoneNumber);

    void onShowMailbox(String mailbox);

    void onShowIsExistLoginPassword(boolean isExistLoginPassword);

}
