package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.EducationalExperienceBean;
import com.allintask.lingdao.bean.user.SearchInformationBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.user.IEducationalExperienceView;

import java.util.Date;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/2/7.
 */

public class EducationalExperiencePresenter extends BasePresenter<IEducationalExperienceView> {

    private IUserModel userModel;

    private int educationalExperienceId;
    private int educationalBackgroundId;

    public EducationalExperiencePresenter() {
        userModel = new UserModel();
    }

    public void fetchEducationalExperienceRequest(int educationalExperienceId) {
        this.educationalExperienceId = educationalExperienceId;
        OkHttpRequest fetchEducationalExperienceRequest = userModel.fetchEducationalExperienceRequest(this.educationalExperienceId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchEducationalExperienceRequest);
    }

    public void fetchEducationalBackgroundListRequest(int educationalBackgroundId) {
        this.educationalBackgroundId = educationalBackgroundId;
        OkHttpRequest fetchEducationBackgroundListRequest = userModel.fetchEducationalBackgroundListRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchEducationBackgroundListRequest);
    }

    public void compileEducationExperienceRequest(int id, String school, String major, int educationLevelId, String startAt, String endAt) {
        int userId = UserPreferences.getInstance().getUserId();
        OkHttpRequest compileEducationExperienceRequest = userModel.compileEducationExperienceRequest(userId, id, school, major, educationLevelId, startAt, endAt);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.PUT, compileEducationExperienceRequest);
    }

    public void removeEducationExperienceRequest(int educationalExperienceId) {
        this.educationalExperienceId = educationalExperienceId;
        OkHttpRequest removeEducationExperienceRequest = userModel.removeEducationExperienceRequest(this.educationalExperienceId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.DELETE, removeEducationExperienceRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_EDU_INFO + "/" + String.valueOf(educationalExperienceId))) {
            if (success) {
                EducationalExperienceBean educationalExperienceBean = JSONObject.parseObject(data, EducationalExperienceBean.class);

                if (null != educationalExperienceBean) {
                    String educationalInstitution = TypeUtils.getString(educationalExperienceBean.school, "");
                    String major = TypeUtils.getString(educationalExperienceBean.major, "");
                    int educationalBackgroundId = TypeUtils.getInteger(educationalExperienceBean.educationLevelId, -1);
                    Date startTimeDate = educationalExperienceBean.startAt;
                    Date endTimeDate = educationalExperienceBean.endAt;

                    String startTime = null;
                    String endTime = null;

                    if (null != startTimeDate) {
                        startTime = CommonConstant.commonDateFormat.format(startTimeDate);
                    }

                    if (null != endTimeDate) {
                        endTime = CommonConstant.commonDateFormat.format(endTimeDate);
                    }

                    if (null != getView()) {
                        getView().onShowEducationalInstitution(educationalInstitution);
                        getView().onShowMajor(major);
                        getView().onShowEducationalBackgroundId(educationalBackgroundId);
                        getView().onShowStartTime(startTime);
                        getView().onShowEndTime(endTime);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GENERAL_DATA_EDU_LEVEL_LIST)) {
            if (success) {
                List<SearchInformationBean> educationBackgroundList = JSONArray.parseArray(data, SearchInformationBean.class);

                if (null != educationBackgroundList && educationBackgroundList.size() > 0) {
                    for (int i = 0; i < educationBackgroundList.size(); i++) {
                        SearchInformationBean searchInformationBean = educationBackgroundList.get(i);

                        if (null != searchInformationBean) {
                            int code = Integer.valueOf(searchInformationBean.code);

                            if (code == educationalBackgroundId) {
                                String name = TypeUtils.getString(searchInformationBean.name, "");

                                if (null != getView()) {
                                    getView().onShowEducationalBackground(name);
                                }
                            }
                        }
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_EDU_INFO_UPDATE)) {
            if (success) {
                if (null != getView()) {
                    getView().onCompileEducationExperienceSuccess();
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
    }

}
