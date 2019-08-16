package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.CheckUploadIsSuccessBean;
import com.allintask.lingdao.bean.user.PersonalInformationBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IPersonalInformationView;

import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/18.
 */

public class PersonalInformationPresenter extends BasePresenter<IPersonalInformationView> {

    private IUserModel userModel;

    private String name;
    private int age;
    private int gender;
    private String startWorkTime;
    private int educationalExperienceId;
    private int workExperienceId;
    private int honorAndQualificationId;

    public PersonalInformationPresenter() {
        userModel = new UserModel();
    }

    public void fetchPersonalInfoRequest() {
        OkHttpRequest fetchPersonalInfoRequest = userModel.fetchPersonalInfoRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchPersonalInfoRequest);
    }

    public void checkUploadIsSuccessRequest(String flagId) {
        OkHttpRequest checkUploadIsSuccessRequest = userModel.checkUploadIsSuccessRequest(flagId);
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.POST, checkUploadIsSuccessRequest);
    }

    public void setUserHeadPortraitRequest(int imageId, String tmpUrl, int width, int height, String format, int cutStartX, int cutStartY, int cutWidth, int cutHeight) {
        OkHttpRequest setUserHeadPortraitRequest = userModel.setUserHeadPortraitRequest(imageId, tmpUrl, width, height, format, cutStartX, cutStartY, cutWidth, cutHeight);
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.POST, setUserHeadPortraitRequest);
    }

    public void setBirthdayRequest(String birthday, int age) {
        this.age = age;
        OkHttpRequest setBirthdayRequest = userModel.setBirthdayRequest(birthday);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, setBirthdayRequest);
    }

    public void setGenderRequest(int gender) {
        this.gender = gender;
        OkHttpRequest setGenderRequest = userModel.setGenderRequest(this.gender);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, setGenderRequest);
    }

    public void setStartWorkAtRequest(String startWorkAt) {
        startWorkTime = startWorkAt;
        OkHttpRequest setStartWorkAtRequest = userModel.setStartWorkAtRequest(startWorkTime);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, setStartWorkAtRequest);
    }

    public void removeEducationExperienceRequest(int educationalExperienceId) {
        this.educationalExperienceId = educationalExperienceId;
        OkHttpRequest removeEducationExperienceRequest = userModel.removeEducationExperienceRequest(this.educationalExperienceId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.DELETE, removeEducationExperienceRequest);
    }

    public void removeWorkExperienceRequest(int workExperienceId) {
        this.workExperienceId = workExperienceId;
        OkHttpRequest removeWorkExperienceRequest = userModel.removeWorkExperienceRequest(this.workExperienceId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.DELETE, removeWorkExperienceRequest);
    }

    public void removeHonorAndQualificationRequest(int honorAndQualificationId) {
        this.honorAndQualificationId = honorAndQualificationId;
        OkHttpRequest removeHonorAndQualificationRequest = userModel.removeHonorAndQualificationRequest(this.honorAndQualificationId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.DELETE, removeHonorAndQualificationRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_PERSONAL_INFO)) {
            if (success) {
                PersonalInformationBean personalInformationBean = JSONObject.parseObject(data, PersonalInformationBean.class);

                if (null != getView()) {
                    getView().onShowPersonalInformationBean(personalInformationBean);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_FILE_OSS_A_LI_YUN_IMAGE_URL_RETURN)) {
            if (success) {
                List<CheckUploadIsSuccessBean> checkUploadIsSuccessList = JSONArray.parseArray(data, CheckUploadIsSuccessBean.class);

                int status = -1;
                String codeId = null;
                String imgUrl = null;

                if (null != checkUploadIsSuccessList && checkUploadIsSuccessList.size() > 0) {
                    CheckUploadIsSuccessBean checkUploadIsSuccessBean = checkUploadIsSuccessList.get(0);

                    if (null != checkUploadIsSuccessBean) {
                        status = TypeUtils.getInteger(checkUploadIsSuccessBean.status, -1);
                        codeId = TypeUtils.getString(checkUploadIsSuccessBean.codeId, "");
                        imgUrl = TypeUtils.getString(checkUploadIsSuccessBean.imgUrl, "");
                    }
                }

                if (null != getView()) {
                    getView().uploadSuccess(status, codeId, imgUrl);
                }
            } else {
                if (null != getView()) {
                    getView().uploadFail();
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SET_AVATAR)) {
            if (success) {
                if (null != getView()) {
                    getView().onSetUserHeadPortraitSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }

            if (null != getView()) {
                getView().dismissProgressDialog();
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SET_BIRTHDAY)) {
            if (success) {
                if (null != getView()) {
                    getView().onSetBirthdaySuccess(age);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SET_GENDER)) {
            if (success) {
                if (null != getView()) {
                    getView().onSetGenderSuccess(gender);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SET_START_WORK_AT)) {
            if (success) {
                if (null != getView()) {
                    getView().onSetStartWorkAtSuccess(startWorkTime);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_EDU_INFO_DELETE + "/" + String.valueOf(educationalExperienceId))) {
            if (success) {
                if (null != getView()) {
                    getView().onRemoveEducationExperienceSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_WORK_INFO_DELETE + "/" + String.valueOf(workExperienceId))) {
            if (success) {
                if (null != getView()) {
                    getView().onRemoveWorkExperienceSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_HONOR_INFO_DELETE + "/" + String.valueOf(honorAndQualificationId))) {
            if (success) {
                if (null != getView()) {
                    getView().onRemoveHonorAndQualificationSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
