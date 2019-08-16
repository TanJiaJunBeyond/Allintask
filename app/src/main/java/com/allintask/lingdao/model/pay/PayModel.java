package com.allintask.lingdao.model.pay;

import com.allintask.lingdao.constant.ApiKey;
import com.allintask.lingdao.constant.ServiceAPIConstant;

import cn.tanjiajun.sdk.conn.OkHttpRequest;

/**
 * Created by TanJiaJun on 2018/3/9.
 */

public class PayModel implements IPayModel {

    @Override
    public OkHttpRequest fetchPaymentMethodListRequest() {
        OkHttpRequest fetchPaymentMethodListRequest = new OkHttpRequest();
        fetchPaymentMethodListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_PAY_CHANNEL_LIST);
        fetchPaymentMethodListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_PAY_CHANNEL_LIST);
        return fetchPaymentMethodListRequest;
    }

    @Override
    public OkHttpRequest getBuyServiceBidAlipayOrderStringRequest(int serveBidId, double payAmount, int salaryTrusteeshipOrderId) {
        OkHttpRequest getBuyServeBidAlipayOrderString = new OkHttpRequest();
        getBuyServeBidAlipayOrderString.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_PAY_ALIPAY_GET_BUY_SERVE_BID_ORDER_STRING);
        getBuyServeBidAlipayOrderString.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_PAY_ALIPAY_GET_BUY_SERVE_BID_ORDER_STRING);
        getBuyServeBidAlipayOrderString.addRequestFormParam(ApiKey.ORDER_SERVE_BID_ID, String.valueOf(serveBidId));
        getBuyServeBidAlipayOrderString.addRequestFormParam(ApiKey.ORDER_PAY_AMOUNT, String.valueOf(payAmount));

        if (salaryTrusteeshipOrderId != -1) {
            getBuyServeBidAlipayOrderString.addRequestFormParam(ApiKey.ORDER_SALARY_TRUSTEESHIP_ORDER_ID, String.valueOf(salaryTrusteeshipOrderId));
        }
        return getBuyServeBidAlipayOrderString;
    }

    @Override
    public OkHttpRequest getBuyServiceBidAllintaskPayOrderStringRequest(int serveBidId, double payAmount, int salaryTrusteeshipOrderId) {
        OkHttpRequest getBuyServiceBidAllintaskPayOrderStringRequest = new OkHttpRequest();
        getBuyServiceBidAllintaskPayOrderStringRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_PAY_LING_DAO_GET_BUY_SERVE_BID_ORDER_STRING);
        getBuyServiceBidAllintaskPayOrderStringRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_PAY_LING_DAO_GET_BUY_SERVE_BID_ORDER_STRING);
        getBuyServiceBidAllintaskPayOrderStringRequest.addRequestFormParam(ApiKey.ORDER_SERVE_BID_ID, String.valueOf(serveBidId));
        getBuyServiceBidAllintaskPayOrderStringRequest.addRequestFormParam(ApiKey.ORDER_PAY_AMOUNT, String.valueOf(payAmount));

        if (salaryTrusteeshipOrderId != -1) {
            getBuyServiceBidAllintaskPayOrderStringRequest.addRequestFormParam(ApiKey.ORDER_SALARY_TRUSTEESHIP_ORDER_ID, String.valueOf(salaryTrusteeshipOrderId));
        }
        return getBuyServiceBidAllintaskPayOrderStringRequest;
    }

    @Override
    public OkHttpRequest allintaskPayRequest(String orderString, String payPwd, String ip) {
        OkHttpRequest allintaskPayRequest = new OkHttpRequest();
        allintaskPayRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_LING_DAO_PAY_PAY);
        allintaskPayRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_LING_DAO_PAY_PAY);
        allintaskPayRequest.addRequestFormParam(ApiKey.ORDER_ORDER_STRING, orderString);
        allintaskPayRequest.addRequestFormParam(ApiKey.ORDER_PAY_PWD, payPwd);
        allintaskPayRequest.addRequestFormParam(ApiKey.ORDER_IP, ip);
        return allintaskPayRequest;
    }

    @Override
    public OkHttpRequest getTrusteeshipAlipayOrderStringRequest(int demandId, double payAmount) {
        OkHttpRequest getTrusteeshipAlipayOrderStringRequest = new OkHttpRequest();
        getTrusteeshipAlipayOrderStringRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_SALARY_TRUSTEESHIP_ALIPAY_GET_SALARY_TRUSEESHIP_ORDER_STRING);
        getTrusteeshipAlipayOrderStringRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_SALARY_TRUSTEESHIP_ALIPAY_GET_SALARY_TRUSEESHIP_ORDER_STRING);
        getTrusteeshipAlipayOrderStringRequest.addRequestFormParam(ApiKey.ORDER_DEMAND_ID, String.valueOf(demandId));
        getTrusteeshipAlipayOrderStringRequest.addRequestFormParam(ApiKey.ORDER_PAY_AMOUNT, String.valueOf(payAmount));
        return getTrusteeshipAlipayOrderStringRequest;
    }

    @Override
    public OkHttpRequest getTrusteeshipAllintaskPayOrderStringRequest(int demandId, double payAmount) {
        OkHttpRequest getTrusteeshipAllintaskPayOrderStringRequest = new OkHttpRequest();
        getTrusteeshipAllintaskPayOrderStringRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_SALARY_TRUSTEESHIP_LING_DAO_GET_SALARY_TRUSTEESHIP_ORDER_STRING);
        getTrusteeshipAllintaskPayOrderStringRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_SALARY_TRUSTEESHIP_LING_DAO_GET_SALARY_TRUSTEESHIP_ORDER_STRING);
        getTrusteeshipAllintaskPayOrderStringRequest.addRequestFormParam(ApiKey.ORDER_DEMAND_ID, String.valueOf(demandId));
        getTrusteeshipAllintaskPayOrderStringRequest.addRequestFormParam(ApiKey.ORDER_PAY_AMOUNT, String.valueOf(payAmount));
        return getTrusteeshipAllintaskPayOrderStringRequest;
    }

    @Override
    public OkHttpRequest fetchTrusteeshipSurplusRequest(int demandId) {
        OkHttpRequest fetchTrusteeshipSurplusRequest = new OkHttpRequest();
        fetchTrusteeshipSurplusRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_SALARY_TRUSTEESHIP_PAY_SALARY_TRUSTEESHIP_SURPLUS);
        fetchTrusteeshipSurplusRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_SALARY_TRUSTEESHIP_PAY_SALARY_TRUSTEESHIP_SURPLUS);
        fetchTrusteeshipSurplusRequest.addRequestFormParam(ApiKey.ORDER_DEMAND_ID, String.valueOf(demandId));
        return fetchTrusteeshipSurplusRequest;
    }

    @Override
    public OkHttpRequest getTrusteeshipBuyServiceBidAllintaskPayOrderStringRequest(int serveBidId, double payAmount, int salaryTrusteeshipOrderId) {
        OkHttpRequest getTrusteeshipBuyServiceBidAllintaskPayOrderStringRequest = new OkHttpRequest();
        getTrusteeshipBuyServiceBidAllintaskPayOrderStringRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_PAY_SALARY_TRUSTEESHIP_GET_BUY_SERVE_BID_ORDER_STRING);
        getTrusteeshipBuyServiceBidAllintaskPayOrderStringRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_PAY_SALARY_TRUSTEESHIP_GET_BUY_SERVE_BID_ORDER_STRING);
        getTrusteeshipBuyServiceBidAllintaskPayOrderStringRequest.addRequestFormParam(ApiKey.ORDER_SERVE_BID_ID, String.valueOf(serveBidId));
        getTrusteeshipBuyServiceBidAllintaskPayOrderStringRequest.addRequestFormParam(ApiKey.ORDER_PAY_AMOUNT, String.valueOf(payAmount));

        if (salaryTrusteeshipOrderId != -1) {
            getTrusteeshipBuyServiceBidAllintaskPayOrderStringRequest.addRequestFormParam(ApiKey.ORDER_SALARY_TRUSTEESHIP_ORDER_ID, String.valueOf(salaryTrusteeshipOrderId));
        }
        return getTrusteeshipBuyServiceBidAllintaskPayOrderStringRequest;
    }

    @Override
    public OkHttpRequest trusteeshipPayRequest(String orderString, String payPwd, String ip) {
        OkHttpRequest trusteeshipPayRequest = new OkHttpRequest();
        trusteeshipPayRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_SALARY_TRUSTEESHIP_PAY_PAY);
        trusteeshipPayRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_SALARY_TRUSTEESHIP_PAY_PAY);
        trusteeshipPayRequest.addRequestFormParam(ApiKey.ORDER_ORDER_STRING, orderString);
        trusteeshipPayRequest.addRequestFormParam(ApiKey.ORDER_PAY_PWD, payPwd);
        trusteeshipPayRequest.addRequestFormParam(ApiKey.ORDER_IP, ip);
        return trusteeshipPayRequest;
    }

    @Override
    public OkHttpRequest getPublishPaymentDemandAlipayOrderStringRequest(String serveBidNo) {
        OkHttpRequest getPublishPaymentDemandAlipayOrderStringRequest = new OkHttpRequest();
        getPublishPaymentDemandAlipayOrderStringRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_PAY_ALIPAY_GET_PAY_PUBLISH_DEMAND_ORDER_STRING);
        getPublishPaymentDemandAlipayOrderStringRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_PAY_ALIPAY_GET_PAY_PUBLISH_DEMAND_ORDER_STRING);
        getPublishPaymentDemandAlipayOrderStringRequest.addRequestFormParam(ApiKey.ORDER_SERVE_BID_NO, serveBidNo);
        return getPublishPaymentDemandAlipayOrderStringRequest;
    }

    @Override
    public OkHttpRequest getPublishPaymentDemandAllintaskPayOrderStringRequest(String serveBidNo) {
        OkHttpRequest getPublishPaymentDemandAllintaskPayOrderStringRequest = new OkHttpRequest();
        getPublishPaymentDemandAllintaskPayOrderStringRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_PAY_LING_DAO_GET_PAY_PUBLISH_DEMAND_ORDER_STRING);
        getPublishPaymentDemandAllintaskPayOrderStringRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_PAY_LING_DAO_GET_PAY_PUBLISH_DEMAND_ORDER_STRING);
        getPublishPaymentDemandAllintaskPayOrderStringRequest.addRequestFormParam(ApiKey.ORDER_SERVE_BID_NO, serveBidNo);
        return getPublishPaymentDemandAllintaskPayOrderStringRequest;
    }

    @Override
    public OkHttpRequest allintaskPayV1Request(String orderString, String payPwd, String ip) {
        OkHttpRequest allintaskPayV1Request = new OkHttpRequest();
        allintaskPayV1Request.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_LING_DAO_PAY_V1_PAY);
        allintaskPayV1Request.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_LING_DAO_PAY_V1_PAY);
        allintaskPayV1Request.addRequestFormParam(ApiKey.ORDER_ORDER_STRING, orderString);
        allintaskPayV1Request.addRequestFormParam(ApiKey.ORDER_PAY_PWD, payPwd);
        allintaskPayV1Request.addRequestFormParam(ApiKey.ORDER_IP, ip);
        return allintaskPayV1Request;
    }

}
