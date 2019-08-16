package com.allintask.lingdao.presenter.user;

import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.ISetPaymentPasswordSecondStepView;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/27.
 */

public class SetPaymentPasswordSecondStepPresenter extends BasePresenter<ISetPaymentPasswordSecondStepView> {

    private IUserModel userModel;

    public SetPaymentPasswordSecondStepPresenter() {
        userModel = new UserModel();
    }

    public void setPaymentPasswordRequest(String captcha, String payPwd) {
        OkHttpRequest setPaymentPasswordRequest = userModel.setPaymentPasswordRequest(captcha, payPwd);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, setPaymentPasswordRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_SET_PAY_PWD)) {
            if (success) {
                if (null != getView()) {
                    getView().onSetPaymentPasswordSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().onSetPaymentPasswordFail();
                }
            }
        }
    }

}
