package com.allintask.lingdao.constant;

/**
 * Created by TanJiaJun on 2017/12/28.
 */

public class ServiceAPIConstant {

    /* BASE URL */
    public static final String API_BASE_URL = CommonConstant.IS_DEBUG_MODE ? "https://ifs1-local.allintask.com" : "https://allintask-app-ifs.allintask.com";
    public static final String SOCKET_HOST = CommonConstant.IS_DEBUG_MODE ? "ifs1-local.allintask.com" : "allintask-app-ifs.allintask.com";
    public static final int SOCKET_PORT = 19008;

    /* WebUrl */
    public static final String USER_AGREEMENT_WEB_URL = "http://ue.allintask.com/agreement/userAgree";
    public static final String STRATEGIES_TO_MAKE_MONEY_WEB_URL = "http://ue.allintask.com/about/makeMoney";
    public static final String ABOUT_US_WEB_URL = "http://ue.allintask.com/about";

    /* 公共管理数据 */
    public static final String REQUEST_API_NAME_GENERAL_DATA_ADDRESS_LIST = "/generaldata/address/list";
    public static final String REQUEST_API_NAME_GENERAL_DATA_EDU_LEVEL_LIST = "/generaldata/eduLevel/list";
    public static final String REQUEST_AP_NAME_GENERAL_DATA_MAJOR_INFO_LIST = "/generaldata/majorInfo/list";
    public static final String REQUEST_API_NAME_GENERAL_DATA_SCHOOL_INFO_LIST = "/generaldata/schoolInfo/list";
    public static final String REQUEST_API_NAME_GENERAL_DATA_VERSION_CONTROL_ANDROID = "/generaldata/versionControl/android";
    public static final String REQUEST_API_NAME_GENERAL_DATA_BANNER_LIST = "/generaldata/banner/list";

    /* 用户中心 */
    public static final String REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_BIND_QQ = "/usercenter/user/message/sms/sendCaptchaToBindQq";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_BIND_WX = "/usercenter/user/message/sms/sendCaptchaToBindWx";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_FORGET_LOGIN_PWD = "/usercenter/user/message/sms/sendCaptchaToForgetLoginPwd";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_LOGIN = "/usercenter/user/message/sms/sendCaptchaToLogin";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_MODIFY_LOGIN_PWD = "/usercenter/user/message/sms/sendCaptchaToModifyLoginPwd";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_MODIFY_MOBILE = "/usercenter/user/message/sms/sendCaptchaToModifyMobile";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_MODIFY_PAY_PWD = "/usercenter/user/message/sms/sendCaptchaToModifyPayPwd";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_SET_LOGIN_PWD = "/usercenter/user/message/sms/sendCaptchaToSetLoginPwd";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_SET_PAY_PWD = "/usercenter/user/message/sms/sendCaptchaToSetPayPwd";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_MAIL_SEND_CAPTCHA_TO_MODIFY_EMAIL = "/usercenter/user/message/mail/sendCaptchaToModifyEmail";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_MAIL_SEND_CAPTCHA_TO_SET_EMAIL = "/usercenter/user/message/mail/sendCaptchaToSetEmail";
    public static final String REQUEST_API_NAME_USER_CENTER_MOBILE_COUNTRY_CODE_GET_DEFAULT = "/usercenter/mobileCountryCode/get/default";
    public static final String REQUEST_API_NAME_USER_CENTER_MOBILE_COUNTRY_CODE_LIST = "/usercenter/mobileCountryCode/list";
    public static final String REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_BIND_BY_QQ_UNION_ID = "/usercenter/accessToken/bindByQqUnionId";
    public static final String REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_BIND_WX_UNION_ID = "/usercenter/accessToken/bindByWxUnionId";
    public static final String REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_GET_BY_MOBILE_AND_CAPTCHA = "/usercenter/accessToken/getByMobileAndCaptcha";
    public static final String REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_GET_BY_MOBILE_AND_PWD = "/usercenter/accessToken/getByMobileAndPwd";
    public static final String REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_GET_BY_QQ = "/usercenter/accessToken/getByQq";
    public static final String REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_GET_BY_WX = "/usercenter/accessToken/getByWx";
    public static final String REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_REFRESH_TOKEN = "/usercenter/accessToken/refreshToken";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_RESET_PWD_BY_CAPTCHA = "/usercenter/user/resetPwdByCaptcha";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_EDU_INFO_SAVE_LIST = "/usercenter/user/eduInfo/saveList";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_WORK_INFO_SAVE_LIST = "/usercenter/user/workInfo/saveList";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_HONOR_INFO_SAVE_LIST = "/usercenter/user/honorInfo/saveList";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SET_BASIS = "/usercenter/user/setBasis";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_PERSONAL_INFO = "/usercenter/user/personalInfo";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SET_AVATAR = "/usercenter/user/setAvatar";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SET_BIRTHDAY = "/usercenter/user/setBirthday";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SET_GENDER = "/usercenter/user/setGender";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SET_NAME = "/usercenter/user/setName";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SET_START_WORK_AT = "/usercenter/user/setStartWorkAt";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_EDU_INFO = "/usercenter/user/eduInfo";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_EDU_INFO_UPDATE = "/usercenter/user/eduInfo/update";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_EDU_INFO_DELETE = "/usercenter/user/eduInfo/delete";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_WORK_INFO = "/usercenter/user/workInfo";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_WORK_INFO_UPDATE = "/usercenter/user/workInfo/update";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_WORK_INFO_DELETE = "/usercenter/user/workInfo/delete";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_HONOR_INFO = "/usercenter/user/honorInfo";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_HONOR_INFO_UPDATE = "/usercenter/user/honorInfo/update";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_HONOR_INFO_DELETE = "/usercenter/user/honorInfo/delete";
    public static final String REQUEST_API_NAME_USER_CENTER_STORE_UP_CANCEL = "/usercenter/storeUp/cancel";
    public static final String REQUEST_API_NAME_USER_CENTER_STORE_UP_SAVE = "/usercenter/storeUp/save";
    public static final String REQUEST_API_NAME_USER_CENTER_LIKE_CANCEL = "/usercenter/like/cancel";
    public static final String REQUEST_API_NAME_USER_CENTER_LIKE_SAVE = "/usercenter/like/save";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_MINE = "/usercenter/user/mine";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_INFO_TO_SERVE_DETAILS = "/usercenter/user/info/toServeDetails";
    public static final String REQUEST_API_NAME_USER_CENTER_ACCESS_TOKEN_LOGOUT = "/usercenter/accessToken/logout";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SETTING = "/usercenter/user/setting";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SETTING_BIND_QQ = "/usercenter/user/setting/bindQq";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SETTING_BIND_WX = "/usercenter/user/setting/bindWx";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SETTING_CHECK_MODIFY_PAY_PWD_CAPTCHA = "/usercenter/user/setting/checkModifyPayPwdCaptcha";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SETTING_CHECK_PAY_PWD = "/usercenter/user/setting/checkPayPwd";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SETTING_CHECK_SET_PAY_PWD_CAPTCHA = "/usercenter/user/setting/checkSetPayPwdCaptcha";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SETTING_MODIFY_EMAIL = "/usercenter/user/setting/modifyEmail";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SETTING_MODIFY_LOGIN_PASSWORD = "/usercenter/user/setting/modifyLoginPassword";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SETTING_MODIFY_MOBILE = "/usercenter/user/setting/modifyMobile";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SETTING_MODIFY_PAY_PWD = "/usercenter/user/setting/modifyPayPwd";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SETTING_SET_EMAIL = "/usercenter/user/setting/setEmail";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SETTING_SET_LOGIN_PASSWORD = "/usercenter/user/setting/setLoginPassword";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SETTING_SET_PAY_PWD = "/usercenter/user/setting/setPayPwd";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SETTING_UNBIND_QQ = "/usercenter/user/setting/unbindQQ";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SETTING_UNBIND_WX = "/usercenter/user/setting/unbindWx";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_INFO_GET_USER_IM_MSG = "/usercenter/user/info/getUserImMsg";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_CHECK_BASIC_MSG = "/usercenter/user/checkBasicMsg";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_CHECK_PERSONAL_INFO = "/usercenter/user/checkPersonalInfo";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_UPDATE_BASIS = "/usercenter/user/updateBasis";
    public static final String REQUEST_API_NAME_USER_CENTER_STORE_UP_LIST = "/usercenter/storeUp/list";
    public static final String REQUEST_API_NAME_USER_CENTER_BANK_CARD_GET = "/usercenter/bankCard/get";
    public static final String REQUEST_API_NAME_USER_CENTER_BANK_CARD_SAVE = "/usercenter/bankCard/save";
    public static final String REQUEST_API_NAME_USER_CENTER_BANK_CARD_UPDATE = "/usercenter/bankCard/update";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_UPDATE_LOGIN_TIME_AND_LOCATION = "/usercenter/user/updateLoginTimeAndLocation";
    public static final String REQUEST_API_NAME_USER_CENTER_REPORT_USER_RECORD_LIST_REASON = "/usercenter/reportUserRecord/list/reason";
    public static final String REQUEST_API_NAME_USER_CENTER_REPORT_USER_RECORD_SAVE = "/usercenter/reportUserRecord/save";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_INFO_TO_DEMAND_DETAILS = "/usercenter/user/info/toDemandDetails";
    public static final String REQUEST_API_NAME_USER_CENTER_BANK_CARD_WITHDRAW_DETAILS = "/usercenter/bankCard/withdraw/details";
    public static final String REQUEST_API_NAME_USER_CENTER_BANK_CARD_CHECK_CAN_WITHDRAW = "/usercenter/bankCard/checkCanWithdraw";
    public static final String REQUEST_API_NAME_USER_CENTER_BANK_CARD_WITHDRAW = "/usercenter/bankCard/withdraw";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_ZMRZ_GENERATE_ZMRZ_URL = "/usercenter/userZmrz/generateZmrzUrl";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_ZMRZ_ANALYSIS_RESULTS = "/usercenter/userZmrz/analysisResults";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_ZMRZ_GET_LAST_DATA = "/usercenter/userZmrz/getLastData";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_CHECK_FORGET_PAY_PWD_CAPTCHA = "/usercenter/userSecurityManage/checkForgetPayPwdCaptcha";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_CHECK_GESTURE_PWD = "/usercenter/userSecurityManage/checkGesturePwd";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_CHECK_PAY_PWD = "/usercenter/userSecurityManage/checkPayPwd";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_CLEAN_GESTURE_PWD = "/usercenter/userSecurityManage/cleanGesturePwd";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_CREATE_GESTURE_PWD = "/usercenter/userSecurityManage/createGesturePwd";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_FORGET_PAY_PWD = "/usercenter/userSecurityManage/forgetPayPwd";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_GET_HIDDEN_USER_DATA = "/usercenter/userSecurityManage/getHiddenUserData";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_MESSAGE_SMS_SEND_CAPTCHA_TO_FORGET_PAY_PWD = "/usercenter/user/message/sms/sendCaptchaToForgetPayPwd";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_ZMRZ_CHECK_ID_CARD_NO = "/usercenter/userZmrz/checkIdCardNo";
    public static final String REQUEST_API_NAME_USER_CENTER_USER_GET_RESUME_COMPLETE_RATE = "/usercenter/user/getResumeCompleteRate";

    /* 产品 */
    public static final String REQUEST_API_NAME_GOODS_CATEGORY_LIST_HOT = "/goods/category/listHot";
    public static final String REQUEST_API_NAME_GOODS_SERVE_EXPERIENCE = "/goods/serve/experience";
    public static final String REQUEST_API_NAME_GOODS_DEMAND_EXPERIENCE = "/goods/demand/experience";
    public static final String REQUEST_API_NAME_GOODS_CATEGORY_LIST_TO_SERVE = "/goods/category/listToServe";
    public static final String REQUEST_API_NAME_GOODS_CATEGORY_V1_LIST_SERVE_WAY_AND_PRICE_UNIT = "/goods/category/v1/listServeWayAndPriceUnit";
    public static final String REQUEST_API_NAME_GOODS_CATEGORY_LIST_SERVE_IS_PUBLISH = "/goods/serve/isPublish";
    public static final String REQUEST_API_NAME_GOODS_CATEGORY_LIST_SERVE_PUBLISH = "/goods/serve/publish";
    public static final String REQUEST_API_NAME_GOODS_GET_ID_TO_CHINESE = "/goods/getIdToChineseData";
    public static final String REQUEST_API_NAME_GOODS_SERVE_MY_SERVE_LIST = "/goods/serve/myServe/list";
    public static final String REQUEST_API_NAME_GOODS_SERVE_GO_ONLINE = "/goods/serve/goOnline";
    public static final String REQUEST_API_NAME_GOODS_SERVE_GO_OFFLINE = "/goods/serve/goOffline";
    public static final String REQUEST_API_NAME_GOODS_SERVE_DELETE = "/goods/serve/delete";
    public static final String REQUEST_API_NAME_GOODS_SERVE_UPDATE = "/goods/serve/update";
    public static final String REQUEST_API_NAME_GOODS_SERVE_GET = "/goods/serve/get";
    public static final String REQUEST_API_NAME_GOODS_DEMAND_V1_GET = "/goods/demand/v1/get";
    public static final String REQUEST_API_NAME_GOODS_DEMAND_PUBLISH = "/goods/demand/publish";
    public static final String REQUEST_API_NAME_GOODS_DELIVER_CYCLE_LIST = "/goods/deliverCycle/list";
    public static final String REQUEST_API_NAME_GOODS_ALBUM_DELETE = "/goods/album/delete";
    public static final String REQUEST_API_NAME_GOODS_ALBUM_GET_PERSONAL_ALBUM_SURPLUS = "/goods/album/getPersonalAlbumSurplus";
    public static final String REQUEST_API_NAME_GOODS_ALBUM_GET_SERVE_ALBUM_SURPLUS = "/goods/album/getServeAlbumSurplus";
    public static final String REQUEST_API_NAME_GOODS_ALBUM_LIST = "/goods/album/list";
    public static final String REQUEST_API_NAME_GOODS_ALBUM_SAVE_LIST = "/goods/album/saveList";
    public static final String REQUEST_API_NAME_GOODS_SERVE_MATCH_WAITING_BID_LIST = "/goods/serveMatch/waitingBid/list";
    public static final String REQUEST_API_NAME_GOODS_SERVE_BID_CHANGE_PRICE = "/goods/serveBid/changePrice";
    public static final String REQUEST_API_MAME_GOODS_SERVE_BID_LIST_TO_BUYER_VALID = "/goods/serveBid/list/to/buyer/valid";
    public static final String REQUEST_API_NAME_GOODS_SERVE_BID_LIST_TO_SELLER_VALID = "/goods/serveBid/list/to/seller/valid";
    public static final String REQUEST_API_NAME_GOODS_SERVE_BID_SAVE = "/goods/serveBid/save";
    public static final String REQUEST_API_NAME_GOODS_DEMAND_GET_DEMAND_MANAGE_LIST = "/goods/demand/getDemandManageList";
    public static final String REQUEST_API_NAME_GOODS_SERVE_BID_LIST_TO_BUYER_EXPIRE = "/goods/serveBid/list/to/buyer/expire";
    public static final String REQUEST_API_NAME_GOODS_SERVE_BID_LIST_TO_SELLER_EXPIRE = "/goods/serveBid/list/to/seller/expire";
    public static final String REQUEST_API_NAME_GOODS_DEMAND_DETAILS_TO_BUYER = "/goods/demand/details/toBuyer";
    public static final String REQUEST_API_NAME_GOODS_CHECK_NEED_GUIDE = "/goods/checkNeedGuide";
    public static final String REQUEST_API_NAME_GOODS_DEMAND_DETAILS_TO_SELLER = "/goods/demand/details/toSeller";
    public static final String REQUEST_API_NAME_GOODS_SERVE_LIST_PUBLISH = "/goods/serve/list/publish";
    public static final String REQUEST_API_NAME_GOODS_SERVE_MATCH_RADAR_LIST = "/goods/serveMatch/radar/list";
    public static final String REQUEST_API_NAME_GOODS_GET_SERVE_ALBUM_SURPLUS = "/goods/album/getServeAlbumSurplus";
    public static final String REQUEST_API_NAME_GOODS_SERVE_LIST_TO_SERVE_DETAILS = "/goods/serve/list/toServeDetails";
    public static final String REQUEST_API_NAME_GOODS_SOLR_SERVE_RECOMMEND = "/goods/solrServe/recommend";
    public static final String REQUEST_API_NAME_GOODS_ALBUM_GET = "/goods/album/get";
    public static final String REQUEST_API_NAME_GOODS_LIST_TO_SEARCH_SERVE = "/goods/category/listToSearchServe";
    public static final String REQUEST_API_NAME_GOODS_LIST_TO_SEARCH_DEMAND = "/goods/category/listToSearchDemand";
    public static final String REQUEST_API_NAME_GOODS_SOLR_SERVE_GET_ADVANCED_FILTER = "/goods/solrServe/getAdvancedFilter";
    public static final String REQUEST_API_NAME_GOODS_VOICE_EXAMPLE_GET_SERVE_VOICE_EXAMPLE = "/goods/voiceExample/getServeVoiceExample";
    public static final String REQUEST_API_NAME_GOODS_SOLR_DEMAND_GET_ADVANCED_FILTER = "/goods/solrDemand/getAdvancedFilter";
    public static final String REQUEST_API_NAME_GOODS_SOLR_DEMAND_RECOMMEND = "/goods/solrDemand/recommend";
    public static final String REQUEST_API_NAME_GOODS_DEMAND_DETAILS = "/goods/demand/details";
    public static final String REQUEST_API_NAME_GOODS_DEMAND_STORE_UP_SAVE = "/goods/demandStoreUp/save";
    public static final String REQUEST_API_NAME_GOODS_DEMAND_STORE_UP_CANCEL = "/goods/demandStoreUp/cancel";
    public static final String REQUEST_API_NAME_GOODS_DEMAND_GO_ONLINE = "/goods/demand/goOnline";
    public static final String REQUEST_API_NAME_GOODS_DEMAND_GO_OFFLINE = "/goods/demand/goOffline";
    public static final String REQUEST_API_NAME_GOODS_DEMAND_DELETE = "/goods/demand/delete";
    public static final String REQUEST_API_NAME_GOODS_SERVE_GET_BY_CATEGORY_ID = "/goods/serve/getByCategoryId";
    public static final String REQUEST_API_NAME_GOODS_SOLR_SERVE_SEARCH = "/goods/solrServe/search";
    public static final String REQUEST_API_NAME_GOODS_SOLR_DEMAND_SEARCH = "/goods/solrDemand/search";
    public static final String REQUEST_API_NAME_GOODS_CATEGORY_LIST_TO_DEMAND = "/goods/category/listToDemand";
    public static final String REQUEST_API_NAME_GOODS_DEMAND_V1_PUBLISH = "/goods/demand/v1/publish";
    public static final String REQUEST_API_NAME_GOODS_CATEGORY_LIST_TO_HOME_PAGE = "/goods/category/listToHomePage";
    public static final String REQUEST_API_NAME_GOODS_DEMAND_V1_UPDATE = "/goods/demand/v1/update";
    public static final String REQUEST_API_NAME_GOODS_DEMAND_V1_PAY_PUBLISH = "/goods/demand/v1/payPublish";
    public static final String REQUEST_API_NAME_GOODS_GET_NAVIGATION_DATA = "/goods/getNavigationData";
    public static final String REQUEST_API_NAME_GOODS_DEMAND_STORE_UP_LIST = "/goods/demandStoreUp/list";
    public static final String REQUEST_API_NAME_GOODS_SOLR_SAVE_KEYWORDS_SEARCH_LOG = "/goods/solr/saveKeywordsSearchLog";

    /* 订单中心 */
    public static final String REQUEST_API_NAME_ORDER_CENTER_ARBITRATION_APPLY_SAVE = "/ordercenter/arbitrationApply/save";
    public static final String REQUEST_API_NAME_ORDER_CENTER_ORDER_PAY_ALIPAY_GET_BUY_SERVE_BID_ORDER_STRING = "/ordercenter/orderPay/alipay/getBuyServeBidOrderString";
    public static final String REQUEST_API_NAME_ORDER_CENTER_ORDER_LIST_TO_BUYER_ING = "/ordercenter/order/list/to/buyer/ing";
    public static final String REQUEST_API_NAME_ORDER_CENTER_ORDER_OPERATE_CONFIRM_COMPLETE_WORK = "/ordercenter/order/operate/confirmCompleteWork";
    public static final String REQUEST_API_NAME_ORDER_CENTER_ORDER_OPERATE_CONFIRM_START_WORK = "/ordercenter/order/operate/confirmStartWork";
    public static final String REQUEST_API_NAME_ORDER_CENTER_ORDER_OPERATE_DELAY_COMPLETE_WORD = "/ordercenter/order/operate/delayCompleteWork";
    public static final String REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_BUYER_SAVE = "/ordercenter/evaluation/toBuyer/save";
    public static final String REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_SELLER_SAVE = "/ordercenter/evaluation/toSeller/save";
    public static final String REQUEST_API_NAME_ORDER_CENTER_REFUND_APPLY_ACCEPT = "/ordercenter/refundApply/accept";
    public static final String REQUEST_API_NAME_ORDER_CENTER_REFUND_APPLY_REFUSE = "/ordercenter/refundApply/refuse";
    public static final String REQUEST_API_NAME_ORDER_CENTER_REFUND_APPLY_SAVE = "/ordercenter/refundApply/save";
    public static final String REQUEST_API_NAME_ORDER_CENTER_ORDER_LIST_TO_BUYER_COMPLETE = "/ordercenter/order/list/to/buyer/complete";
    public static final String REQUEST_API_NAME_ORDER_CENTER_ORDER_LIST_TO_SELLER_ING = "/ordercenter/order/list/to/seller/ing";
    public static final String REQUEST_API_NAME_ORDER_CENTER_ORDER_LIST_TO_SELLER_COMPLETE = "/ordercenter/order/list/to/seller/complete";
    public static final String REQUEST_API_NAME_ORDER_CENTER_ORDER_MSG_TO_DEMAND = "/ordercenter/order/msg/toDemand";
    public static final String REQUEST_API_NAME_ORDER_CENTER_OPERATE_REASON_LIST_BUYER_ARBITRATION_APPLY = "/ordercenter/operateReason/list/buyerArbitrationApply";
    public static final String REQUEST_API_NAME_ORDER_CENTER_OPERATE_REASON_LIST_BUYER_CANCEL_BEFORE_START_WORK = "/ordercenter/operateReason/list/buyerCancelBeforeStartWork";
    public static final String REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_BUYER_DETAILS = "/ordercenter/evaluation/toBuyer/details";
    public static final String REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_SELLER_DETAILS = "/ordercenter/evaluation/toSeller/details";
    public static final String REQUEST_API_NAME_ORDER_CENTER_ORDER_OPERATE_REFUND = "/ordercenter/order/operate/refund";
    public static final String REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_SELLER_TO_SERVE_DETAILS = "/ordercenter/evaluation/toSeller/toServeDetails";
    public static final String REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_SELLER_LIST = "/ordercenter/evaluation/toSeller/list";
    public static final String REQUEST_API_NAME_ORDER_CENTER_ORDER_PAY_LING_DAO_GET_BUY_SERVE_BID_ORDER_STRING = "/ordercenter/orderPay/lingDao/getBuyServeBidOrderString";
    public static final String REQUEST_API_NAME_ORDER_CENTER_SALARY_TRUSTEESHIP_PAY_SALARY_TRUSTEESHIP_SURPLUS = "/ordercenter/salaryTrusteeship/salaryTrusteeshipSurplus";
    public static final String REQUEST_API_NAME_ORDER_CENTER_SALARY_TRUSTEESHIP_ALIPAY_GET_SALARY_TRUSEESHIP_ORDER_STRING = "/ordercenter/salaryTrusteeshipPay/alipay/getSalaryTrusteeshipOrderString";
    public static final String REQUEST_API_NAME_ORDER_CENTER_SALARY_TRUSTEESHIP_LING_DAO_GET_SALARY_TRUSTEESHIP_ORDER_STRING = "/ordercenter/salaryTrusteeshipPay/lingDao/getSalaryTrusteeshipOrderString";
    public static final String REQUEST_API_NAME_ORDER_CENTER_ORDER_PAY_SALARY_TRUSTEESHIP_GET_BUY_SERVE_BID_ORDER_STRING = "/ordercenter/orderPay/salaryTrusteeship/getBuyServeBidOrderString";
    public static final String REQUEST_API_NAME_ORDER_CENTER_ORDER_PAY_ALIPAY_GET_PAY_PUBLISH_DEMAND_ORDER_STRING = "/ordercenter/orderPay/alipay/getPayPublishDemandOrderString";
    public static final String REQUEST_API_NAME_ORDER_CENTER_ORDER_PAY_LING_DAO_GET_PAY_PUBLISH_DEMAND_ORDER_STRING = "/ordercenter/orderPay/lingDao/getPayPublishDemandOrderString";

    /* 财务中心 */
    public static final String REQUEST_API_NAME_FINANCE_CENTER_PAY_CHANNEL_LIST = "/financecenter/payChannel/list";
    public static final String REQUEST_API_NAME_FINANCE_CENTER_WALLET_DETAILS = "/financecenter/wallet/details";
    public static final String REQUEST_API_NAME_FINANCE_CENTER_BILL_LIST = "/financecenter/bill/list";
    public static final String REQUEST_API_NAME_FINANCE_CENTER_GHT_BANK_INFO_GET = "/financecenter/ghtBankInfo/get";
    public static final String REQUEST_API_NAME_FINANCE_CENTER_GHT_BANK_INFO_LIST = "/financecenter/ghtBankInfo/list";
    public static final String REQUEST_API_NAME_FINANCE_CENTER_LING_DAO_PAY_PAY = "/financecenter/lingDaoPay/pay";
    public static final String REQUEST_API_NAME_FINANCE_CENTER_SALARY_TRUSTEESHIP_PAY_PAY = "/financecenter/salaryTrusteeshipPay/pay";
    public static final String REQUEST_API_NAME_FINANCE_CENTER_LING_DAO_PAY_V1_PAY = "/financecenter/lingDaoPay/v1/pay";

    /* 上传 */
    public static final String REQUEST_API_NAME_FILE_OSS_A_LI_YUN_IMAGE_URL_RETURN = "/fileoss/aLiYunImageUrl/return";
    public static final String REQUEST_API_NAME_FILE_OSS_VOICE_UPLOAD = "/fileoss/voice/upload";

}
