package com.allintask.lingdao.presenter.user;

import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IGesturePasswordUnlockView;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/5/25.
 */

public class GesturePasswordUnlockPresenter extends BasePresenter<IGesturePasswordUnlockView> {

    private IUserModel userModel;

    public GesturePasswordUnlockPresenter() {
        userModel = new UserModel();
    }

    public void checkGesturePasswordRequest(String gesturePwd) {
        OkHttpRequest checkGesturePasswordRequest = userModel.checkGesturePasswordRequest(gesturePwd);
        addJsonStringRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, checkGesturePasswordRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_CHECK_GESTURE_PWD)) {
            if (success) {
                if (null != getView()) {
                    getView().onCheckGesturePasswordSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().onCheckGesturePasswordFail(errorMessage);
                }
            }
        }
    }

}
