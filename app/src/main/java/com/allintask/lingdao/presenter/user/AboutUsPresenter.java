package com.allintask.lingdao.presenter.user;

import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IAboutUsView;

/**
 * Created by TanJiaJun on 2018/2/26.
 */

public class AboutUsPresenter extends BasePresenter<IAboutUsView> {

    private IUserModel userModel;

    public AboutUsPresenter() {
        userModel = new UserModel();
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {

    }

}
