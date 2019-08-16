package com.allintask.lingdao.presenter.user;

import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IForgetPaymentPasswordFifthStepView;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/7/9.
 */

public class ForgetPaymentPasswordFifthStepPresenter extends BasePresenter<IForgetPaymentPasswordFifthStepView> {

    private IUserModel userModel;

    public ForgetPaymentPasswordFifthStepPresenter() {
        userModel = new UserModel();
    }

    public void resetPaymentPasswordRequest(String captcha, String idCardNo, String payPwd, String confirmPayPwd) {
        OkHttpRequest resetPaymentPasswordRequest = userModel.resetPaymentPasswordRequest(captcha, idCardNo, payPwd, confirmPayPwd);
        addJsonStringRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, resetPaymentPasswordRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_FORGET_PAY_PWD)) {
            if (success) {
                if (null != getView()) {
                    getView().onResetPaymentPasswordSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().onResetPaymentPasswordFail(errorMessage);
                }
            }
        }
    }

}
