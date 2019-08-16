package com.allintask.lingdao.presenter.demand;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.demand.ApplyForRefundReasonBean;
import com.allintask.lingdao.bean.demand.DemandCompletedListBean;
import com.allintask.lingdao.bean.demand.DemandDetailsBean;
import com.allintask.lingdao.bean.demand.DemandExpiredListBean;
import com.allintask.lingdao.bean.demand.DemandInTheBiddingListBean;
import com.allintask.lingdao.bean.demand.DemandUnderwayListBean;
import com.allintask.lingdao.bean.demand.ShowDemandDetailsBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.demand.DemandModel;
import com.allintask.lingdao.model.demand.IDemandModel;
import com.allintask.lingdao.model.order.IOrderModel;
import com.allintask.lingdao.model.order.OrderModel;
import com.allintask.lingdao.model.service.IServiceModel;
import com.allintask.lingdao.model.service.ServiceModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.demand.IEmployerDemandDetailsView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/16.
 */

public class EmployerDemandDetailsPresenter extends BasePresenter<IEmployerDemandDetailsView> {

    private IDemandModel demandModel;
    private IServiceModel serviceModel;
    private IOrderModel orderModel;

    private List<DemandInTheBiddingListBean.DemandInTheBiddingBean> demandInTheBiddingList;
    private List<DemandUnderwayListBean.DemandUnderwayBean> demandUnderWayList;
    private List<DemandCompletedListBean.DemandCompletedBean> demandCompletedList;
    private List<DemandExpiredListBean.DemandExpiredBean> demandExpiredList;

    private String tag;
    private int pageNumber = CommonConstant.DEFAULT_PAGE_NUMBER;
    private int userId;
    private int demandId;

    public EmployerDemandDetailsPresenter() {
        demandModel = new DemandModel();
        serviceModel = new ServiceModel();
        orderModel = new OrderModel();

        demandInTheBiddingList = new ArrayList<>();
        demandUnderWayList = new ArrayList<>();
        demandCompletedList = new ArrayList<>();
        demandExpiredList = new ArrayList<>();
    }

    public void fetchDemandDetailsRequest(int demandId) {
//        switch (demandStatus) {
//            case CommonConstant.DEMAND_STATUS_IN_THE_BIDDING:
//                tag = CommonConstant.DEMAND_STATUS_IN_THE_BIDDING_STRING;
//                break;
//
//            case CommonConstant.DEMAND_STATUS_UNDERWAY:
//                tag = CommonConstant.DEMAND_STATUS_UNDERWAY_STRING;
//                break;
//
//            case CommonConstant.DEMAND_STATUS_COMPLETED:
//                tag = CommonConstant.DEMAND_STATUS_COMPLETED_STRING;
//                break;
//
//            case CommonConstant.DEMAND_STATUS_EXPIRED:
//                tag = CommonConstant.DEMAND_STATUS_EXPIRED_STRING;
//                break;
//        }

        OkHttpRequest fetchDemandDetailsRequest = demandModel.fetchDemandDetailsRequest(demandId);
        addRequestAsyncTaskForJsonNoProgress(true, OkHttpRequestOptions.GET, fetchDemandDetailsRequest);
    }

    private void fetchBuyerHasBidServiceListRequest(boolean isRefresh, int demandId) {
        if (isRefresh) {
            demandInTheBiddingList.clear();
        }

        OkHttpRequest fetchBuyerHasBidServiceListRequest = serviceModel.fetchBuyerHasBidServiceListRequest(pageNumber, demandId);
        addRequestAsyncTaskForJsonNoProgress(true, OkHttpRequestOptions.GET, fetchBuyerHasBidServiceListRequest);
    }

    private void fetchBuyerUnderwayServiceListRequest(boolean isRefresh, int demandId) {
        if (isRefresh) {
            demandUnderWayList.clear();
        }

        OkHttpRequest fetchBuyerUnderwayServiceList = orderModel.fetchBuyerUnderwayServiceList(pageNumber, demandId);
        addRequestAsyncTaskForJsonNoProgress(true, OkHttpRequestOptions.GET, fetchBuyerUnderwayServiceList);
    }

    private void fetchBuyerCompletedServiceListRequest(boolean isRefresh, int demandId) {
        if (isRefresh) {
            demandCompletedList.clear();
        }

        OkHttpRequest fetchBuyerCompletedServiceListRequest = orderModel.fetchBuyerCompletedServiceListRequest(pageNumber, demandId);
        addRequestAsyncTaskForJsonNoProgress(true, OkHttpRequestOptions.GET, fetchBuyerCompletedServiceListRequest);
    }

    private void fetchBuyerExpiredServiceListRequest(boolean isRefresh, int demandId) {
        if (isRefresh) {
            demandExpiredList.clear();
        }

        OkHttpRequest fetchBuyerExpiredServiceListRequest = serviceModel.fetchBuyerExpiredServiceListRequest(pageNumber, demandId);
        addRequestAsyncTaskForJsonNoProgress(true, OkHttpRequestOptions.GET, fetchBuyerExpiredServiceListRequest);
    }

    private ShowDemandDetailsBean getShowDemandDetailsBean(String title, String content, int color) {
        ShowDemandDetailsBean showDemandDetailsBean = new ShowDemandDetailsBean();
        showDemandDetailsBean.title = title;
        showDemandDetailsBean.content = content;
        showDemandDetailsBean.color = color;
        return showDemandDetailsBean;
    }

    public void refresh(int demandStatus, int demandId) {
        if (!getView().isLoadingMore()) {
            pageNumber = CommonConstant.DEFAULT_PAGE_NUMBER;
            getView().setRefresh(true);

            switch (demandStatus) {
                case CommonConstant.DEMAND_STATUS_IN_THE_BIDDING:
                    fetchBuyerHasBidServiceListRequest(true, demandId);
                    break;

                case CommonConstant.DEMAND_STATUS_UNDERWAY:
                    fetchBuyerUnderwayServiceListRequest(true, demandId);
                    break;

                case CommonConstant.DEMAND_STATUS_COMPLETED:
                    fetchBuyerCompletedServiceListRequest(true, demandId);
                    break;

                case CommonConstant.DEMAND_STATUS_EXPIRED:
                    fetchBuyerExpiredServiceListRequest(true, demandId);
                    break;
            }
        } else {
            getView().setRefresh(false);
        }
    }

    public void loadMore(int demandStatus, int demandId) {
        if (!getView().isRefreshing()) {
            getView().setLoadMore(true);

            switch (demandStatus) {
                case CommonConstant.DEMAND_STATUS_IN_THE_BIDDING:
                    fetchBuyerHasBidServiceListRequest(false, demandId);
                    break;

                case CommonConstant.DEMAND_STATUS_UNDERWAY:
                    fetchBuyerUnderwayServiceListRequest(false, demandId);
                    break;

                case CommonConstant.DEMAND_STATUS_COMPLETED:
                    fetchBuyerCompletedServiceListRequest(false, demandId);
                    break;

                case CommonConstant.DEMAND_STATUS_EXPIRED:
                    fetchBuyerExpiredServiceListRequest(false, demandId);
                    break;
            }
        } else {
            getView().setLoadMore(false);
        }
    }

    public void buyerConfirmStartWorkRequest(int userId, int demandId, int orderId, Date startWorkAt) {
        this.userId = userId;
        this.demandId = demandId;

        OkHttpRequest buyerConfirmStartWorkRequest = orderModel.buyerConfirmStartWorkRequest(orderId, startWorkAt);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, buyerConfirmStartWorkRequest);
    }

    public void buyerConfirmCompleteWorkRequest(int userId, int demandId, int orderId) {
        this.userId = userId;
        this.demandId = demandId;

        OkHttpRequest buyerConfirmCompleteWorkRequest = orderModel.buyerConfirmCompleteWorkRequest(orderId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, buyerConfirmCompleteWorkRequest);
    }

    public void buyerDelayCompleteWorkRequest(int userId, int demandId, int orderId) {
        this.userId = userId;
        this.demandId = demandId;

        OkHttpRequest buyerDelayCompleteWorkRequest = orderModel.buyerDelayCompleteWorkRequest(orderId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, buyerDelayCompleteWorkRequest);
    }

    public void fetchRefundReasonListRequest(int userId, int demandId) {
        this.userId = userId;
        this.demandId = demandId;

        OkHttpRequest fetchRefundReasonListRequest = orderModel.fetchRefundReasonListRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchRefundReasonListRequest);
    }

    public void applyRefundRequest(int userId, int demandId, int orderId, int reasonId, String reasonTag) {
        this.userId = userId;
        this.demandId = demandId;

        OkHttpRequest applyRefundRequest = demandModel.applyForRefundRequest(orderId, reasonTag, reasonId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, applyRefundRequest);
    }

    public void goOnlineDemandRequest(int demandId) {
        this.demandId = demandId;
        OkHttpRequest goOnlineDemandRequest = demandModel.goOnlineDemandRequest(this.demandId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.PUT, goOnlineDemandRequest);
    }

    public void goOfflineDemandRequest(int demandId) {
        this.demandId = demandId;
        OkHttpRequest goOfflineDemandRequest = demandModel.goOfflineDemandRequest(this.demandId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.PUT, goOfflineDemandRequest);
    }

    public void deleteDemandRequest(int demandId) {
        this.demandId = demandId;
        OkHttpRequest deleteDemandRequest = demandModel.deleteDemandRequest(demandId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.DELETE, deleteDemandRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_DETAILS_TO_BUYER)) {
            if (success) {
                DemandDetailsBean demandDetailsBean = JSONObject.parseObject(data, DemandDetailsBean.class);

                if (null != demandDetailsBean) {
                    int demandStatus = TypeUtils.getInteger(demandDetailsBean.demandStatus, -1);
                    String categoryIconUrl = TypeUtils.getString(demandDetailsBean.categoryIconUrl, "");
                    String categoryName = TypeUtils.getString(demandDetailsBean.categoryName, "");
                    String expiredTip = TypeUtils.getString(demandDetailsBean.expiredTip, "");
                    int salaryTrusteeship = TypeUtils.getInteger(demandDetailsBean.salaryTrusteeship, 0);
                    Date bookingDate = demandDetailsBean.bookingDate;
                    String deliverCycleValue = TypeUtils.getString(demandDetailsBean.deliverCycleValue, "");
                    List<DemandDetailsBean.CategoryPropertyChineseListBean> categoryPropertyChineseList = demandDetailsBean.categoryPropertyChineseList;
                    Date createAt = demandDetailsBean.createAt;
                    int expireDuration = TypeUtils.getInteger(demandDetailsBean.expireDuration, 0);
                    String serveWay = TypeUtils.getString(demandDetailsBean.serveWay, "");
                    String province = TypeUtils.getString(demandDetailsBean.province, "");
                    String city = TypeUtils.getString(demandDetailsBean.city, "");
                    int budget = TypeUtils.getInteger(demandDetailsBean.budget, 0);
                    String introduce = TypeUtils.getString(demandDetailsBean.introduce, "");
                    int auditCode = TypeUtils.getInteger(demandDetailsBean.auditCode, 0);
                    int onOffLine = TypeUtils.getInteger(demandDetailsBean.onOffLine, 0);
                    int isShare = TypeUtils.getInteger(demandDetailsBean.isShare, 0);

                    List<ShowDemandDetailsBean> showDemandDetailsList = new ArrayList<>();

                    if (null != getView() && auditCode == 2) {
                        showDemandDetailsList.add(getShowDemandDetailsBean(getView().getParentContext().getString(R.string.audit_status), getView().getParentContext().getString(R.string.audit_failure), getView().getParentContext().getResources().getColor(R.color.text_red)));
                    }

                    if (null != getView() && salaryTrusteeship != 0) {
                        showDemandDetailsList.add(getShowDemandDetailsBean(getView().getParentContext().getString(R.string.trusteeship_amount), String.valueOf(salaryTrusteeship) + "元", getView().getParentContext().getResources().getColor(R.color.text_orange)));
                    }

                    if (null != getView() && null != bookingDate) {
                        String subscribeTime = CommonConstant.subscribeTimeFormat.format(bookingDate);
                        showDemandDetailsList.add(getShowDemandDetailsBean(getView().getParentContext().getString(R.string.subscribe_start_time), subscribeTime, getView().getParentContext().getResources().getColor(R.color.text_dark_black)));
                    }

                    if (null != getView() && !TextUtils.isEmpty(deliverCycleValue)) {
                        showDemandDetailsList.add(getShowDemandDetailsBean(getView().getParentContext().getString(R.string.employment_period), deliverCycleValue, getView().getParentContext().getResources().getColor(R.color.text_dark_black)));
                    }

                    if (null != getView() && null != categoryPropertyChineseList && categoryPropertyChineseList.size() > 0) {
                        for (int i = 0; i < categoryPropertyChineseList.size(); i++) {
                            DemandDetailsBean.CategoryPropertyChineseListBean categoryPropertyChineseListBean = categoryPropertyChineseList.get(i);

                            if (null != categoryPropertyChineseListBean) {
                                String categoryProperty = TypeUtils.getString(categoryPropertyChineseListBean.categoryProperty, "");
                                List<String> valuesList = categoryPropertyChineseListBean.values;

                                StringBuilder stringBuilder = new StringBuilder();

                                if (null != valuesList && valuesList.size() > 0) {
                                    for (int j = 0; j < valuesList.size(); j++) {
                                        String value = valuesList.get(j);
                                        stringBuilder.append(value);

                                        if (j != valuesList.size() - 1) {
                                            stringBuilder.append("、");
                                        }
                                    }
                                }

                                showDemandDetailsList.add(getShowDemandDetailsBean(categoryProperty, stringBuilder.toString(), getView().getParentContext().getResources().getColor(R.color.text_dark_black)));
                            }
                        }
                    }

                    if (null != getView() && null != createAt) {
                        String publishTime = CommonConstant.commonTimeFormat.format(createAt);
                        showDemandDetailsList.add(getShowDemandDetailsBean(getView().getParentContext().getString(R.string.publish_time), publishTime, getView().getParentContext().getResources().getColor(R.color.text_dark_black)));
                    }

                    if (null != getView() && demandStatus != 20 && demandStatus != 30 && expireDuration != 0) {
                        showDemandDetailsList.add(getShowDemandDetailsBean(getView().getParentContext().getString(R.string.demand_valid_time), String.valueOf(expireDuration) + "天", getView().getParentContext().getResources().getColor(R.color.text_dark_black)));
                    }

                    if (null != getView() && !TextUtils.isEmpty(serveWay)) {
                        showDemandDetailsList.add(getShowDemandDetailsBean(getView().getParentContext().getString(R.string.work_way), serveWay, getView().getParentContext().getResources().getColor(R.color.text_dark_black)));
                    }

                    StringBuilder stringBuilder = new StringBuilder();

                    if (null != getView() && !TextUtils.isEmpty(province)) {
                        stringBuilder.append(province);

                        if (!TextUtils.isEmpty(city) && !province.equals(city)) {
                            stringBuilder.append(city);
                        }

                        showDemandDetailsList.add(getShowDemandDetailsBean(getView().getParentContext().getString(R.string.employment_site), stringBuilder.toString(), getView().getParentContext().getResources().getColor(R.color.text_dark_black)));
                    }

                    if (null != getView() && budget != 0) {
                        showDemandDetailsList.add(getShowDemandDetailsBean(getView().getParentContext().getString(R.string.employment_price), "￥" + String.valueOf(budget), getView().getParentContext().getResources().getColor(R.color.text_dark_black)));
                    }

                    if (null != getView() && !TextUtils.isEmpty(introduce)) {
                        showDemandDetailsList.add(getShowDemandDetailsBean(getView().getParentContext().getString(R.string.employer_demand_details_demand_introduction), introduce, getView().getParentContext().getResources().getColor(R.color.text_dark_black)));
                    }

                    if (null != getView()) {
                        getView().onShowDemandStatus(demandStatus);

                        String userHeadPortraitUrl = null;

                        if (!TextUtils.isEmpty(categoryIconUrl)) {
                            userHeadPortraitUrl = "https:" + categoryIconUrl;
                        }

                        getView().onShowUserHeadPortraitUrl(userHeadPortraitUrl);
                        getView().onShowCategoryName(categoryName);
                        getView().onShowTime(expiredTip);
                        getView().onShowShowDemandDetailsList(showDemandDetailsList);
                        getView().onShowAuditCodeAndOnOffLine(auditCode, onOffLine);
                        getView().onShowIsShare(isShare);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_MAME_GOODS_SERVE_BID_LIST_TO_BUYER_VALID)) {
            if (success) {
                DemandInTheBiddingListBean demandInTheBiddingListBean = JSONObject.parseObject(data, DemandInTheBiddingListBean.class);

                if (null != demandInTheBiddingListBean) {
                    List<DemandInTheBiddingListBean.DemandInTheBiddingBean> demandInTheBiddingList = demandInTheBiddingListBean.list;

                    if (null != demandInTheBiddingList && demandInTheBiddingList.size() > 0) {
                        this.demandInTheBiddingList.addAll(demandInTheBiddingList);
                        pageNumber++;
                    }

                    if (null != getView()) {
                        getView().onShowDemandInTheBiddingList(this.demandInTheBiddingList);
                    }
                } else if (null == this.demandInTheBiddingList || this.demandInTheBiddingList.size() <= 0) {
                    if (null != getView()) {
                        getView().onShowNoServiceProvider();
                    }
                }
            } else {
                if (null != getView()) {
                    getView().onShowNoServiceProvider();
                    getView().showToast(errorMessage);
                }
            }

            if (null != getView()) {
                getView().setRefresh(false);
                getView().setLoadMore(false);
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_LIST_TO_BUYER_ING)) {
            if (success) {
                DemandUnderwayListBean demandUnderwayListBean = JSONObject.parseObject(data, DemandUnderwayListBean.class);

                if (null != demandUnderwayListBean) {
                    List<DemandUnderwayListBean.DemandUnderwayBean> demandUnderwayList = demandUnderwayListBean.list;

                    if (null != demandUnderwayList && demandUnderwayList.size() > 0) {
                        this.demandUnderWayList.addAll(demandUnderwayList);
                        pageNumber++;
                    }

                    if (null != getView()) {
                        getView().onShowDemandUnderwayList(this.demandUnderWayList);
                    }
                } else if (null == this.demandUnderWayList || this.demandUnderWayList.size() <= 0) {
                    if (null != getView()) {
                        getView().onShowNoServiceProvider();
                    }
                }
            } else {
                if (null != getView()) {
                    getView().onShowNoServiceProvider();
                    getView().showToast(errorMessage);
                }
            }

            if (null != getView()) {
                getView().setRefresh(false);
                getView().setLoadMore(false);
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_LIST_TO_BUYER_COMPLETE)) {
            if (success) {
                DemandCompletedListBean demandCompletedListBean = JSONObject.parseObject(data, DemandCompletedListBean.class);

                if (null != demandCompletedListBean) {
                    List<DemandCompletedListBean.DemandCompletedBean> demandCompletedList = demandCompletedListBean.list;

                    if (null != demandCompletedList && demandCompletedList.size() > 0) {
                        this.demandCompletedList.addAll(demandCompletedList);
                        pageNumber++;
                    }

                    if (null != getView()) {
                        getView().onShowDemandCompletedList(this.demandCompletedList);
                    }
                } else {
                    if (null != getView()) {
                        getView().onShowNoServiceProvider();
                    }
                }
            } else if (null == this.demandCompletedList || this.demandCompletedList.size() <= 0) {
                if (null != getView()) {
                    getView().onShowNoServiceProvider();
                }
            }

            if (null != getView()) {
                getView().setRefresh(false);
                getView().setLoadMore(false);
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_BID_LIST_TO_BUYER_EXPIRE)) {
            if (success) {
                DemandExpiredListBean demandExpiredListBean = JSONObject.parseObject(data, DemandExpiredListBean.class);

                if (null != demandExpiredListBean) {
                    List<DemandExpiredListBean.DemandExpiredBean> demandExpiredList = demandExpiredListBean.list;

                    if (null != demandExpiredList && demandExpiredList.size() > 0) {
                        this.demandExpiredList.addAll(demandExpiredList);
                        pageNumber++;
                    }

                    if (null != getView()) {
                        getView().onShowDemandExpiredList(this.demandExpiredList);
                    }
                } else {
                    if (null != getView()) {
                        getView().onShowNoServiceProvider();
                    }
                }
            } else if (null == this.demandExpiredList || this.demandExpiredList.size() <= 0) {
                if (null != getView()) {
                    getView().onShowNoServiceProvider();
                }
            }

            if (null != getView()) {
                getView().setRefresh(false);
                getView().setLoadMore(false);
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_OPERATE_CONFIRM_START_WORK)) {
            if (success) {
                if (null != getView()) {
                    getView().onConfirmStartWorkSuccess(userId, demandId);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_OPERATE_CONFIRM_COMPLETE_WORK)) {
            if (success) {
                if (null != getView()) {
                    getView().onConfirmCompleteWorkSuccess(userId, demandId);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_OPERATE_DELAY_COMPLETE_WORD)) {
            if (success) {
                if (null != getView()) {
                    getView().onDelayCompleteWorkSuccess(userId, demandId);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_OPERATE_REASON_LIST_BUYER_CANCEL_BEFORE_START_WORK)) {
            if (success) {
                List<ApplyForRefundReasonBean> applyForRefundReasonList = JSONArray.parseArray(data, ApplyForRefundReasonBean.class);

                if (null != getView()) {
                    getView().onShowApplyForRefundReasonList(userId, demandId, applyForRefundReasonList);
                }
            } else {
                if (null != getView()) {
                    getView().showToast("请求失败");
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_OPERATE_REFUND)) {
            if (success) {
                if (null != getView()) {
                    getView().onApplyForRefundSuccess(userId, demandId);
                }
            } else {
                if (null != getView()) {
                    getView().showToast("申请退款失败");
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_GO_ONLINE + "/" + String.valueOf(demandId))) {
            if (success) {
                if (null != getView()) {
                    getView().onGoOnlineDemandSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().onGoOnlineDemandFail();
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_GO_OFFLINE + "/" + String.valueOf(demandId))) {
            if (success) {
                if (null != getView()) {
                    getView().onGoOfflineDemandSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().onGoOfflineDemandFail();
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_DELETE + "/" + String.valueOf(demandId))) {
            if (success) {
                if (null != getView()) {
                    getView().onDeleteDemandSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("删除失败");
                }
            }
        }
    }

}
