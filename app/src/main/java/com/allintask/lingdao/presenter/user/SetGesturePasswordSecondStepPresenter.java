package com.allintask.lingdao.presenter.user;

import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.ISetGesturePasswordSecondStepView;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/5/25.
 */

public class SetGesturePasswordSecondStepPresenter extends BasePresenter<ISetGesturePasswordSecondStepView> {

    private IUserModel userModel;

    public SetGesturePasswordSecondStepPresenter() {
        userModel = new UserModel();
    }

    public void createGesturePasswordRequest(String gesturePwd, String confirmGesturePwd) {
        OkHttpRequest createGesturePasswordRequest = userModel.createGesturePasswordRequest(gesturePwd, confirmGesturePwd);
        addJsonStringRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, createGesturePasswordRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_CREATE_GESTURE_PWD)) {
            if (success) {
                if (null != getView()) {
                    getView().onCreditGesturePasswordSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
