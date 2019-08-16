package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/7/10.
 */

public interface IPaymentPasswordUnlockView extends IBaseView {

    void onCheckPaymentPasswordSuccess();

    void onCheckPaymentPasswordFail(String errorMessage);
}
