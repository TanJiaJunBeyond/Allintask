package com.allintask.lingdao.presenter.user;

import android.os.CountDownTimer;

import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IModifyPaymentPasswordSecondStepView;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/3/22.
 */

public class ModifyPaymentPasswordSecondStepPresenter extends BasePresenter<IModifyPaymentPasswordSecondStepView> {

    private IUserModel userModel;

    public ModifyPaymentPasswordSecondStepPresenter() {
        userModel = new UserModel();
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

    public void sendModifyPaymentPasswordSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile) {
        OkHttpRequest sendModifyPaymentPasswordSmsIdentifyCodeRequest = userModel.sendModifyPaymentPasswordSmsIdentifyCodeRequest(mobileCountryCodeId, mobile);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, sendModifyPaymentPasswordSmsIdentifyCodeRequest);
    }

    public void checkModifyPaymentPasswordSmsIdentifyCodeRequest(String captcha) {
        OkHttpRequest checkModifyPaymentPasswordSmsIdentifyCodeRequest = userModel.checkModifyPaymentPasswordSmsIdentifyCodeRequest(captcha);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, checkModifyPaymentPasswordSmsIdentifyCodeRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_MODIFY_PAY_PWD)) {
            if (success) {
                if (null != getView()) {
                    getView().onSendSmsIdentifyCodeSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("发送验证码失败");
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_CHECK_MODIFY_PAY_PWD_CAPTCHA)) {
            if (success) {
                boolean smsIdentifyCodeCorrect = Boolean.valueOf(data);

                if (smsIdentifyCodeCorrect) {
                    if (null != getView()) {
                        getView().onCheckSmsIdentifyCodeSuccess();
                    }
                } else {
                    if (null != getView()) {
                        getView().showToast("验证码错误");
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast("请求失败");
                }
            }
        }
    }

}
