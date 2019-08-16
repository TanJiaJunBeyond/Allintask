package com.allintask.lingdao.presenter.user;

import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.ICompileNameView;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/2/7.
 */

public class CompileNamePresenter extends BasePresenter<ICompileNameView> {

    private IUserModel userModel;

    public CompileNamePresenter() {
        userModel = new UserModel();
    }

    public void setNameRequest(String name) {
        OkHttpRequest setNameRequest = userModel.setNameRequest(name);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, setNameRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SET_NAME)) {
            if (success) {
                if (null != getView()) {
                    getView().onCompileNameSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("修改姓名失败");
                }
            }
        }
    }

}
