package com.allintask.lingdao.presenter.user;

import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.ISelectResetPaymentPasswordWayView;

/**
 * Created by TanJiaJun on 2018/7/5.
 */

public class SelectResetPaymentPasswordWayPresenter extends BasePresenter<ISelectResetPaymentPasswordWayView> {

    private IUserModel userModel;

    public SelectResetPaymentPasswordWayPresenter() {
        userModel = new UserModel();
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {

    }

}
