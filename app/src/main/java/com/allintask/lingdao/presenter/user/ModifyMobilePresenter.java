package com.allintask.lingdao.presenter.user;

import android.os.CountDownTimer;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.PhoneNumberHomeLocationBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IModifyMobileView;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/26.
 */

public class ModifyMobilePresenter extends BasePresenter<IModifyMobileView> {

    private IUserModel userModel;

    public ModifyMobilePresenter() {
        userModel = new UserModel();
    }

    public void fetchDefaultPhoneNumberHomeLocationRequest() {
        OkHttpRequest fetchDefaultPhoneNumberHomeLocationRequest = userModel.fetchDefaultPhoneNumberHomeLocationRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchDefaultPhoneNumberHomeLocationRequest);
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

    public void sendModifyPhoneNumberSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile) {
        OkHttpRequest sendModifyPhoneNumberSmsIdentifyCodeRequest = userModel.sendModifyPhoneNumberSmsIdentifyCodeRequest(mobileCountryCodeId, mobile);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, sendModifyPhoneNumberSmsIdentifyCodeRequest);
    }

    public void modifyPhoneNumberRequest(int oldMobileCountryCodeId, String oldMobile, String password, int newMobileCountryCodeId, String newMobile, String captcha) {
        OkHttpRequest modifyPhoneNumberRequest = userModel.modifyPhoneNumberRequest(oldMobileCountryCodeId, oldMobile, password, newMobileCountryCodeId, newMobile, captcha);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.PUT, modifyPhoneNumberRequest);
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
                    getView().showToast("获取手机归属地区号失败");
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_MODIFY_MOBILE)) {
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

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_MODIFY_MOBILE)) {
            if (success) {
                if (null != getView()) {
                    getView().onModifyPhoneNumberSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
