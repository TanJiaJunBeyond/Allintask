package com.allintask.lingdao.presenter.service;

import com.allintask.lingdao.bean.user.AddEducationalExperienceBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.service.IPublishServiceAddEducationalExperienceView;

import java.util.List;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/2/3.
 */

public class PublishServiceAddEducationalExperiencePresenter extends BasePresenter<IPublishServiceAddEducationalExperienceView> {

    private IUserModel userModel;

    public PublishServiceAddEducationalExperiencePresenter() {
        userModel = new UserModel();
    }

    public void saveEducationalExperienceRequest(List<AddEducationalExperienceBean> educationExperienceList) {
        OkHttpRequest saveEducationalExperienceRequest = userModel.saveEducationalExperienceRequest(educationExperienceList);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, saveEducationalExperienceRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_EDU_INFO_SAVE_LIST)) {
            if (success) {
                if (null != getView()) {
                    getView().onSaveEducationalExperienceSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
