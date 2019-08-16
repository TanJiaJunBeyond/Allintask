package com.allintask.lingdao.presenter.user;

import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IPaymentPasswordUnlockView;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/7/10.
 */

public class PaymentPasswordUnlockPresenter extends BasePresenter<IPaymentPasswordUnlockView> {

    private IUserModel userModel;

    public PaymentPasswordUnlockPresenter() {
        userModel = new UserModel();
    }

    public void checkPaymentPasswordRequest(String payPwd) {
        OkHttpRequest checkPaymentPasswordRequest = userModel.checkPaymentPasswordRequest(payPwd);
        addJsonStringRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, checkPaymentPasswordRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_CHECK_PAY_PWD)) {
            if (success) {
                if (null != getView()) {
                    getView().onCheckPaymentPasswordSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().onCheckPaymentPasswordFail(errorMessage);
                }
            }
        }
    }

}
