package com.allintask.lingdao.presenter.user;

import android.os.CountDownTimer;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.PhoneNumberHomeLocationBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IForgetPasswordView;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2017/12/29.
 */

public class ForgetPasswordPresenter extends BasePresenter<IForgetPasswordView> {

    private IUserModel userModel;

    public ForgetPasswordPresenter() {
        userModel = new UserModel();
    }

    public void fetchDefaultPhoneNumberHomeLocationRequest() {
        OkHttpRequest fetchDefaultPhoneNumberHomeLocationRequest = userModel.fetchDefaultPhoneNumberHomeLocationRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchDefaultPhoneNumberHomeLocationRequest);
    }

    public void sendForgetPasswordSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile) {
        OkHttpRequest sendForgetPasswordSmsIdentifyCodeRequest = userModel.sendForgetPasswordSmsIdentifyCodeRequest(mobileCountryCodeId, mobile);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, sendForgetPasswordSmsIdentifyCodeRequest);
    }

    public void resetLoginPasswordRequest(int mobileCountryCodeId, String mobile, String captcha, String pwd, String confirmPwd) {
        OkHttpRequest resetLoginPasswordRequest = userModel.resetLoginPasswordRequest(mobileCountryCodeId, mobile, captcha, pwd, confirmPwd);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, resetLoginPasswordRequest);
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

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_MOBILE_COUNTRY_CODE_GET_DEFAULT)) {
            if (success) {
                if (null != getView()) {
                    PhoneNumberHomeLocationBean phoneNumberHomeLocationBean = JSONObject.parseObject(data, PhoneNumberHomeLocationBean.class);

                    if (null != phoneNumberHomeLocationBean) {
                        int id = TypeUtils.getInteger(phoneNumberHomeLocationBean.mobileCountryCodeId, 0);
                        String code = TypeUtils.getString(phoneNumberHomeLocationBean.value, "");
                        String regularEx = TypeUtils.getString(phoneNumberHomeLocationBean.regularEx, "");

                        if (null != getView()) {
                            getView().onShowPhoneNumberHomeLocationMobileCountryCodeId(id);
                            getView().onShowPhoneNumberHomeLocationValue(code);
                            getView().onShowPhoneNumberHomeLocationRegularEx(regularEx);
                        }
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_FORGET_LOGIN_PWD)) {
            if (success) {
                if (null != getView()) {
                    getView().onSendSmsIdentifyCodeSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_RESET_PWD_BY_CAPTCHA)) {
            if (success) {
                if (null != getView()) {
                    getView().onResetLoginPasswordSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
