package com.allintask.lingdao.presenter.user;

import android.os.CountDownTimer;

import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IModifyMailboxView;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/26.
 */

public class ModifyMailboxPresenter extends BasePresenter<IModifyMailboxView> {

    private IUserModel userModel;

    public ModifyMailboxPresenter() {
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

    public void sendModifyMailboxSmsIdentifyCodeRequest(String email) {
        OkHttpRequest sendModifyMailboxSmsIdentifyCodeRequest = userModel.sendModifyMailboxSmsIdentifyCodeRequest(email);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, sendModifyMailboxSmsIdentifyCodeRequest);
    }

    public void modifyMailboxRequest(String captcha, String email) {
        OkHttpRequest modifyMailboxRequest = userModel.modifyMailboxRequest(captcha, email);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.PUT, modifyMailboxRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_MAIL_SEND_CAPTCHA_TO_MODIFY_EMAIL)) {
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

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_MODIFY_EMAIL)) {
            if (success) {
                if (null != getView()) {
                    getView().onModifyMailboxSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("修改邮箱失败");
                }
            }
        }
    }

}
