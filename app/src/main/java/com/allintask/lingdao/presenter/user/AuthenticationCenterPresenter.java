package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.MyDataBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IAuthenticationCenterView;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/6/15.
 */

public class AuthenticationCenterPresenter extends BasePresenter<IAuthenticationCenterView> {

    private IUserModel userModel;

    public AuthenticationCenterPresenter() {
        userModel = new UserModel();
    }

    public void fetchMyDataRequest() {
        OkHttpRequest fetchMyDataRequest = userModel.fetchMyDataRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchMyDataRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MINE)) {
            if (success) {
                MyDataBean myDataBean = JSONObject.parseObject(data, MyDataBean.class);

                if (null != myDataBean) {
                    boolean zmrzAuthSuccess = TypeUtils.getBoolean(myDataBean.zmrzAuthSuccess, false);

                    if (null != getView()) {
                        getView().showIsZmrzAuthSuccess(zmrzAuthSuccess);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
