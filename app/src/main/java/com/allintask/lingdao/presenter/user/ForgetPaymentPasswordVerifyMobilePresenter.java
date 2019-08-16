package com.allintask.lingdao.presenter.user;

import android.os.CountDownTimer;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.GetHiddenUserDataBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IForgetPaymentPasswordVerifyMobileView;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/7/5.
 */

public class ForgetPaymentPasswordVerifyMobilePresenter extends BasePresenter<IForgetPaymentPasswordVerifyMobileView> {

    private IUserModel userModel;

    public ForgetPaymentPasswordVerifyMobilePresenter() {
        userModel = new UserModel();
    }

    public void getHiddenUserDataRequest() {
        OkHttpRequest getHiddenUserDataRequest = userModel.getHiddenUserDataRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, getHiddenUserDataRequest);
    }

    public void sendForgetPaymentPasswordIdentifyCodeRequest() {
        OkHttpRequest sendForgetPaymentPasswordIdentifyCodeRequest = userModel.sendForgetPaymentPasswordIdentifyCodeRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, sendForgetPaymentPasswordIdentifyCodeRequest);
    }

    public CountDownTimer startCountDownTimer() {
        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (null != getView()) {
                    getView().onCountDownTimerTick(millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                if (null != getView()) {
                    getView().onCountDownTimerFinish();
                }
            }
        };

        countDownTimer.start();
        return countDownTimer;
    }

    public void checkForgetPaymentPasswordIdentifyCodeRequest(String identifyCode) {
        OkHttpRequest checkForgetPaymentPasswordIdentifyCodeRequest = userModel.checkForgetPaymentPasswordIdentifyCodeRequest(identifyCode);
        addJsonStringRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, checkForgetPaymentPasswordIdentifyCodeRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_GET_HIDDEN_USER_DATA)) {
            if (success) {
                GetHiddenUserDataBean getHiddenUserDataBean = JSONObject.parseObject(data, GetHiddenUserDataBean.class);

                if (null != getHiddenUserDataBean) {
                    String hiddenMobile = TypeUtils.getString(getHiddenUserDataBean.hiddenMobile, "");

                    if (null != getView()) {
                        getView().onShowHiddenMobile(hiddenMobile);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_FORGET_PAY_PWD)) {
            if (success) {
                if (null != getView()) {
                    getView().onSendForgetPaymentPasswordIdentifyCodeSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_CHECK_FORGET_PAY_PWD_CAPTCHA)) {
            if (success) {
                if (null != getView()) {
                    getView().onCheckForgetPaymentPasswordIdentifyCodeSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().onCheckForgetPaymentPasswordIdentifyCodeFail(errorMessage);
                }
            }
        }
    }

}
