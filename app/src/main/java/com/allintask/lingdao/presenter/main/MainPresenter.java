package com.allintask.lingdao.presenter.main;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.AppUpdateBean;
import com.allintask.lingdao.bean.user.PersonalInformationBean;
import com.allintask.lingdao.bean.user.SettingBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.main.IMainView;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/3/24.
 */

public class MainPresenter extends BasePresenter<IMainView> {

    private IUserModel userModel;

    public MainPresenter() {
        userModel = new UserModel();
    }

    public void appUpdateRequest(Context context) {
        try {
            PackageInfo packageInfo = context.getApplicationContext().getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String versionName = packageInfo.versionName;
            OkHttpRequest appUpdateRequest = userModel.appUpdateRequest(versionName);
            addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, appUpdateRequest);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void fetchPersonalInfoRequest() {
        OkHttpRequest fetchPersonalInfoRequest = userModel.fetchPersonalInfoRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchPersonalInfoRequest);
    }

    public void fetchSettingRequest() {
        OkHttpRequest fetchSettingRequest = userModel.fetchSettingRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchSettingRequest);
    }

    public void updateLoginTimeAndLocationRequest(double longitude, double latitude, String provinceCode, String cityCode, String location) {
        OkHttpRequest updateLoginTimeAndLocationRequest = userModel.updateLoginTimeAndLocationRequest(longitude, latitude, provinceCode, cityCode, location);
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.POST, updateLoginTimeAndLocationRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GENERAL_DATA_VERSION_CONTROL_ANDROID)) {
            if (success) {
                AppUpdateBean appUpdateBean = JSONObject.parseObject(data, AppUpdateBean.class);

                if (null != getView()) {
                    getView().onShowAppUpdateBean(appUpdateBean);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

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

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING)) {
            if (success) {
                SettingBean settingBean = JSONObject.parseObject(data, SettingBean.class);

                if (null != settingBean) {
                    int mobileCountryCodeId = TypeUtils.getInteger(settingBean.mobileCountryCodeId, -1);
                    String mobile = TypeUtils.getString(settingBean.mobile, "");

                    UserPreferences.getInstance().setMobileCountryCodeId(mobileCountryCodeId);
                    UserPreferences.getInstance().setPhoneNumber(mobile);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_UPDATE_LOGIN_TIME_AND_LOCATION)) {
            if (success) {

            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
