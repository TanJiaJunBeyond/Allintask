package com.allintask.lingdao.ui.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.demand.DemandPeriodListBean;
import com.allintask.lingdao.bean.message.UserInfoListRequestBean;
import com.allintask.lingdao.bean.message.UserInfoResponseBean;
import com.allintask.lingdao.bean.service.PublishServiceBean;
import com.allintask.lingdao.bean.service.ServiceCategoryListBean;
import com.allintask.lingdao.bean.service.ServiceModeAndPriceModeBean;
import com.allintask.lingdao.bean.user.IsAllBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.domain.EaseUser;
import com.allintask.lingdao.helper.EMChatHelper;
import com.allintask.lingdao.presenter.main.PublishDemandPresenter;
import com.allintask.lingdao.ui.activity.BaseActivity;
import com.allintask.lingdao.ui.activity.pay.TrusteeshipPayActivity;
import com.allintask.lingdao.ui.adapter.main.PublishDemandAdapter;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.main.IPublishDemandView;
import com.allintask.lingdao.widget.CommonRecyclerView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/1/8.
 */

public class PublishDemandActivity extends BaseActivity<IPublishDemandView, PublishDemandPresenter> implements IPublishDemandView {

    private static final int MESSAGE_CODE_EMPLOYMENT_PERIOD = 100;
    private static final int MESSAGE_CODE_DECREASE_AND_INCREASE_EMPLOYMENT_PERIOD = 200;
    private static final int MESSAGE_CODE_EMPLOYMENT_TIME = 300;
    private static final int MESSAGE_CODE_DECREASE_AND_INCREASE_EMPLOYMENT_TIME = 400;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.recycler_view)
    CommonRecyclerView recyclerView;
    @BindView(R.id.btn_publish_demand_and_pay)
    Button publishDemandAndPayBtn;

    private int publishDemandType = CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON;
    private int serviceId = -1;
    private int sendUserId = -1;
    private int categoryId = -1;
    private String userHeadPortraitUrl;
    private String name;
    private int gender = -1;
    private String age;
    private String serviceCategory;
    private String distance;
    private ArrayList<Integer> serviceWayIdArrayList;
    private ArrayList<String> servicePriceArrayList;
    private String serviceWayPriceUnit;
    private String serviceWay;
    private String servicePrice;
    private String serviceUnit;
    private String advantage;
    private int mDemandMaxBudget = 0;

    private InputMethodManager inputMethodManager;

    private volatile boolean employmentPeriodIsStart = true;
    private int mEmploymentPeriod = 0;

    private volatile boolean mEmploymentTimeIsStart = true;
    private int mEmploymentTime = 0;

    private PublishDemandAdapter publishDemandAdapter;
    private List<PublishServiceBean> publishServiceList;
    private List<ServiceCategoryListBean> serviceCategoryList;

    private int recommendMoreStatus = CommonConstant.DEMAND_IS_NOT_SHARE;
    private int demandId = -1;

    private int mMinEmploymentTime = 0;
    private int mMaxEmploymentTime = 0;
    private int mMinEmploymentPeriod = 0;
    private int mMaxEmploymentPeriod = 0;

    private final PublishDemandHandler publishDemandHandler = new PublishDemandHandler(this);

    private static class PublishDemandHandler extends Handler {

        private final WeakReference<Activity> activityWeakReference;

        public PublishDemandHandler(Activity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PublishDemandActivity publishDemandActivity = (PublishDemandActivity) activityWeakReference.get();

            if (msg.what == MESSAGE_CODE_EMPLOYMENT_PERIOD) {
                if (publishDemandActivity.mEmploymentPeriod < publishDemandActivity.mMinEmploymentPeriod) {
                    publishDemandActivity.showToast("小于最小雇佣周期");
                    publishDemandActivity.publishDemandAdapter.setMinAndMaxEmploymentPeriod(publishDemandActivity.mMinEmploymentPeriod);
                } else if (publishDemandActivity.mEmploymentPeriod > publishDemandActivity.mMaxEmploymentPeriod) {
                    publishDemandActivity.showToast("大于最大雇佣周期");
                    publishDemandActivity.publishDemandAdapter.setMinAndMaxEmploymentPeriod(publishDemandActivity.mMaxEmploymentPeriod);
                }
            } else if (msg.what == MESSAGE_CODE_DECREASE_AND_INCREASE_EMPLOYMENT_PERIOD) {
                if (null != publishDemandActivity.publishDemandAdapter) {
                    publishDemandActivity.publishDemandAdapter.setEmploymentPeriod(publishDemandActivity.mEmploymentPeriod);
                }
            } else if (msg.what == MESSAGE_CODE_EMPLOYMENT_TIME) {
                if (publishDemandActivity.mEmploymentTime < publishDemandActivity.mMinEmploymentTime) {
                    publishDemandActivity.showToast("小于最小雇佣周期");
                    publishDemandActivity.publishDemandAdapter.setMinAndMaxEmploymentTime(publishDemandActivity.mMinEmploymentTime);
                } else if (publishDemandActivity.mEmploymentTime > publishDemandActivity.mMaxEmploymentTime) {
                    publishDemandActivity.showToast("大于最大雇佣周期");
                    publishDemandActivity.publishDemandAdapter.setMinAndMaxEmploymentTime(publishDemandActivity.mMaxEmploymentTime);
                }
            } else if (msg.what == MESSAGE_CODE_DECREASE_AND_INCREASE_EMPLOYMENT_TIME) {
                if (null != publishDemandActivity.publishDemandAdapter) {
                    publishDemandActivity.publishDemandAdapter.setEmploymentTime(publishDemandActivity.mEmploymentTime);
                }
            }
        }

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_publish_demand;
    }

    @Override
    protected PublishDemandPresenter CreatePresenter() {
        return new PublishDemandPresenter();
    }

    @Override
    protected void init() {
        Intent intent = getIntent();

        if (null != intent) {
            publishDemandType = intent.getIntExtra(CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE, CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON);
            sendUserId = intent.getIntExtra(CommonConstant.EXTRA_SEND_USER_ID, -1);
            serviceId = intent.getIntExtra(CommonConstant.EXTRA_SERVICE_ID, -1);
            categoryId = intent.getIntExtra(CommonConstant.EXTRA_CATEGORY_ID, -1);
            userHeadPortraitUrl = intent.getStringExtra(CommonConstant.EXTRA_DEMAND_USER_HEAD_PORTRAIT_URL);
            name = intent.getStringExtra(CommonConstant.EXTRA_NAME);
            gender = intent.getIntExtra(CommonConstant.EXTRA_GENDER, -1);
            age = intent.getStringExtra(CommonConstant.EXTRA_AGE);
            serviceCategory = intent.getStringExtra(CommonConstant.EXTRA_SERVICE_CATEGORY);
            distance = intent.getStringExtra(CommonConstant.EXTRA_DISTANCE);
            serviceWayIdArrayList = intent.getIntegerArrayListExtra(CommonConstant.EXTRA_SERVICE_WAY_ID_ARRAY_LIST);
            servicePriceArrayList = intent.getStringArrayListExtra(CommonConstant.EXTRA_SERVICE_PRICE_ARRAY_LIST);
            serviceWayPriceUnit = intent.getStringExtra(CommonConstant.EXTRA_SERVICE_WAY_PRICE_UNIT);
            serviceWay = intent.getStringExtra(CommonConstant.EXTRA_SERVICE_WAY);
            servicePrice = intent.getStringExtra(CommonConstant.EXTRA_SERVICE_PRICE);
            serviceUnit = intent.getStringExtra(CommonConstant.EXTRA_SERVICE_UNIT);
            advantage = intent.getStringExtra(CommonConstant.EXTRA_ADVANTAGE);
        }

        initToolbar();
        initUI();
        initData();
    }

    private void initToolbar() {
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != inputMethodManager && inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }

                finish();
            }
        });

        toolbar.setTitle("");

        if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON) {
            titleTv.setText(getString(R.string.publish_demand));
        } else if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE) {
            titleTv.setText(getString(R.string.employ_at_once));

        }

        setSupportActionBar(toolbar);
    }

    private void initUI() {
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        recyclerView.setFocusableInTouchMode(false);

        publishDemandAdapter = new PublishDemandAdapter(getParentContext(), publishDemandType);
        recyclerView.setAdapter(publishDemandAdapter);

        publishDemandAdapter.setPersonalInformation(userHeadPortraitUrl, name, gender, age, serviceCategory, distance, serviceWayPriceUnit);
        publishDemandAdapter.setOnClickListener(new PublishDemandAdapter.OnClickListener() {
            @Override
            public void onSwitchCompatCheckedChange(boolean isChecked) {
                if (isChecked) {
                    titleTv.setText(getString(R.string.publish_demand));
                    publishDemandAndPayBtn.setText(getString(R.string.publish_demand));
                } else {
                    titleTv.setText(getString(R.string.employ_at_once));
                    publishDemandAndPayBtn.setText(getString(R.string.confirm_employ));
                }
            }

            @Override
            public void onTagClickListener(int tempCategoryId) {
                categoryId = tempCategoryId;
                mPresenter.fetchServiceModeAndPriceModeRequest(categoryId);
            }

            @Override
            public void onIncreaseTouch(int minEmployPeriod, int maxEmployPeriod, int employmentPeriod) {
                employmentPeriodIsStart = true;
                mMinEmploymentPeriod = minEmployPeriod;
                mMaxEmploymentPeriod = maxEmployPeriod;
                mEmploymentPeriod = employmentPeriod;

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (employmentPeriodIsStart) {
                            if (mEmploymentPeriod < mMaxEmploymentPeriod) {
                                try {
                                    mEmploymentPeriod++;

                                    Message message = Message.obtain();
                                    message.what = MESSAGE_CODE_DECREASE_AND_INCREASE_EMPLOYMENT_PERIOD;
                                    publishDemandHandler.sendMessage(message);

                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                employmentPeriodIsStart = false;
                            }
                        }
                    }
                });

                thread.start();
            }

            @Override
            public void onDecreaseTouch(int minEmployPeriod, int maxEmployPeriod, int employmentPeriod) {
                employmentPeriodIsStart = true;
                mMinEmploymentPeriod = minEmployPeriod;
                mMaxEmploymentPeriod = maxEmployPeriod;
                mEmploymentPeriod = employmentPeriod;

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (employmentPeriodIsStart) {
                            if (mEmploymentPeriod > mMinEmploymentPeriod) {
                                try {
                                    mEmploymentPeriod--;

                                    Message message = Message.obtain();
                                    message.what = MESSAGE_CODE_DECREASE_AND_INCREASE_EMPLOYMENT_PERIOD;
                                    publishDemandHandler.sendMessage(message);

                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                employmentPeriodIsStart = false;
                            }
                        }
                    }
                });

                thread.start();
            }

            @Override
            public void onEmploymentPeriodActionUp() {
                employmentPeriodIsStart = false;
            }

            @Override
            public void onCheckEmploymentPeriod(int minEmployPeriod, int maxEmployPeriod, int employmentPeriod) {
                mMinEmploymentPeriod = minEmployPeriod;
                mMaxEmploymentPeriod = maxEmployPeriod;
                mEmploymentPeriod = employmentPeriod;

                if (publishDemandHandler.hasMessages(MESSAGE_CODE_EMPLOYMENT_PERIOD)) {
                    publishDemandHandler.removeMessages(MESSAGE_CODE_EMPLOYMENT_PERIOD);
                }

                publishDemandHandler.sendEmptyMessageDelayed(MESSAGE_CODE_EMPLOYMENT_PERIOD, 500);
            }

            @Override
            public void onEmploymentTimeIncreaseTouch(int minEmployTime, int maxEmployTime, int employmentTime) {
                mEmploymentTimeIsStart = true;
                mMinEmploymentTime = minEmployTime;
                mMaxEmploymentTime = maxEmployTime;
                mEmploymentTime = employmentTime;

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (mEmploymentTimeIsStart) {
                            if (mEmploymentTime < mMaxEmploymentTime) {
                                try {
                                    mEmploymentTime++;

                                    Message message = Message.obtain();
                                    message.what = MESSAGE_CODE_DECREASE_AND_INCREASE_EMPLOYMENT_TIME;
                                    publishDemandHandler.sendMessage(message);

                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                mEmploymentTimeIsStart = false;
                            }
                        }
                    }
                });

                thread.start();
            }

            @Override
            public void onEmploymentTimeDecreaseTouch(int minEmployTime, int maxEmployTime, int employmentTime) {
                mEmploymentTimeIsStart = true;
                mMinEmploymentTime = minEmployTime;
                mMaxEmploymentTime = maxEmployTime;
                mEmploymentTime = employmentTime;

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (mEmploymentTimeIsStart) {
                            if (mEmploymentTime > mMinEmploymentTime) {
                                try {
                                    mEmploymentTime--;

                                    Message message = Message.obtain();
                                    message.what = MESSAGE_CODE_DECREASE_AND_INCREASE_EMPLOYMENT_TIME;
                                    publishDemandHandler.sendMessage(message);

                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                mEmploymentTimeIsStart = false;
                            }
                        }
                    }
                });

                thread.start();
            }

            @Override
            public void onEmploymentTimeActionUp() {
                mEmploymentTimeIsStart = false;
            }

            @Override
            public void onCheckEmploymentTime(int minEmployTime, int maxEmployTime, int employmentTime) {
                mMinEmploymentTime = minEmployTime;
                mMaxEmploymentTime = maxEmployTime;
                mEmploymentTime = employmentTime;

                if (publishDemandHandler.hasMessages(MESSAGE_CODE_EMPLOYMENT_TIME)) {
                    publishDemandHandler.removeMessages(MESSAGE_CODE_EMPLOYMENT_TIME);
                }

                publishDemandHandler.sendEmptyMessageDelayed(MESSAGE_CODE_EMPLOYMENT_TIME, 500);
            }
        });

        if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON) {
            publishDemandAndPayBtn.setText(getString(R.string.publish_demand));
        } else if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE) {
            publishDemandAndPayBtn.setText(getString(R.string.confirm_employ));
        }
    }

    private void initData() {
        publishServiceList = new ArrayList<>();
        mPresenter.fetchAddressListRequest();
//        mPresenter.fetchPaymentPeriodListRequest();
    }

    private void sendMessage(int sendUserId, int demandId, String IMMessage) {
        String nickname = UserPreferences.getInstance().getNickname();
        String headPortraitUrl = UserPreferences.getInstance().getHeadPortraitUrl();

        EMMessage serviceEMMessage = EMMessage.createTxtSendMessage(IMMessage, String.valueOf(sendUserId));

        if (!TextUtils.isEmpty(nickname)) {
            serviceEMMessage.setAttribute(CommonConstant.EMCHAT_NICKNAME, nickname);

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(CommonConstant.EMCHAT_EXTERN, nickname);
                serviceEMMessage.setAttribute(CommonConstant.EMCHAT_EM_APNS_EXT, jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(headPortraitUrl)) {
            String tempHeadPortrait = headPortraitUrl.replace("https:", "");
            serviceEMMessage.setAttribute(CommonConstant.EMCHAT_HEAD_PORTRAIT_URL, tempHeadPortrait);
        }

        serviceEMMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_TYPE, CommonConstant.MESSAGE_ATTRIBUTE_TYPE_SERVICE);
        serviceEMMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVICE_ID, String.valueOf(serviceId));
        serviceEMMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVE_TYPE, serviceCategory);
        serviceEMMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVE_SERVICE_MODE, serviceWay);
        serviceEMMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVE_SERVICE_PRICE, "￥" + servicePrice + "/" + serviceUnit);
        serviceEMMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_SERVE_INTRO, advantage);
        EMClient.getInstance().chatManager().sendMessage(serviceEMMessage);

        EMMessage bidEMMessage = EMMessage.createTxtSendMessage(IMMessage, String.valueOf(sendUserId));

        if (!TextUtils.isEmpty(nickname)) {
            bidEMMessage.setAttribute(CommonConstant.EMCHAT_NICKNAME, nickname);

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(CommonConstant.EMCHAT_EXTERN, nickname);
                serviceEMMessage.setAttribute(CommonConstant.EMCHAT_EM_APNS_EXT, jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(headPortraitUrl)) {
            String tempHeadPortrait = headPortraitUrl.replace("https:", "");
            bidEMMessage.setAttribute(CommonConstant.EMCHAT_HEAD_PORTRAIT_URL, tempHeadPortrait);
        }

        bidEMMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_TYPE, CommonConstant.MESSAGE_ATTRIBUTE_TYPE_BID);
        bidEMMessage.setAttribute(CommonConstant.MESSAGE_ATTRIBUTE_DEMAND_ID, demandId);
        EMClient.getInstance().chatManager().sendMessage(bidEMMessage);

        UserInfoListRequestBean userInfoListRequestBean = new UserInfoListRequestBean();

        List<Integer> userIdList = new ArrayList<>();
        userIdList.add(sendUserId);
        userInfoListRequestBean.userIds = userIdList;
        mPresenter.fetchUserInfoListRequest(userInfoListRequestBean);
    }

    @OnClick({R.id.btn_publish_demand_and_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_publish_demand_and_pay:
                if (null != inputMethodManager && inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }

                if (null != publishDemandAdapter) {
                    int categoryId = publishDemandAdapter.getCategoryId();
                    String serviceCategoryListString = publishDemandAdapter.getServiceCategoryListString();
                    List<Integer> mustSelectedCategoryIdList = publishDemandAdapter.getMustSelectedCategoryIdList();
                    int serviceModeId = publishDemandAdapter.getServiceModeId();
                    boolean isNeedAddress = publishDemandAdapter.getIsNeedAddress();
                    String provinceCode = publishDemandAdapter.getProvinceCode();
                    String cityCode = publishDemandAdapter.getCityCode();
                    String workStartTime = publishDemandAdapter.getWorkStartTime();
                    int showEmploymentTimes = publishDemandAdapter.getShowEmploymentTimes();
                    int employmentTime = publishDemandAdapter.getEmploymentTime();
                    int demandPeriodId = publishDemandAdapter.getDemandPeriodId();
                    int employmentPeriod = publishDemandAdapter.getEmploymentPeriod();
                    int employmentPeriodUnitId = publishDemandAdapter.getEmploymentPeriodUnitId();
                    int aggregateDemandBudget = publishDemandAdapter.getAggregateDemandBudget();
                    int employmentTimeServicePayment = publishDemandAdapter.getEmploymentTimeServicePayment();
                    int servicePayment = publishDemandAdapter.getServicePayment();
                    String demandIntroduction = publishDemandAdapter.getDemandIntroduction();
                    recommendMoreStatus = publishDemandAdapter.getRecommendMoreStatus();

                    if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON) {
                        serviceCategory = publishDemandAdapter.getCategoryName();

//                        if (categoryId != -1) {
//                            if (null != mustSelectedCategoryIdList && mustSelectedCategoryIdList.size() > 0) {
//                                if (!TextUtils.isEmpty(serviceCategoryListString)) {
//                                    boolean isServiceCategoryWhole = true;
//
//                                    for (int i = 0; i < mustSelectedCategoryIdList.size(); i++) {
//                                        int mustSelectedCategoryId = mustSelectedCategoryIdList.get(i);
//
//                                        if (!serviceCategoryListString.contains(String.valueOf(mustSelectedCategoryId))) {
//                                            isServiceCategoryWhole = false;
//                                        }
//                                    }
//
//                                    if (isServiceCategoryWhole) {
//                                        if (serviceModeId != -1) {
//                                            if (!TextUtils.isEmpty(provinceCode)) {
//                                                if (!TextUtils.isEmpty(workStartTime)) {
//                                                    if (demandPeriodId != -1) {
//                                                        if (aggregateDemandBudget > 0) {
//                                                            if (!TextUtils.isEmpty(demandIntroduction)) {
//                                                                if (demandIntroduction.length() >= 30) {
//                                                                    mPresenter.publishDemandRequest(serviceId, CommonConstant.DEMAND_IS_SHARE, categoryId, serviceCategoryListString, serviceModeId, workStartTime, demandPeriodId, aggregateDemandBudget, demandIntroduction, provinceCode, cityCode);
//                                                                } else {
//                                                                    showToast("需求介绍不够字数");
//                                                                }
//                                                            } else {
//                                                                showToast("请填写需求介绍");
//                                                            }
//                                                        } else {
//                                                            showToast("请填写正确的需求总预算");
//                                                        }
//                                                    } else {
//                                                        showToast("请选择支付周期");
//                                                    }
//                                                } else {
//                                                    showToast("请选择工作开始时间");
//                                                }
//                                            } else {
//                                                showToast("请选择服务城市");
//                                            }
//                                        } else {
//                                            showToast("请选择工作方式");
//                                        }
//                                    } else {
//                                        showToast("还有必选项没选择");
//                                    }
//                                } else {
//                                    showToast("请选择需求品类分类");
//                                }
//                            } else {
//                                if (serviceModeId != -1) {
//                                    if (!TextUtils.isEmpty(provinceCode)) {
//                                        if (!TextUtils.isEmpty(workStartTime)) {
//                                            if (demandPeriodId != -1) {
//                                                if (aggregateDemandBudget > 0) {
//                                                    if (!TextUtils.isEmpty(demandIntroduction)) {
//                                                        if (demandIntroduction.length() >= 30) {
//                                                            mPresenter.publishDemandRequest(serviceId, CommonConstant.DEMAND_IS_SHARE, categoryId, serviceCategoryListString, serviceModeId, workStartTime, demandPeriodId, aggregateDemandBudget, demandIntroduction, provinceCode, cityCode);
//                                                        } else {
//                                                            showToast("需求介绍不够字数");
//                                                        }
//                                                    } else {
//                                                        showToast("请填写需求介绍");
//                                                    }
//                                                } else {
//                                                    showToast("请填写正确的需求总预算");
//                                                }
//                                            } else {
//                                                showToast("请选择支付周期");
//                                            }
//                                        } else {
//                                            showToast("请选择工作开始时间");
//                                        }
//                                    } else {
//                                        showToast("请选择服务城市");
//                                    }
//                                } else {
//                                    showToast("请选择工作方式");
//                                }
//                            }
//                        } else {
//                            showToast("请选择需求品类");
//                        }

                        if (categoryId != -1) {
                            if (null != mustSelectedCategoryIdList && mustSelectedCategoryIdList.size() > 0) {
                                if (!TextUtils.isEmpty(serviceCategoryListString)) {
                                    boolean isServiceCategoryWhole = true;

                                    for (int i = 0; i < mustSelectedCategoryIdList.size(); i++) {
                                        int mustSelectedCategoryId = mustSelectedCategoryIdList.get(i);

                                        if (!serviceCategoryListString.contains(String.valueOf(mustSelectedCategoryId))) {
                                            isServiceCategoryWhole = false;
                                        }
                                    }

                                    if (isServiceCategoryWhole) {
                                        if (serviceModeId != -1) {
                                            if (isNeedAddress) {
                                                if (!TextUtils.isEmpty(provinceCode)) {
                                                    if (!TextUtils.isEmpty(workStartTime)) {
                                                        if (employmentPeriod > 0) {
                                                            if (aggregateDemandBudget > 0) {
                                                                if (aggregateDemandBudget < mDemandMaxBudget) {
                                                                    if (!TextUtils.isEmpty(demandIntroduction)) {
                                                                        if (demandIntroduction.length() >= 30) {
                                                                            mPresenter.publishDemandV1Request(CommonConstant.DEMAND_IS_SHARE, categoryId, serviceCategoryListString, serviceModeId, provinceCode, cityCode, workStartTime, employmentPeriod, employmentPeriod, employmentPeriodUnitId, aggregateDemandBudget, demandIntroduction, 0);
                                                                        } else {
                                                                            showToast("需求介绍不够字数");
                                                                        }
                                                                    } else {
                                                                        showToast("请填写需求介绍");
                                                                    }
                                                                } else {
                                                                    showToast("雇用价格不能大于最大值" + String.valueOf(mDemandMaxBudget));
                                                                }
                                                            } else {
                                                                showToast("请填写正确的雇用价格");
                                                            }
                                                        } else {
                                                            showToast("请输入正确的雇佣周期");
                                                        }
                                                    } else {
                                                        showToast("请选择预约工作时间");
                                                    }
                                                } else {
                                                    showToast("请选择雇佣城市");
                                                }
                                            } else {
                                                if (!TextUtils.isEmpty(workStartTime)) {
                                                    if (employmentPeriod > 0) {
                                                        if (aggregateDemandBudget > 0) {
                                                            if (aggregateDemandBudget < mDemandMaxBudget) {
                                                                if (!TextUtils.isEmpty(demandIntroduction)) {
                                                                    if (demandIntroduction.length() >= 30) {
                                                                        mPresenter.publishDemandV1Request(CommonConstant.DEMAND_IS_SHARE, categoryId, serviceCategoryListString, serviceModeId, null, null, workStartTime, employmentPeriod, employmentPeriod, employmentPeriodUnitId, aggregateDemandBudget, demandIntroduction, 0);
                                                                    } else {
                                                                        showToast("需求介绍不够字数");
                                                                    }
                                                                } else {
                                                                    showToast("请填写需求介绍");
                                                                }
                                                            } else {
                                                                showToast("雇用价格不能大于最大值：￥" + String.valueOf(mDemandMaxBudget));
                                                            }
                                                        } else {
                                                            showToast("请填写正确的雇用价格");
                                                        }
                                                    } else {
                                                        showToast("请输入正确的雇佣周期");
                                                    }
                                                } else {
                                                    showToast("请选择预约工作时间");
                                                }
                                            }
                                        } else {
                                            showToast("请选择雇佣方式");
                                        }
                                    } else {
                                        showToast("还有必选项没选择");
                                    }
                                } else {
                                    showToast("请选择需求品类分类");
                                }
                            } else {
                                if (serviceModeId != -1) {
                                    if (isNeedAddress) {
                                        if (!TextUtils.isEmpty(provinceCode)) {
                                            if (!TextUtils.isEmpty(workStartTime)) {
                                                if (employmentPeriod > 0) {
                                                    if (aggregateDemandBudget > 0) {
                                                        if (aggregateDemandBudget < mDemandMaxBudget) {
                                                            if (!TextUtils.isEmpty(demandIntroduction)) {
                                                                if (demandIntroduction.length() >= 30) {
                                                                    mPresenter.publishDemandV1Request(CommonConstant.DEMAND_IS_SHARE, categoryId, serviceCategoryListString, serviceModeId, provinceCode, cityCode, workStartTime, employmentPeriod, employmentPeriod, employmentPeriodUnitId, aggregateDemandBudget, demandIntroduction, 0);
                                                                } else {
                                                                    showToast("需求介绍不够字数");
                                                                }
                                                            } else {
                                                                showToast("请填写需求介绍");
                                                            }
                                                        } else {
                                                            showToast("雇用价格不能大于最大值：￥" + String.valueOf(mDemandMaxBudget));
                                                        }
                                                    } else {
                                                        showToast("请填写正确的雇用价格");
                                                    }
                                                } else {
                                                    showToast("请输入正确的雇佣周期");
                                                }
                                            } else {
                                                showToast("请选择预约工作时间");
                                            }
                                        } else {
                                            showToast("请选择雇佣城市");
                                        }
                                    } else {
                                        if (!TextUtils.isEmpty(workStartTime)) {
                                            if (employmentPeriod > 0) {
                                                if (aggregateDemandBudget > 0) {
                                                    if (aggregateDemandBudget < mDemandMaxBudget) {
                                                        if (!TextUtils.isEmpty(demandIntroduction)) {
                                                            if (demandIntroduction.length() >= 30) {
                                                                mPresenter.publishDemandV1Request(CommonConstant.DEMAND_IS_SHARE, categoryId, serviceCategoryListString, serviceModeId, null, null, workStartTime, employmentPeriod, employmentPeriod, employmentPeriodUnitId, aggregateDemandBudget, demandIntroduction, 0);
                                                            } else {
                                                                showToast("需求介绍不够字数");
                                                            }
                                                        } else {
                                                            showToast("请填写需求介绍");
                                                        }
                                                    } else {
                                                        showToast("雇用价格不能大于最大值：￥" + String.valueOf(mDemandMaxBudget));
                                                    }
                                                } else {
                                                    showToast("请填写正确的雇用价格");
                                                }
                                            } else {
                                                showToast("请输入正确的雇佣周期");
                                            }
                                        } else {
                                            showToast("请选择预约工作时间");
                                        }
                                    }
                                } else {
                                    showToast("请选择雇佣方式");
                                }
                            }
                        } else {
                            showToast("请选择需求品类");
                        }
                    } else if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE) {
                        if (recommendMoreStatus == CommonConstant.DEMAND_IS_NOT_SHARE) {
//                            if (serviceModeId != -1) {
//                                if (!TextUtils.isEmpty(workStartTime)) {
//                                    if (demandPeriodId != -1) {
//                                        if (aggregateDemandBudget > 0) {
//                                            mPresenter.publishDemandRequest(serviceId, recommendMoreStatus, this.categoryId, null, serviceModeId, workStartTime, demandPeriodId, aggregateDemandBudget, null, null, null);
//                                        } else {
//                                            showToast("请填写正确的需求总预算");
//                                        }
//                                    } else {
//                                        showToast("请选择支付周期");
//                                    }
//                                } else {
//                                    showToast("请选择工作开始时间");
//                                }
//                            } else {
//                                showToast("请选择工作方式");
//                            }

                            if (this.categoryId != -1) {
                                if (serviceId != -1) {
                                    if (serviceModeId != -1) {
//                                        if (isNeedAddress) {
//                                            if (!TextUtils.isEmpty(provinceCode)) {
                                        if (showEmploymentTimes == 0) {
                                            if (employmentPeriod > 0) {
                                                if (servicePayment < mDemandMaxBudget) {
                                                    mPresenter.publishPaymentDemandRequest(serviceId, this.categoryId, serviceCategoryListString, serviceModeId, "0", "0", employmentPeriod, employmentPeriod, employmentPeriodUnitId, servicePayment, showEmploymentTimes);
                                                } else {
                                                    showToast("雇用价格不能大于最大值：￥" + String.valueOf(mDemandMaxBudget));
                                                }
                                            } else {
                                                showToast("请输入正确的雇佣周期");
                                            }
                                        } else if (showEmploymentTimes == 1) {
                                            if (employmentPeriod > 0) {
                                                if (employmentTimeServicePayment < mDemandMaxBudget) {
                                                    mPresenter.publishPaymentDemandRequest(serviceId, this.categoryId, serviceCategoryListString, serviceModeId, "0", "0", employmentTime, employmentPeriod, employmentPeriodUnitId, employmentTimeServicePayment, showEmploymentTimes);
                                                } else {
                                                    showToast("雇用价格不能大于最大值：￥" + String.valueOf(mDemandMaxBudget));
                                                }
                                            } else {
                                                showToast("请输入正确的雇佣周期");
                                            }
                                        }
//                                            } else {
//                                                showToast("请选择雇佣城市");
//                                            }
//                                        } else {
//                                            if (showEmploymentTimes == 0) {
//                                                if (employmentPeriod > 0) {
//                                                    mPresenter.publishPaymentDemandRequest(serviceId, this.categoryId, serviceCategoryListString, serviceModeId, null, null, employmentPeriod, employmentPeriod, employmentPeriodUnitId, servicePayment, showEmploymentTimes);
//                                                } else {
//                                                    showToast("请输入正确的雇佣周期");
//                                                }
//                                            } else if (showEmploymentTimes == 1) {
//                                                if (employmentPeriod > 0) {
//                                                    mPresenter.publishPaymentDemandRequest(serviceId, this.categoryId, serviceCategoryListString, serviceModeId, null, null, employmentTime, employmentPeriod, employmentPeriodUnitId, employmentTimeServicePayment, showEmploymentTimes);
//                                                } else {
//                                                    showToast("请输入正确的雇佣周期");
//                                                }
//                                            }
//                                        }
                                    } else {
                                        showToast("请选择雇佣方式");
                                    }
                                }
                            } else {
                                showToast("请选择需求品类");
                            }
                        } else if (recommendMoreStatus == CommonConstant.DEMAND_IS_SHARE) {
//                            if (null != mustSelectedCategoryIdList && mustSelectedCategoryIdList.size() > 0) {
//                                if (!TextUtils.isEmpty(serviceCategoryListString)) {
//                                    boolean isServiceCategoryWhole = true;
//
//                                    for (int i = 0; i < mustSelectedCategoryIdList.size(); i++) {
//                                        int mustSelectedCategoryId = mustSelectedCategoryIdList.get(i);
//
//                                        if (!serviceCategoryListString.contains(String.valueOf(mustSelectedCategoryId))) {
//                                            isServiceCategoryWhole = false;
//                                        }
//                                    }
//
//                                    if (isServiceCategoryWhole) {
//                                        if (serviceModeId != -1) {
//                                            if (!TextUtils.isEmpty(provinceCode)) {
//                                                if (!TextUtils.isEmpty(workStartTime)) {
//                                                    if (demandPeriodId != -1) {
//                                                        if (aggregateDemandBudget > 0) {
//                                                            if (!TextUtils.isEmpty(demandIntroduction)) {
//                                                                if (demandIntroduction.length() >= 30) {
//                                                                    mPresenter.publishDemandRequest(serviceId, recommendMoreStatus, this.categoryId, serviceCategoryListString, serviceModeId, workStartTime, demandPeriodId, aggregateDemandBudget, demandIntroduction, provinceCode, cityCode);
//                                                                } else {
//                                                                    showToast("需求介绍不够字数");
//                                                                }
//                                                            } else {
//                                                                showToast("请填写需求介绍");
//                                                            }
//                                                        } else {
//                                                            showToast("请填写正确的需求总预算");
//                                                        }
//                                                    } else {
//                                                        showToast("请选择支付周期");
//                                                    }
//                                                } else {
//                                                    showToast("请选择工作开始时间");
//                                                }
//                                            } else {
//                                                showToast("请选择服务城市");
//                                            }
//                                        } else {
//                                            showToast("请选择工作方式");
//                                        }
//                                    } else {
//                                        showToast("还有必选项没选择");
//                                    }
//                                } else {
//                                    showToast("请选择需求品类分类");
//                                }
//                            } else {
//                                if (serviceModeId != -1) {
//                                    if (!TextUtils.isEmpty(provinceCode)) {
//                                        if (!TextUtils.isEmpty(workStartTime)) {
//                                            if (demandPeriodId != -1) {
//                                                if (aggregateDemandBudget > 0) {
//                                                    if (!TextUtils.isEmpty(demandIntroduction)) {
//                                                        if (demandIntroduction.length() >= 30) {
//                                                            mPresenter.publishDemandRequest(serviceId, recommendMoreStatus, this.categoryId, serviceCategoryListString, serviceModeId, workStartTime, demandPeriodId, aggregateDemandBudget, demandIntroduction, provinceCode, cityCode);
//                                                        } else {
//                                                            showToast("需求介绍不够字数");
//                                                        }
//                                                    } else {
//                                                        showToast("请填写需求介绍");
//                                                    }
//                                                } else {
//                                                    showToast("请填写正确的需求总预算");
//                                                }
//                                            } else {
//                                                showToast("请选择支付周期");
//                                            }
//                                        } else {
//                                            showToast("请选择工作开始时间");
//                                        }
//                                    } else {
//                                        showToast("请选择服务城市");
//                                    }
//                                } else {
//                                    showToast("请选择工作方式");
//                                }
//                            }

                            if (this.categoryId != -1) {
                                if (null != mustSelectedCategoryIdList && mustSelectedCategoryIdList.size() > 0) {
                                    if (!TextUtils.isEmpty(serviceCategoryListString)) {
                                        boolean isServiceCategoryWhole = true;

                                        for (int i = 0; i < mustSelectedCategoryIdList.size(); i++) {
                                            int mustSelectedCategoryId = mustSelectedCategoryIdList.get(i);

                                            if (!serviceCategoryListString.contains(String.valueOf(mustSelectedCategoryId))) {
                                                isServiceCategoryWhole = false;
                                            }
                                        }

                                        if (isServiceCategoryWhole) {
                                            if (serviceModeId != -1) {
                                                if (isNeedAddress) {
                                                    if (!TextUtils.isEmpty(provinceCode)) {
                                                        if (!TextUtils.isEmpty(workStartTime)) {
                                                            if (employmentPeriod > 0) {
                                                                if (aggregateDemandBudget > 0) {
                                                                    if (aggregateDemandBudget < mDemandMaxBudget) {
                                                                        if (!TextUtils.isEmpty(demandIntroduction)) {
                                                                            if (demandIntroduction.length() >= 30) {
                                                                                mPresenter.publishDemandV1Request(CommonConstant.DEMAND_IS_SHARE, this.categoryId, serviceCategoryListString, serviceModeId, provinceCode, cityCode, workStartTime, employmentPeriod, employmentPeriod, employmentPeriodUnitId, aggregateDemandBudget, demandIntroduction, 0);
                                                                            } else {
                                                                                showToast("需求介绍不够字数");
                                                                            }
                                                                        } else {
                                                                            showToast("请填写需求介绍");
                                                                        }
                                                                    } else {
                                                                        showToast("雇用价格不能大于最大值：￥" + String.valueOf(mDemandMaxBudget));
                                                                    }
                                                                } else {
                                                                    showToast("请填写正确的雇用价格");
                                                                }
                                                            } else {
                                                                showToast("请输入正确的雇佣周期");
                                                            }
                                                        } else {
                                                            showToast("请选择预约工作时间");
                                                        }
                                                    } else {
                                                        showToast("请选择雇佣城市");
                                                    }
                                                } else {
                                                    if (!TextUtils.isEmpty(workStartTime)) {
                                                        if (employmentPeriod > 0) {
                                                            if (aggregateDemandBudget > 0) {
                                                                if (aggregateDemandBudget < mDemandMaxBudget) {
                                                                    if (!TextUtils.isEmpty(demandIntroduction)) {
                                                                        if (demandIntroduction.length() >= 30) {
                                                                            mPresenter.publishDemandV1Request(CommonConstant.DEMAND_IS_SHARE, this.categoryId, serviceCategoryListString, serviceModeId, null, null, workStartTime, employmentPeriod, employmentPeriod, employmentPeriodUnitId, aggregateDemandBudget, demandIntroduction, 0);
                                                                        } else {
                                                                            showToast("需求介绍不够字数");
                                                                        }
                                                                    } else {
                                                                        showToast("请填写需求介绍");
                                                                    }
                                                                } else {
                                                                    showToast("雇用价格不能大于最大值：￥" + String.valueOf(mDemandMaxBudget));
                                                                }
                                                            } else {
                                                                showToast("请填写正确的雇用价格");
                                                            }
                                                        } else {
                                                            showToast("请输入正确的雇佣周期");
                                                        }
                                                    } else {
                                                        showToast("请选择预约工作时间");
                                                    }
                                                }
                                            } else {
                                                showToast("请选择雇佣方式");
                                            }
                                        } else {
                                            showToast("还有必选项没选择");
                                        }
                                    } else {
                                        showToast("请选择需求品类分类");
                                    }
                                } else {
                                    if (serviceModeId != -1) {
                                        if (isNeedAddress) {
                                            if (!TextUtils.isEmpty(provinceCode)) {
                                                if (!TextUtils.isEmpty(workStartTime)) {
                                                    if (employmentPeriod > 0) {
                                                        if (aggregateDemandBudget > 0) {
                                                            if (aggregateDemandBudget < mDemandMaxBudget) {
                                                                if (!TextUtils.isEmpty(demandIntroduction)) {
                                                                    if (demandIntroduction.length() >= 30) {
                                                                        mPresenter.publishDemandV1Request(CommonConstant.DEMAND_IS_SHARE, this.categoryId, serviceCategoryListString, serviceModeId, provinceCode, cityCode, workStartTime, employmentPeriod, employmentPeriod, employmentPeriodUnitId, aggregateDemandBudget, demandIntroduction, 0);
                                                                    } else {
                                                                        showToast("需求介绍不够字数");
                                                                    }
                                                                } else {
                                                                    showToast("请填写需求介绍");
                                                                }
                                                            } else {
                                                                showToast("雇用价格不能大于最大值：￥" + String.valueOf(mDemandMaxBudget));
                                                            }
                                                        } else {
                                                            showToast("请填写正确的需求总预算");
                                                        }
                                                    } else {
                                                        showToast("请输入正确的雇佣周期");
                                                    }
                                                } else {
                                                    showToast("请选择预约工作时间");
                                                }
                                            } else {
                                                showToast("请选择雇佣城市");
                                            }
                                        } else {
                                            if (!TextUtils.isEmpty(workStartTime)) {
                                                if (employmentPeriod > 0) {
                                                    if (aggregateDemandBudget > 0) {
                                                        if (aggregateDemandBudget < mDemandMaxBudget) {
                                                            if (!TextUtils.isEmpty(demandIntroduction)) {
                                                                if (demandIntroduction.length() >= 30) {
                                                                    mPresenter.publishDemandV1Request(CommonConstant.DEMAND_IS_SHARE, this.categoryId, serviceCategoryListString, serviceModeId, null, null, workStartTime, employmentPeriod, employmentPeriod, employmentPeriodUnitId, aggregateDemandBudget, demandIntroduction, 0);
                                                                } else {
                                                                    showToast("需求介绍不够字数");
                                                                }
                                                            } else {
                                                                showToast("请填写需求介绍");
                                                            }
                                                        } else {
                                                            showToast("雇用价格不能大于最大值：￥" + String.valueOf(mDemandMaxBudget));
                                                        }
                                                    } else {
                                                        showToast("请填写正确的雇用价格");
                                                    }
                                                } else {
                                                    showToast("请输入正确的雇佣周期");
                                                }
                                            } else {
                                                showToast("请选择预约工作时间");
                                            }
                                        }
                                    } else {
                                        showToast("请选择雇佣方式");
                                    }
                                }
                            } else {
                                showToast("请选择需求品类");
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onShowServiceCategoryList(List<ServiceCategoryListBean> serviceCategoryList) {
        if (null != serviceCategoryList && serviceCategoryList.size() > 0) {
            this.serviceCategoryList = serviceCategoryList;
            publishDemandAdapter.setServiceCategoryList(this.serviceCategoryList);

            switch (publishDemandType) {
                case CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON:
                    List<String> subclassNameList = new ArrayList<>();
                    Set<Integer> isSelectedSet = new HashSet<>();
                    List<Integer> isSelectedCategoryIdList = new ArrayList<>();

                    for (int i = 0; i < this.serviceCategoryList.size(); i++) {
                        ServiceCategoryListBean serviceCategoryListBean = this.serviceCategoryList.get(i);
                        String subclassName = TypeUtils.getString(serviceCategoryListBean.name, "");
                        subclassNameList.add(subclassName);
                    }

                    PublishServiceBean publishServiceBean = new PublishServiceBean();
                    publishServiceBean.isShow = true;
                    publishServiceBean.isRequired = true;
                    publishServiceBean.name = "需求品类（单选）";
                    publishServiceBean.maxSelectCount = 1;
                    publishServiceBean.subclassNameList = subclassNameList;
                    publishServiceBean.isSelectedSet = isSelectedSet;
                    publishServiceBean.isSelectedCategoryIdList = isSelectedCategoryIdList;
                    publishServiceList.add(publishServiceBean);
                    publishDemandAdapter.setDateList(publishServiceList);
                    break;

                case CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE:
                    for (int i = 0; i < serviceCategoryList.size(); i++) {
                        ServiceCategoryListBean serviceCategoryListBean = serviceCategoryList.get(i);

                        if (null != serviceCategoryListBean) {
                            int categoryId = TypeUtils.getInteger(serviceCategoryListBean.code, -1);

                            if (categoryId == this.categoryId) {
                                List<ServiceCategoryListBean.ServiceCategoryFirstBean> serviceCategoryFirstList = serviceCategoryListBean.sub;

                                if (null != serviceCategoryFirstList && serviceCategoryFirstList.size() > 0) {
                                    for (int j = 0; j < serviceCategoryFirstList.size(); j++) {
                                        ServiceCategoryListBean.ServiceCategoryFirstBean serviceCategoryFirstBean = serviceCategoryFirstList.get(j);
                                        int subclassCategoryId = TypeUtils.getInteger(serviceCategoryFirstBean.code, -1);
                                        int mustSelect = TypeUtils.getInteger(serviceCategoryFirstBean.mustSelect, 0);
                                        String firstName = TypeUtils.getString(serviceCategoryFirstBean.name, "");
                                        int maxSelectLen = TypeUtils.getInteger(serviceCategoryFirstBean.maxSelectLen, 0);
                                        List<ServiceCategoryListBean.ServiceCategoryFirstBean.ServiceCategorySecondBean> serviceCategorySecondList = serviceCategoryFirstList.get(j).sub;

                                        if (null != serviceCategorySecondList && serviceCategorySecondList.size() > 0) {
                                            List<String> tempSubclassNameList = new ArrayList<>();
                                            Set<Integer> tempIsSelectedSet = new HashSet<>();
                                            List<Integer> tempIsSelectedCategoryIdList = new ArrayList<>();

                                            for (int k = 0; k < serviceCategorySecondList.size(); k++) {
                                                ServiceCategoryListBean.ServiceCategoryFirstBean.ServiceCategorySecondBean serviceCategorySecondBean = serviceCategorySecondList.get(k);
                                                String secondName = TypeUtils.getString(serviceCategorySecondBean.name, "");
                                                tempSubclassNameList.add(secondName);
                                            }

                                            PublishServiceBean tempPublishServiceBean = new PublishServiceBean();
                                            tempPublishServiceBean.isShow = false;

                                            if (mustSelect == 0) {
                                                tempPublishServiceBean.isRequired = false;
                                            } else {
                                                tempPublishServiceBean.isRequired = true;

                                                if (null != publishDemandAdapter) {
                                                    publishDemandAdapter.addMustSelectedCategoryId(subclassCategoryId);
                                                }
                                            }

                                            tempPublishServiceBean.categoryId = subclassCategoryId;

                                            if (maxSelectLen == 1) {
                                                tempPublishServiceBean.name = firstName + "（单选）";
                                            } else if (maxSelectLen == 100) {
                                                tempPublishServiceBean.name = firstName + "（多选）";
                                            } else {
                                                tempPublishServiceBean.name = firstName + "（" + maxSelectLen + "个）";
                                            }

                                            tempPublishServiceBean.maxSelectCount = maxSelectLen;
                                            tempPublishServiceBean.subclassNameList = tempSubclassNameList;
                                            tempPublishServiceBean.isSelectedSet = tempIsSelectedSet;
                                            tempPublishServiceBean.isSelectedCategoryIdList = tempIsSelectedCategoryIdList;
                                            publishServiceList.add(tempPublishServiceBean);
                                        }
                                    }

                                    publishDemandAdapter.setDateList(publishServiceList);
                                    publishDemandAdapter.setFirstServiceCategoryIndex(i);
                                }
                            }
                        }
                    }

                    mPresenter.fetchServiceModeAndPriceModeRequest(this.categoryId);
                    break;
            }
        }
    }

    @Override
    public void onShowServiceModeAndPriceModeList(int demandMaxBudget, int maxEmploymentTimes, List<ServiceModeAndPriceModeBean> serviceModeAndPriceModeList) {
        mDemandMaxBudget = demandMaxBudget;

        if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_COMMON) {
            publishDemandAdapter.setServiceModeAndPriceModeList(serviceModeAndPriceModeList, null, null, demandMaxBudget, maxEmploymentTimes);
        } else if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE) {
            if (null != serviceModeAndPriceModeList && serviceModeAndPriceModeList.size() > 0) {
                List<ServiceModeAndPriceModeBean> tempServiceModeAndPriceModeList = new ArrayList<>();

                for (int i = 0; i < serviceModeAndPriceModeList.size(); i++) {
                    ServiceModeAndPriceModeBean serviceModeAndPriceModeBean = serviceModeAndPriceModeList.get(i);

                    if (null != serviceModeAndPriceModeBean) {
                        int serveWayId = TypeUtils.getInteger(serviceModeAndPriceModeBean.serveWayId, -1);

                        if (null != serviceWayIdArrayList && serviceWayIdArrayList.size() > 0) {
                            for (int j = 0; j < serviceWayIdArrayList.size(); j++) {
                                int serviceWayId = serviceWayIdArrayList.get(j);

                                if (serviceWayId == serveWayId) {
                                    tempServiceModeAndPriceModeList.add(serviceModeAndPriceModeBean);
                                }
                            }
                        }
                    }
                }

                publishDemandAdapter.setServiceModeAndPriceModeList(tempServiceModeAndPriceModeList, serviceWayIdArrayList, servicePriceArrayList, demandMaxBudget, maxEmploymentTimes);
            }
        }
    }

    @Override
    public void onShowAddressList(List<IsAllBean> addressList) {
        if (null != addressList && addressList.size() > 0) {
            if (null != publishDemandAdapter) {
                publishDemandAdapter.setAddressList(addressList);
            }
        }

        mPresenter.fetchDemandCategoryListRequest();
    }

    @Override
    public void onPublishDemandSuccess(int demandId, String IMMessage) {
        this.demandId = demandId;

        if (publishDemandType == CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE_ONE_TO_ONE_SUBSCRIBE && sendUserId != -1) {
//            sendMessage(sendUserId, this.demandId, IMMessage);

            int aggregateDemandBudget = publishDemandAdapter.getAggregateDemandBudget();

            Intent intent = new Intent(getParentContext(), TrusteeshipPayActivity.class);
            intent.putExtra(CommonConstant.EXTRA_USER_ID, sendUserId);
            intent.putExtra(CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE, publishDemandType);
            intent.putExtra(CommonConstant.EXTRA_RECOMMEND_MORE_STATUS, CommonConstant.DEMAND_IS_SHARE);
            intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
            intent.putExtra(CommonConstant.EXTRA_DEMAND_NAME, serviceCategory);
            intent.putExtra(CommonConstant.EXTRA_TRUSTEESHIP_AMOUNT, aggregateDemandBudget);
            startActivity(intent);

            finish();
        } else {
            int aggregateDemandBudget = publishDemandAdapter.getAggregateDemandBudget();

            Intent intent = new Intent(getParentContext(), TrusteeshipPayActivity.class);
            intent.putExtra(CommonConstant.EXTRA_USER_ID, sendUserId);
            intent.putExtra(CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE, publishDemandType);
            intent.putExtra(CommonConstant.EXTRA_RECOMMEND_MORE_STATUS, CommonConstant.DEMAND_IS_SHARE);
            intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, this.demandId);
            intent.putExtra(CommonConstant.EXTRA_DEMAND_NAME, serviceCategory);
            intent.putExtra(CommonConstant.EXTRA_TRUSTEESHIP_AMOUNT, aggregateDemandBudget);
            startActivity(intent);

            finish();
        }
    }

    @Override
    public void onPublishPaymentDemandSuccess(String serveBidNo, double totalAmount) {
        Intent intent = new Intent(getParentContext(), TrusteeshipPayActivity.class);
        intent.putExtra(CommonConstant.EXTRA_USER_ID, sendUserId);
        intent.putExtra(CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE, publishDemandType);
        intent.putExtra(CommonConstant.EXTRA_RECOMMEND_MORE_STATUS, CommonConstant.DEMAND_IS_NOT_SHARE);
        intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
        intent.putExtra(CommonConstant.EXTRA_DEMAND_NAME, serviceCategory);
        intent.putExtra(CommonConstant.EXTRA_TRUSTEESHIP_AMOUNT, (int) totalAmount);
        intent.putExtra(CommonConstant.EXTRA_SERVICE_BID_NUMBER, serveBidNo);
        startActivity(intent);
    }

    @Override
    public void onShowDemandPeriodList(List<DemandPeriodListBean> demandPeriodList) {
        if (null != publishDemandAdapter) {
            publishDemandAdapter.setDemandPeriodList(demandPeriodList);
            mPresenter.fetchDemandCategoryListRequest();
        }
    }

    @Override
    public void onShowUserInfoBean(UserInfoResponseBean userInfoResponseBean) {
        if (null != userInfoResponseBean) {
            String name = TypeUtils.getString(userInfoResponseBean.name, "");
            String avatarUrl = TypeUtils.getString(userInfoResponseBean.avatarUrl, "");

            if (sendUserId != -1) {
                EaseUser easeUser = new EaseUser(String.valueOf(sendUserId));
                easeUser.setNickname(name);
                String headPortraitUrl = null;

                if (!TextUtils.isEmpty(avatarUrl)) {
                    headPortraitUrl = "https:" + avatarUrl;
                }

                easeUser.setAvatar(headPortraitUrl);

                Map<String, EaseUser> contactList = EMChatHelper.getInstance().getContactList();
                contactList.put(String.valueOf(sendUserId), easeUser);

                EMChatHelper.getInstance().saveContact(easeUser);
                EMChatHelper.getInstance().getModel().setContactSynced(true);
                EMChatHelper.getInstance().notifyContactsSyncListener(true);

                int aggregateDemandBudget = publishDemandAdapter.getAggregateDemandBudget();

                Intent intent = new Intent(getParentContext(), TrusteeshipPayActivity.class);
                intent.putExtra(CommonConstant.EXTRA_USER_ID, sendUserId);
                intent.putExtra(CommonConstant.EXTRA_PUBLISH_DEMAND_TYPE, publishDemandType);
                intent.putExtra(CommonConstant.EXTRA_RECOMMEND_MORE_STATUS, CommonConstant.DEMAND_IS_SHARE);
                intent.putExtra(CommonConstant.EXTRA_DEMAND_ID, demandId);
                intent.putExtra(CommonConstant.EXTRA_DEMAND_NAME, serviceCategory);
                intent.putExtra(CommonConstant.EXTRA_TRUSTEESHIP_AMOUNT, aggregateDemandBudget);
                startActivity(intent);

                finish();
            }
        }
    }

}
