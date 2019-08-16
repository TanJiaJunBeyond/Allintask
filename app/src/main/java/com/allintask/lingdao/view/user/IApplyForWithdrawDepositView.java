package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/5/3.
 */

public interface IApplyForWithdrawDepositView extends IBaseView {

    void onShowWithdrawDetailsData(String realName, int bankCardId, String bankCardDetails, double withdrawRate, double canWithdraw, int withdrawLowPrice, boolean isCollectWithdrawPoundage, String withdrawRateTip, Integer surplusPayPwdInputCount, String payPwdInputErrorTip);

    void onWithdrawDepositSuccess();

    void onWithdrawDepositFail(String errorMessage);

}
