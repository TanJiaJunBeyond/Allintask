package com.allintask.lingdao.model.user;

import com.allintask.lingdao.bean.user.AddEducationalExperienceBean;
import com.allintask.lingdao.bean.user.AddHonorAndQualificationBean;
import com.allintask.lingdao.bean.user.AddWorkExperienceBean;
import com.allintask.lingdao.bean.message.UserInfoListRequestBean;
import com.allintask.lingdao.bean.user.SaveBankCardRequestBean;

import java.util.List;
import java.util.Map;

import cn.tanjiajun.sdk.conn.OkHttpRequest;

/**
 * Created by TanJiaJun on 2018/1/23.
 */

public interface IUserModel {

    OkHttpRequest fetchDefaultPhoneNumberHomeLocationRequest();

    OkHttpRequest fetchPhoneNumberHomeLocationListRequest();

    OkHttpRequest sendQQSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile);

    OkHttpRequest sendWechatSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile);

    OkHttpRequest sendForgetPasswordSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile);

    OkHttpRequest sendLoginSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile);

    OkHttpRequest bindByQQRequest(int mobileCountryCodeId, String mobile, String captcha, String qqUnionId, String ip);

    OkHttpRequest bindByWechatRequest(int mobileCountryCodeId, String mobile, String captcha, String wxUnionId, String ip);

    OkHttpRequest loginByPhoneNumberAndSmsIdentifyCodeRequest(int mobileCountryCodeId, String phoneNumber, String smsIdentifyCode, String ip);

    OkHttpRequest loginByPhoneNumberAndPasswordRequest(int mobileCountryCodeId, String mobile, String password, String ip);

    OkHttpRequest loginByQQRequest(String openId, String unionId, String name, String nickname, String ip);

    OkHttpRequest loginByWechatRequest(String openId, String unionId, String nickname, String ip);

    OkHttpRequest resetLoginPasswordRequest(int mobileCountryCodeId, String mobile, String captcha, String pwd, String confirmPwd);

    OkHttpRequest fetchEducationalInstitutionListRequest();

    OkHttpRequest fetchMajorListRequest();

    OkHttpRequest fetchEducationalBackgroundListRequest();

    OkHttpRequest saveEducationalExperienceRequest(List<AddEducationalExperienceBean> educationExperienceList);

    OkHttpRequest saveWorkExperienceRequest(List<AddWorkExperienceBean> workExperienceList);

    OkHttpRequest saveHonorAndQualificationRequest(List<AddHonorAndQualificationBean> honorAndQualificationList);

    OkHttpRequest completePersonalInformationRequest(String tmpUrl, int width, int height, String format, int cutStartX, int cutStartY, int cutWidth, int cutHeight, String name, int gender, String birthday);

    OkHttpRequest fetchPersonalInfoRequest();

    OkHttpRequest setUserHeadPortraitRequest(int imageId, String tmpUrl, int width, int height, String format, int cutStartX, int cutStartY, int cutWidth, int cutHeight);

    OkHttpRequest setBirthdayRequest(String birthday);

    OkHttpRequest setGenderRequest(int gender);

    OkHttpRequest setNameRequest(String name);

    OkHttpRequest setStartWorkAtRequest(String startWorkAt);

    OkHttpRequest fetchEducationalExperienceRequest(int id);

    OkHttpRequest compileEducationExperienceRequest(int userId, int id, String school, String major, int educationLevelId, String startAt, String endAt);

    OkHttpRequest removeEducationExperienceRequest(int id);

    OkHttpRequest fetchWorkExperienceRequest(int id);

    OkHttpRequest compileWorkExperienceRequest(int userId, int id, String company, String post, String startAt, String endAt);

    OkHttpRequest removeWorkExperienceRequest(int id);

    OkHttpRequest fetchHonorAndQualificationRequest(int id);

    OkHttpRequest compileHonorAndQualificationRequest(int userId, int id, String prize, String issuingAuthority, String gainAt);

    OkHttpRequest removeHonorAndQualificationRequest(int id);

    OkHttpRequest deletePhotoAlbumRequest(int albumId, int imageId, String qualityUrl);

    OkHttpRequest getMyPhotoAlbumSurplusRequest();

    OkHttpRequest getServicePhotoAlbumSurplusRequest();

    OkHttpRequest fetchMyPhotoAlbumListRequest(int userId, int pageNum);

    OkHttpRequest savePhotoAlbumListRequest(String albumRequestJson);

    OkHttpRequest collectUserRequest(int beUserId);

    OkHttpRequest cancelCollectUserRequest(int userId, int beUserId);

    OkHttpRequest likeUserRequest(int beUserId);

    OkHttpRequest cancelLikeUserRequest(int userId, int beUserId);

    OkHttpRequest checkUploadIsSuccessRequest(String flagId);

    OkHttpRequest checkNeedGuideRequest();

    OkHttpRequest fetchMyDataRequest();

    OkHttpRequest logoutRequest();

    OkHttpRequest sendModifyLoginPasswordSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile);

    OkHttpRequest sendModifyPhoneNumberSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile);

    OkHttpRequest sendModifyPaymentPasswordSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile);

    OkHttpRequest sendSetLoginPasswordSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile);

    OkHttpRequest sendSetPaymentPasswordSmsIdentifyCodeRequest(int mobileCountryCodeId, String mobile);

    OkHttpRequest sendModifyMailboxSmsIdentifyCodeRequest(String email);

    OkHttpRequest sendBindMailboxSmsIdentifyCodeRequest(String email);

    OkHttpRequest fetchSettingRequest();

    OkHttpRequest bindQQRequest(String openId, String unionId, String name, String nickname, String ip);

    OkHttpRequest bindWechatRequest(String openId, String unionId, String nickname, String ip);

    OkHttpRequest checkModifyPaymentPasswordSmsIdentifyCodeRequest(String captcha);

    OkHttpRequest checkOldPaymentPasswordRequest(String payPwd);

    OkHttpRequest checkSetPaymentPasswordSmsIdentifyCodeRequest(String captcha);

    OkHttpRequest modifyMailboxRequest(String captcha, String email);

    OkHttpRequest modifyLoginPasswordRequest(String captcha, String password, String confirmPassword);

    OkHttpRequest modifyPhoneNumberRequest(int oldMobileCountryCodeId, String oldMobile, String password, int newMobileCountryCodeId, String newMobile, String captcha);

    OkHttpRequest modifyPaymentPasswordRequest(String captcha, String oldPayPwd, String newPayPwd);

    OkHttpRequest bindMailboxRequest(String captcha, String email);

    OkHttpRequest setLoginPasswordRequest(String captcha, String password, String confirmPassword);

    OkHttpRequest setPaymentPasswordRequest(String captcha, String payPwd);

    OkHttpRequest unbindQQRequest();

    OkHttpRequest unbindWechatRequest();

    OkHttpRequest fetchUserInfoListRequest(UserInfoListRequestBean userInfoListRequestBean);

    OkHttpRequest checkBasicPersonalInformationWholeRequest();

    OkHttpRequest checkPersonalInformationWholeRequest();

    OkHttpRequest updateBasicPersonalInformationRequest(int imageId, String tmpUrl, int width, int height, String format, int cutStartX, int cutStartY, int cutWidth, int cutHeight, String name, int gender, String birthday);

    OkHttpRequest fetchRecommendDetailsUserInformationRequest(int userId);

    OkHttpRequest fetchAddressListRequest();

    OkHttpRequest fetchRecommendListRequest(int pageNum, int serveWayId, int priceMax, int priceMin, int unitId, int order);

    OkHttpRequest fetchAlbumDetailsRequest(int albumId);

    OkHttpRequest fetchMyCollectListRequest(int pageNum);

    OkHttpRequest fetchCategoryListRequest();

    OkHttpRequest fetchDemandCategoryListRequest();

    OkHttpRequest fetchAccountBalanceRequest();

    OkHttpRequest fetchTransactionRecordListRequest(int pageNum);

    OkHttpRequest fetchBankCardDetailsRequest(int id);

    OkHttpRequest saveBankCardRequest(SaveBankCardRequestBean saveBankCardRequestBean);

    OkHttpRequest updateBankCardRequest(String updateBankCardRequestBeanJsonString);

    OkHttpRequest fetchBankInfoListRequest();

    OkHttpRequest fetchBankInfoDetailsRequest(String code);

    OkHttpRequest appUpdateRequest(String version);

    OkHttpRequest fetchAdvancedFilterRequest();

    OkHttpRequest searchRequest(Map<String, Object> searchMap);

    OkHttpRequest updateLoginTimeAndLocationRequest(double longitude, double latitude, String provinceCode, String cityCode, String location);

    OkHttpRequest fetchReportReasonListRequest();

    OkHttpRequest saveReportRequest(int beUserId, String reportReasonIdList, String content);

    OkHttpRequest fetchRecommendDemandListRequest(int pageNum, int serveWayId, int isTrusteeship, int priceMin, int priceMax, int deliverCycleId, int order);

    OkHttpRequest searchDemandRequest(Map<String, Object> searchMap);

    OkHttpRequest fetchDemandAdvancedFilterRequest();

    OkHttpRequest collectDemandRequest(int demandId);

    OkHttpRequest cancelCollectDemandRequest(int userId, int demandId);

    OkHttpRequest fetchWithdrawDetailsRequest();

    OkHttpRequest checkCanWithdrawRequest();

    OkHttpRequest withdrawDepositRequest(int bankCardId, int amount, String payPwd, String ip, String withdrawLogId);

    OkHttpRequest fetchBannerListRequest(int width, int height);

    OkHttpRequest fetchRecommendGridListRequest();

    OkHttpRequest getZhiMaCreditAuthenticationUrlRequest(String certName, String certNo);

    OkHttpRequest fetchZhiMaCreditAuthenticationResultRequest(String sign, String params);

    OkHttpRequest fetchZhiMaCreditAuthenticationDataRequest();

    OkHttpRequest fetchRecommendDataRequest();

    OkHttpRequest fetchMyCollectionDemandListRequest(int pageNum);

    OkHttpRequest saveKeyWordsSearchLogRequest(int type, String keywords);

    OkHttpRequest getHiddenUserDataRequest();

    OkHttpRequest sendForgetPaymentPasswordIdentifyCodeRequest();

    OkHttpRequest checkForgetPaymentPasswordIdentifyCodeRequest(String captcha);

    OkHttpRequest checkIdentifyCardRequest(String idCardNo);

    OkHttpRequest resetPaymentPasswordRequest(String captcha, String idCardNo, String payPwd, String confirmPayPwd);

    OkHttpRequest createGesturePasswordRequest(String gesturePwd, String confirmGesturePwd);

    OkHttpRequest deleteGesturePasswordRequest();

    OkHttpRequest checkGesturePasswordRequest(String gesturePwd);

    OkHttpRequest checkPaymentPasswordRequest(String payPwd);

    OkHttpRequest getResumeCompleteRateRequest();

}
