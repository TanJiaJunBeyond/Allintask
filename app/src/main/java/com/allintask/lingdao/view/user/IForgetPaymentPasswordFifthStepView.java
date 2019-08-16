package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/7/9.
 */

public interface IForgetPaymentPasswordFifthStepView extends IBaseView {

    void onResetPaymentPasswordSuccess();

    void onResetPaymentPasswordFail(String errorMessage);

}
