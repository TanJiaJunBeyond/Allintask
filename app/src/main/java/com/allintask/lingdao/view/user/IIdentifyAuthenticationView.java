package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/6/15.
 */

public interface IIdentifyAuthenticationView extends IBaseView {

    void onShowZhiMaCreditAuthenticationData(String realName, String idCardNo);

    void onGetZhiMaCreditAuthenticationUrlSuccess(String zhiMaCreditAuthenticationUrl);

    void onShowIdentifyAuthenticationData(boolean passed);

}
