package com.allintask.lingdao.view.pay;

import com.allintask.lingdao.bean.pay.PaymentMethodBean;
import com.allintask.lingdao.view.IBaseView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/5/12.
 */

public interface ITrusteeshipPayView extends IBaseView {

    void onShowPaymentMethodList(List<PaymentMethodBean> paymentMethodList);

    void onGetAliPayOrderStringSuccess(String orderString, double totalAmount);

    void onShowCheckCanWithdrawData(boolean isExistBankCard, boolean isExistPayPwd, boolean isExistRealName);

    void onGetAllintaskPayOrderStringSuccess(String orderString, double totalAmount);

    void onAllintaskPaySuccess();

    void onAllintaskPayFail(int errorCode, String errorMessage);

}
