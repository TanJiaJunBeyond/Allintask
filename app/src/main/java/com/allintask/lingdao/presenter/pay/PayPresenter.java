package com.allintask.lingdao.presenter.pay;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.pay.PayBean;
import com.allintask.lingdao.bean.pay.PaymentMethodBean;
import com.allintask.lingdao.bean.pay.TrusteeshipSurplusBean;
import com.allintask.lingdao.bean.user.CheckCanWithdrawBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.pay.IPayModel;
import com.allintask.lingdao.model.pay.PayModel;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.utils.IPAddressUtils;
import com.allintask.lingdao.view.pay.IPayView;

import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/3/9.
 */

public class PayPresenter extends BasePresenter<IPayView> {

    private IPayModel payModel;
    private IUserModel userModel;

    public PayPresenter() {
        payModel = new PayModel();
        userModel = new UserModel();
    }

    public void fetchTrusteeshipSurplusRequest(int demandId) {
        OkHttpRequest fetchTrusteeshipSurplusRequest = payModel.fetchTrusteeshipSurplusRequest(demandId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, fetchTrusteeshipSurplusRequest);
    }

    public void fetchPaymentMethodListRequest() {
        OkHttpRequest fetchPaymentMethodListRequest = payModel.fetchPaymentMethodListRequest();
        addRequestAsyncTaskForJson(true, OkHttpRequestOptions.POST, fetchPaymentMethodListRequest);
    }

    public void getBuyServiceBidAlipayOrderStringRequest(int serveBidId, double payAmount, int salaryTrusteeshipOrderId) {
        OkHttpRequest getBuyServiceBidAlipayOrderStringRequest = payModel.getBuyServiceBidAlipayOrderStringRequest(serveBidId, payAmount, salaryTrusteeshipOrderId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, getBuyServiceBidAlipayOrderStringRequest);
    }

    public void checkCanWithdrawRequest() {
        OkHttpRequest checkCanWithdrawRequest = userModel.checkCanWithdrawRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.POST, checkCanWithdrawRequest);
    }

    public void getBuyServiceBidAllintaskPayOrderStringRequest(int serveBidId, double payAmount, int salaryTrusteeshipOrderId) {
        OkHttpRequest getBuyServiceBidAllintaskPayOrderStringRequest = payModel.getBuyServiceBidAllintaskPayOrderStringRequest(serveBidId, payAmount, salaryTrusteeshipOrderId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, getBuyServiceBidAllintaskPayOrderStringRequest);
    }

    public void allintaskPayRequest(String orderString, String payPwd) {
        String IPAddress = IPAddressUtils.getIPAddress(getView().getParentContext());
        OkHttpRequest allintaskPayRequest = payModel.allintaskPayRequest(orderString, payPwd, IPAddress);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, allintaskPayRequest);
    }

    public void getTrusteeshipBuyServiceBidAllintaskPayOrderStringRequest(int serveBidId, double payAmount, int salaryTrusteeshipOrderId) {
        OkHttpRequest getTrusteeshipBuyServiceBidAllintaskPayOrderStringRequest = payModel.getTrusteeshipBuyServiceBidAllintaskPayOrderStringRequest(serveBidId, payAmount, salaryTrusteeshipOrderId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, getTrusteeshipBuyServiceBidAllintaskPayOrderStringRequest);
    }

    public void trusteeshipPayRequest(String orderString, String payPwd) {
        String IPAddress = IPAddressUtils.getIPAddress(getView().getParentContext());
        OkHttpRequest trusteeshipPayRequest = payModel.trusteeshipPayRequest(orderString, payPwd, IPAddress);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, trusteeshipPayRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_SALARY_TRUSTEESHIP_PAY_SALARY_TRUSTEESHIP_SURPLUS)) {
            if (success) {
                TrusteeshipSurplusBean trusteeshipSurplusBean = JSONObject.parseObject(data, TrusteeshipSurplusBean.class);

                if (null != trusteeshipSurplusBean) {
                    int salaryTrusteeshipId = TypeUtils.getInteger(trusteeshipSurplusBean.salaryTrusteeshipId, -1);
                    Double surplusAmount = trusteeshipSurplusBean.surplusAmount;

                    if (null == surplusAmount) {
                        surplusAmount = 0D;
                    }

                    if (null != getView()) {
                        getView().onShowTrusteeshipSurplusData(salaryTrusteeshipId, surplusAmount);
                    }
                } else {
                    if (null != getView()) {
                        getView().onShowTrusteeshipNoSurplus();
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_PAY_CHANNEL_LIST)) {
            if (success) {
                List<PaymentMethodBean> paymentMethodList = JSONArray.parseArray(data, PaymentMethodBean.class);

                if (null != getView()) {
                    for (int i = 0; i < paymentMethodList.size(); i++) {
                        PaymentMethodBean paymentMethodBean = paymentMethodList.get(i);

                        if (null != paymentMethodBean) {
                            if (i == 0) {
                                paymentMethodBean.isSelected = true;
                            } else {
                                paymentMethodBean.isSelected = false;
                            }
                        }
                    }

                    getView().onShowPaymentMethodList(paymentMethodList);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_PAY_ALIPAY_GET_BUY_SERVE_BID_ORDER_STRING)) {
            if (success) {
                PayBean payBean = JSONObject.parseObject(data, PayBean.class);

                if (null != payBean) {
                    String orderString = TypeUtils.getString(payBean.orderString, "");
                    double totalAmount = TypeUtils.getDouble(payBean.totalAmount, 0D);

                    if (null != getView()) {
                        getView().onGetAliPayOrderStringSuccess(orderString, totalAmount);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_BANK_CARD_CHECK_CAN_WITHDRAW)) {
            if (success) {
                CheckCanWithdrawBean checkCanWithdrawBean = JSONObject.parseObject(data, CheckCanWithdrawBean.class);

                if (null != checkCanWithdrawBean) {
                    boolean isExistBankCard = TypeUtils.getBoolean(checkCanWithdrawBean.isExistBankCard, false);
                    boolean isExistPayPwd = TypeUtils.getBoolean(checkCanWithdrawBean.isExistPayPwd, false);
                    boolean isExistRealName = TypeUtils.getBoolean(checkCanWithdrawBean.isExistRealName, false);

                    if (null != getView()) {
                        getView().onShowCheckCanWithdrawData(isExistBankCard, isExistPayPwd, isExistRealName);
                        getView().showContentView();
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_PAY_LING_DAO_GET_BUY_SERVE_BID_ORDER_STRING)) {
            if (success) {
                PayBean payBean = JSONObject.parseObject(data, PayBean.class);

                if (null != payBean) {
                    String orderString = TypeUtils.getString(payBean.orderString, "");
                    double totalAmount = TypeUtils.getDouble(payBean.totalAmount, 0D);

                    if (null != getView()) {
                        getView().onGetAllintaskPayOrderStringSuccess(orderString, totalAmount);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_LING_DAO_PAY_PAY)) {
            if (success) {
                if (null != getView()) {
                    getView().onAllintaskPaySuccess();
                }
            } else {
                if (null != getView()) {
                    getView().onAllintaskPayFail(errorCode, errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_PAY_SALARY_TRUSTEESHIP_GET_BUY_SERVE_BID_ORDER_STRING)) {
            if (success) {
                PayBean payBean = JSONObject.parseObject(data, PayBean.class);

                if (null != payBean) {
                    String orderString = TypeUtils.getString(payBean.orderString, "");
                    double totalAmount = TypeUtils.getDouble(payBean.totalAmount, 0D);

                    if (null != getView()) {
                        getView().onGetTrusteeshipBuyServiceBidAllintaskPayOrderStringSuccess(orderString, totalAmount);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_SALARY_TRUSTEESHIP_PAY_PAY)) {
            if (success) {
                if (null != getView()) {
                    getView().onTrusteeshipPaySuccess();
                }
            } else {
                if (null != getView()) {
                    getView().onTrusteeshipPayFail(errorCode, errorMessage);
                }
            }
        }
    }

}
