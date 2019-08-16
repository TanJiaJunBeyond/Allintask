package com.allintask.lingdao.constant;

import java.text.SimpleDateFormat;

/**
 * Created by TanJiaJun on 2017/12/28.
 */

public class CommonConstant {

    public static final boolean IS_DEBUG_MODE = true;

    public static final boolean IS_OPEN_UMENG = true;

    public static final String DES_KEY = "Allintask";

    public static final String AES_KEY = "a5a4da37b09b4bd7ad2c34d6ced2b409";
    public static final String WITHDRAW_DEPOSIT_AES_KEY = "f599f76ace9046e0a4e5a39d494187cf";

    public static final String EMCHAT_APP_KEY = CommonConstant.IS_DEBUG_MODE ? "1128171031115193#allintasktest" : "1128171031115193#allintask";

    public static final String WECHAT_APP_ID = "wx2c61e519d5da67c5";
    public static final String WECHAT_APP_SECRET = "fb1eb1bd2317780470844262bc231595";

    public static final String QQ_APP_ID = "1106469899";
    public static final String QQ_APP_SECRET = "7MmxPRx0C5lUzcqh";

    public static final String WECHAT_MINI_APPS_USERNAME = "gh_2016552758bb";
    public static final String WECHAT_MINI_APPS_SERVICE_PATH = "pages/serverDetail/detail/detail?userId={userId}&serveId={serveId}";
    public static final String WECHAT_MINI_APPS_DEMAND_PATH = "pages/requireDetail/detail/detail?userId={userId}&id={demandId}";

    public static final String IS_FIRST = "IS_FIRST";

    public static final String IS_FIRST_GUIDE = "IS_FIRST_GUIDE";

    public static final String IS_FIRST_ENTER_DEMAND_MANAGEMENT = "IS_FIRST_ENTER_DEMAND_MANAGEMENT";
    public static final String IS_FIRST_ENTER_SERVICE_MANAGEMENT = "IS_FIRST_ENTER_SERVICE_MANAGEMENT";
    public static final String IS_FIRST_SHOW_ADD_PHOTOS_LAYOUT = "IS_FIRST_SHOW_ADD_PHOTOS_LAYOUT";
    public static final String IS_FIRST_SHOW_COMPILE_SERVICE_LAYOUT = "IS_FIRST_SHOW_COMPILE_SERVICE_LAYOUT";
    public static final String IS_USE_FINGERPRINT_VERIFY = "IS_USE_FINGERPRINT_VERIFY";

    public static final int REQUEST_CODE = 0;
    public static final int RESULT_CODE = 1;

    public static final int REFRESH_RESULT_CODE = 100;

    public static final int DEFAULT_PAGE_NUMBER = 1;

    public static final SimpleDateFormat refreshTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat serverTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    public static final SimpleDateFormat dateServerFormat = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat commonDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat commonTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    public static final SimpleDateFormat subscribeTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static final String NEWLINE = "{newline}";

    public static final String ACTION_LOGOUT = "ACTION_LOGOUT";
    public static final String ACTION_REFRESH_FRAGMENT = "ACTION_REFRESH_FRAGMENT";
    public static final String ACTION_REFRESH = "ACTION_REFRESH";

    public static final String EXTRA_REFRESH_FRAGMENT = "EXTRA_REFRESH_FRAGMENT";

    public static final int MSG_UPDATE_VALID_CODE = 101;

    // 登录
    public static final int LOGIN_TYPE_LOGIN_BY_PHONE_NUMBER_AND_SMS_IDENTIFY_CODE = 0;
    public static final int LOGIN_TYPE_LOGIN_BY_PHONE_NUMBER_AND_PASSWORD = 1;
    public static final int LOGIN_TYPE_LOGIN_BY_WECHAT = 2;
    public static final int LOGIN_TYPE_LOGIN_BY_QQ = 3;

    // 完善个人资料
    public static final String EXTRA_COMPLETE_PERSONAL_INFORMATION_TYPE = "EXTRA_COMPLETE_PERSONAL_INFORMATION_TYPE";

    public static final int COMPLETE_PERSONAL_INFORMATION_TYPE_SET = 0;
    public static final int COMPLETE_PERSONAL_INFORMATION_TYPE_COMPLETE = 1;

    public static final int MALE = 1;
    public static final int FEMALE = 2;

    public static final String EXTRA_PUBLISH_TYPE = "EXTRA_PUBLISH_TYPE";

    public static final int PUBLISH_TYPE_PUBLISH_SERVICE = 0;
    public static final int PUBLISH_TYPE_PUBLISH_DEMAND = 1;

    // 首页
    public static final String EXTRA_FRAGMENT_NAME = "EXTRA_FRAGMENT_NAME";

    public static final String RECOMMEND_FRAGMENT = "RECOMMEND_FRAGMENT";
    public static final String DEMAND_MANAGEMENT_FRAGMENT = "DEMAND_MANAGEMENT_FRAGMENT";
    public static final String MESSAGE_FRAGMENT = "MESSAGE_FRAGMENT";
    public static final String SERVICE_MANAGEMENT_FRAGMENT = "SERVICE_MANAGEMENT_FRAGMENT";
    public static final String MINE_FRAGMENT = "MINE_FRAGMENT";

    public static final String EXTRA_FRAGMENT_STATUS = "EXTRA_FRAGMENT_STATUS";

    public static final String EXTRA_RECOMMEND_GRID_LIST = "EXTRA_RECOMMEND_GRID_LIST";

    // 推荐
    public static final String WIDTH = "{width}";
    public static final String HEIGHT = "{height}";

    public static final int INTELLIGENT_RECOMMENDATION = 0;
    public static final int PUBLISH_RECENTLY = 1;
    public static final int ADVANCED_FILTER = 2;

    public static final int EDUCATIONAL_EXPERIENCE = 0;
    public static final int WORK_EXPERIENCE = 1;
    public static final int HONOR_AND_QUALIFICATION = 2;

    public static final int RECOMMEND_STATUS_DEFAULT = -1;
    public static final int SERVICE_LOBBY = 0;
    public static final int DEMAND_LOBBY = 1;

    // 消息
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";

    public static final String MESSAGE_TYPE_RECALL = "message_recall";

    public static final String MESSAGE_ATTR_IS_BIG_EXPRESSION = "em_is_big_expression";
    public static final String MESSAGE_ATTR_EXPRESSION_ID = "em_expression_id";

    public static final String MESSAGE_ATTR_AT_MSG = "em_at_list";
    public static final String MESSAGE_ATTR_VALUE_AT_MSG_ALL = "ALL";

    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;
    public static final int CHATTYPE_CHATROOM = 3;

    public static final String EXTRA_CHAT_TYPE = "chatType";
    public static final String EXTRA_USER_ID = "userId";
    public static final String EXTRA_SEND_USER_ID = "sendUserId";

    public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
    public static final String GROUP_USERNAME = "item_groups";
    public static final String CHAT_ROOM = "item_chatroom";
    public static final String ACCOUNT_REMOVED = "account_removed";
    public static final String ACCOUNT_CONFLICT = "conflict";
    public static final String ACCOUNT_FORBIDDEN = "user_forbidden";
    public static final String ACCOUNT_KICKED_BY_CHANGE_PASSWORD = "kicked_by_change_password";
    public static final String ACCOUNT_KICKED_BY_OTHER_DEVICE = "kicked_by_another_device";
    public static final String CHAT_ROBOT = "item_robots";
    public static final String MESSAGE_ATTR_ROBOT_MSGTYPE = "msgtype";
    public static final String ACTION_GROUP_CHANAGED = "action_group_changed";
    public static final String ACTION_CONTACT_CHANAGED = "action_contact_changed";

    public static final String MESSAGE_ATTRIBUTE_TYPE = "type";

    public static final String MESSAGE_ATTRIBUTE_TYPE_SERVICE = "service";
    public static final String MESSAGE_ATTRIBUTE_TYPE_BID = "bid";
    public static final String MESSAGE_ATTRIBUTE_TYPE_MESSAGE = "message";

    public static final String MESSAGE_ATTRIBUTE_SERVICE_ID = "serviceId";
    public static final String MESSAGE_ATTRIBUTE_SERVE_TYPE = "serve_type";
    public static final String MESSAGE_ATTRIBUTE_SERVE_SERVICE_MODE = "serve_service_mode";
    public static final String MESSAGE_ATTRIBUTE_SERVE_SERVICE_PRICE = "serve_service_price";
    public static final String MESSAGE_ATTRIBUTE_SERVE_INTRO = "serve_intro";

    public static final int MESSAGE_STATUS_BID_SUCCESS = 1;
    public static final int MESSAGE_STATUS_MODIFY_PRICE = 2;
    public static final int MESSAGE_STATUS_PAY_SUCCESS = 3;
    public static final int MESSAGE_STATUS_REFUNDED = 4;
    public static final int MESSAGE_STATUS_START_WORK = 5;
    public static final int MESSAGE_STATUS_APPLY_FOR_REFUND = 6;
    public static final int MESSAGE_STATUS_AGREE_REFUND = 7;
    public static final int MESSAGE_STATUS_REJECT_REFUND = 8;
    public static final int MESSAGE_STATUS_APPLY_FOR_ARBITRAMENT = 9;
    public static final int MESSAGE_STATUS_DELAY = 10;
    public static final int MESSAGE_STATUS_ORDER_FINISHED = 11;
    public static final int MESSAGE_STATUS_EVALUATED_FACILITATOR = 12;
    public static final int MESSAGE_STATUS_EVALUATED_EMPLOYER = 13;
    public static final int MESSAGE_STATUS_RECOMMEND = 14;
    public static final int MESSAGE_STATUS_EMPLOY_SUCCESS = 15;

    public static final int MESSAGE_STATUS_USER_TYPE_FACILITATOR = 0;
    public static final int MESSAGE_STATUS_USER_TYPE_EMPLOYER = 1;

    public static final String MESSAGE_ATTRIBUTE_DEMAND_TYPE = "demandType";
    public static final String MESSAGE_ATTRIBUTE_DEMAND_ID = "demandId";
    public static final String MESSAGE_ATTRIBUTE_DEMAND_TYPE_TITLE = "demandTypeTitle";
    public static final String MESSAGE_ATTRIBUTE_DEMAND_CONTENT_SENDER = "demandContentSender";
    public static final String MESSAGE_ATTRIBUTE_DEMAND_CONTENT_RECEIVE = "demandContentReceive";
    public static final String MESSAGE_ATTRIBUTE_DEMAND_TITLE_SENDER = "demandTitleSender";
    public static final String MESSAGE_ATTRIBUTE_DEMAND_TITLE_RECEIVE = "demandTitleReceive";
    public static final String MESSAGE_ATTRIBUTE_ORDER_ID = "orderId";
    public static final String MESSAGE_ATTRIBUTE_SERVICE_STATUS = "serviceStatus";
    public static final String MESSAGE_ATTRIBUTE_USER_TYPE = "userType";
    public static final String MESSAGE_ATTRIBUTE_DEMAND_CHANGED_PRICE = "demandChangedPrice";

    // 需求
    public static final String EXTRA_DEMAND_ID = "EXTRA_DEMAND_ID";
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_DEMAND_STATUS = "EXTRA_DEMAND_STATUS";
    public static final String EXTRA_DEMAND_USER_HEAD_PORTRAIT_URL = "EXTRA_DEMAND_USER_HEAD_PORTRAIT_URL";

    public static final int DEMAND_STATUS_DEFAULT = -1;
    public static final int DEMAND_STATUS_IN_THE_BIDDING = 0;
    public static final int DEMAND_STATUS_UNDERWAY = 1;
    public static final int DEMAND_STATUS_COMPLETED = 2;
    public static final int DEMAND_STATUS_EXPIRED = 3;

    public static final String ACTION_DEMAND_STATUS = "ACTION_DEMAND_STATUS";

    public static final String DEMAND_STATUS_IN_THE_BIDDING_STRING = "bidding";
    public static final String DEMAND_STATUS_UNDERWAY_STRING = "conducting";
    public static final String DEMAND_STATUS_COMPLETED_STRING = "complete";
    public static final String DEMAND_STATUS_EXPIRED_STRING = "expire";

    public static final String EXTRA_PUBLISH_DEMAND_TYPE = "EXTRA_PUBLISH_DEMAND_TYPE";

    public static final int EXTRA_PUBLISH_DEMAND_TYPE_COMMON = 0;
    public static final int EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE = 1;
    public static final int EXTRA_PUBLISH_DEMAND_TYPE_UPDATE = 2;

    public static final String EXTRA_RECOMMEND_MORE_STATUS = "EXTRA_RECOMMEND_MORE_STATUS";

    public static final int DEMAND_IS_NOT_SHARE = 0;
    public static final int DEMAND_IS_SHARE = 1;

    public static final String EXTRA_SERVICE_CATEGORY_POSITION = "EXTRA_SERVICE_CATEGORY_POSITION";
    public static final String EXTRA_SERVICE_CATEGORY = "EXTRA_SERVICE_CATEGORY";

    public static final int LOOK_OVER_WIN_THE_BIDDING = 0;
    public static final int SELECT_MORE_BIDDING = 1;

    public static final int LOOK_OVER_COMPLETE = 0;
    public static final int LOOK_OVER_BIDDER = 1;

    public static final int INTELLIGENT_MATCH_INFORM_SERVICE_PROVIDER_FIRST_TIME = 0;
    public static final int INTELLIGENT_MATCH_INFORM_SERVICE_PROVIDER_SECOND_TIME = 1;
    public static final int INTELLIGENT_MATCH_INFORM_SERVICE_PROVIDER_THIRD_TIME = 2;

    public static final String EXTRA_GENDER = "EXTRA_GENDER";
    public static final String EXTRA_AGE = "EXTRA_AGE";
    public static final String EXTRA_DISTANCE = "EXTRA_DISTANCE";
    public static final String EXTRA_SERVICE_WAY_ID_ARRAY_LIST = "EXTRA_SERVICE_WAY_ID_ARRAY_LIST";
    public static final String EXTRA_SERVICE_PRICE_ARRAY_LIST = "EXTRA_SERVICE_PRICE_ARRAY_LIST";
    public static final String EXTRA_SERVICE_WAY_PRICE_UNIT = "EXTRA_SERVICE_WAY_PRICE_UNIT";
    public static final String EXTRA_SERVICE_WAY = "EXTRA_SERVICE_WAY";
    public static final String EXTRA_SERVICE_PRICE = "EXTRA_SERVICE_PRICE";
    public static final String EXTRA_SERVICE_UNIT = "EXTRA_SERVICE_UNIT";
    public static final String EXTRA_ADVANTAGE = "EXTRA_ADVANTAGE";
    public static final String EXTRA_TIME = "EXTRA_TIME";
    public static final String EXTRA_IS_BOOK = "EXTRA_IS_BOOK";

    public static final String EXTRA_DEMAND_NAME = "EXTRA_DEMAND_NAME";

    public static final String EXTRA_TRUSTEESHIP_AMOUNT = "EXTRA_TRUSTEESHIP_AMOUNT";
    public static final String EXTRA_SERVICE_BID_NUMBER = "EXTRA_SERVICE_BID_NUMBER";
    public static final String EXTRA_IS_UPDATE_DEMAND = "EXTRA_IS_UPDATE_DEMAND";

    public static final int EVALUATE_REQUEST_CODE = 100;

    public static final int DEMAND_DETAILS_STATUS_IN_THE_BIDDING = 10;
    public static final int DEMAND_DETAILS_STATUS_UNDERWAY = 20;
    public static final int DEMAND_DETAILS_STATUS_COMPLETED = 30;
    public static final int DEMAND_DETAILS_STATUS_EXPIRED = 40;

    public static final int PAID_RESULT_CODE = 200;

    public static final String ACTION_REFRESH_COMPILE_DEMAND = "ACTION_REFRESH_COMPILE_DEMAND";

    // 订单状态
    public static final int ORDER_STATUS_ABNORMAL = 0;
    public static final int ORDER_STATUS_UNPAID = 10;
    public static final int ORDER_STATUS_WAIT_FOR_START = 20;
    public static final int ORDER_STATUS_WAIT_FOR_COMPLETE = 30;
    public static final int ORDER_STATUS_COMPLETE = 40;

    // 服务
    public static final String EXTRA_SERVICE_STATUS = "EXTRA_SERVICE_STATUS";

    public static final int SERVICE_STATUS_DEFAULT = -1;
    public static final int SERVICE_STATUS_WAIT_BID = 0;
    public static final int SERVICE_STATUS_HAS_BID = 1;
    public static final int SERVICE_STATUS_UNDERWAY = 2;
    public static final int SERVICE_STATUS_COMPLETED = 3;
    public static final int SERVICE_STATUS_EXPIRED = 4;

    public static final String ACTION_SERVICE_STATUS = "ACTION_SERVICE_STATUS";

    public static final String EXTRA_PUBLISH_SERVICE_TYPE = "EXTRA_PUBLISH_SERVICE_TYPE";

    public static final int EXTRA_PUBLISH_SERVICE_TYPE_ADD = 0;
    public static final int EXTRA_PUBLISH_SERVICE_TYPE_UPDATE = 1;

    public static final String EXTRA_SERVICE_ID = "EXTRA_SERVICE_ID";
    public static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";

    public static final String EXTRA_FIRST_CONTENT = "EXTRA_FIRST_CONTENT";
    public static final String EXTRA_SECOND_CONTENT = "EXTRA_SECOND_CONTENT";

    public static final String EXTRA_SERVICE_WAIT_BID_BEAN = "EXTRA_SERVICE_WAIT_BID_BEAN";

    public static final String EXTRA_BID_ID = "EXTRA_BID_ID";

    public static final String EXTRA_ORIGINAL_PRICE = "EXTRA_ORIGINAL_PRICE";

    public static final String EXTRA_SERVICE_COMPLETED_BEAN = "EXTRA_SERVICE_COMPLETED_BEAN";

    public static final int NO_VOICE_DEMO = 0;
    public static final int NO_VOICE = 1;
    public static final int VOICE_DEMO_DOWNLOADING = 2;
    public static final int VOICE_DOWNLOADING = 3;
    public static final int VOICE_DEMO_DOWNLOAD_SUCCESS = 4;
    public static final int VOICE_DOWNLOAD_SUCCESS = 5;
    public static final int VOICE_DEMO_DOWNLOAD_FAIL = 6;
    public static final int VOICE_DOWNLOAD_FAIL = 7;

    public static final int VOICE_UNCHANGED = 0;
    public static final int UPDATE_VOICE = 1;
    public static final int DELETE_OLD_VOICE = 2;

    // 个人信息
    public static final String EXTRA_NAME = "EXTRA_NAME";

    public static final String EXTRA_EDUCATIONAL_EXPERIENCE_ID = "EXTRA_EDUCATIONAL_EXPERIENCE_ID";
    public static final String EXTRA_WORK_EXPERIENCE_ID = "EXTRA_WORK_EXPERIENCE_ID";
    public static final String EXTRA_HONOR_AND_QUALIFICATION_ID = "EXTRA_HONOR_AND_QUALIFICATION_ID";

    public static final int EXTRA_ADD_PERSONAL_INFORMATION_MODE_ADD_PERSONAL_INFORMATION = 0;
    public static final int EXTRA_ADD_PERSONAL_INFORMATION_MODE_PUBLISH_SERVICE_ADD_EDUCATIONAL_EXPERIENCE = 1;
    public static final int EXTRA_ADD_PERSONAL_INFORMATION_MODE_PUBLISH_SERVICE_ADD_WORK_EXPERIENCE = 2;
    public static final int EXTRA_ADD_PERSONAL_INFORMATION_MODE_PUBLISH_SERVICE_ADD_HONOR_AND_QUALIFICATION = 3;

    public static final String EXTRA_ADD_PERSONAL_INFORMATION_TYPE = "EXTRA_ADD_PERSONAL_INFORMATION_TYPE";

    public static final int ADD_PERSONAL_INFORMATION_TYPE_EDUCATION_EXPERIENCE = 0;
    public static final int ADD_PERSONAL_INFORMATION_TYPE_WORK_EXPERIENCE = 1;
    public static final int ADD_PERSONAL_INFORMATION_TYPE_HONOR_AND_QUALIFICATION = 2;

    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    public static final String EXTRA_SEARCH_INFORMATION = "EXTRA_SEARCH_INFORMATION";
    public static final String EXTRA_EDUCATIONAL_BACKGROUND_ID = "EXTRA_EDUCATIONAL_BACKGROUND_ID";

    public static final String EXTRA_IS_EXIST_WORK_EXPERIENCE = "EXTRA_IS_EXIST_WORK_EXPERIENCE";
    public static final String EXTRA_IS_EXIST_HONOR_AND_QUALIFICATION = "EXTRA_IS_EXIST_HONOR_AND_QUALIFICATION";
    public static final String EXTRA_IS_NEED_SERVICE_PHOTO_ALBUM = "EXTRA_IS_NEED_SERVICE_PHOTO_ALBUM";

    public static final String EXTRA_CHECK_BASIC_PERSONAL_INFORMATION_BEAN = "EXTRA_CHECK_BASIC_PERSONAL_INFORMATION_BEAN";

    // 手机号归属地
    public static final String EXTRA_PHONE_NUMBER_HOME_LOCATION_MOBILE_COUNTRY_CODE_ID = "EXTRA_PHONE_NUMBER_HOME_LOCATION_MOBILE_COUNTRY_CODE_ID";
    public static final String EXTRA_PHONE_NUMBER_HOME_LOCATION_VALUE = "EXTRA_PHONE_NUMBER_HOME_LOCATION_VALUE";
    public static final String EXTRA_PHONE_NUMBER_HOME_LOCATION_REGULAR_EX = "EXTRA_PHONE_NUMBER_HOME_LOCATION_REGULAR_EX";

    // 支付密码
    public static final String EXTRA_PAYMENT_PASSWORD_TYPE = "EXTRA_PAYMENT_PASSWORD_TYPE";

    public static final int PAYMENT_PASSWORD_TYPE_SET_PAYMENT_PASSWORD = 0;
    public static final int PAYMENT_PASSWORD_TYPE_MODIFY_PAYMENT_PASSWORD = 1;
    public static final int PAYMENT_PASSWORD_TYPE_FORGET_PAYMENT_PASSWORD = 2;
    public static final int PAYMENT_PASSWORD_TYPE_PAYMENT_PASSWORD_UNLOCK = 3;
    public static final int PAYMENT_PASSWORD_TYPE_SET_GESTURE_PASSWORD = 4;
    public static final int PAYMENT_PASSWORD_TYPE_DELETE_GESTURE_PASSWORD = 5;

    public static final String EXTRA_IDENTIFY_CARD_NUMBER = "EXTRA_IDENTIFY_CARD_NUMBER";
    public static final String EXTRA_SET_NEW_PAYMENT_PASSWORD = "EXTRA_SET_NEW_PAYMENT_PASSWORD";

    // 搜索信息
    public static final String EXTRA_SEARCH_INFORMATION_TYPE = "EXTRA_SEARCH_INFORMATION_TYPE";

    public static final int SEARCH_INFORMATION_TYPE_EDUCATIONAL_INSTITUTION = 0;
    public static final int SEARCH_INFORMATION_TYPE_MAJOR = 1;
    public static final int SEARCH_INFORMATION_TYPE_EDUCATIONAL_BACKGROUND = 2;

    // 相册
    public static final String EXTRA_ALBUM_ID = "EXTRA_ALBUM_ID";
    public static final String EXTRA_PHOTO_DESCRIPTION = "EXTRA_PHOTO_DESCRIPTION";
    public static final String EXTRA_IMAGE_ID = "EXTRA_IMAGE_ID";
    public static final String EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL";

    public static final int SELECT_PHOTO_ALBUM_MORE_NO_DELETE = 0;
    public static final int SELECT_PHOTO_ALBUM_MORE_HAVE_DELETE = 1;

    public static final String EXTRA_UPLOAD_ALBUM_TYPE = "EXTRA_UPLOAD_ALBUM_TYPE";

    public static final int UPLOAD_ALBUM_PERSONAL = 0;
    public static final int UPLOAD_ALBUM_PUBLISH_SERVICE = 1;

    // 银行卡设置
    public static final String EXTRA_BANK_CARD_SETTING_TYPE = "EXTRA_BANK_CARD_SETTING_TYPE";

    public static final int BANK_CARD_SETTING_TYPE_SET_BANK_CARD = 0;
    public static final int BANK_CARD_SETTING_TYPE_MODIFY_BANK_CARD = 1;

    // 评价
    public static final String EXTRA_EVALUATE_TYPE = "EXTRA_EVALUATE_TYPE";

    public static final int EVALUATE_TYPE_EMPLOYER = 0;
    public static final int EVALUATE_TYPE_SERVICE = 1;

    public static final String EXTRA_EVALUATE_DETAILS_TYPE = "EXTRA_EVALUATE_DETAILS_TYPE";

    public static final int EVALUATE_DETAILS_TYPE_EMPLOYER = 0;
    public static final int EVALUATE_DETAILS_TYPE_SERVICE = 1;

    // 支付
    public static final int PAYMENT_METHOD_DEFAULT = -1;
    public static final int PAYMENT_METHOD_ALLINTASK_ACCOUNT = 0;
    public static final int PAYMENT_METHOD_ALIPAY = 1;
    public static final int PAYMENT_METHOD_WECHAT_PAY = 2;
    public static final int PAYMENT_METHOD_WITHHOLD_TRUSTEESHIP = 3;

    public static final String ALIPAY_RESULT_STATUS = "resultStatus";

    public static final String ALIPAY_RESULT_STATUS_SUCCESS = "9000";
    public static final String ALIPAY_RESULT_STATUS_BEING_PROCESSED = "8000";
    public static final String ALIPAY_RESULT_STATUS_FAIL = "4000";
    public static final String ALIPAY_RESULT_STATUS_REPEAT_REQUEST = "5000";
    public static final String ALIPAY_RESULT_STATUS_CANCEL = "6001";
    public static final String ALIPAY_RESULT_NETWORK_CONNECTION_ERROR = "6002";
    public static final String ALIPAY_RESULT_PAY_RESULT_UNKNOWN = "6004";

    public static final String EXTRA_SELLER_NAME = "EXTRA_SELLER_NAME";
    public static final String EXTRA_PAYMENT_MONEY = "EXTRA_PAYMENT_MONEY";
    public static final String EXTRA_EARNEST_MONEY = "EXTRA_EARNEST_MONEY";
    public static final String EXTRA_PRICE = "EXTRA_PRICE";

    // 申请退款
    public static final String EXTRA_ORDER_ID = "EXTRA_ORDER_ID";
    public static final String EXTRA_ORDER_PRICE = "EXTRA_ORDER_PRICE";

    public static final int WORK_STATUS_UNFINISHED = 0;
    public static final int WORK_STATUS_COMPLETED = 1;

    // 评价
    public static final String EXTRA_BUYER_USER_ID = "EXTRA_BUYER_USER_ID";
    public static final String EXTRA_SELLER_USER_ID = "EXTRA_SELLER_USER_ID";

    public static final String EXTRA_BUYER_OVERALL_MERIT = "EXTRA_BUYER_OVERALL_MERIT";
    public static final String EXTRA_BUYER_EVALUATE_CONTENT = "EXTRA_BUYER_EVALUATE_CONTENT";
    public static final String EXTRA_SELLER_OVERALL_MERIT = "EXTRA_SELLER_OVERALL_MERIT";
    public static final String EXTRA_SELLER_COMPLETE_ON_TIME = "EXTRA_SELLER_COMPLETE_ON_TIME";
    public static final String EXTRA_SELLER_SERVICE_INTEGRITY = "EXTRA_SELLER_SERVICE_INTEGRITY";
    public static final String EXTRA_SELLER_EVALUATE_CONTENT = "EXTRA_SELLER_EVALUATE_CONTENT";

    // 裁剪图片
    public static final String EXTRA_CROP_IMAGE_LEFT = "EXTRA_CROP_IMAGE_LEFT";
    public static final String EXTRA_CROP_IMAGE_TOP = "EXTRA_CROP_IMAGE_TOP";
    public static final String EXTRA_CROP_IMAGE_RIGHT = "EXTRA_CROP_IMAGE_RIGHT";
    public static final String EXTRA_CROP_IMAGE_BOTTOM = "EXTRA_CROP_IMAGE_BOTTOM";
    public static final String EXTRA_CROP_IMAGE_WIDTH = "EXTRA_CROP_IMAGE_WIDTH";
    public static final String EXTRA_CROP_IMAGE_HEIGHT = "EXTRA_CROP_IMAGE_HEIGHT";

    // 设置
    public static final String EXTRA_MOBILE_COUNTRY_CODE_ID = "EXTRA_MOBILE_COUNTRY_CODE_ID";
    public static final String EXTRA_SMS_IDENTIFY_CODE = "EXTRA_SMS_IDENTIFY_CODE";
    public static final String EXTRA_OLD_PAYMENT_PASSWORD = "EXTRA_OLD_PAYMENT_PASSWORD";
    public static final String EXTRA_MAILBOX = "EXTRA_MAILBOX";
    public static final String EXTRA_BANK_ID = "EXTRA_BANK_ID";
    public static final String EXTRA_BANK_CARD_NAME = "EXTRA_BANK_CARD_NAME";

    // 环信
    public static final String EMCHAT_NICKNAME = "nickname";
    public static final String EMCHAT_EM_APNS_EXT = "em_apns_ext";
    public static final String EMCHAT_EXTERN = "extern";
    public static final String EMCHAT_HEAD_PORTRAIT_URL = "headPortraitUrl";

    // 下载
    public static final String ACTION_DOWNLOAD_APK_SUCCESS = "ACTION_DOWNLOAD_SUCCESS";
    public static final String ACTION_DOWNLOAD_VOICE_SUCCESS = "ACTION_DOWNLOAD_VOICE_SUCCESS";
    public static final String ACTION_DOWNLOAD_VOICE_DEMO_SUCCESS = "ACTION_DOWNLOAD_VOICE_DEMO_SUCCESS";

    // 搜索
    public static final int RECENTLY_VISITED = 0;
    public static final int NEWEST = 1;
    public static final int LOWEST_PRICE = 2;
    public static final int HIGHEST_PRICE = 3;

    public static final String EXTRA_SEARCH_STATUS = "EXTRA_SEARCH_STATUS";

    public static final int SEARCH_STATUS_SERVICE = 0;
    public static final int SEARCH_STATUS_DEMAND = 1;

    public static final String EXTRA_KEYWORDS = "EXTRA_KEYWORDS";
    public static final String EXTRA_PROVINCE_CODE = "EXTRA_PROVINCE_CODE";
    public static final String EXTRA_CITY_CODE = "EXTRA_CITY_CODE";
    public static final String EXTRA_CATEGORY_PROPERTY_VALUE_ID_LIST = "EXTRA_CATEGORY_PROPERTY_VALUE_ID_LIST";

    // APP更新
    public static final int NOT_FORCE_UPDATE = 0;
    public static final int FORCE_UPDATE = 1;

    public static final int NOT_SHOW_APP_UPDATE_DIALOG = 0;
    public static final int SHOW_APP_UPDATE_DIALOG = 1;

    // 举报
    public static final String EXTRA_REPORT_REASON_ID_LIST_STRING = "EXTRA_REPORT_REASON_ID_LIST_STRING";
    public static final String EXTRA_REPORT_REASON_STRING = "EXTRA_REPORT_REASON_STRING";

    // 提现
    public static final String POUNDAGE = "{poundage}";

    // 手势密码
    public static final String EXTRA_GESTURE_PASSWORD_TYPE = "EXTRA_GESTURE_PASSWORD_TYPE";

    public static final int SET_GESTURE_PASSWORD = 0;
    public static final int MODIFY_GESTURE_PASSWORD = 1;
    public static final int GESTURE_PASSWORD_LOCK = 2;

    public static final String EXTRA_GESTURE_PASSWORD = "EXTRA_GESTURE_PASSWORD";

    public static final int SET_GESTURE_PASSWORD_REQUEST_CODE = 300;
    public static final int SET_GESTURE_PASSWORD_RESULT_CODE = 400;

    public static final int DELETE_GESTURE_PASSWORD_REQUEST_CODE = 500;
    public static final int DELETE_GESTURE_PASSWORD_RESULT_CODE = 600;

    // 指纹识别
    public static final int FINGERPRINT_VERIFY_REQUEST_CODE = 700;
    public static final int FINGERPRINT_VERIFY_RESULT_CODE = 800;

    // 认证
    public static final String EXTRA_IS_ZHI_MA_CREDIT_AUTHENTICATION_SUCCESS = "EXTRA_IS_ZHI_MA_CREDIT_AUTHENTICATION_SUCCESS";

    // 我的收藏
    public static final String EXTRA_MY_COLLECTION_STATUS = "EXTRA_MY_COLLECTION_STATUS";

    public static final int MY_COLLECTION_SERVICE = 0;
    public static final int MY_COLLECTION_DEMAND = 1;

}
