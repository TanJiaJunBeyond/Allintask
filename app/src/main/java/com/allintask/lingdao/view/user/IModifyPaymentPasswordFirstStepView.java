package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/3/22.
 */

public interface IModifyPaymentPasswordFirstStepView extends IBaseView {

    void onCheckOldPaymentPasswordSuccess();

    void onCheckOldPaymentPasswordError();

    void onCheckOldPaymentPasswordFail();

}
