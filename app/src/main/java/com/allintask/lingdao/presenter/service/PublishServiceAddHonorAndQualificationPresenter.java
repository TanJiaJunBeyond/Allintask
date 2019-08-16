package com.allintask.lingdao.presenter.service;

import com.allintask.lingdao.bean.user.AddHonorAndQualificationBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.service.IPublishServiceAddHonorAndQualificationView;

import java.util.List;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/2/3.
 */

public class PublishServiceAddHonorAndQualificationPresenter extends BasePresenter<IPublishServiceAddHonorAndQualificationView> {

    private IUserModel userModel;

    public PublishServiceAddHonorAndQualificationPresenter() {
        userModel = new UserModel();
    }

    public void saveHonorAndQualificationRequest(List<AddHonorAndQualificationBean> honorAndQualificationList) {
        OkHttpRequest saveHonorAndQualificationRequest = userModel.saveHonorAndQualificationRequest(honorAndQualificationList);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, saveHonorAndQualificationRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_HONOR_INFO_SAVE_LIST)) {
            if (success) {
                if (null != getView()) {
                    getView().onSaveHonorAndQualificationSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
