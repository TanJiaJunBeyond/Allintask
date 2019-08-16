package com.allintask.lingdao.presenter.user;

import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IFingerprintVerifyView;

/**
 * Created by TanJiaJun on 2018/5/28.
 */

public class FingerprintVerifyPresenter extends BasePresenter<IFingerprintVerifyView> {

    private IUserModel userModel;

    public FingerprintVerifyPresenter() {
        userModel = new UserModel();
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {

    }

}
