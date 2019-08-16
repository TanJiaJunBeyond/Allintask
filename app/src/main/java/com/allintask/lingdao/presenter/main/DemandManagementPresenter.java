package com.allintask.lingdao.presenter.main;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.CheckBasicPersonalInformationBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.main.IDemandManagementView;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/4/2.
 */

public class DemandManagementPresenter extends BasePresenter<IDemandManagementView> {

    private IUserModel userModel;

    public DemandManagementPresenter() {
        userModel = new UserModel();
    }

    public void checkBasicPersonalInformationWholeRequest() {
        OkHttpRequest checkBasicPersonalInformationWholeRequest = userModel.checkBasicPersonalInformationWholeRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, checkBasicPersonalInformationWholeRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_CHECK_BASIC_MSG)) {
            if (success) {
                CheckBasicPersonalInformationBean checkBasicPersonalInformationBean = JSONObject.parseObject(data, CheckBasicPersonalInformationBean.class);

                if (null != checkBasicPersonalInformationBean) {
                    boolean isComplete = TypeUtils.getBoolean(checkBasicPersonalInformationBean.isComplete, false);

                    if (null != getView()) {
                        if (isComplete) {
                            getView().onShowCompletedBasicPersonalInformation();
                        } else {
                            getView().onShowBasicPersonalInformationBean(checkBasicPersonalInformationBean);
                        }
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast("请求失败");
                }
            }
        }
    }

}
