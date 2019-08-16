package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.WithdrawDetailsBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.utils.IPAddressUtils;
import com.allintask.lingdao.view.user.IApplyForWithdrawDepositView;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/5/3.
 */

public class ApplyForWithdrawDepositPresenter extends BasePresenter<IApplyForWithdrawDepositView> {

    private IUserModel userModel;

    public ApplyForWithdrawDepositPresenter() {
        userModel = new UserModel();
    }

    public void fetchWithdrawDetailsRequest() {
        OkHttpRequest fetchWithdrawDetailsRequest = userModel.fetchWithdrawDetailsRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchWithdrawDetailsRequest);
    }

    public void withdrawDepositRequest(int bankCardId, int amount, String payPwd, String withdrawLogId) {
        String IPAddress = IPAddressUtils.getIPAddress(getView().getParentContext());
        OkHttpRequest withdrawDepositRequest = userModel.withdrawDepositRequest(bankCardId, amount, payPwd, IPAddress, withdrawLogId);
        addJsonStringRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, withdrawDepositRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_BANK_CARD_WITHDRAW_DETAILS)) {
            if (success) {
                WithdrawDetailsBean withdrawDetailsBean = JSONObject.parseObject(data, WithdrawDetailsBean.class);

                if (null != withdrawDetailsBean) {
                    String realName = TypeUtils.getString(withdrawDetailsBean.realName, "");
                    int bankCardId = TypeUtils.getInteger(withdrawDetailsBean.bankCardId, -1);
                    String bankCardDetails = TypeUtils.getString(withdrawDetailsBean.bankCardDetails, "");
                    double withdrawRate = TypeUtils.getDouble(withdrawDetailsBean.withdrawRate, 0D);
                    double canWithdraw = TypeUtils.getDouble(withdrawDetailsBean.canWithdraw, 0D);
                    int withdrawLowPrice = TypeUtils.getInteger(withdrawDetailsBean.withdrawLowPrice, 0);
                    boolean isCollectWithdrawPoundage = TypeUtils.getBoolean(withdrawDetailsBean.isCollectWithdrawPoundage, false);
                    String withdrawRateTip = TypeUtils.getString(withdrawDetailsBean.withdrawRateTip, "");
                    Integer surplusPayPwdInputCount = withdrawDetailsBean.surplusPayPwdInputCount;
                    String payPwdInputErrorTip = TypeUtils.getString(withdrawDetailsBean.payPwdInputErrorTip, "");

                    if (null != getView()) {
                        getView().onShowWithdrawDetailsData(realName, bankCardId, bankCardDetails, withdrawRate, canWithdraw, withdrawLowPrice, isCollectWithdrawPoundage, withdrawRateTip, surplusPayPwdInputCount, payPwdInputErrorTip);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_BANK_CARD_WITHDRAW)) {
            if (success) {
                if (null != getView()) {
                    getView().onWithdrawDepositSuccess();
                }
            } else {
                if (errorCode == 40008) {
                    if (null != getView()) {
                        getView().onWithdrawDepositFail(errorMessage);
                    }
                } else {
                    if (null != getView()) {
                        getView().showToast(errorMessage);
                    }
                }
            }
        }
    }

}
