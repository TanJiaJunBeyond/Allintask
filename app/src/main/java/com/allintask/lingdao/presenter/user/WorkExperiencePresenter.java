package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.WorkExperienceBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.user.IWorkExperienceView;

import java.util.Date;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/2/7.
 */

public class WorkExperiencePresenter extends BasePresenter<IWorkExperienceView> {

    private IUserModel userModel;

    private int workExperienceId;

    public WorkExperiencePresenter() {
        userModel = new UserModel();
    }

    public void fetchWorkExperienceRequest(int workExperienceId) {
        this.workExperienceId = workExperienceId;
        OkHttpRequest fetchWorkExperienceRequest = userModel.fetchWorkExperienceRequest(this.workExperienceId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchWorkExperienceRequest);
    }

    public void compileWorkExperienceRequest(int id, String company, String post, String startAt, String endAt) {
        int userId = UserPreferences.getInstance().getUserId();
        OkHttpRequest compileWorkExperienceRequest = userModel.compileWorkExperienceRequest(userId, id, company, post, startAt, endAt);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.PUT, compileWorkExperienceRequest);
    }

    public void removeWorkExperienceRequest(int workExperienceId) {
        this.workExperienceId = workExperienceId;
        OkHttpRequest removeWorkExperienceRequest = userModel.removeWorkExperienceRequest(this.workExperienceId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.DELETE, removeWorkExperienceRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_WORK_INFO + "/" + String.valueOf(workExperienceId))) {
            if (success) {
                WorkExperienceBean workExperienceBean = JSONObject.parseObject(data, WorkExperienceBean.class);

                if (null != workExperienceBean) {
                    String workUnit = TypeUtils.getString(workExperienceBean.company, "");
                    String position = TypeUtils.getString(workExperienceBean.post, "");
                    Date startTimeDate = workExperienceBean.startAt;
                    Date endTimeDate = workExperienceBean.endAt;

                    String startTime = null;
                    String endTime = null;

                    if (null != startTimeDate) {
                        startTime = CommonConstant.commonDateFormat.format(startTimeDate);
                    }

                    if (null != endTimeDate) {
                        endTime = CommonConstant.commonDateFormat.format(endTimeDate);
                    }

                    if (null != getView()) {
                        getView().onShowWorkUnit(workUnit);
                        getView().onShowPosition(position);
                        getView().onShowStartTime(startTime);
                        getView().onShowEndTime(endTime);
                    }
                }
            }
        } else {
            if (null != getView()) {
                getView().showToast(errorMessage);
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_WORK_INFO_UPDATE)) {
            if (success) {
                if (null != getView()) {
                    getView().onCompileWorkExperienceSuccess();
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
    }

}
