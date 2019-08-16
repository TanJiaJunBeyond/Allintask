package com.allintask.lingdao.presenter.user;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.SettingBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.utils.EaseUI;
import com.allintask.lingdao.utils.IPAddressUtils;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.user.ISettingView;
import com.hyphenate.chat.EMClient;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/26.
 */

public class SettingPresenter extends BasePresenter<ISettingView> {

    private IUserModel userModel;

    public SettingPresenter() {
        userModel = new UserModel();
    }

    public void fetchSettingRequest() {
        OkHttpRequest fetchSettingRequest = userModel.fetchSettingRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchSettingRequest);
    }

    public UMAuthListener getUMAuthListener() {
        UMAuthListener umAuthListener = new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                switch (share_media) {
                    case WEIXIN:
                        if (null != getView()) {
                            getView().onWechatUMAuthComplete(map);
                        }
                        break;

                    case QQ:
                        if (null != getView()) {
                            getView().onQQUMAuthComplete(map);
                        }
                        break;
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                switch (share_media) {
                    case WEIXIN:
                        if (null != getView()) {
                            getView().onWechatUMAuthError();
                        }
                        break;

                    case QQ:
                        if (null != getView()) {
                            getView().onQQUMAuthError();
                        }
                        break;
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        };
        return umAuthListener;
    }

    public void bindWechatRequest(String openId, String unionId, String nickname) {
        String IPAddress = IPAddressUtils.getIPAddress(getView().getParentContext());

        if (!TextUtils.isEmpty(IPAddress)) {
            OkHttpRequest bindWechatRequest = userModel.bindWechatRequest(openId, unionId, nickname, IPAddress);
            addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, bindWechatRequest);
        }
    }

    public void bindQQRequest(String openId, String unionId, String name, String nickname) {
        String IPAddress = IPAddressUtils.getIPAddress(getView().getParentContext());

        if (!TextUtils.isEmpty(IPAddress)) {
            OkHttpRequest bindQQRequest = userModel.bindQQRequest(openId, unionId, name, nickname, IPAddress);
            addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, bindQQRequest);
        }
    }

    public void unbindWechatRequest() {
        OkHttpRequest unbindWechatRequest = userModel.unbindWechatRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.PUT, unbindWechatRequest);
    }

    public void unbindQQRequest() {
        OkHttpRequest unbindQQRequest = userModel.unbindQQRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.PUT, unbindQQRequest);
    }

    public void logoutRequest() {
        OkHttpRequest logoutRequest = userModel.logoutRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, logoutRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING)) {
            if (success) {
                SettingBean settingBean = JSONObject.parseObject(data, SettingBean.class);

                if (null != settingBean) {
                    int mobileCountryCodeId = TypeUtils.getInteger(settingBean.mobileCountryCodeId, -1);
                    String mobile = TypeUtils.getString(settingBean.mobile, "");
                    boolean isExistGesturePwd = TypeUtils.getBoolean(settingBean.isExistGesturePwd, false);
                    String email = TypeUtils.getString(settingBean.email, "");
                    boolean isExistLoginPwd = TypeUtils.getBoolean(settingBean.isExistLoginPwd, false);
                    boolean isExistPayPwd = TypeUtils.getBoolean(settingBean.isExistPayPwd, false);
                    Integer bardId = TypeUtils.getInteger(settingBean.bardId, -1);
                    String ghtBankName = TypeUtils.getString(settingBean.ghtBankName, "");
                    String wxName = TypeUtils.getString(settingBean.wxName, "");
                    String qqName = TypeUtils.getString(settingBean.qqName, "");

                    if (null != getView()) {
                        getView().onShowMobileCountryCodeId(mobileCountryCodeId);
                        getView().onShowPhoneNumber(mobile);
                        getView().onShowIsExistGesturePwd(isExistGesturePwd);
//                        getView().onShowMailbox(email);
//                        getView().onShowIsExistLoginPassword(isExistLoginPwd);
//                        getView().onShowIsExistPaymentPassword(isExistPayPwd);
                        getView().onShowBankId(bardId);
                        getView().onShowBankCardName(ghtBankName);
                        getView().onShowWechatName(wxName);
                        getView().onShowQQName(qqName);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_BIND_WX)) {
            if (success) {
                if (null != getView()) {
                    getView().onBindWechatSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_BIND_QQ)) {
            if (success) {
                if (null != getView()) {
                    getView().onBindQQSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_UNBIND_WX)) {
            if (success) {
                if (null != getView()) {
                    getView().onUnbindWechatSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_UNBIND_QQ)) {
            if (success) {
                if (null != getView()) {
                    getView().onUnbindQQSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_LOGOUT)) {
            if (success) {
                UserPreferences.getInstance().setUserBean(null);
                EaseUI.getInstance().getNotifier().reset();
                EMClient.getInstance().logout(true);

                if (null != getView()) {
                    getView().onLogoutSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("退出登录失败");
                }
            }
        }
    }

}
