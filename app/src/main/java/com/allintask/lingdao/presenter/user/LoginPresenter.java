package com.allintask.lingdao.presenter.user;

import android.os.CountDownTimer;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.AllintaskApplication;
import com.allintask.lingdao.bean.user.PhoneNumberHomeLocationBean;
import com.allintask.lingdao.bean.user.UserBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.utils.IPAddressUtils;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.user.ILoginView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;

import java.util.Map;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2017/12/28.
 */

public class LoginPresenter extends BasePresenter<ILoginView> {

    private IUserModel userModel;

    public LoginPresenter() {
        userModel = new UserModel();
    }

    public void fetchDefaultPhoneNumberHomeLocationRequest() {
        OkHttpRequest fetchDefaultPhoneNumberHomeLocationRequest = userModel.fetchDefaultPhoneNumberHomeLocationRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchDefaultPhoneNumberHomeLocationRequest);
    }

    public CountDownTimer startCountDownTimer() {
        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (null != getView()) {
                    getView().onCountDownTimerTick(millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                if (null != getView()) {
                    getView().onCountDownTimerFinish();
                }
            }
        };

        countDownTimer.start();
        return countDownTimer;
    }

    public void sendLoginSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile) {
        OkHttpRequest sendLoginSmsIdentifyCodeRequest = userModel.sendLoginSmsIdentifyCodeRequest(mobileCountryCodeId, mobile);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, sendLoginSmsIdentifyCodeRequest);
    }

    public void loginByPhoneNumberAndSmsIdentifyCodeRequest(int mobileCountryCodeId, String phoneNumber, String smsIdentifyCode) {
        String IPAddress = IPAddressUtils.getIPAddress(getView().getParentContext());
        OkHttpRequest loginByPhoneNumberAndSmsIdentifyCodeRequest = userModel.loginByPhoneNumberAndSmsIdentifyCodeRequest(mobileCountryCodeId, phoneNumber, smsIdentifyCode, IPAddress);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, loginByPhoneNumberAndSmsIdentifyCodeRequest);
    }

    public void loginByPhoneNumberAndPasswordRequest(int mobileCountryCodeId, String phoneNumber, String password) {
        String IPAddress = IPAddressUtils.getIPAddress(getView().getParentContext());
        OkHttpRequest loginByPhoneNumberAndPasswordRequest = userModel.loginByPhoneNumberAndPasswordRequest(mobileCountryCodeId, phoneNumber, password, IPAddress);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, loginByPhoneNumberAndPasswordRequest);
    }

    public void sendWechatSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile) {
        OkHttpRequest sendWechatSmsIdentifyCodeRequest = userModel.sendWechatSmsIdentifyCodeRequest(mobileCountryCodeId, mobile);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, sendWechatSmsIdentifyCodeRequest);
    }

    public void sendQQSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile) {
        OkHttpRequest sendQQSmsIdentifyCodeRequest = userModel.sendQQSmsIdentifyCodeRequest(mobileCountryCodeId, mobile);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, sendQQSmsIdentifyCodeRequest);
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
                            getView().onWechatUMAuthError(throwable);
                        }
                        break;

                    case QQ:
                        if (null != getView()) {
                            getView().onQQUMAuthError(throwable);
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

    public void loginByWechatRequest(String openId, String unionId, String nickname) {
        String IPAddress = IPAddressUtils.getIPAddress(getView().getParentContext());
        OkHttpRequest loginByWechatRequest = userModel.loginByWechatRequest(openId, unionId, nickname, IPAddress);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, loginByWechatRequest);
    }

    public void bindByWechatRequest(int mobileCountryCodeId, String mobile, String captcha, String wxUnionId) {
        String IPAddress = IPAddressUtils.getIPAddress(getView().getParentContext());
        OkHttpRequest bindByWechatRequest = userModel.bindByWechatRequest(mobileCountryCodeId, mobile, captcha, wxUnionId, IPAddress);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, bindByWechatRequest);
    }

    public void loginByQQRequest(String openId, String unionId, String name, String nickname) {
        String IPAddress = IPAddressUtils.getIPAddress(getView().getParentContext());
        OkHttpRequest loginByQQRequest = userModel.loginByQQRequest(openId, unionId, name, nickname, IPAddress);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, loginByQQRequest);
    }

    public void bindByQQRequest(int mobileCountryCodeId, String mobile, String captcha, String qqUnionId) {
        String IPAddress = IPAddressUtils.getIPAddress(getView().getParentContext());
        OkHttpRequest bindByQQRequest = userModel.bindByQQRequest(mobileCountryCodeId, mobile, captcha, qqUnionId, IPAddress);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, bindByQQRequest);
    }

    public void checkNeedGuideRequest() {
        OkHttpRequest checkNeedGuideRequest = userModel.checkNeedGuideRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, checkNeedGuideRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_MOBILE_COUNTRY_CODE_GET_DEFAULT)) {
            if (success) {
                if (null != getView()) {
                    PhoneNumberHomeLocationBean phoneNumberHomeLocationBean = JSONObject.parseObject(data, PhoneNumberHomeLocationBean.class);

                    if (null != phoneNumberHomeLocationBean) {
                        int id = TypeUtils.getInteger(phoneNumberHomeLocationBean.mobileCountryCodeId, 0);
                        String code = TypeUtils.getString(phoneNumberHomeLocationBean.value, "");
                        String regularEx = TypeUtils.getString(phoneNumberHomeLocationBean.regularEx, "");

                        if (null != getView()) {
                            getView().onShowPhoneNumberHomeLocationMobileCountryCodeId(id);
                            getView().onShowPhoneNumberHomeLocationValue(code);
                            getView().onShowPhoneNumberHomeLocationRegularEx(regularEx);
                        }
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_LOGIN)) {
            if (success) {
                if (null != getView()) {
                    getView().onSendSmsIdentifyCodeSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("发送验证码失败");
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_BIND_WX)) {
            if (success) {
                if (null != getView()) {
                    getView().onSendSmsIdentifyCodeSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("发送验证码失败");
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_BIND_QQ)) {
            if (success) {
                if (null != getView()) {
                    getView().onSendSmsIdentifyCodeSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("发送验证码失败");
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_GET_BY_MOBILE_AND_CAPTCHA)) {
            if (success) {
                UserBean userBean = JSONObject.parseObject(data, UserBean.class);

                if (null != userBean) {
                    UserPreferences.getInstance().setUserBean(userBean);
                }

                if (null != getView()) {
                    getView().onLoginSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_GET_BY_MOBILE_AND_PWD)) {
            if (success) {
                UserBean userBean = JSONObject.parseObject(data, UserBean.class);

                if (null != userBean) {
                    UserPreferences.getInstance().setUserBean(userBean);
                }

                if (null != getView()) {
                    getView().onLoginSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_GET_BY_WX)) {
            if (success) {
                if (!TextUtils.isEmpty(data)) {
                    UserBean userBean = JSONObject.parseObject(data, UserBean.class);

                    if (null != userBean) {
                        UserPreferences.getInstance().setUserBean(userBean);
                    }

                    if (null != getView()) {
                        getView().onWechatLoginIsBinding();
                    }
                } else {
                    if (null != getView()) {
                        getView().onWechatLoginUnbind();
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_BIND_WX_UNION_ID)) {
            if (success) {
                UserBean userBean = JSONObject.parseObject(data, UserBean.class);

                if (null != userBean) {
                    UserPreferences.getInstance().setUserBean(userBean);
                }

                if (null != getView()) {
                    getView().onLoginSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_GET_BY_QQ)) {
            if (success) {
                if (!TextUtils.isEmpty(data)) {
                    UserBean userBean = JSONObject.parseObject(data, UserBean.class);

                    if (null != userBean) {
                        UserPreferences.getInstance().setUserBean(userBean);
                    }

                    if (null != getView()) {
                        getView().onQQLoginIsBinding();
                    }
                } else {
                    if (null != getView()) {
                        getView().onQQLoginUnbind();
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_BIND_QQ)) {
            if (success) {
                UserBean userBean = JSONObject.parseObject(data, UserBean.class);

                if (null != userBean) {
                    UserPreferences.getInstance().setUserBean(userBean);

                    if (null != getView()) {
                        getView().onLoginSuccess();
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CHECK_NEED_GUIDE)) {
            if (success) {
                if (!TextUtils.isEmpty(data)) {
                    boolean isNeedGuide = Boolean.valueOf(data);

                    if (null != getView()) {
                        getView().onCheckNeedGuideSuccess(isNeedGuide);
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
