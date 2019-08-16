package com.allintask.lingdao.presenter.user;

import android.os.CountDownTimer;

import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.ISetPaymentPasswordFirstStepView;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/27.
 */

public class SetPaymentPasswordFirstStepPresenter extends BasePresenter<ISetPaymentPasswordFirstStepView> {

    private IUserModel userModel;

    public SetPaymentPasswordFirstStepPresenter() {
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

    public void sendSetPaymentPasswordSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile) {
        OkHttpRequest sendSetPaymentPasswordSmsIdentifyCodeRequest = userModel.sendSetPaymentPasswordSmsIdentifyCodeRequest(mobileCountryCodeId, mobile);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, sendSetPaymentPasswordSmsIdentifyCodeRequest);
    }

    public void checkSetPaymentPasswordSmsIdentifyCodeRequest(String captcha) {
        OkHttpRequest checkSetPaymentPasswordSmsIdentifyCodeRequest = userModel.checkSetPaymentPasswordSmsIdentifyCodeRequest(captcha);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, checkSetPaymentPasswordSmsIdentifyCodeRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_SET_PAY_PWD)) {
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

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_CHECK_SET_PAY_PWD_CAPTCHA)) {
            if (success) {
                boolean smsIdentifyCodeCorrect = Boolean.valueOf(data);

                if (smsIdentifyCodeCorrect) {
                    if (null != getView()) {
                        getView().onCheckSetPaymentPasswordSmsIdentifyCodeSuccess();
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
