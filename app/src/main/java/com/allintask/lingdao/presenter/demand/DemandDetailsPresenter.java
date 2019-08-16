package com.allintask.lingdao.presenter.demand;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.demand.RecommendDemandDetailsBean;
import com.allintask.lingdao.bean.demand.RecommendDemandDetailsUserInformationBean;
import com.allintask.lingdao.bean.service.BidInformationBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.demand.DemandModel;
import com.allintask.lingdao.model.demand.IDemandModel;
import com.allintask.lingdao.model.service.IServiceModel;
import com.allintask.lingdao.model.service.ServiceModel;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.demand.IDemandDetailsView;

import java.util.Date;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/5/3.
 */

public class DemandDetailsPresenter extends BasePresenter<IDemandDetailsView> {

    private IDemandModel demandModel;
    private IUserModel userModel;
    private IServiceModel serviceModel;

    private int userId;
    private int myUserId;
    private int demandId;

    public DemandDetailsPresenter() {
        demandModel = new DemandModel();
        userModel = new UserModel();
        serviceModel = new ServiceModel();
    }

    public void fetchRecommendDemandDetailsRequest(int demandId) {
        OkHttpRequest fetchRecommendDemandDetailsRequest = demandModel.fetchRecommendDemandDetailsRequest(demandId);
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchRecommendDemandDetailsRequest);
    }

    public void fetchRecommendDemandDetailsUserInformationRequest(int userId) {
        this.userId = userId;
        OkHttpRequest fetchRecommendDemandDetailsUserInformationRequest = demandModel.fetchRecommendDemandDetailsUserInformationRequest(this.userId);
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchRecommendDemandDetailsUserInformationRequest);
    }

    public void fetchBidInformationRequest(int categoryId) {
        OkHttpRequest fetchBidInformationRequest = serviceModel.fetchBidInformationRequest(categoryId);
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchBidInformationRequest);
    }

    public void bidRequest(int buyerUserId, int demandId, String price, String advantage, int isBook) {
        userId = buyerUserId;
        this.demandId = demandId;
        OkHttpRequest saveBidRequest = serviceModel.bidRequest(demandId, price, advantage, isBook);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, saveBidRequest);
    }

    public void collectDemandRequest(int demandId) {
        OkHttpRequest collectDemandRequest = userModel.collectDemandRequest(demandId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, collectDemandRequest);
    }

    public void cancelCollectDemandRequest(int demandId) {
        this.demandId = demandId;
        myUserId = UserPreferences.getInstance().getUserId();

        if (myUserId != -1) {
            OkHttpRequest cancelCollectDemandRequest = userModel.cancelCollectDemandRequest(myUserId, this.demandId);
            addRequestAsyncTaskForJson(false, OkHttpRequestOptions.DELETE, cancelCollectDemandRequest);
        }
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_DETAILS)) {
            if (success) {
                RecommendDemandDetailsBean recommendDemandDetailsBean = JSONObject.parseObject(data, RecommendDemandDetailsBean.class);

                if (null != recommendDemandDetailsBean) {
                    int userId = TypeUtils.getInteger(recommendDemandDetailsBean.userId, -1);
                    int categoryId = TypeUtils.getInteger(recommendDemandDetailsBean.categoryId, -1);
                    boolean isStoreUp = TypeUtils.getBoolean(recommendDemandDetailsBean.isStoreUp, false);
                    String categoryName = TypeUtils.getString(recommendDemandDetailsBean.categoryName, "");
                    int demandStatus = TypeUtils.getInteger(recommendDemandDetailsBean.demandStatus, -1);
                    Integer bidCount = recommendDemandDetailsBean.bidCount;
                    Integer buyCount = recommendDemandDetailsBean.buyCount;
                    Integer viewCount = recommendDemandDetailsBean.viewCount;
                    String publishTip = TypeUtils.getString(recommendDemandDetailsBean.publishTip, "");
                    String expireTip = TypeUtils.getString(recommendDemandDetailsBean.expireTip, "");
                    String serveWay = TypeUtils.getString(recommendDemandDetailsBean.serveWay, "");
                    Integer budget = recommendDemandDetailsBean.budget;
                    int isTrusteeship = TypeUtils.getInteger(recommendDemandDetailsBean.isTrusteeship, 0);
                    List<RecommendDemandDetailsBean.CategoryPropertyChineseBean> categoryPropertyChineseList = recommendDemandDetailsBean.categoryPropertyChineseList;
                    String province = TypeUtils.getString(recommendDemandDetailsBean.province, "");
                    String city = TypeUtils.getString(recommendDemandDetailsBean.city, "");
                    Date bookingDate = recommendDemandDetailsBean.bookingDate;
                    String deliverCycleValue = TypeUtils.getString(recommendDemandDetailsBean.deliverCycleValue, "");
                    String introduce = TypeUtils.getString(recommendDemandDetailsBean.introduce, "");
                    boolean isBid = TypeUtils.getBoolean(recommendDemandDetailsBean.isBid, false);
                    boolean isBidServe = TypeUtils.getBoolean(recommendDemandDetailsBean.isBidServe, false);

                    if (null == bidCount) {
                        bidCount = 0;
                    }

                    if (null == buyCount) {
                        buyCount = 0;
                    }

                    if (null == viewCount) {
                        viewCount = 0;
                    }

                    if (null == budget) {
                        budget = 0;
                    }

                    StringBuilder categoryPropertyChineseSB = new StringBuilder();

                    if (null != categoryPropertyChineseList && categoryPropertyChineseList.size() > 0) {
                        for (int i = 0; i < categoryPropertyChineseList.size(); i++) {
                            RecommendDemandDetailsBean.CategoryPropertyChineseBean categoryPropertyChineseBean = categoryPropertyChineseList.get(i);

                            if (null != categoryPropertyChineseBean) {
                                String categoryProperty = TypeUtils.getString(categoryPropertyChineseBean.categoryProperty, "");
                                List<String> valuesList = categoryPropertyChineseBean.values;

                                categoryPropertyChineseSB.append(categoryProperty).append("：");

                                if (null != valuesList && valuesList.size() > 0) {
                                    for (int j = 0; j < valuesList.size(); j++) {
                                        String value = valuesList.get(j);
                                        categoryPropertyChineseSB.append(value);

                                        if (j != valuesList.size() - 1) {
                                            categoryPropertyChineseSB.append("、");
                                        }
                                    }
                                }

                                if (i < categoryPropertyChineseList.size() - 1) {
                                    categoryPropertyChineseSB.append("\n");
                                }
                            }
                        }
                    }

                    StringBuilder addressSB = new StringBuilder();

                    if (!TextUtils.isEmpty(province)) {
                        addressSB.append(province);

                        if (!TextUtils.isEmpty(city) && !province.equals(city)) {
                            addressSB.append(city);
                        }
                    }

                    String subscribeStartTime = null;

                    if (null != bookingDate) {
                        subscribeStartTime = CommonConstant.subscribeTimeFormat.format(bookingDate);
                    }

                    if (null != getView()) {
                        getView().onShowUserId(userId);
                        getView().onShowCategoryId(categoryId);
                        getView().onShowIsCollected(isStoreUp);
                        getView().onShowCategoryName(categoryName);
                        getView().onShowDemandStatus(demandStatus);
                        getView().onShowHasBidAmount(bidCount);
                        getView().onShowHasPayAmount(buyCount);
                        getView().onShowHasBrowseAmount(viewCount);
                        getView().onShowPublishTip(publishTip);
                        getView().onShowExpireTip(expireTip);
                        getView().onShowServiceWay(serveWay);
                        getView().onShowBudget(budget);
                        getView().onShowIsTrusteeship(isTrusteeship);
                        getView().onShowCategoryPropertyChineseString(categoryPropertyChineseSB.toString());
                        getView().onShowAddress(addressSB.toString());
                        getView().onShowSubscribeStartTime(subscribeStartTime);
                        getView().onShowDeliveryCycle(deliverCycleValue);
                        getView().onShowDemandIntroduction(introduce);
                        getView().onShowIsHasBid(isBid);
                        getView().onShowIsBidServe(isBidServe);
                    }
                } else {
                    getView().showEmptyView();
                }
            } else {
                if (null != getView()) {
                    getView().setRefresh(false);
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_INFO_TO_DEMAND_DETAILS + "/" + String.valueOf(userId))) {
            if (success) {
                RecommendDemandDetailsUserInformationBean recommendDemandDetailsUserInformationBean = JSONObject.parseObject(data, RecommendDemandDetailsUserInformationBean.class);

                if (null != recommendDemandDetailsUserInformationBean) {
                    String avatarUrl = TypeUtils.getString(recommendDemandDetailsUserInformationBean.avatarUrl, "");
                    String realName = TypeUtils.getString(recommendDemandDetailsUserInformationBean.realName, "");
                    String loginTimeTip = TypeUtils.getString(recommendDemandDetailsUserInformationBean.loginTimeTip, "");

                    String userHeadPortraitUrl = null;

                    if (!TextUtils.isEmpty(avatarUrl)) {
                        userHeadPortraitUrl = "https:" + avatarUrl;
                    }

                    if (null != getView()) {
                        getView().onShowUserHeadPortraitUrl(userHeadPortraitUrl);
                        getView().onShowName(realName);
                        getView().onShowTime(loginTimeTip);
                        getView().showContentView();
                    }
                }
            } else {
                getView().showToast(errorMessage);
            }

            if (null != getView()) {
                getView().setRefresh(false);
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_GET_BY_CATEGORY_ID)) {
            if (success) {
                BidInformationBean bidInformationBean = JSONObject.parseObject(data, BidInformationBean.class);

                if (null != bidInformationBean) {
                    String advantage = TypeUtils.getString(bidInformationBean.advantage, "");
                    Integer tempIsBook = bidInformationBean.isBook;

                    int isBook = 0;

                    if (null != tempIsBook) {
                        isBook = tempIsBook;
                    }

                    if (null != getView()) {
                        getView().onShowAdvantageAndIsBook(advantage, isBook);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().setRefresh(false);
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_BID_SAVE)) {
            if (success) {
                if (null != getView()) {
                    getView().onBidSuccess(userId, demandId);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_STORE_UP_SAVE)) {
            if (success) {
                if (null != getView()) {
                    getView().onCollectDemandSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("收藏失败");
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_STORE_UP_CANCEL + "/" + String.valueOf(myUserId) + "/" + String.valueOf(demandId))) {
            if (success) {
                if (null != getView()) {
                    getView().onCancelCollectDemandSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("取消收藏失败");
                }
            }
        }
    }

}
