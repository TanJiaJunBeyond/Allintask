package com.allintask.lingdao.presenter.user;

import android.os.CountDownTimer;

import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IBindMailboxView;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/26.
 */

public class BindMailboxPresenter extends BasePresenter<IBindMailboxView> {

    private IUserModel userModel;

    public BindMailboxPresenter() {
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

    public void sendBindMailboxSmsIdentifyCodeRequest(String email) {
        OkHttpRequest sendBindMailboxSmsIdentifyCodeRequest = userModel.sendBindMailboxSmsIdentifyCodeRequest(email);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, sendBindMailboxSmsIdentifyCodeRequest);
    }

    public void bindMailboxRequest(String captcha, String email) {
        OkHttpRequest bindMailBoxRequest = userModel.bindMailboxRequest(captcha, email);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, bindMailBoxRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_MAIL_SEND_CAPTCHA_TO_SET_EMAIL)) {
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

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_SET_EMAIL)) {
            if (success) {
                if (null != getView()) {
                    getView().onBindMailboxSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("设置邮箱失败");
                }
            }
        }
    }

}
