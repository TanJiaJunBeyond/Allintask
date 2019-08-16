package com.allintask.lingdao.presenter.user;

import android.os.CountDownTimer;

import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.ISetLoginPasswordView;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/27.
 */

public class SetLoginPasswordPresenter extends BasePresenter<ISetLoginPasswordView> {

    private IUserModel userModel;

    public SetLoginPasswordPresenter() {
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

    public void sendSetLoginPasswordSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile) {
        OkHttpRequest sendSetLoginPasswordSmsIdentifyCodeRequest = userModel.sendSetLoginPasswordSmsIdentifyCodeRequest(mobileCountryCodeId, mobile);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, sendSetLoginPasswordSmsIdentifyCodeRequest);
    }

    public void setLoginPasswordRequest(String captcha, String password, String confirmPassword) {
        OkHttpRequest setLoginPasswordRequest = userModel.setLoginPasswordRequest(captcha, password, confirmPassword);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, setLoginPasswordRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_SET_LOGIN_PWD)) {
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

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_SET_LOGIN_PASSWORD)) {
            if (success) {
                if (null != getView()) {
                    getView().onSetLoginPasswordSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("设置登录密码失败");
                }
            }
        }
    }

}
