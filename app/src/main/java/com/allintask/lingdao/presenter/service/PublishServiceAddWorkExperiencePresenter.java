package com.allintask.lingdao.presenter.service;

import com.allintask.lingdao.bean.user.AddWorkExperienceBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.service.IPublishServiceAddWorkExperienceView;

import java.util.List;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/2/3.
 */

public class PublishServiceAddWorkExperiencePresenter extends BasePresenter<IPublishServiceAddWorkExperienceView> {

    private IUserModel userModel;

    public PublishServiceAddWorkExperiencePresenter() {
        userModel = new UserModel();
    }

    public void saveWorkExperienceRequest(List<AddWorkExperienceBean> workExperienceList) {
        OkHttpRequest saveWorkExperienceRequest = userModel.saveWorkExperienceRequest(workExperienceList);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, saveWorkExperienceRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_WORK_INFO_SAVE_LIST)) {
            if (success) {
                if (null != getView()) {
                    getView().onSaveWorkExperienceSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
