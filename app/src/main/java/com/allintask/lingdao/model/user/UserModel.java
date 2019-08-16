package com.allintask.lingdao.model.user;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.allintask.lingdao.bean.user.AddEducationalExperienceBean;
import com.allintask.lingdao.bean.user.AddHonorAndQualificationBean;
import com.allintask.lingdao.bean.user.AddWorkExperienceBean;
import com.allintask.lingdao.bean.message.UserInfoListRequestBean;
import com.allintask.lingdao.bean.user.SaveBankCardRequestBean;
import com.allintask.lingdao.constant.ApiKey;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.constant.ServiceAPIConstant;

import java.util.List;
import java.util.Map;

import cn.tanjiajun.sdk.conn.OkHttpRequest;

/**
 * Created by TanJiaJun on 2018/1/23.
 */

public class UserModel implements IUserModel {

    @Override
    public OkHttpRequest fetchDefaultPhoneNumberHomeLocationRequest() {
        OkHttpRequest fetchDefaultPhoneNumberHomeLocationRequest = new OkHttpRequest();
        fetchDefaultPhoneNumberHomeLocationRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_MOBILE_COUNTRY_CODE_GET_DEFAULT);
        fetchDefaultPhoneNumberHomeLocationRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_MOBILE_COUNTRY_CODE_GET_DEFAULT);
        return fetchDefaultPhoneNumberHomeLocationRequest;
    }

    @Override
    public OkHttpRequest fetchPhoneNumberHomeLocationListRequest() {
        OkHttpRequest fetchPhoneNumberHomeLocationListRequest = new OkHttpRequest();
        fetchPhoneNumberHomeLocationListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_MOBILE_COUNTRY_CODE_LIST);
        fetchPhoneNumberHomeLocationListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_MOBILE_COUNTRY_CODE_LIST);
        return fetchPhoneNumberHomeLocationListRequest;
    }

    @Override
    public OkHttpRequest sendQQSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile) {
        OkHttpRequest sendQQIdentifyCodeRequest = new OkHttpRequest();
        sendQQIdentifyCodeRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_BIND_QQ);
        sendQQIdentifyCodeRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_BIND_QQ);
        sendQQIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_MOBILE_COUNTRY_CODE_ID, String.valueOf(mobileCountryCodeId));
        sendQQIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_MOBILE, mobile);
        return sendQQIdentifyCodeRequest;
    }

    @Override
    public OkHttpRequest sendWechatSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile) {
        OkHttpRequest sendWechatIdentifyCodeRequest = new OkHttpRequest();
        sendWechatIdentifyCodeRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_BIND_WX);
        sendWechatIdentifyCodeRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_BIND_WX);
        sendWechatIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_MOBILE_COUNTRY_CODE_ID, String.valueOf(mobileCountryCodeId));
        sendWechatIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_MOBILE, mobile);
        return sendWechatIdentifyCodeRequest;
    }

    @Override
    public OkHttpRequest sendForgetPasswordSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile) {
        OkHttpRequest sendForgetPasswordIdentifyCodeRequest = new OkHttpRequest();
        sendForgetPasswordIdentifyCodeRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_FORGET_LOGIN_PWD);
        sendForgetPasswordIdentifyCodeRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_FORGET_LOGIN_PWD);
        sendForgetPasswordIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_MOBILE_COUNTRY_CODE_ID, String.valueOf(mobileCountryCodeId));
        sendForgetPasswordIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_MOBILE, mobile);
        return sendForgetPasswordIdentifyCodeRequest;
    }

    @Override
    public OkHttpRequest sendLoginSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile) {
        OkHttpRequest sendLoginIdentifyCodeRequest = new OkHttpRequest();
        sendLoginIdentifyCodeRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_LOGIN);
        sendLoginIdentifyCodeRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_LOGIN);
        sendLoginIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_MOBILE_COUNTRY_CODE_ID, String.valueOf(mobileCountryCodeId));
        sendLoginIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_MOBILE, mobile);
        return sendLoginIdentifyCodeRequest;
    }

    @Override
    public OkHttpRequest bindByQQRequest(int mobileCountryCodeId, String mobile, String captcha, String qqUnionId, String ip) {
        OkHttpRequest bindByQQRequest = new OkHttpRequest();
        bindByQQRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_BIND_BY_QQ_UNION_ID);
        bindByQQRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_BIND_BY_QQ_UNION_ID);
        bindByQQRequest.addRequestFormParam(ApiKey.USER_MOBILE_COUNTRY_CODE_ID, String.valueOf(mobileCountryCodeId));
        bindByQQRequest.addRequestFormParam(ApiKey.USER_MOBILE, mobile);
        bindByQQRequest.addRequestFormParam(ApiKey.USER_CAPTCHA, captcha);
        bindByQQRequest.addRequestFormParam(ApiKey.USER_QQ_UNION_ID, qqUnionId);
        bindByQQRequest.addRequestFormParam(ApiKey.USER_IP, ip);
        return bindByQQRequest;
    }

    @Override
    public OkHttpRequest bindByWechatRequest(int mobileCountryCodeId, String mobile, String captcha, String wxUnionId, String ip) {
        OkHttpRequest bindByWechatRequest = new OkHttpRequest();
        bindByWechatRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_BIND_WX_UNION_ID);
        bindByWechatRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_BIND_WX_UNION_ID);
        bindByWechatRequest.addRequestFormParam(ApiKey.USER_MOBILE_COUNTRY_CODE_ID, String.valueOf(mobileCountryCodeId));
        bindByWechatRequest.addRequestFormParam(ApiKey.USER_MOBILE, mobile);
        bindByWechatRequest.addRequestFormParam(ApiKey.USER_CAPTCHA, captcha);
        bindByWechatRequest.addRequestFormParam(ApiKey.USER_WX_UNION_ID, wxUnionId);
        bindByWechatRequest.addRequestFormParam(ApiKey.USER_IP, ip);
        return bindByWechatRequest;
    }

    @Override
    public OkHttpRequest loginByPhoneNumberAndSmsIdentifyCodeRequest(int mobileCountryCodeId, String phoneNumber, String smsIdentifyCode, String ip) {
        OkHttpRequest loginByPhoneNumberAndSmsIdentifyCodeRequest = new OkHttpRequest();
        loginByPhoneNumberAndSmsIdentifyCodeRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_GET_BY_MOBILE_AND_CAPTCHA);
        loginByPhoneNumberAndSmsIdentifyCodeRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_GET_BY_MOBILE_AND_CAPTCHA);
        loginByPhoneNumberAndSmsIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_MOBILE_COUNTRY_CODE_ID, String.valueOf(mobileCountryCodeId));
        loginByPhoneNumberAndSmsIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_MOBILE, phoneNumber);
        loginByPhoneNumberAndSmsIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_CAPTCHA, smsIdentifyCode);
        loginByPhoneNumberAndSmsIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_IP, ip);
        return loginByPhoneNumberAndSmsIdentifyCodeRequest;
    }

    @Override
    public OkHttpRequest loginByPhoneNumberAndPasswordRequest(int mobileCountryCodeId, String mobile, String password, String ip) {
        OkHttpRequest loginByPhoneNumberAndPasswordRequest = new OkHttpRequest();
        loginByPhoneNumberAndPasswordRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_GET_BY_MOBILE_AND_PWD);
        loginByPhoneNumberAndPasswordRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_GET_BY_MOBILE_AND_PWD);
        loginByPhoneNumberAndPasswordRequest.addRequestFormParam(ApiKey.USER_MOBILE_COUNTRY_CODE_ID, String.valueOf(mobileCountryCodeId));
        loginByPhoneNumberAndPasswordRequest.addRequestFormParam(ApiKey.USER_MOBILE, mobile);
        loginByPhoneNumberAndPasswordRequest.addRequestFormParam(ApiKey.USER_PASSWORD, password);
        loginByPhoneNumberAndPasswordRequest.addRequestFormParam(ApiKey.USER_IP, ip);
        return loginByPhoneNumberAndPasswordRequest;
    }

    @Override
    public OkHttpRequest loginByQQRequest(String openId, String unionId, String name, String nickname, String ip) {
        OkHttpRequest loginByQQRequest = new OkHttpRequest();
        loginByQQRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_GET_BY_QQ);
        loginByQQRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_GET_BY_QQ);
        loginByQQRequest.addRequestFormParam(ApiKey.USER_OPEN_ID, openId);
        loginByQQRequest.addRequestFormParam(ApiKey.USER_UNION_ID, unionId);
        loginByQQRequest.addRequestFormParam(ApiKey.USER_NAME, name);
        loginByQQRequest.addRequestFormParam(ApiKey.USER_NICKNAME, nickname);
        loginByQQRequest.addRequestFormParam(ApiKey.USER_IP, ip);
        return loginByQQRequest;
    }

    @Override
    public OkHttpRequest loginByWechatRequest(String openId, String unionId, String nickname, String ip) {
        OkHttpRequest loginByWechatRequest = new OkHttpRequest();
        loginByWechatRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_GET_BY_WX);
        loginByWechatRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_GET_BY_WX);
        loginByWechatRequest.addRequestFormParam(ApiKey.USER_OPEN_ID, openId);
        loginByWechatRequest.addRequestFormParam(ApiKey.USER_UNION_ID, unionId);
        loginByWechatRequest.addRequestFormParam(ApiKey.USER_NICKNAME, nickname);
        loginByWechatRequest.addRequestFormParam(ApiKey.USER_IP, ip);
        return loginByWechatRequest;
    }

    @Override
    public OkHttpRequest resetLoginPasswordRequest(int mobileCountryCodeId, String mobile, String captcha, String pwd, String confirmPwd) {
        OkHttpRequest resetLoginPasswordRequest = new OkHttpRequest();
        resetLoginPasswordRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_RESET_PWD_BY_CAPTCHA);
        resetLoginPasswordRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_RESET_PWD_BY_CAPTCHA);
        resetLoginPasswordRequest.addRequestFormParam(ApiKey.USER_MOBILE_COUNTRY_CODE_ID, String.valueOf(mobileCountryCodeId));
        resetLoginPasswordRequest.addRequestFormParam(ApiKey.USER_MOBILE, mobile);
        resetLoginPasswordRequest.addRequestFormParam(ApiKey.USER_CAPTCHA, captcha);
        resetLoginPasswordRequest.addRequestFormParam(ApiKey.USER_PWD, pwd);
        resetLoginPasswordRequest.addRequestFormParam(ApiKey.USER_CONFIRM_PWD, confirmPwd);
        return resetLoginPasswordRequest;
    }

    @Override
    public OkHttpRequest fetchEducationalInstitutionListRequest() {
        OkHttpRequest fetchEducationalInstitutionListRequest = new OkHttpRequest();
        fetchEducationalInstitutionListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GENERAL_DATA_SCHOOL_INFO_LIST);
        fetchEducationalInstitutionListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GENERAL_DATA_SCHOOL_INFO_LIST);
        return fetchEducationalInstitutionListRequest;
    }

    @Override
    public OkHttpRequest fetchMajorListRequest() {
        OkHttpRequest fetchMajorInfoListRequest = new OkHttpRequest();
        fetchMajorInfoListRequest.setAPIPath(ServiceAPIConstant.REQUEST_AP_NAME_GENERAL_DATA_MAJOR_INFO_LIST);
        fetchMajorInfoListRequest.setRequestID(ServiceAPIConstant.REQUEST_AP_NAME_GENERAL_DATA_MAJOR_INFO_LIST);
        return fetchMajorInfoListRequest;
    }

    @Override
    public OkHttpRequest fetchEducationalBackgroundListRequest() {
        OkHttpRequest fetchEducationalBackgroundListRequest = new OkHttpRequest();
        fetchEducationalBackgroundListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GENERAL_DATA_EDU_LEVEL_LIST);
        fetchEducationalBackgroundListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GENERAL_DATA_EDU_LEVEL_LIST);
        return fetchEducationalBackgroundListRequest;
    }

    @Override
    public OkHttpRequest saveEducationalExperienceRequest(List<AddEducationalExperienceBean> educationExperienceList) {
        OkHttpRequest saveEducationalExperienceRequest = new OkHttpRequest();
        saveEducationalExperienceRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_EDU_INFO_SAVE_LIST);
        saveEducationalExperienceRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_EDU_INFO_SAVE_LIST);
        saveEducationalExperienceRequest.addRequestFormParam(ApiKey.USER_USER_EDU_INFO_LIST, JSONArray.toJSONString(educationExperienceList, SerializerFeature.DisableCircularReferenceDetect));
        return saveEducationalExperienceRequest;
    }

    @Override
    public OkHttpRequest saveWorkExperienceRequest(List<AddWorkExperienceBean> workExperienceList) {
        OkHttpRequest saveWorkExperienceRequest = new OkHttpRequest();
        saveWorkExperienceRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_WORK_INFO_SAVE_LIST);
        saveWorkExperienceRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_WORK_INFO_SAVE_LIST);
        saveWorkExperienceRequest.addRequestFormParam(ApiKey.USER_WORK_INFO_LIST, JSONArray.toJSONString(workExperienceList, SerializerFeature.DisableCircularReferenceDetect));
        return saveWorkExperienceRequest;
    }

    @Override
    public OkHttpRequest saveHonorAndQualificationRequest(List<AddHonorAndQualificationBean> honorAndQualificationList) {
        OkHttpRequest saveHonorAndQualificationRequest = new OkHttpRequest();
        saveHonorAndQualificationRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_HONOR_INFO_SAVE_LIST);
        saveHonorAndQualificationRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_HONOR_INFO_SAVE_LIST);
        saveHonorAndQualificationRequest.addRequestFormParam(ApiKey.USER_HONOR_INFO_LIST, JSONArray.toJSONString(honorAndQualificationList, SerializerFeature.DisableCircularReferenceDetect));
        return saveHonorAndQualificationRequest;
    }

    @Override
    public OkHttpRequest completePersonalInformationRequest(String tmpUrl, int width, int height, String format, int cutStartX, int cutStartY, int cutWidth, int cutHeight, String name, int gender, String birthday) {
        OkHttpRequest completePersonalInformationRequest = new OkHttpRequest();
        completePersonalInformationRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SET_BASIS);
        completePersonalInformationRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SET_BASIS);
        completePersonalInformationRequest.addRequestFormParam(ApiKey.USER_TMP_URL, tmpUrl);
        completePersonalInformationRequest.addRequestFormParam(ApiKey.USER_WIDTH, String.valueOf(width));
        completePersonalInformationRequest.addRequestFormParam(ApiKey.USER_HEIGHT, String.valueOf(height));
        completePersonalInformationRequest.addRequestFormParam(ApiKey.USER_FORMAT, format);
        completePersonalInformationRequest.addRequestFormParam(ApiKey.USER_CUT_START_X, String.valueOf(cutStartX));
        completePersonalInformationRequest.addRequestFormParam(ApiKey.USER_CUT_START_Y, String.valueOf(cutStartY));
        completePersonalInformationRequest.addRequestFormParam(ApiKey.USER_CUT_WIDTH, String.valueOf(cutWidth));
        completePersonalInformationRequest.addRequestFormParam(ApiKey.USER_CUT_HEIGHT, String.valueOf(cutHeight));
        completePersonalInformationRequest.addRequestFormParam(ApiKey.USER_NAME, name);
        completePersonalInformationRequest.addRequestFormParam(ApiKey.USER_GENDER, String.valueOf(gender));
        completePersonalInformationRequest.addRequestFormParam(ApiKey.USER_BIRTHDAY, birthday);
        return completePersonalInformationRequest;
    }

    @Override
    public OkHttpRequest fetchPersonalInfoRequest() {
        OkHttpRequest fetchPersonalInfoRequest = new OkHttpRequest();
        fetchPersonalInfoRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_PERSONAL_INFO);
        fetchPersonalInfoRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_PERSONAL_INFO);
        return fetchPersonalInfoRequest;
    }

    @Override
    public OkHttpRequest setUserHeadPortraitRequest(int imageId, String tmpUrl, int width, int height, String format, int cutStartX, int cutStartY, int cutWidth, int cutHeight) {
        OkHttpRequest setUserHeadPortraitRequest = new OkHttpRequest();
        setUserHeadPortraitRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SET_AVATAR);
        setUserHeadPortraitRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SET_AVATAR);

        if (imageId != -1) {
            setUserHeadPortraitRequest.addRequestFormParam(ApiKey.USER_IMAGE_ID, String.valueOf(imageId));
        }

        setUserHeadPortraitRequest.addRequestFormParam(ApiKey.USER_TMP_URL, tmpUrl);
        setUserHeadPortraitRequest.addRequestFormParam(ApiKey.USER_WIDTH, String.valueOf(width));
        setUserHeadPortraitRequest.addRequestFormParam(ApiKey.USER_HEIGHT, String.valueOf(height));
        setUserHeadPortraitRequest.addRequestFormParam(ApiKey.USER_FORMAT, format);
        setUserHeadPortraitRequest.addRequestFormParam(ApiKey.USER_CUT_START_X, String.valueOf(cutStartX));
        setUserHeadPortraitRequest.addRequestFormParam(ApiKey.USER_CUT_START_Y, String.valueOf(cutStartY));
        setUserHeadPortraitRequest.addRequestFormParam(ApiKey.USER_CUT_WIDTH, String.valueOf(cutWidth));
        setUserHeadPortraitRequest.addRequestFormParam(ApiKey.USER_CUT_HEIGHT, String.valueOf(cutHeight));
        return setUserHeadPortraitRequest;
    }

    @Override
    public OkHttpRequest setBirthdayRequest(String birthday) {
        OkHttpRequest setBirthdayRequest = new OkHttpRequest();
        setBirthdayRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SET_BIRTHDAY);
        setBirthdayRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SET_BIRTHDAY);
        setBirthdayRequest.addRequestFormParam(ApiKey.USER_BIRTHDAY, birthday);
        return setBirthdayRequest;
    }

    @Override
    public OkHttpRequest setGenderRequest(int gender) {
        OkHttpRequest setGenderRequest = new OkHttpRequest();
        setGenderRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SET_GENDER);
        setGenderRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SET_GENDER);
        setGenderRequest.addRequestFormParam(ApiKey.USER_GENDER, String.valueOf(gender));
        return setGenderRequest;
    }

    @Override
    public OkHttpRequest setNameRequest(String name) {
        OkHttpRequest setNameRequest = new OkHttpRequest();
        setNameRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SET_NAME);
        setNameRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SET_NAME);
        setNameRequest.addRequestFormParam(ApiKey.USER_NAME, name);
        return setNameRequest;
    }

    @Override
    public OkHttpRequest setStartWorkAtRequest(String startWorkAt) {
        OkHttpRequest setStartWorkAtRequest = new OkHttpRequest();
        setStartWorkAtRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SET_START_WORK_AT);
        setStartWorkAtRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SET_START_WORK_AT);
        setStartWorkAtRequest.addRequestFormParam(ApiKey.USER_START_WORK_TIME_AT, startWorkAt);
        return setStartWorkAtRequest;
    }

    @Override
    public OkHttpRequest fetchEducationalExperienceRequest(int id) {
        OkHttpRequest fetchEducationalExperienceRequest = new OkHttpRequest();
        fetchEducationalExperienceRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_EDU_INFO + "/" + String.valueOf(id));
        fetchEducationalExperienceRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_EDU_INFO + "/" + String.valueOf(id));
        return fetchEducationalExperienceRequest;
    }

    @Override
    public OkHttpRequest compileEducationExperienceRequest(int userId, int id, String school, String major, int educationLevelId, String startAt, String endAt) {
        OkHttpRequest compileEducationExperienceRequest = new OkHttpRequest();
        compileEducationExperienceRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_EDU_INFO_UPDATE);
        compileEducationExperienceRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_EDU_INFO_UPDATE);
        compileEducationExperienceRequest.addRequestFormParam(ApiKey.COMMON_USER_ID, String.valueOf(userId));
        compileEducationExperienceRequest.addRequestFormParam(ApiKey.COMMON_ID, String.valueOf(id));
        compileEducationExperienceRequest.addRequestFormParam(ApiKey.USER_SCHOOL, school);
        compileEducationExperienceRequest.addRequestFormParam(ApiKey.USER_MAJOR, major);
        compileEducationExperienceRequest.addRequestFormParam(ApiKey.USER_EDUCATION_LEVEL_ID, String.valueOf(educationLevelId));
        compileEducationExperienceRequest.addRequestFormParam(ApiKey.USER_START_AT, startAt);
        compileEducationExperienceRequest.addRequestFormParam(ApiKey.USER_END_AT, endAt);
        return compileEducationExperienceRequest;
    }

    @Override
    public OkHttpRequest removeEducationExperienceRequest(int id) {
        OkHttpRequest removeEducationExperienceRequest = new OkHttpRequest();
        removeEducationExperienceRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_EDU_INFO_DELETE + "/" + String.valueOf(id));
        removeEducationExperienceRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_EDU_INFO_DELETE + "/" + String.valueOf(id));
        return removeEducationExperienceRequest;
    }

    @Override
    public OkHttpRequest fetchWorkExperienceRequest(int id) {
        OkHttpRequest fetchWorkExperienceRequest = new OkHttpRequest();
        fetchWorkExperienceRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_WORK_INFO + "/" + String.valueOf(id));
        fetchWorkExperienceRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_WORK_INFO + "/" + String.valueOf(id));
        return fetchWorkExperienceRequest;
    }

    @Override
    public OkHttpRequest compileWorkExperienceRequest(int userId, int id, String company, String post, String startAt, String endAt) {
        OkHttpRequest compileWorkExperienceRequest = new OkHttpRequest();
        compileWorkExperienceRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_WORK_INFO_UPDATE);
        compileWorkExperienceRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_WORK_INFO_UPDATE);
        compileWorkExperienceRequest.addRequestFormParam(ApiKey.COMMON_USER_ID, String.valueOf(userId));
        compileWorkExperienceRequest.addRequestFormParam(ApiKey.COMMON_ID, String.valueOf(id));
        compileWorkExperienceRequest.addRequestFormParam(ApiKey.USER_COMPANY, company);
        compileWorkExperienceRequest.addRequestFormParam(ApiKey.USER_POST, post);
        compileWorkExperienceRequest.addRequestFormParam(ApiKey.USER_START_AT, startAt);
        compileWorkExperienceRequest.addRequestFormParam(ApiKey.USER_END_AT, endAt);
        return compileWorkExperienceRequest;
    }

    @Override
    public OkHttpRequest removeWorkExperienceRequest(int id) {
        OkHttpRequest removeWorkExperienceRequest = new OkHttpRequest();
        removeWorkExperienceRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_WORK_INFO_DELETE + "/" + String.valueOf(id));
        removeWorkExperienceRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_WORK_INFO_DELETE + "/" + String.valueOf(id));
        return removeWorkExperienceRequest;
    }

    @Override
    public OkHttpRequest fetchHonorAndQualificationRequest(int id) {
        OkHttpRequest fetchHonorAndQualificationRequest = new OkHttpRequest();
        fetchHonorAndQualificationRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_HONOR_INFO + "/" + String.valueOf(id));
        fetchHonorAndQualificationRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_HONOR_INFO + "/" + String.valueOf(id));
        return fetchHonorAndQualificationRequest;
    }

    @Override
    public OkHttpRequest compileHonorAndQualificationRequest(int userId, int id, String prize, String issuingAuthority, String gainAt) {
        OkHttpRequest compileHonorAndQualificationRequest = new OkHttpRequest();
        compileHonorAndQualificationRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_HONOR_INFO_UPDATE);
        compileHonorAndQualificationRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_HONOR_INFO_UPDATE);
        compileHonorAndQualificationRequest.addRequestFormParam(ApiKey.COMMON_USER_ID, String.valueOf(userId));
        compileHonorAndQualificationRequest.addRequestFormParam(ApiKey.COMMON_ID, String.valueOf(id));
        compileHonorAndQualificationRequest.addRequestFormParam(ApiKey.USER_PRIZE, prize);
        compileHonorAndQualificationRequest.addRequestFormParam(ApiKey.USER_ISSUING_AUTHORITY, issuingAuthority);
        compileHonorAndQualificationRequest.addRequestFormParam(ApiKey.USER_GAIN_AT, gainAt);
        return compileHonorAndQualificationRequest;
    }

    @Override
    public OkHttpRequest removeHonorAndQualificationRequest(int id) {
        OkHttpRequest removeHonorAndQualificationRequest = new OkHttpRequest();
        removeHonorAndQualificationRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_HONOR_INFO_DELETE + "/" + String.valueOf(id));
        removeHonorAndQualificationRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_HONOR_INFO_DELETE + "/" + String.valueOf(id));
        return removeHonorAndQualificationRequest;
    }

    @Override
    public OkHttpRequest deletePhotoAlbumRequest(int albumId, int imageId, String qualityUrl) {
        OkHttpRequest deletePhotoAlbumRequest = new OkHttpRequest();
        deletePhotoAlbumRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_ALBUM_DELETE + "/" + albumId + "/" + imageId);
        deletePhotoAlbumRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_ALBUM_DELETE + "/" + albumId + "/" + imageId);
        deletePhotoAlbumRequest.addRequestFormParam(ApiKey.USER_QUALITY_URL, qualityUrl);
        return deletePhotoAlbumRequest;
    }

    @Override
    public OkHttpRequest getMyPhotoAlbumSurplusRequest() {
        OkHttpRequest getMyPhotoAlbumSurplusRequest = new OkHttpRequest();
        getMyPhotoAlbumSurplusRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_ALBUM_GET_PERSONAL_ALBUM_SURPLUS);
        getMyPhotoAlbumSurplusRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_ALBUM_GET_PERSONAL_ALBUM_SURPLUS);
        return getMyPhotoAlbumSurplusRequest;
    }

    @Override
    public OkHttpRequest getServicePhotoAlbumSurplusRequest() {
        OkHttpRequest getServicePhotoAlbumSurplusRequest = new OkHttpRequest();
        getServicePhotoAlbumSurplusRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_ALBUM_GET_SERVE_ALBUM_SURPLUS);
        getServicePhotoAlbumSurplusRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_ALBUM_GET_SERVE_ALBUM_SURPLUS);
        return getServicePhotoAlbumSurplusRequest;
    }

    @Override
    public OkHttpRequest fetchMyPhotoAlbumListRequest(int userId, int pageNum) {
        OkHttpRequest fetchMyPhotoAlbumListRequest = new OkHttpRequest();
        fetchMyPhotoAlbumListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_ALBUM_LIST);
        fetchMyPhotoAlbumListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_ALBUM_LIST);
        fetchMyPhotoAlbumListRequest.addRequestFormParam(ApiKey.COMMON_USER_ID, String.valueOf(userId));
        fetchMyPhotoAlbumListRequest.addRequestFormParam(ApiKey.COMMON_PAGE_NUM, String.valueOf(pageNum));
        return fetchMyPhotoAlbumListRequest;
    }

    @Override
    public OkHttpRequest savePhotoAlbumListRequest(String albumRequestJson) {
        OkHttpRequest savePhotoAlbumListRequest = new OkHttpRequest();
        savePhotoAlbumListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_ALBUM_SAVE_LIST);
        savePhotoAlbumListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_ALBUM_SAVE_LIST);
        savePhotoAlbumListRequest.addRequestFormParam(ApiKey.USER_ALBUM_REQUEST_JSON, albumRequestJson);
        return savePhotoAlbumListRequest;
    }

    @Override
    public OkHttpRequest collectUserRequest(int beUserId) {
        OkHttpRequest collectUserRequest = new OkHttpRequest();
        collectUserRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_STORE_UP_SAVE);
        collectUserRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_STORE_UP_SAVE);
        collectUserRequest.addRequestFormParam(ApiKey.USER_BE_USER_ID, String.valueOf(beUserId));
        return collectUserRequest;
    }

    @Override
    public OkHttpRequest cancelCollectUserRequest(int userId, int beUserId) {
        OkHttpRequest cancelCollectUserRequest = new OkHttpRequest();
        cancelCollectUserRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_STORE_UP_CANCEL + "/" + String.valueOf(userId) + "/" + String.valueOf(beUserId));
        cancelCollectUserRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_STORE_UP_CANCEL + "/" + String.valueOf(userId) + "/" + String.valueOf(beUserId));
        return cancelCollectUserRequest;
    }

    @Override
    public OkHttpRequest likeUserRequest(int beUserId) {
        OkHttpRequest likeUserRequest = new OkHttpRequest();
        likeUserRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_LIKE_SAVE);
        likeUserRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_LIKE_SAVE);
        likeUserRequest.addRequestFormParam(ApiKey.USER_BE_USER_ID, String.valueOf(beUserId));
        return likeUserRequest;
    }

    @Override
    public OkHttpRequest cancelLikeUserRequest(int userId, int beUserId) {
        OkHttpRequest cancelLikeUserRequest = new OkHttpRequest();
        cancelLikeUserRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_LIKE_CANCEL + "/" + String.valueOf(userId) + "/" + String.valueOf(beUserId));
        cancelLikeUserRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_LIKE_CANCEL + "/" + String.valueOf(userId) + "/" + String.valueOf(beUserId));
        return cancelLikeUserRequest;
    }

    @Override
    public OkHttpRequest checkUploadIsSuccessRequest(String flagId) {
        OkHttpRequest checkUploadIsSuccessRequest = new OkHttpRequest();
        checkUploadIsSuccessRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_FILE_OSS_A_LI_YUN_IMAGE_URL_RETURN);
        checkUploadIsSuccessRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_FILE_OSS_A_LI_YUN_IMAGE_URL_RETURN);
        checkUploadIsSuccessRequest.addRequestFormParam(ApiKey.USER_FLAG_ID, flagId);
        return checkUploadIsSuccessRequest;
    }

    @Override
    public OkHttpRequest checkNeedGuideRequest() {
        OkHttpRequest checkUploadIsSuccessRequest = new OkHttpRequest();
        checkUploadIsSuccessRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CHECK_NEED_GUIDE);
        checkUploadIsSuccessRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CHECK_NEED_GUIDE);
        return checkUploadIsSuccessRequest;
    }

    @Override
    public OkHttpRequest fetchMyDataRequest() {
        OkHttpRequest fetchMyDataRequest = new OkHttpRequest();
        fetchMyDataRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MINE);
        fetchMyDataRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MINE);
        return fetchMyDataRequest;
    }

    @Override
    public OkHttpRequest logoutRequest() {
        OkHttpRequest logoutRequest = new OkHttpRequest();
        logoutRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_LOGOUT);
        logoutRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_LOGOUT);
        return logoutRequest;
    }

    @Override
    public OkHttpRequest sendModifyLoginPasswordSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile) {
        OkHttpRequest sendModifyLoginPasswordSmsIdentifyCodeRequest = new OkHttpRequest();
        sendModifyLoginPasswordSmsIdentifyCodeRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_MODIFY_LOGIN_PWD);
        sendModifyLoginPasswordSmsIdentifyCodeRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_MODIFY_LOGIN_PWD);
        sendModifyLoginPasswordSmsIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_MOBILE_COUNTRY_CODE_ID, String.valueOf(mobileCountryCodeId));
        sendModifyLoginPasswordSmsIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_MOBILE, mobile);
        return sendModifyLoginPasswordSmsIdentifyCodeRequest;
    }

    @Override
    public OkHttpRequest sendModifyPhoneNumberSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile) {
        OkHttpRequest sendModifyPhoneNumberSmsIdentifyCodeRequest = new OkHttpRequest();
        sendModifyPhoneNumberSmsIdentifyCodeRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_MODIFY_MOBILE);
        sendModifyPhoneNumberSmsIdentifyCodeRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_MODIFY_MOBILE);
        sendModifyPhoneNumberSmsIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_MOBILE_COUNTRY_CODE_ID, String.valueOf(mobileCountryCodeId));
        sendModifyPhoneNumberSmsIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_MOBILE, mobile);
        return sendModifyPhoneNumberSmsIdentifyCodeRequest;
    }

    @Override
    public OkHttpRequest sendModifyPaymentPasswordSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile) {
        OkHttpRequest sendModifyPaymentPasswordSmsIdentifyCodeRequest = new OkHttpRequest();
        sendModifyPaymentPasswordSmsIdentifyCodeRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_MODIFY_PAY_PWD);
        sendModifyPaymentPasswordSmsIdentifyCodeRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_MODIFY_PAY_PWD);
        sendModifyPaymentPasswordSmsIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_MOBILE_COUNTRY_CODE_ID, String.valueOf(mobileCountryCodeId));
        sendModifyPaymentPasswordSmsIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_MOBILE, mobile);
        return sendModifyPaymentPasswordSmsIdentifyCodeRequest;
    }

    @Override
    public OkHttpRequest sendSetLoginPasswordSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile) {
        OkHttpRequest sendSetLoginPasswordSmsIdentifyCodeRequest = new OkHttpRequest();
        sendSetLoginPasswordSmsIdentifyCodeRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_SET_LOGIN_PWD);
        sendSetLoginPasswordSmsIdentifyCodeRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_SET_LOGIN_PWD);
        sendSetLoginPasswordSmsIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_MOBILE_COUNTRY_CODE_ID, String.valueOf(mobileCountryCodeId));
        sendSetLoginPasswordSmsIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_MOBILE, mobile);
        return sendSetLoginPasswordSmsIdentifyCodeRequest;
    }

    @Override
    public OkHttpRequest sendSetPaymentPasswordSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile) {
        OkHttpRequest sendSetPaymentPasswordSmsIdentifyCodeRequest = new OkHttpRequest();
        sendSetPaymentPasswordSmsIdentifyCodeRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_SET_PAY_PWD);
        sendSetPaymentPasswordSmsIdentifyCodeRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_SET_PAY_PWD);
        sendSetPaymentPasswordSmsIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_MOBILE_COUNTRY_CODE_ID, String.valueOf(mobileCountryCodeId));
        sendSetPaymentPasswordSmsIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_MOBILE, mobile);
        return sendSetPaymentPasswordSmsIdentifyCodeRequest;
    }

    @Override
    public OkHttpRequest sendModifyMailboxSmsIdentifyCodeRequest(String email) {
        OkHttpRequest sendModifyMailboxSmsIdentifyCodeRequest = new OkHttpRequest();
        sendModifyMailboxSmsIdentifyCodeRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_MAIL_SEND_CAPTCHA_TO_MODIFY_EMAIL);
        sendModifyMailboxSmsIdentifyCodeRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_MAIL_SEND_CAPTCHA_TO_MODIFY_EMAIL);
        sendModifyMailboxSmsIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_EMAIL, email);
        return sendModifyMailboxSmsIdentifyCodeRequest;
    }

    @Override
    public OkHttpRequest sendBindMailboxSmsIdentifyCodeRequest(String email) {
        OkHttpRequest sendBindMailboxSmsIdentifyCodeRequest = new OkHttpRequest();
        sendBindMailboxSmsIdentifyCodeRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_MAIL_SEND_CAPTCHA_TO_SET_EMAIL);
        sendBindMailboxSmsIdentifyCodeRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_MAIL_SEND_CAPTCHA_TO_SET_EMAIL);
        sendBindMailboxSmsIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_EMAIL, email);
        return sendBindMailboxSmsIdentifyCodeRequest;
    }

    @Override
    public OkHttpRequest fetchSettingRequest() {
        OkHttpRequest fetchSettingRequest = new OkHttpRequest();
        fetchSettingRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING);
        fetchSettingRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING);
        return fetchSettingRequest;
    }

    @Override
    public OkHttpRequest bindQQRequest(String openId, String unionId, String name, String nickname, String ip) {
        OkHttpRequest bindQQRequest = new OkHttpRequest();
        bindQQRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_BIND_QQ);
        bindQQRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_BIND_QQ);
        bindQQRequest.addRequestFormParam(ApiKey.USER_OPEN_ID, openId);
        bindQQRequest.addRequestFormParam(ApiKey.USER_UNION_ID, unionId);
        bindQQRequest.addRequestFormParam(ApiKey.USER_NAME, name);
        bindQQRequest.addRequestFormParam(ApiKey.USER_NICKNAME, nickname);
        bindQQRequest.addRequestFormParam(ApiKey.USER_IP, ip);
        return bindQQRequest;
    }

    @Override
    public OkHttpRequest bindWechatRequest(String openId, String unionId, String nickname, String ip) {
        OkHttpRequest bindWechatRequest = new OkHttpRequest();
        bindWechatRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_BIND_WX);
        bindWechatRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_BIND_WX);
        bindWechatRequest.addRequestFormParam(ApiKey.USER_OPEN_ID, openId);
        bindWechatRequest.addRequestFormParam(ApiKey.USER_UNION_ID, unionId);
        bindWechatRequest.addRequestFormParam(ApiKey.USER_NICKNAME, nickname);
        bindWechatRequest.addRequestFormParam(ApiKey.USER_IP, ip);
        return bindWechatRequest;
    }

    @Override
    public OkHttpRequest checkModifyPaymentPasswordSmsIdentifyCodeRequest(String captcha) {
        OkHttpRequest checkModifyPaymentPasswordSmsIdentifyCodeRequest = new OkHttpRequest();
        checkModifyPaymentPasswordSmsIdentifyCodeRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_CHECK_MODIFY_PAY_PWD_CAPTCHA);
        checkModifyPaymentPasswordSmsIdentifyCodeRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_CHECK_MODIFY_PAY_PWD_CAPTCHA);
        checkModifyPaymentPasswordSmsIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_CAPTCHA, captcha);
        return checkModifyPaymentPasswordSmsIdentifyCodeRequest;
    }

    @Override
    public OkHttpRequest checkOldPaymentPasswordRequest(String payPwd) {
        OkHttpRequest checkOldPaymentPasswordRequest = new OkHttpRequest();
        checkOldPaymentPasswordRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_CHECK_PAY_PWD);
        checkOldPaymentPasswordRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_CHECK_PAY_PWD);
        checkOldPaymentPasswordRequest.addRequestFormParam(ApiKey.USER_PAY_PWD, payPwd);
        return checkOldPaymentPasswordRequest;
    }

    @Override
    public OkHttpRequest checkSetPaymentPasswordSmsIdentifyCodeRequest(String captcha) {
        OkHttpRequest checkSetPaymentPasswordSmsIdentifyCodeRequest = new OkHttpRequest();
        checkSetPaymentPasswordSmsIdentifyCodeRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_CHECK_SET_PAY_PWD_CAPTCHA);
        checkSetPaymentPasswordSmsIdentifyCodeRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_CHECK_SET_PAY_PWD_CAPTCHA);
        checkSetPaymentPasswordSmsIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_CAPTCHA, captcha);
        return checkSetPaymentPasswordSmsIdentifyCodeRequest;
    }

    @Override
    public OkHttpRequest modifyMailboxRequest(String captcha, String email) {
        OkHttpRequest modifyMailboxRequest = new OkHttpRequest();
        modifyMailboxRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_MODIFY_EMAIL);
        modifyMailboxRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_MODIFY_EMAIL);
        modifyMailboxRequest.addRequestFormParam(ApiKey.USER_CAPTCHA, captcha);
        modifyMailboxRequest.addRequestFormParam(ApiKey.USER_EMAIL, email);
        return modifyMailboxRequest;
    }

    @Override
    public OkHttpRequest modifyLoginPasswordRequest(String captcha, String password, String confirmPassword) {
        OkHttpRequest modifyLoginPasswordRequest = new OkHttpRequest();
        modifyLoginPasswordRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_MODIFY_LOGIN_PASSWORD);
        modifyLoginPasswordRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_MODIFY_LOGIN_PASSWORD);
        modifyLoginPasswordRequest.addRequestFormParam(ApiKey.USER_CAPTCHA, captcha);
        modifyLoginPasswordRequest.addRequestFormParam(ApiKey.USER_PASSWORD, password);
        modifyLoginPasswordRequest.addRequestFormParam(ApiKey.USER_CONFIRM_PASSWORD, confirmPassword);
        return modifyLoginPasswordRequest;
    }

    @Override
    public OkHttpRequest modifyPhoneNumberRequest(int oldMobileCountryCodeId, String oldMobile, String password, int newMobileCountryCodeId, String newMobile, String captcha) {
        OkHttpRequest modifyPhoneNumberRequest = new OkHttpRequest();
        modifyPhoneNumberRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_MODIFY_MOBILE);
        modifyPhoneNumberRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_MODIFY_MOBILE);
        modifyPhoneNumberRequest.addRequestFormParam(ApiKey.USER_OLD_MOBILE_COUNTRY_CODE_ID, String.valueOf(oldMobileCountryCodeId));
        modifyPhoneNumberRequest.addRequestFormParam(ApiKey.USER_OLD_MOBILE, oldMobile);
        modifyPhoneNumberRequest.addRequestFormParam(ApiKey.USER_PASSWORD, password);
        modifyPhoneNumberRequest.addRequestFormParam(ApiKey.USER_NEW_MOBILE_COUNTRY_CODE_ID, String.valueOf(newMobileCountryCodeId));
        modifyPhoneNumberRequest.addRequestFormParam(ApiKey.USER_NEW_MOBILE, newMobile);
        modifyPhoneNumberRequest.addRequestFormParam(ApiKey.USER_CAPTCHA, captcha);
        return modifyPhoneNumberRequest;
    }

    @Override
    public OkHttpRequest modifyPaymentPasswordRequest(String captcha, String oldPayPwd, String newPayPwd) {
        OkHttpRequest modifyPaymentPasswordRequest = new OkHttpRequest();
        modifyPaymentPasswordRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_MODIFY_PAY_PWD);
        modifyPaymentPasswordRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_MODIFY_PAY_PWD);
        modifyPaymentPasswordRequest.addRequestFormParam(ApiKey.USER_CAPTCHA, captcha);
        modifyPaymentPasswordRequest.addRequestFormParam(ApiKey.USER_OLD_PAY_PWD, oldPayPwd);
        modifyPaymentPasswordRequest.addRequestFormParam(ApiKey.USER_NEW_PAY_PWD, newPayPwd);
        return modifyPaymentPasswordRequest;
    }

    @Override
    public OkHttpRequest bindMailboxRequest(String captcha, String email) {
        OkHttpRequest bindMailboxRequest = new OkHttpRequest();
        bindMailboxRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_SET_EMAIL);
        bindMailboxRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_SET_EMAIL);
        bindMailboxRequest.addRequestFormParam(ApiKey.USER_CAPTCHA, captcha);
        bindMailboxRequest.addRequestFormParam(ApiKey.USER_EMAIL, email);
        return bindMailboxRequest;
    }

    @Override
    public OkHttpRequest setLoginPasswordRequest(String captcha, String password, String confirmPassword) {
        OkHttpRequest setLoginPasswordRequest = new OkHttpRequest();
        setLoginPasswordRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_SET_LOGIN_PASSWORD);
        setLoginPasswordRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_SET_LOGIN_PASSWORD);
        setLoginPasswordRequest.addRequestFormParam(ApiKey.USER_CAPTCHA, captcha);
        setLoginPasswordRequest.addRequestFormParam(ApiKey.USER_PASSWORD, password);
        setLoginPasswordRequest.addRequestFormParam(ApiKey.USER_CONFIRM_PASSWORD, confirmPassword);
        return setLoginPasswordRequest;
    }

    @Override
    public OkHttpRequest setPaymentPasswordRequest(String captcha, String payPwd) {
        OkHttpRequest setPaymentPasswordRequest = new OkHttpRequest();
        setPaymentPasswordRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_SET_PAY_PWD);
        setPaymentPasswordRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_SET_PAY_PWD);
        setPaymentPasswordRequest.addRequestFormParam(ApiKey.USER_CAPTCHA, captcha);
        setPaymentPasswordRequest.addRequestFormParam(ApiKey.USER_PAY_PWD, payPwd);
        return setPaymentPasswordRequest;
    }

    @Override
    public OkHttpRequest unbindQQRequest() {
        OkHttpRequest unbindQQRequest = new OkHttpRequest();
        unbindQQRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_UNBIND_QQ);
        unbindQQRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_UNBIND_QQ);
        return unbindQQRequest;
    }

    @Override
    public OkHttpRequest unbindWechatRequest() {
        OkHttpRequest unbindWechatRequest = new OkHttpRequest();
        unbindWechatRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_UNBIND_WX);
        unbindWechatRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SETTING_UNBIND_WX);
        return unbindWechatRequest;
    }

    @Override
    public OkHttpRequest fetchUserInfoListRequest(UserInfoListRequestBean userInfoListRequestBean) {
        OkHttpRequest fetchUserInfoListRequest = new OkHttpRequest();
        fetchUserInfoListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_INFO_GET_USER_IM_MSG);
        fetchUserInfoListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_INFO_GET_USER_IM_MSG);
        fetchUserInfoListRequest.addRequestFormParam(ApiKey.USER_USER_ID_JSON, JSONObject.toJSONString(userInfoListRequestBean, SerializerFeature.DisableCircularReferenceDetect));
        return fetchUserInfoListRequest;
    }

    @Override
    public OkHttpRequest checkBasicPersonalInformationWholeRequest() {
        OkHttpRequest checkBasicPersonalInformationWhole = new OkHttpRequest();
        checkBasicPersonalInformationWhole.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_CHECK_BASIC_MSG);
        checkBasicPersonalInformationWhole.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_CHECK_BASIC_MSG);
        return checkBasicPersonalInformationWhole;
    }

    @Override
    public OkHttpRequest checkPersonalInformationWholeRequest() {
        OkHttpRequest checkPersonalInformationWholeRequest = new OkHttpRequest();
        checkPersonalInformationWholeRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_CHECK_PERSONAL_INFO);
        checkPersonalInformationWholeRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_CHECK_PERSONAL_INFO);
        return checkPersonalInformationWholeRequest;
    }

    @Override
    public OkHttpRequest updateBasicPersonalInformationRequest(int imageId, String tmpUrl, int width, int height, String format, int cutStartX, int cutStartY, int cutWidth, int cutHeight, String name, int gender, String birthday) {
        OkHttpRequest updateBasicPersonalInformationRequest = new OkHttpRequest();
        updateBasicPersonalInformationRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_UPDATE_BASIS);
        updateBasicPersonalInformationRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_UPDATE_BASIS);

        if (imageId != -1) {
            updateBasicPersonalInformationRequest.addRequestFormParam(ApiKey.USER_IMAGE_ID, String.valueOf(imageId));
        }

        if (!TextUtils.isEmpty(tmpUrl)) {
            updateBasicPersonalInformationRequest.addRequestFormParam(ApiKey.USER_TMP_URL, tmpUrl);
        }

        if (width != -1) {
            updateBasicPersonalInformationRequest.addRequestFormParam(ApiKey.USER_WIDTH, String.valueOf(width));
        }

        if (height != -1) {
            updateBasicPersonalInformationRequest.addRequestFormParam(ApiKey.USER_HEIGHT, String.valueOf(height));
        }

        if (!TextUtils.isEmpty(format)) {
            updateBasicPersonalInformationRequest.addRequestFormParam(ApiKey.USER_FORMAT, format);
        }

        if (cutStartX != -1) {
            updateBasicPersonalInformationRequest.addRequestFormParam(ApiKey.USER_CUT_START_X, String.valueOf(cutStartX));
        }

        if (cutStartY != -1) {
            updateBasicPersonalInformationRequest.addRequestFormParam(ApiKey.USER_CUT_START_Y, String.valueOf(cutStartY));
        }

        if (cutWidth != -1) {
            updateBasicPersonalInformationRequest.addRequestFormParam(ApiKey.USER_CUT_WIDTH, String.valueOf(cutWidth));
        }

        if (cutHeight != -1) {
            updateBasicPersonalInformationRequest.addRequestFormParam(ApiKey.USER_CUT_HEIGHT, String.valueOf(cutHeight));
        }


        updateBasicPersonalInformationRequest.addRequestFormParam(ApiKey.USER_NAME, name);
        updateBasicPersonalInformationRequest.addRequestFormParam(ApiKey.USER_GENDER, String.valueOf(gender));
        updateBasicPersonalInformationRequest.addRequestFormParam(ApiKey.USER_BIRTHDAY, birthday);
        return updateBasicPersonalInformationRequest;
    }

    @Override
    public OkHttpRequest fetchRecommendDetailsUserInformationRequest(int userId) {
        OkHttpRequest fetchRecommendDetailsUserInformationRequest = new OkHttpRequest();
        fetchRecommendDetailsUserInformationRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_INFO_TO_SERVE_DETAILS + "/" + String.valueOf(userId));
        fetchRecommendDetailsUserInformationRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_INFO_TO_SERVE_DETAILS + "/" + String.valueOf(userId));
        return fetchRecommendDetailsUserInformationRequest;
    }

    @Override
    public OkHttpRequest fetchAddressListRequest() {
        OkHttpRequest fetchAddressListRequest = new OkHttpRequest();
        fetchAddressListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GENERAL_DATA_ADDRESS_LIST);
        fetchAddressListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GENERAL_DATA_ADDRESS_LIST);
        return fetchAddressListRequest;
    }

    @Override
    public OkHttpRequest fetchRecommendListRequest(int pageNum, int serveWayId, int priceMax, int priceMin, int unitId, int order) {
        OkHttpRequest fetchRecommendListRequest = new OkHttpRequest();
        fetchRecommendListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_SERVE_RECOMMEND);
        fetchRecommendListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_SERVE_RECOMMEND);
        fetchRecommendListRequest.addRequestFormParam(ApiKey.COMMON_PAGE_NUM, String.valueOf(pageNum));

        if (serveWayId != -1) {
            fetchRecommendListRequest.addRequestFormParam(ApiKey.USER_SERVE_WAY_ID, String.valueOf(serveWayId));
        }

        if (priceMax != -1) {
            fetchRecommendListRequest.addRequestFormParam(ApiKey.USER_PRICE_MAX, String.valueOf(priceMax));
        }

        if (priceMin != -1) {
            fetchRecommendListRequest.addRequestFormParam(ApiKey.USER_PRICE_MIN, String.valueOf(priceMin));
        }

        if (unitId != -1) {
            fetchRecommendListRequest.addRequestFormParam(ApiKey.USER_UNIT_ID, String.valueOf(unitId));
        }

        if (order != -1) {
            fetchRecommendListRequest.addRequestFormParam(ApiKey.USER_ORDER, String.valueOf(order));
        }
        return fetchRecommendListRequest;
    }

    @Override
    public OkHttpRequest fetchAlbumDetailsRequest(int albumId) {
        OkHttpRequest fetchAlbumDetailsRequest = new OkHttpRequest();
        fetchAlbumDetailsRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_ALBUM_GET + "/" + String.valueOf(albumId));
        fetchAlbumDetailsRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_ALBUM_GET + "/" + String.valueOf(albumId));
        return fetchAlbumDetailsRequest;
    }

    @Override
    public OkHttpRequest fetchMyCollectListRequest(int pageNum) {
        OkHttpRequest fetchMyCollectListRequest = new OkHttpRequest();
        fetchMyCollectListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_STORE_UP_LIST);
        fetchMyCollectListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_STORE_UP_LIST);
        fetchMyCollectListRequest.addRequestFormParam(ApiKey.COMMON_PAGE_NUM, String.valueOf(pageNum));
        return fetchMyCollectListRequest;
    }

    @Override
    public OkHttpRequest fetchCategoryListRequest() {
        OkHttpRequest fetchCategoryListRequest = new OkHttpRequest();
        fetchCategoryListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_LIST_TO_SEARCH_SERVE);
        fetchCategoryListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_LIST_TO_SEARCH_SERVE);
        return fetchCategoryListRequest;
    }

    @Override
    public OkHttpRequest fetchDemandCategoryListRequest() {
        OkHttpRequest fetchDemandCategoryListRequest = new OkHttpRequest();
        fetchDemandCategoryListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_LIST_TO_SEARCH_DEMAND);
        fetchDemandCategoryListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_LIST_TO_SEARCH_DEMAND);
        return fetchDemandCategoryListRequest;
    }

    @Override
    public OkHttpRequest fetchAccountBalanceRequest() {
        OkHttpRequest fetchAccountBalanceRequest = new OkHttpRequest();
        fetchAccountBalanceRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_WALLET_DETAILS);
        fetchAccountBalanceRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_WALLET_DETAILS);
        return fetchAccountBalanceRequest;
    }

    @Override
    public OkHttpRequest fetchTransactionRecordListRequest(int pageNum) {
        OkHttpRequest fetchTransactionRecordListRequest = new OkHttpRequest();
        fetchTransactionRecordListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_BILL_LIST);
        fetchTransactionRecordListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_BILL_LIST);
        fetchTransactionRecordListRequest.addRequestFormParam(ApiKey.COMMON_PAGE_NUM, String.valueOf(pageNum));
        return fetchTransactionRecordListRequest;
    }

    @Override
    public OkHttpRequest fetchBankCardDetailsRequest(int id) {
        OkHttpRequest fetchBankCardDetailsRequest = new OkHttpRequest();
        fetchBankCardDetailsRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_BANK_CARD_GET + "/" + String.valueOf(id));
        fetchBankCardDetailsRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_BANK_CARD_GET + "/" + String.valueOf(id));
        return fetchBankCardDetailsRequest;
    }

    @Override
    public OkHttpRequest saveBankCardRequest(SaveBankCardRequestBean saveBankCardRequestBean) {
        OkHttpRequest saveBankCardRequest = new OkHttpRequest();
        saveBankCardRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_BANK_CARD_SAVE);
        saveBankCardRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_BANK_CARD_SAVE);
        saveBankCardRequest.addRequestFormParam(ApiKey.USER_BANK_CARD_REQUEST_JSON, JSONObject.toJSONString(saveBankCardRequestBean, SerializerFeature.DisableCircularReferenceDetect));
        return saveBankCardRequest;
    }

    @Override
    public OkHttpRequest updateBankCardRequest(String updateBankCardRequestBeanJsonString) {
        OkHttpRequest updateBankCardRequest = new OkHttpRequest();
        updateBankCardRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_BANK_CARD_UPDATE);
        updateBankCardRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_BANK_CARD_UPDATE);
        updateBankCardRequest.addRequestFormParam(ApiKey.USER_BANK_CARD_REQUEST_JSON, updateBankCardRequestBeanJsonString);
        return updateBankCardRequest;
    }

    @Override
    public OkHttpRequest fetchBankInfoListRequest() {
        OkHttpRequest fetchBankInfoListRequest = new OkHttpRequest();
        fetchBankInfoListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_GHT_BANK_INFO_LIST);
        fetchBankInfoListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_GHT_BANK_INFO_LIST);
        return fetchBankInfoListRequest;
    }

    @Override
    public OkHttpRequest fetchBankInfoDetailsRequest(String code) {
        OkHttpRequest fetchBankInfoDetailsRequest = new OkHttpRequest();
        fetchBankInfoDetailsRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_GHT_BANK_INFO_GET + "/" + code);
        fetchBankInfoDetailsRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_FINANCE_CENTER_GHT_BANK_INFO_GET + "/" + code);
        return fetchBankInfoDetailsRequest;
    }

    @Override
    public OkHttpRequest appUpdateRequest(String version) {
        OkHttpRequest appUpdateRequest = new OkHttpRequest();
        appUpdateRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GENERAL_DATA_VERSION_CONTROL_ANDROID);
        appUpdateRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GENERAL_DATA_VERSION_CONTROL_ANDROID);
        appUpdateRequest.addRequestFormParam(ApiKey.USER_VERSION, version);
        return appUpdateRequest;
    }

    @Override
    public OkHttpRequest fetchAdvancedFilterRequest() {
        OkHttpRequest fetchAdvancedFilterRequest = new OkHttpRequest();
        fetchAdvancedFilterRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_SERVE_GET_ADVANCED_FILTER);
        fetchAdvancedFilterRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_SERVE_GET_ADVANCED_FILTER);
        return fetchAdvancedFilterRequest;
    }

    @Override
    public OkHttpRequest searchRequest(Map<String, Object> searchMap) {
        OkHttpRequest searchRequest = new OkHttpRequest();
        searchRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_SERVE_SEARCH);
        searchRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_SERVE_SEARCH);
        searchRequest.addRequestFormParam(ApiKey.USER_SOLR_SERVE_REQUEST_JSON, JSONObject.toJSONString(searchMap, SerializerFeature.DisableCircularReferenceDetect));
        return searchRequest;
    }

    @Override
    public OkHttpRequest updateLoginTimeAndLocationRequest(double longitude, double latitude, String provinceCode, String cityCode, String location) {
        OkHttpRequest updateLoginTimeAndLocationRequest = new OkHttpRequest();
        updateLoginTimeAndLocationRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_UPDATE_LOGIN_TIME_AND_LOCATION);
        updateLoginTimeAndLocationRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_UPDATE_LOGIN_TIME_AND_LOCATION);

        if (longitude != 0D) {
            updateLoginTimeAndLocationRequest.addRequestFormParam(ApiKey.USER_LONGITUDE, String.valueOf(longitude));
        }

        if (latitude != 0D) {
            updateLoginTimeAndLocationRequest.addRequestFormParam(ApiKey.USER_LATITUDE, String.valueOf(latitude));
        }

        if (!TextUtils.isEmpty(provinceCode)) {
            updateLoginTimeAndLocationRequest.addRequestFormParam(ApiKey.USER_PROVINCE_CODE, provinceCode);
        }

        if (!TextUtils.isEmpty(cityCode)) {
            updateLoginTimeAndLocationRequest.addRequestFormParam(ApiKey.USER_CITY_CODE, cityCode);
        }

        if (!TextUtils.isEmpty(location)) {
            updateLoginTimeAndLocationRequest.addRequestFormParam(ApiKey.USER_LOCATION, location);
        }
        return updateLoginTimeAndLocationRequest;
    }

    @Override
    public OkHttpRequest fetchReportReasonListRequest() {
        OkHttpRequest fetchReportReasonListRequest = new OkHttpRequest();
        fetchReportReasonListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_REPORT_USER_RECORD_LIST_REASON);
        fetchReportReasonListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_REPORT_USER_RECORD_LIST_REASON);
        return fetchReportReasonListRequest;
    }

    @Override
    public OkHttpRequest saveReportRequest(int beUserId, String reportReasonIdList, String content) {
        OkHttpRequest saveReportRequest = new OkHttpRequest();
        saveReportRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_REPORT_USER_RECORD_SAVE);
        saveReportRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_REPORT_USER_RECORD_SAVE);
        saveReportRequest.addRequestFormParam(ApiKey.USER_BE_USER_ID, String.valueOf(beUserId));
        saveReportRequest.addRequestFormParam(ApiKey.USER_REPORT_REASON_ID_LIST, reportReasonIdList);
        saveReportRequest.addRequestFormParam(ApiKey.USER_CONTENT, content);
        return saveReportRequest;
    }

    @Override
    public OkHttpRequest fetchRecommendDemandListRequest(int pageNum, int serveWayId, int isTrusteeship, int priceMin, int priceMax, int deliverCycleId, int order) {
        OkHttpRequest fetchRecommendDemandListRequest = new OkHttpRequest();
        fetchRecommendDemandListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_DEMAND_RECOMMEND);
        fetchRecommendDemandListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_DEMAND_RECOMMEND);
        fetchRecommendDemandListRequest.addRequestFormParam(ApiKey.COMMON_PAGE_NUM, String.valueOf(pageNum));

        if (serveWayId != -1) {
            fetchRecommendDemandListRequest.addRequestFormParam(ApiKey.USER_SERVE_WAY_ID, String.valueOf(serveWayId));
        }

        if (isTrusteeship != -1) {
            fetchRecommendDemandListRequest.addRequestFormParam(ApiKey.USER_IS_TRUSTEESHIP, String.valueOf(isTrusteeship));
        }

        if (priceMin != -1) {
            fetchRecommendDemandListRequest.addRequestFormParam(ApiKey.USER_PRICE_MIN, String.valueOf(priceMin));
        }

        if (priceMax != -1) {
            fetchRecommendDemandListRequest.addRequestFormParam(ApiKey.USER_PRICE_MAX, String.valueOf(priceMax));
        }

        if (deliverCycleId != -1) {
            fetchRecommendDemandListRequest.addRequestFormParam(ApiKey.USER_DELIVER_CYCLE_ID, String.valueOf(deliverCycleId));
        }

        if (order != -1) {
            fetchRecommendDemandListRequest.addRequestFormParam(ApiKey.USER_ORDER, String.valueOf(order));
        }
        return fetchRecommendDemandListRequest;
    }

    @Override
    public OkHttpRequest searchDemandRequest(Map<String, Object> searchDemandMap) {
        OkHttpRequest searchDemandRequest = new OkHttpRequest();
        searchDemandRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_DEMAND_SEARCH);
        searchDemandRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_DEMAND_SEARCH);
        searchDemandRequest.addRequestFormParam(ApiKey.USER_SOLR_DEMAND_REQUEST_JSON, JSONObject.toJSONString(searchDemandMap, SerializerFeature.DisableCircularReferenceDetect));
        return searchDemandRequest;
    }

    @Override
    public OkHttpRequest fetchDemandAdvancedFilterRequest() {
        OkHttpRequest fetchDemandAdvancedFilterRequest = new OkHttpRequest();
        fetchDemandAdvancedFilterRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_DEMAND_GET_ADVANCED_FILTER);
        fetchDemandAdvancedFilterRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_DEMAND_GET_ADVANCED_FILTER);
        return fetchDemandAdvancedFilterRequest;
    }

    @Override
    public OkHttpRequest collectDemandRequest(int demandId) {
        OkHttpRequest collectDemandRequest = new OkHttpRequest();
        collectDemandRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_STORE_UP_SAVE);
        collectDemandRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_STORE_UP_SAVE);
        collectDemandRequest.addRequestFormParam(ApiKey.USER_DEMAND_ID, String.valueOf(demandId));
        return collectDemandRequest;
    }

    @Override
    public OkHttpRequest cancelCollectDemandRequest(int userId, int demandId) {
        OkHttpRequest cancelCollectDemandRequest = new OkHttpRequest();
        cancelCollectDemandRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_STORE_UP_CANCEL + "/" + String.valueOf(userId) + "/" + String.valueOf(demandId));
        cancelCollectDemandRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_STORE_UP_CANCEL + "/" + String.valueOf(userId) + "/" + String.valueOf(demandId));
        return cancelCollectDemandRequest;
    }

    @Override
    public OkHttpRequest fetchWithdrawDetailsRequest() {
        OkHttpRequest fetchWithdrawDetailsRequest = new OkHttpRequest();
        fetchWithdrawDetailsRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_BANK_CARD_WITHDRAW_DETAILS);
        fetchWithdrawDetailsRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_BANK_CARD_WITHDRAW_DETAILS);
        return fetchWithdrawDetailsRequest;
    }

    @Override
    public OkHttpRequest checkCanWithdrawRequest() {
        OkHttpRequest checkCanWithdrawRequest = new OkHttpRequest();
        checkCanWithdrawRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_BANK_CARD_CHECK_CAN_WITHDRAW);
        checkCanWithdrawRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_BANK_CARD_CHECK_CAN_WITHDRAW);
        return checkCanWithdrawRequest;
    }

    @Override
    public OkHttpRequest withdrawDepositRequest(int bankCardId, int amount, String payPwd, String ip, String withdrawLogId) {
        OkHttpRequest withdrawDepositRequest = new OkHttpRequest();
        withdrawDepositRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_BANK_CARD_WITHDRAW);
        withdrawDepositRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_BANK_CARD_WITHDRAW);
        withdrawDepositRequest.addRequestFormParam(ApiKey.USER_BANK_CARD_ID, String.valueOf(bankCardId));
        withdrawDepositRequest.addRequestFormParam(ApiKey.USER_AMOUNT, String.valueOf(amount));
        withdrawDepositRequest.addRequestFormParam(ApiKey.USER_PAY_PWD, payPwd);
        withdrawDepositRequest.addRequestFormParam(ApiKey.USER_IP, ip);
        withdrawDepositRequest.addRequestFormParam(ApiKey.USER_WITHDRAW_LOG_ID, withdrawLogId);
        return withdrawDepositRequest;
    }

    @Override
    public OkHttpRequest fetchBannerListRequest(int width, int height) {
        OkHttpRequest fetchBannerListRequest = new OkHttpRequest();
        fetchBannerListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GENERAL_DATA_BANNER_LIST);
        fetchBannerListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GENERAL_DATA_BANNER_LIST);
        fetchBannerListRequest.addRequestFormParam(ApiKey.USER_WIDTH, String.valueOf(width));
        fetchBannerListRequest.addRequestFormParam(ApiKey.USER_HEIGHT, String.valueOf(height));
        return fetchBannerListRequest;
    }

    @Override
    public OkHttpRequest fetchRecommendGridListRequest() {
        OkHttpRequest fetchRecommendGridListRequest = new OkHttpRequest();
        fetchRecommendGridListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_LIST_TO_HOME_PAGE);
        fetchRecommendGridListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_LIST_TO_HOME_PAGE);
        return fetchRecommendGridListRequest;
    }

    @Override
    public OkHttpRequest getZhiMaCreditAuthenticationUrlRequest(String certName, String certNo) {
        OkHttpRequest getZhiMaCreditAuthenticationUrlRequest = new OkHttpRequest();
        getZhiMaCreditAuthenticationUrlRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_ZMRZ_GENERATE_ZMRZ_URL);
        getZhiMaCreditAuthenticationUrlRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_ZMRZ_GENERATE_ZMRZ_URL);
        getZhiMaCreditAuthenticationUrlRequest.addRequestFormParam(ApiKey.USER_CERT_NAME, certName);
        getZhiMaCreditAuthenticationUrlRequest.addRequestFormParam(ApiKey.USER_CERT_NO, certNo);
        return getZhiMaCreditAuthenticationUrlRequest;
    }

    @Override
    public OkHttpRequest fetchZhiMaCreditAuthenticationResultRequest(String sign, String params) {
        OkHttpRequest fetchZhiMaCreditAuthenticationResultRequest = new OkHttpRequest();
        fetchZhiMaCreditAuthenticationResultRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_ZMRZ_ANALYSIS_RESULTS);
        fetchZhiMaCreditAuthenticationResultRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_ZMRZ_ANALYSIS_RESULTS);
        fetchZhiMaCreditAuthenticationResultRequest.addRequestFormParam(ApiKey.USER_SIGN, sign);
        fetchZhiMaCreditAuthenticationResultRequest.addRequestFormParam(ApiKey.USER_PARAMS, params);
        return fetchZhiMaCreditAuthenticationResultRequest;
    }

    @Override
    public OkHttpRequest fetchZhiMaCreditAuthenticationDataRequest() {
        OkHttpRequest fetchZhiMaCreditAuthenticationDataRequest = new OkHttpRequest();
        fetchZhiMaCreditAuthenticationDataRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_ZMRZ_GET_LAST_DATA);
        fetchZhiMaCreditAuthenticationDataRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_ZMRZ_GET_LAST_DATA);
        return fetchZhiMaCreditAuthenticationDataRequest;
    }

    @Override
    public OkHttpRequest fetchRecommendDataRequest() {
        OkHttpRequest fetchRecommendDataRequest = new OkHttpRequest();
        fetchRecommendDataRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_GET_NAVIGATION_DATA);
        fetchRecommendDataRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_GET_NAVIGATION_DATA);
        return fetchRecommendDataRequest;
    }

    @Override
    public OkHttpRequest fetchMyCollectionDemandListRequest(int pageNum) {
        OkHttpRequest fetchMyCollectionDemandListRequest = new OkHttpRequest();
        fetchMyCollectionDemandListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_STORE_UP_LIST);
        fetchMyCollectionDemandListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_STORE_UP_LIST);
        fetchMyCollectionDemandListRequest.addRequestFormParam(ApiKey.COMMON_PAGE_NUM, String.valueOf(pageNum));
        return fetchMyCollectionDemandListRequest;
    }

    @Override
    public OkHttpRequest saveKeyWordsSearchLogRequest(int type, String keywords) {
        OkHttpRequest saveKeyWordsSearchLogRequest = new OkHttpRequest();
        saveKeyWordsSearchLogRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_SAVE_KEYWORDS_SEARCH_LOG);
        saveKeyWordsSearchLogRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_SAVE_KEYWORDS_SEARCH_LOG);
        saveKeyWordsSearchLogRequest.addRequestFormParam(ApiKey.USER_TYPE, String.valueOf(type));
        saveKeyWordsSearchLogRequest.addRequestFormParam(ApiKey.USER_KEYWORDS, keywords);
        return saveKeyWordsSearchLogRequest;
    }

    @Override
    public OkHttpRequest getHiddenUserDataRequest() {
        OkHttpRequest getHiddenUserDataRequest = new OkHttpRequest();
        getHiddenUserDataRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_GET_HIDDEN_USER_DATA);
        getHiddenUserDataRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_GET_HIDDEN_USER_DATA);
        return getHiddenUserDataRequest;
    }

    @Override
    public OkHttpRequest sendForgetPaymentPasswordIdentifyCodeRequest() {
        OkHttpRequest sendForgetPaymentPasswordIdentifyCodeRequest = new OkHttpRequest();
        sendForgetPaymentPasswordIdentifyCodeRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_FORGET_PAY_PWD);
        sendForgetPaymentPasswordIdentifyCodeRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_FORGET_PAY_PWD);
        return sendForgetPaymentPasswordIdentifyCodeRequest;
    }

    @Override
    public OkHttpRequest checkForgetPaymentPasswordIdentifyCodeRequest(String captcha) {
        OkHttpRequest checkForgetPaymentPasswordIdentifyCodeRequest = new OkHttpRequest();
        checkForgetPaymentPasswordIdentifyCodeRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_CHECK_FORGET_PAY_PWD_CAPTCHA);
        checkForgetPaymentPasswordIdentifyCodeRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_CHECK_FORGET_PAY_PWD_CAPTCHA);
        checkForgetPaymentPasswordIdentifyCodeRequest.addRequestFormParam(ApiKey.USER_CAPTCHA, captcha);
        return checkForgetPaymentPasswordIdentifyCodeRequest;
    }

    @Override
    public OkHttpRequest checkIdentifyCardRequest(String idCardNo) {
        OkHttpRequest checkIdentifyCardRequest = new OkHttpRequest();
        checkIdentifyCardRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_ZMRZ_CHECK_ID_CARD_NO);
        checkIdentifyCardRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_ZMRZ_CHECK_ID_CARD_NO);
        checkIdentifyCardRequest.addRequestFormParam(ApiKey.USER_ID_CARD_NO, idCardNo);
        return checkIdentifyCardRequest;
    }

    @Override
    public OkHttpRequest resetPaymentPasswordRequest(String captcha, String idCardNo, String payPwd, String confirmPayPwd) {
        OkHttpRequest resetPaymentPasswordRequest = new OkHttpRequest();
        resetPaymentPasswordRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_FORGET_PAY_PWD);
        resetPaymentPasswordRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_FORGET_PAY_PWD);
        resetPaymentPasswordRequest.addRequestFormParam(ApiKey.USER_CAPTCHA, captcha);
        resetPaymentPasswordRequest.addRequestFormParam(ApiKey.USER_ID_CARD_NO, idCardNo);
        resetPaymentPasswordRequest.addRequestFormParam(ApiKey.USER_PAY_PWD, payPwd);
        resetPaymentPasswordRequest.addRequestFormParam(ApiKey.USER_CONFIRM_PAY_PWD, confirmPayPwd);
        return resetPaymentPasswordRequest;
    }

    @Override
    public OkHttpRequest createGesturePasswordRequest(String gesturePwd, String confirmGesturePwd) {
        OkHttpRequest createGesturePasswordRequest = new OkHttpRequest();
        createGesturePasswordRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_CREATE_GESTURE_PWD);
        createGesturePasswordRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_CREATE_GESTURE_PWD);
        createGesturePasswordRequest.addRequestFormParam(ApiKey.USER_GESTURE_PWD, gesturePwd);
        createGesturePasswordRequest.addRequestFormParam(ApiKey.USER_CONFIRM_GESTURE_PWD, confirmGesturePwd);
        return createGesturePasswordRequest;
    }

    @Override
    public OkHttpRequest deleteGesturePasswordRequest() {
        OkHttpRequest deleteGesturePasswordRequest = new OkHttpRequest();
        deleteGesturePasswordRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_CLEAN_GESTURE_PWD);
        deleteGesturePasswordRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_CLEAN_GESTURE_PWD);
        return deleteGesturePasswordRequest;
    }

    @Override
    public OkHttpRequest checkGesturePasswordRequest(String gesturePwd) {
        OkHttpRequest checkGesturePasswordRequest = new OkHttpRequest();
        checkGesturePasswordRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_CHECK_GESTURE_PWD);
        checkGesturePasswordRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_CHECK_GESTURE_PWD);
        checkGesturePasswordRequest.addRequestFormParam(ApiKey.USER_GESTURE_PWD, gesturePwd);
        return checkGesturePasswordRequest;
    }

    @Override
    public OkHttpRequest checkPaymentPasswordRequest(String payPwd) {
        OkHttpRequest checkPaymentPasswordRequest = new OkHttpRequest();
        checkPaymentPasswordRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_CHECK_PAY_PWD);
        checkPaymentPasswordRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_CHECK_PAY_PWD);
        checkPaymentPasswordRequest.addRequestFormParam(ApiKey.USER_PAY_PWD, payPwd);
        return checkPaymentPasswordRequest;
    }

    @Override
    public OkHttpRequest getResumeCompleteRateRequest() {
        OkHttpRequest getResumeCompleteRateRequest = new OkHttpRequest();
        getResumeCompleteRateRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_GET_RESUME_COMPLETE_RATE);
        getResumeCompleteRateRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_GET_RESUME_COMPLETE_RATE);
        return getResumeCompleteRateRequest;
    }

}
