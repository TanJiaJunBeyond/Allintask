package com.allintask.lingdao.presenter.user;

import com.allintask.lingdao.bean.user.AddEducationalExperienceBean;
import com.allintask.lingdao.bean.user.AddHonorAndQualificationBean;
import com.allintask.lingdao.bean.user.AddWorkExperienceBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IAddPersonalInformationView;

import java.util.List;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/19.
 */

public class AddPersonalInformationPresenter extends BasePresenter<IAddPersonalInformationView> {

    private IUserModel userModel;

    public AddPersonalInformationPresenter() {
        userModel = new UserModel();
    }

    public void saveEducationalExperienceRequest(List<AddEducationalExperienceBean> educationExperienceList) {
        OkHttpRequest saveEducationalExperienceRequest = userModel.saveEducationalExperienceRequest(educationExperienceList);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, saveEducationalExperienceRequest);
    }

    public void saveWorkExperienceRequest(List<AddWorkExperienceBean> workExperienceList) {
        OkHttpRequest saveWorkExperienceRequest = userModel.saveWorkExperienceRequest(workExperienceList);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, saveWorkExperienceRequest);
    }

    public void saveHonorAndQualificationRequest(List<AddHonorAndQualificationBean> honorAndQualificationList) {
        OkHttpRequest saveHonorAndQualificationRequest = userModel.saveHonorAndQualificationRequest(honorAndQualificationList);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, saveHonorAndQualificationRequest);
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
