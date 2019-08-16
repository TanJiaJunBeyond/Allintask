package com.allintask.lingdao.presenter.pay;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.pay.PayBean;
import com.allintask.lingdao.bean.pay.PaymentMethodBean;
import com.allintask.lingdao.bean.user.CheckCanWithdrawBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.pay.IPayModel;
import com.allintask.lingdao.model.pay.PayModel;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.utils.IPAddressUtils;
import com.allintask.lingdao.view.pay.ITrusteeshipPayView;

import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/5/12.
 */

public class TrusteeshipPayPresenter extends BasePresenter<ITrusteeshipPayView> {

    private IPayModel payModel;
    private IUserModel userModel;

    private String mServiceBidNumber;

    public TrusteeshipPayPresenter() {
        payModel = new PayModel();
        userModel = new UserModel();
    }

    public void fetchPaymentMethodListRequest(String serviceBidNumber) {
        mServiceBidNumber = serviceBidNumber;
        OkHttpRequest fetchPaymentMethodListRequest = payModel.fetchPaymentMethodListRequest();
        addRequestAsyncTaskForJson(true, OkHttpRequestOptions.POST, fetchPaymentMethodListRequest);
    }

    public void getTrusteeshipAlipayOrderStringRequest(int demandId, double payAmount) {
        OkHttpRequest getTrusteeshipAlipayOrderStringRequest = payModel.getTrusteeshipAlipayOrderStringRequest(demandId, payAmount);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, getTrusteeshipAlipayOrderStringRequest);
    }

    public void checkCanWithdrawRequest() {
        OkHttpRequest checkCanWithdrawRequest = userModel.checkCanWithdrawRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.POST, checkCanWithdrawRequest);
    }

    public void getTrusteeshipAllintaskPayOrderStringRequest(int demandId, double payAmount) {
        OkHttpRequest getTrusteeshipAllintaskPayOrderStringRequest = payModel.getTrusteeshipAllintaskPayOrderStringRequest(demandId, payAmount);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, getTrusteeshipAllintaskPayOrderStringRequest);
    }

    public void allintaskPayRequest(String orderString, String payPwd) {
        String IPAddress = IPAddressUtils.getIPAddress(getView().getParentContext());
        OkHttpRequest allintaskPayRequest = payModel.allintaskPayRequest(orderString, payPwd, IPAddress);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, allintaskPayRequest);
    }

    public void allintaskPayV1Request(String orderString, String payPwd) {
        String IPAddress = IPAddressUtils.getIPAddress(getView().getParentContext());
        OkHttpRequest allintaskPayV1Request = payModel.allintaskPayV1Request(orderString, payPwd, IPAddress);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, allintaskPayV1Request);
    }

    public void getPublishPaymentDemandAlipayOrderStringRequest(String serveBidNo) {
        OkHttpRequest getPublishPaymentDemandAlipayOrderStringRequest = payModel.getPublishPaymentDemandAlipayOrderStringRequest(serveBidNo);
        addJsonStringRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, getPublishPaymentDemandAlipayOrderStringRequest);
    }

    public void getPublishPaymentDemandAllintaskPayOrderStringRequest(String serveBidNo) {
        OkHttpRequest getPublishPaymentDemandAllintaskPayOrderStringRequest = payModel.getPublishPaymentDemandAllintaskPayOrderStringRequest(serveBidNo);
        addJsonStringRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, getPublishPaymentDemandAllintaskPayOrderStringRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_PAY_CHANNEL_LIST)) {
            if (success) {
                List<PaymentMethodBean> paymentMethodList = JSONArray.parseArray(data, PaymentMethodBean.class);

                if (null != getView()) {
                    for (int i = 0; i < paymentMethodList.size(); i++) {
                        PaymentMethodBean paymentMethodBean = paymentMethodList.get(i);

                        if (i == 0) {
                            paymentMethodBean.isSelected = true;
                        } else {
                            paymentMethodBean.isSelected = false;
                        }
                    }

                    if (TextUtils.isEmpty(mServiceBidNumber)) {
                        PaymentMethodBean paymentMethodBean = new PaymentMethodBean();
                        paymentMethodBean.code = "withholdTrusteeship";
                        paymentMethodBean.value = getView().getParentContext().getString(R.string.withhold_trusteeship);
                        paymentMethodBean.isSelected = false;
                        paymentMethodList.add(paymentMethodBean);
                    }

                    getView().onShowPaymentMethodList(paymentMethodList);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_SALARY_TRUSTEESHIP_ALIPAY_GET_SALARY_TRUSEESHIP_ORDER_STRING) || requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_PAY_ALIPAY_GET_PAY_PUBLISH_DEMAND_ORDER_STRING)) {
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

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_SALARY_TRUSTEESHIP_LING_DAO_GET_SALARY_TRUSTEESHIP_ORDER_STRING) || requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_PAY_LING_DAO_GET_PAY_PUBLISH_DEMAND_ORDER_STRING)) {
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

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_LING_DAO_PAY_PAY) || requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_LING_DAO_PAY_V1_PAY)) {
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
    }

}
