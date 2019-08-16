package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.SettingBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IAccountManagemantView;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/5/24.
 */

public class AccountManagementPresenter extends BasePresenter<IAccountManagemantView> {

    private IUserModel userModel;

    public AccountManagementPresenter() {
        userModel = new UserModel();
    }

    public void fetchSettingRequest() {
        OkHttpRequest fetchSettingRequest = userModel.fetchSettingRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchSettingRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING)) {
            if (success) {
                SettingBean settingBean = JSONObject.parseObject(data, SettingBean.class);

                if (null != settingBean) {
                    int mobileCountryCodeId = TypeUtils.getInteger(settingBean.mobileCountryCodeId, -1);
                    String mobile = TypeUtils.getString(settingBean.mobile, "");
                    String email = TypeUtils.getString(settingBean.email, "");
                    boolean isExistLoginPwd = TypeUtils.getBoolean(settingBean.isExistLoginPwd, false);

                    if (null != getView()) {
                        getView().onShowMobileCountryCodeId(mobileCountryCodeId);
                        getView().onShowPhoneNumber(mobile);
                        getView().onShowMailbox(email);
                        getView().onShowIsExistLoginPassword(isExistLoginPwd);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
