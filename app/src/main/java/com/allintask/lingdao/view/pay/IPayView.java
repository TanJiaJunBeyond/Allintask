package com.allintask.lingdao.view.pay;

import com.allintask.lingdao.bean.pay.PaymentMethodBean;
import com.allintask.lingdao.view.IBaseView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/9.
 */

public interface IPayView extends IBaseView {

    void onShowTrusteeshipSurplusData(int salaryTrusteeshipId, double surplusAmount);

    void onShowTrusteeshipNoSurplus();

    void onShowPaymentMethodList(List<PaymentMethodBean> paymentMethodList);

    void onGetAliPayOrderStringSuccess(String orderString, double totalAmount);

    void onShowCheckCanWithdrawData(boolean isExistBankCard, boolean isExistPayPwd, boolean isExistRealName);

    void onGetAllintaskPayOrderStringSuccess(String orderString, double totalAmount);

    void onAllintaskPaySuccess();

    void onAllintaskPayFail(int errorCode, String errorMessage);

    void onGetTrusteeshipBuyServiceBidAllintaskPayOrderStringSuccess(String orderString, double totalAmount);

    void onTrusteeshipPaySuccess();

    void onTrusteeshipPayFail(int errorCode, String errorMessage);

}
