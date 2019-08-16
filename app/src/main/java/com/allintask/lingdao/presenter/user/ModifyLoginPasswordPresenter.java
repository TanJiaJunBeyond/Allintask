package com.allintask.lingdao.presenter.user;

import android.os.CountDownTimer;

import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IModifyLoginPasswordView;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/27.
 */

public class ModifyLoginPasswordPresenter extends BasePresenter<IModifyLoginPasswordView> {

    private IUserModel userModel;

    public ModifyLoginPasswordPresenter() {
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

    public void sendModifyLoginPasswordSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile) {
        OkHttpRequest sendModifyLoginPasswordSmsIdentifyCodeRequest = userModel.sendModifyLoginPasswordSmsIdentifyCodeRequest(mobileCountryCodeId, mobile);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, sendModifyLoginPasswordSmsIdentifyCodeRequest);
    }

    public void modifyLoginPasswordRequest(String captcha, String password, String confirmPassword) {
        OkHttpRequest modifyLoginPasswordRequest = userModel.modifyLoginPasswordRequest(captcha, password, confirmPassword);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.PUT, modifyLoginPasswordRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_MODIFY_LOGIN_PWD)) {
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

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_MODIFY_LOGIN_PASSWORD)) {
            if (success) {
                if (null != getView()) {
                    getView().onModifyLoginPasswordSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
