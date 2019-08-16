package com.allintask.lingdao.model.pay;

import cn.tanjiajun.sdk.conn.OkHttpRequest;

/**
 * Created by TanJiaJun on 2018/3/9.
 */

public interface IPayModel {

    OkHttpRequest fetchPaymentMethodListRequest();

    OkHttpRequest getBuyServiceBidAlipayOrderStringRequest(int serveBidId, double payAmount, int salaryTrusteeshipOrderId);

    OkHttpRequest getBuyServiceBidAllintaskPayOrderStringRequest(int serveBidId, double payAmount, int salaryTrusteeshipOrderId);

    OkHttpRequest allintaskPayRequest(String orderString, String payPwd, String ip);

    OkHttpRequest getTrusteeshipAlipayOrderStringRequest(int demandId, double payAmount);

    OkHttpRequest getTrusteeshipAllintaskPayOrderStringRequest(int demandId, double payAmount);

    OkHttpRequest fetchTrusteeshipSurplusRequest(int demandId);

    OkHttpRequest getTrusteeshipBuyServiceBidAllintaskPayOrderStringRequest(int serveBidId, double payAmount, int salaryTrusteeshipOrderId);

    OkHttpRequest trusteeshipPayRequest(String orderString, String payPwd, String ip);

    OkHttpRequest getPublishPaymentDemandAlipayOrderStringRequest(String serveBidNo);

    OkHttpRequest getPublishPaymentDemandAllintaskPayOrderStringRequest(String serveBidNo);

    OkHttpRequest allintaskPayV1Request(String orderString, String payPwd, String ip);

}
