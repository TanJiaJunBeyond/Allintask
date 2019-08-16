package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/7/5.
 */

public interface IForgetPaymentPasswordVerifyIdentifyView extends IBaseView {

    void onShowHiddenName(String hiddenName);

    void onShowVerifyIdentifyCardSuccess();

    void onShowVerifyIdentifyCardFail(String errorMessage);

}
