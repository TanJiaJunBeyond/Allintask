package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.MyDataBean;
import com.allintask.lingdao.bean.user.SettingBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.ISecurityManagementView;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/5/25.
 */

public class SecurityManagementPresenter extends BasePresenter<ISecurityManagementView> {

    private IUserModel userModel;

    public SecurityManagementPresenter() {
        userModel = new UserModel();
    }

    public void fetchMyDataRequest() {
        OkHttpRequest fetchMyDataRequest = userModel.fetchMyDataRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchMyDataRequest);
    }

    public void fetchSettingRequest() {
        OkHttpRequest fetchSettingRequest = userModel.fetchSettingRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchSettingRequest);
    }

    public void deleteGesturePasswordRequest() {
        OkHttpRequest deleteGesturePasswordRequest = userModel.deleteGesturePasswordRequest();
        addJsonStringRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.POST, deleteGesturePasswordRequest);
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

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING)) {
            if (success) {
                SettingBean settingBean = JSONObject.parseObject(data, SettingBean.class);

                if (null != settingBean) {
                    int mobileCountryCodeId = TypeUtils.getInteger(settingBean.mobileCountryCodeId, -1);
                    boolean isExistPayPwd = TypeUtils.getBoolean(settingBean.isExistPayPwd, false);
                    boolean isExistGesturePwd = TypeUtils.getBoolean(settingBean.isExistGesturePwd, false);

                    if (null != getView()) {
                        getView().onShowMobileCountryCodeId(mobileCountryCodeId);
                        getView().onShowIsExistPaymentPassword(isExistPayPwd);
                        getView().onShowIsExistGesturePwd(isExistGesturePwd);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_CLEAN_GESTURE_PWD)) {
            if (success) {
                if (null != getView()) {
                    getView().onDeleteGesturePasswordSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().onDeleteGesturePasswordFail(errorMessage);
                }
            }
        }
    }

}
