package com.allintask.lingdao.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.allintask.lingdao.AllintaskApplication;
import com.allintask.lingdao.bean.user.UserBean;
import com.allintask.lingdao.constant.ApiKey;
import com.allintask.lingdao.constant.CommonConstant;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * 用户本地缓存信息
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class UserPreferences {

    private static UserPreferences instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private UserPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static UserPreferences getInstance() {
        if (null == instance) {
            instance = new UserPreferences(AllintaskApplication.getInstance().getApplicationContext());
        }
        return instance;
    }

    public void setUserBean(UserBean userBean) {
        String sharedPreferencesKey = AllintaskApplication.getInstance().getSharedPreferencesKey();

        if (null != userBean) {
            int userId = TypeUtils.getInteger(userBean.userId, -1);

            if (userId != -1) {
                editor.putString(ApiKey.COMMON_KEY, sharedPreferencesKey);
                editor.putString(ApiKey.COMMON_USER_ID, DESUtils.encode(sharedPreferencesKey, String.valueOf(userBean.userId)));
                editor.putString(ApiKey.USER_ACCESS_TOKEN, DESUtils.encode(sharedPreferencesKey, userBean.accessToken));
                editor.putString(ApiKey.USER_REFRESH_TOKEN, DESUtils.encode(sharedPreferencesKey, userBean.refreshToken));
                editor.putString(ApiKey.USER_PASSWORD, DESUtils.encode(sharedPreferencesKey, userBean.password));
                editor.commit();
            }
        } else {
            editor.putString(ApiKey.COMMON_KEY, "");
            editor.putString(ApiKey.COMMON_USER_ID, "");
            editor.putString(ApiKey.USER_ACCESS_TOKEN, "");
            editor.putString(ApiKey.USER_REFRESH_TOKEN, "");
            editor.putString(ApiKey.USER_PASSWORD, "");
            editor.commit();
        }
    }

    public boolean getIsFirst() {
        return sharedPreferences.getBoolean(CommonConstant.IS_FIRST, true);
    }

    public void setIsFirst(boolean isFirst) {
        editor.putBoolean(CommonConstant.IS_FIRST, isFirst);
        editor.commit();
    }

    public boolean isLogin() {
        String sharedPreferencesKey = sharedPreferences.getString(ApiKey.COMMON_KEY, "");
        String tempUserId = sharedPreferences.getString(ApiKey.COMMON_USER_ID, "");
        int userId = -1;

        if (!TextUtils.isEmpty(tempUserId)) {
            String userIdStr = DESUtils.decode(sharedPreferencesKey, sharedPreferences.getString(ApiKey.COMMON_USER_ID, ""));
            userId = Integer.valueOf(userIdStr);
        }

        String tempAccessToken = sharedPreferences.getString(ApiKey.USER_ACCESS_TOKEN, "");
        String accessToken = null;

        if (!TextUtils.isEmpty(tempAccessToken)) {
            accessToken = DESUtils.decode(sharedPreferencesKey, tempAccessToken);
        }

        if (userId != -1 && !TextUtils.isEmpty(accessToken)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getIsFirstGuide() {
        return sharedPreferences.getBoolean(CommonConstant.IS_FIRST_GUIDE, true);
    }

    public void setIsFirstGuide(boolean isFirstGuide) {
        editor.putBoolean(CommonConstant.IS_FIRST_GUIDE, isFirstGuide);
        editor.commit();
    }

    public boolean getIsFirstEnterDemandManagement() {
        return sharedPreferences.getBoolean(CommonConstant.IS_FIRST_ENTER_DEMAND_MANAGEMENT, true);
    }

    public void setIsFirstEnterDemandManagement(boolean isFirstEnterDemandManagement) {
        editor.putBoolean(CommonConstant.IS_FIRST_ENTER_DEMAND_MANAGEMENT, isFirstEnterDemandManagement);
        editor.commit();
    }

    public boolean getIsFirstEnterServiceManagement() {
        return sharedPreferences.getBoolean(CommonConstant.IS_FIRST_ENTER_SERVICE_MANAGEMENT, true);
    }

    public void setIsFirstEnterServiceManagement(boolean isFirstEnterServiceManagement) {
        editor.putBoolean(CommonConstant.IS_FIRST_ENTER_SERVICE_MANAGEMENT, isFirstEnterServiceManagement);
        editor.commit();
    }

    public boolean getIsFirstShowAddPhotosLayout() {
        return sharedPreferences.getBoolean(CommonConstant.IS_FIRST_SHOW_ADD_PHOTOS_LAYOUT, true);
    }

    public void setIsFirstShowAddPhotosLayout(boolean isFirstShowAddPhotosLayout) {
        editor.putBoolean(CommonConstant.IS_FIRST_SHOW_ADD_PHOTOS_LAYOUT, isFirstShowAddPhotosLayout);
        editor.commit();
    }

    public boolean getIsFirstShowCompileServiceLayout() {
        return sharedPreferences.getBoolean(CommonConstant.IS_FIRST_SHOW_COMPILE_SERVICE_LAYOUT, true);
    }

    public void setIsFirstShowCompileServiceLayout(boolean isFirstShowCompileServiceLayout) {
        editor.putBoolean(CommonConstant.IS_FIRST_SHOW_COMPILE_SERVICE_LAYOUT, isFirstShowCompileServiceLayout);
        editor.commit();
    }

    public boolean getIsUseFingerprintVerify() {
        return sharedPreferences.getBoolean(CommonConstant.IS_USE_FINGERPRINT_VERIFY, false);
    }

    public void setIsUseFingerprintVerify(boolean isUseFingerprintVerify) {
        editor.putBoolean(CommonConstant.IS_USE_FINGERPRINT_VERIFY, isUseFingerprintVerify);
        editor.commit();
    }

    public int getUserId() {
        String sharedPreferencesKey = sharedPreferences.getString(ApiKey.COMMON_KEY, "");
        String tempUserId = sharedPreferences.getString(ApiKey.COMMON_USER_ID, "");
        int userId = -1;

        if (!TextUtils.isEmpty(tempUserId)) {
            String userIdStr = DESUtils.decode(sharedPreferencesKey, tempUserId);
            userId = Integer.valueOf(userIdStr);
        }
        return userId;
    }

    public String getAccessToken() {
        String sharedPreferencesKey = sharedPreferences.getString(ApiKey.COMMON_KEY, "");
        String tempAccessToken = sharedPreferences.getString(ApiKey.USER_ACCESS_TOKEN, "");
        String accessToken = null;

        if (!TextUtils.isEmpty(tempAccessToken)) {
            accessToken = DESUtils.decode(sharedPreferencesKey, tempAccessToken);
        }
        return accessToken;
    }

    public String getEMChatPassword() {
        String sharedPreferencesKey = sharedPreferences.getString(ApiKey.COMMON_KEY, "");
        String tempEMChatPassword = sharedPreferences.getString(ApiKey.USER_PASSWORD, "");
        String EMChatPassword = null;

        if (!TextUtils.isEmpty(tempEMChatPassword)) {
            EMChatPassword = DESUtils.decode(sharedPreferencesKey, tempEMChatPassword);
        }
        return EMChatPassword;
    }

    public void setEMChatPassword(String EMChatPassword) {
        String sharedPreferencesKey = sharedPreferences.getString(ApiKey.COMMON_KEY, "");
        editor.putString(ApiKey.USER_PASSWORD, DESUtils.encode(sharedPreferencesKey, EMChatPassword));
        editor.commit();
    }

    public String getPhoneNumber() {
        String sharedPreferencesKey = sharedPreferences.getString(ApiKey.COMMON_KEY, "");
        String tempPhoneNumber = sharedPreferences.getString(ApiKey.USER_PHONE_NUMBER, "");
        String phoneNumber = null;

        if (!TextUtils.isEmpty(tempPhoneNumber)) {
            phoneNumber = DESUtils.decode(sharedPreferencesKey, tempPhoneNumber);
        }
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        String sharedPreferencesKey = sharedPreferences.getString(ApiKey.COMMON_KEY, "");
        editor.putString(ApiKey.USER_PHONE_NUMBER, DESUtils.encode(sharedPreferencesKey, phoneNumber));
        editor.commit();
    }

    public String getSearchSetJsonString() {
        return sharedPreferences.getString(ApiKey.USER_SEARCH_SET_JSON_STRING, "");
    }

    public void setSearchSetJsonString(String searchListJsonString) {
        editor.putString(ApiKey.USER_SEARCH_SET_JSON_STRING, searchListJsonString);
        editor.commit();
    }

    public String getNickname() {
        String sharedPreferencesKey = sharedPreferences.getString(ApiKey.COMMON_KEY, "");
        String tempNickname = sharedPreferences.getString(ApiKey.USER_NICKNAME, "");
        String nickname = null;

        if (!TextUtils.isEmpty(tempNickname)) {
            nickname = DESUtils.decode(sharedPreferencesKey, tempNickname);
        }
        return nickname;
    }

    public void setNickname(String nickname) {
        String sharedPreferencesKey = sharedPreferences.getString(ApiKey.COMMON_KEY, "");
        editor.putString(ApiKey.USER_NICKNAME, DESUtils.encode(sharedPreferencesKey, nickname));
        editor.commit();
    }

    public String getHeadPortraitUrl() {
        String sharedPreferencesKey = sharedPreferences.getString(ApiKey.COMMON_KEY, "");
        String tempHeadPortrait = sharedPreferences.getString(ApiKey.USER_HEAD_PORTRAIT_URL, "");
        String headPortrait = null;

        if (!TextUtils.isEmpty(tempHeadPortrait)) {
            headPortrait = DESUtils.decode(sharedPreferencesKey, tempHeadPortrait);
        }
        return headPortrait;
    }

    public void setHeadPortraitUrl(String headPortraitUrl) {
        String sharedPreferencesKey = sharedPreferences.getString(ApiKey.COMMON_KEY, "");
        editor.putString(ApiKey.USER_HEAD_PORTRAIT_URL, DESUtils.encode(sharedPreferencesKey, headPortraitUrl));
        editor.commit();
    }

    public int getMobileCountryCodeId() {
        String sharedPreferencesKey = sharedPreferences.getString(ApiKey.COMMON_KEY, "");
        String tempMobileCountryCodeId = sharedPreferences.getString(ApiKey.USER_MOBILE_COUNTRY_CODE_ID, "");

        int mobileCountryCodeId = -1;

        if (!TextUtils.isEmpty(tempMobileCountryCodeId)) {
            String userIdStr = DESUtils.decode(sharedPreferencesKey, tempMobileCountryCodeId);
            mobileCountryCodeId = Integer.valueOf(userIdStr);
        }
        return mobileCountryCodeId;
    }

    public void setMobileCountryCodeId(int mobileCountryCodeId) {
        String sharedPreferencesKey = sharedPreferences.getString(ApiKey.COMMON_KEY, "");
        editor.putString(ApiKey.USER_MOBILE_COUNTRY_CODE_ID, DESUtils.encode(sharedPreferencesKey, String.valueOf(mobileCountryCodeId)));
        editor.commit();
    }

    public boolean getIsDownloadingAPK() {
        return sharedPreferences.getBoolean(ApiKey.COMMON_IS_DOWNLOADING_APK, false);
    }

    public void setIsDownloadingAPK(boolean isDownloadingAPK) {
        editor.putBoolean(ApiKey.COMMON_IS_DOWNLOADING_APK, isDownloadingAPK);
        editor.commit();
    }

    public String getZhiMaCreditAuthenticationType() {
        String sharedPreferencesKey = sharedPreferences.getString(ApiKey.COMMON_KEY, "");
        String tempZhiMaCreditAuthenticationType = sharedPreferences.getString(ApiKey.COMMON_ZHI_MA_CREDIT_AUTHENTICATION_TYPE, "");
        String zhiMaCreditAuthenticationType = null;

        if (!TextUtils.isEmpty(tempZhiMaCreditAuthenticationType)) {
            zhiMaCreditAuthenticationType = DESUtils.decode(sharedPreferencesKey, tempZhiMaCreditAuthenticationType);
        }
        return zhiMaCreditAuthenticationType;
    }

    public void setZhiMaCreditAuthenticationType(String zhiMaCreditAuthenticationType) {
        String sharedPreferencesKey = sharedPreferences.getString(ApiKey.COMMON_KEY, "");
        editor.putString(ApiKey.COMMON_ZHI_MA_CREDIT_AUTHENTICATION_TYPE, DESUtils.encode(sharedPreferencesKey, zhiMaCreditAuthenticationType));
        editor.commit();
    }

    public String getZhiMaCreditAuthenticationParams() {
        String sharedPreferencesKey = sharedPreferences.getString(ApiKey.COMMON_KEY, "");
        String tempZhiMaCreditAuthenticationParams = sharedPreferences.getString(ApiKey.COMMON_ZHI_MA_CREDIT_AUTHENTICATION_PARAMS, "");
        String zhiMaCreditAuthenticationParams = null;

        if (!TextUtils.isEmpty(tempZhiMaCreditAuthenticationParams)) {
            zhiMaCreditAuthenticationParams = DESUtils.decode(sharedPreferencesKey, tempZhiMaCreditAuthenticationParams);
        }
        return zhiMaCreditAuthenticationParams;
    }

    public void setZhiMaCreditAuthenticationParams(String zhiMaCreditAuthenticationParams) {
        String sharedPreferencesKey = sharedPreferences.getString(ApiKey.COMMON_KEY, "");
        editor.putString(ApiKey.COMMON_ZHI_MA_CREDIT_AUTHENTICATION_PARAMS, DESUtils.encode(sharedPreferencesKey, zhiMaCreditAuthenticationParams));
        editor.commit();
    }

    public String getZhiMaCreditAuthenticationSign() {
        String sharedPreferencesKey = sharedPreferences.getString(ApiKey.COMMON_KEY, "");
        String tempZhiMaCreditAuthenticationSign = sharedPreferences.getString(ApiKey.COMMON_ZHI_MA_CREDIT_AUTHENTICATION_SIGN, "");
        String zhiMaCreditAuthenticationSign = null;

        if (!TextUtils.isEmpty(tempZhiMaCreditAuthenticationSign)) {
            zhiMaCreditAuthenticationSign = DESUtils.decode(sharedPreferencesKey, tempZhiMaCreditAuthenticationSign);
        }
        return zhiMaCreditAuthenticationSign;
    }

    public void setZhiMaCreditAuthenticationSign(String zhiMaCreditAuthenticationSign) {
        String sharedPreferencesKey = sharedPreferences.getString(ApiKey.COMMON_KEY, "");
        editor.putString(ApiKey.COMMON_ZHI_MA_CREDIT_AUTHENTICATION_SIGN, DESUtils.encode(sharedPreferencesKey, zhiMaCreditAuthenticationSign));
        editor.commit();
    }

}
