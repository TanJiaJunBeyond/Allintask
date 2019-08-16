package com.allintask.lingdao.presenter.user;

import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IModifyPaymentPasswordThirdStepView;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/3/22.
 */

public class ModifyPaymentPasswordThirdStepPresenter extends BasePresenter<IModifyPaymentPasswordThirdStepView> {

    private IUserModel userModel;

    public ModifyPaymentPasswordThirdStepPresenter() {
        userModel = new UserModel();
    }

    public void modifyPaymentPasswordRequest(String captcha, String oldPayPwd, String newPayPwd) {
        OkHttpRequest modifyPaymentPasswordRequest = userModel.modifyPaymentPasswordRequest(captcha, oldPayPwd, newPayPwd);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.PUT, modifyPaymentPasswordRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_MODIFY_PAY_PWD)) {
            if (success) {
                if (null != getView()) {
                    getView().onModifyPaymentPasswordSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("修改支付密码失败");
                }
            }
        }
    }

}
