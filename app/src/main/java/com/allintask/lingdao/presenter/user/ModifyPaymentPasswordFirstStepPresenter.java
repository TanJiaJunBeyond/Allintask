package com.allintask.lingdao.presenter.user;

import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IModifyPaymentPasswordFirstStepView;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/3/22.
 */

public class ModifyPaymentPasswordFirstStepPresenter extends BasePresenter<IModifyPaymentPasswordFirstStepView> {

    private IUserModel userModel;

    public ModifyPaymentPasswordFirstStepPresenter() {
        userModel = new UserModel();
    }

    public void checkOldPaymentPasswordRequest(String payPwd) {
        OkHttpRequest checkOldPaymentPasswordRequest = userModel.checkOldPaymentPasswordRequest(payPwd);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, checkOldPaymentPasswordRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_CHECK_PAY_PWD)) {
            if (success) {
                boolean oldPaymentPasswordCorrect = Boolean.valueOf(data);

                if (oldPaymentPasswordCorrect) {
                    if (null != getView()) {
                        getView().onCheckOldPaymentPasswordSuccess();
                    }
                } else {
                    if (null != getView()) {
                        getView().onCheckOldPaymentPasswordError();
                    }
                }
            } else {
                if (null != getView()) {
                    getView().onCheckOldPaymentPasswordFail();
                }
            }
        }
    }

}
