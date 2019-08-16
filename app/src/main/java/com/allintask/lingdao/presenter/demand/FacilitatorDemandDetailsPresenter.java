package com.allintask.lingdao.presenter.demand;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.service.BidInformationBean;
import com.allintask.lingdao.bean.service.FacilitatorDemandDetailsBean;
import com.allintask.lingdao.bean.service.FacilitatorDemandOrderInformationBean;
import com.allintask.lingdao.bean.service.ShowFacilitatorDemandDetailsBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.order.IOrderModel;
import com.allintask.lingdao.model.order.OrderModel;
import com.allintask.lingdao.model.service.IServiceModel;
import com.allintask.lingdao.model.service.ServiceModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.ui.adapter.demand.IFacilitatorDemandDetailsView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/17.
 */

public class FacilitatorDemandDetailsPresenter extends BasePresenter<IFacilitatorDemandDetailsView> {

    private IServiceModel serviceModel;
    private IOrderModel orderModel;

    private String mPrice;

    public FacilitatorDemandDetailsPresenter() {
        serviceModel = new ServiceModel();
        orderModel = new OrderModel();
    }

    public void fetchFacilitatorDemandDetailsRequest(int demandId) {
        OkHttpRequest fetchFacilitatorDemandDetailsRequest = serviceModel.fetchFacilitatorDemandDetailsRequest(demandId);
        addRequestAsyncTaskForJson(true, OkHttpRequestOptions.GET, fetchFacilitatorDemandDetailsRequest);
    }

    public void fetchFacilitatorDemandOrderInformationRequest(int orderId) {
        OkHttpRequest fetchFacilitatorDemandOrderInformationRequest = orderModel.fetchFacilitatorDemandOrderInformationRequest(orderId);
        addRequestAsyncTaskForJson(true, OkHttpRequestOptions.GET, fetchFacilitatorDemandOrderInformationRequest);
    }

    private ShowFacilitatorDemandDetailsBean getShowDemandDetailsBean(String title, String content) {
        ShowFacilitatorDemandDetailsBean showFacilitatorDemandDetailsBean = new ShowFacilitatorDemandDetailsBean();
        showFacilitatorDemandDetailsBean.title = title;
        showFacilitatorDemandDetailsBean.content = content;
        return showFacilitatorDemandDetailsBean;
    }

    public void fetchBidInformationRequest(int categoryId) {
        OkHttpRequest fetchBidInformationRequest = serviceModel.fetchBidInformationRequest(categoryId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchBidInformationRequest);
    }

    public void bidRequest(int demandId, String price, String advantage, int isBook) {
        OkHttpRequest saveBidRequest = serviceModel.bidRequest(demandId, price, advantage, isBook);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, saveBidRequest);
    }

    public void changePriceRequest(int bidId, String price) {
        mPrice = price;
        OkHttpRequest changePriceRequest = serviceModel.changePriceRequest(bidId, mPrice);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, changePriceRequest);
    }

    public void acceptApplyRefundRequest(int orderId) {
        OkHttpRequest acceptApplyRefundRequest = orderModel.acceptApplyRefundRequest(orderId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, acceptApplyRefundRequest);
    }

    public void refuseApplyRefundRequest(int orderId) {
        OkHttpRequest refuseApplyRefundRequest = orderModel.refuseApplyRefundRequest(orderId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, refuseApplyRefundRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_DETAILS_TO_SELLER)) {
            if (success) {
                FacilitatorDemandDetailsBean facilitatorDemandDetailsBean = JSONObject.parseObject(data, FacilitatorDemandDetailsBean.class);

                if (null != facilitatorDemandDetailsBean) {
                    int demandStatus = TypeUtils.getInteger(facilitatorDemandDetailsBean.demandStatus, -1);
                    int categoryId = TypeUtils.getInteger(facilitatorDemandDetailsBean.categoryId, -1);
                    String categoryName = TypeUtils.getString(facilitatorDemandDetailsBean.categoryName, "");
                    List<FacilitatorDemandDetailsBean.CategoryPropertyChineseBean> categoryPropertyChineseList = facilitatorDemandDetailsBean.categoryPropertyChineseList;
                    Date createAt = facilitatorDemandDetailsBean.createAt;
                    int expireDuration = TypeUtils.getInteger(facilitatorDemandDetailsBean.expireDuration, 0);
                    String expiredTip = TypeUtils.getString(facilitatorDemandDetailsBean.expiredTip, "");
                    String serveWay = TypeUtils.getString(facilitatorDemandDetailsBean.serveWay, "");
                    String province = TypeUtils.getString(facilitatorDemandDetailsBean.province, "");
                    String city = TypeUtils.getString(facilitatorDemandDetailsBean.city, "");
                    Date bookingDate = facilitatorDemandDetailsBean.bookingDate;
                    String deliverCycleValue = TypeUtils.getString(facilitatorDemandDetailsBean.deliverCycleValue, "");
                    int budget = TypeUtils.getInteger(facilitatorDemandDetailsBean.budget, 0);
                    String introduce = TypeUtils.getString(facilitatorDemandDetailsBean.introduce, "");
                    int salaryTrusteeship = TypeUtils.getInteger(facilitatorDemandDetailsBean.salaryTrusteeship, 0);

                    List<ShowFacilitatorDemandDetailsBean> showFacilitatorDemandDetailsList = new ArrayList<>();

                    if (null != getView() && salaryTrusteeship != 0) {
                        showFacilitatorDemandDetailsList.add(getShowDemandDetailsBean(getView().getParentContext().getString(R.string.trusteeship_amount), "￥" + String.valueOf(salaryTrusteeship)));
                    }

                    if (null != getView()) {
                        showFacilitatorDemandDetailsList.add(getShowDemandDetailsBean(getView().getParentContext().getString(R.string.service_category), categoryName));
                    }

                    if (null != categoryPropertyChineseList && categoryPropertyChineseList.size() > 0) {
                        for (int i = 0; i < categoryPropertyChineseList.size(); i++) {
                            FacilitatorDemandDetailsBean.CategoryPropertyChineseBean categoryPropertyChineseBean = categoryPropertyChineseList.get(i);

                            if (null != categoryPropertyChineseBean) {
                                String categoryProperty = TypeUtils.getString(categoryPropertyChineseBean.categoryProperty, "");
                                List<String> valuesList = categoryPropertyChineseBean.values;

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

                                showFacilitatorDemandDetailsList.add(getShowDemandDetailsBean(categoryProperty, stringBuilder.toString()));
                            }
                        }
                    }

                    if (null != getView() && null != createAt) {
                        String publishTime = CommonConstant.commonTimeFormat.format(createAt);
                        showFacilitatorDemandDetailsList.add(getShowDemandDetailsBean(getView().getParentContext().getString(R.string.publish_time), publishTime));
                    }

                    if (null != getView() && demandStatus != 20 && demandStatus != 30 && expireDuration != 0) {
                        showFacilitatorDemandDetailsList.add(getShowDemandDetailsBean(getView().getParentContext().getString(R.string.demand_valid_time), expiredTip));
                    }

                    if (null != getView() && !TextUtils.isEmpty(serveWay)) {
                        showFacilitatorDemandDetailsList.add(getShowDemandDetailsBean(getView().getParentContext().getString(R.string.work_way), serveWay));
                    }

                    if (null != getView() && !TextUtils.isEmpty(province)) {
                        StringBuilder stringBuilder = new StringBuilder(province);

                        if (!TextUtils.isEmpty(city)) {
                            stringBuilder.append(city);
                        }

                        showFacilitatorDemandDetailsList.add(getShowDemandDetailsBean(getView().getParentContext().getString(R.string.service_city), stringBuilder.toString()));
                    }

                    if (null != getView() && null != bookingDate) {
                        String subscribeTime = CommonConstant.subscribeTimeFormat.format(bookingDate);
                        showFacilitatorDemandDetailsList.add(getShowDemandDetailsBean(getView().getParentContext().getString(R.string.subscribe_time), subscribeTime));
                    }

                    if (null != getView() && !TextUtils.isEmpty(deliverCycleValue)) {
                        showFacilitatorDemandDetailsList.add(getShowDemandDetailsBean(getView().getParentContext().getString(R.string.delivery_cycle), deliverCycleValue));
                    }

                    if (null != getView() && budget != 0) {
                        showFacilitatorDemandDetailsList.add(getShowDemandDetailsBean(getView().getParentContext().getString(R.string.employment_price), "￥" + String.valueOf(budget)));
                    }

                    if (null != getView() && !TextUtils.isEmpty(introduce)) {
                        showFacilitatorDemandDetailsList.add(getShowDemandDetailsBean(getView().getParentContext().getString(R.string.facilitator_demand_introduction), introduce));
                    }

                    if (null != getView()) {
                        getView().onShowCategoryId(categoryId);
                        getView().onShowShowFacilitatorDemandDetailsList(showFacilitatorDemandDetailsList);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_MSG_TO_DEMAND)) {
            if (success) {
                FacilitatorDemandOrderInformationBean facilitatorDemandOrderInformationBean = JSONObject.parseObject(data, FacilitatorDemandOrderInformationBean.class);

                if (null != facilitatorDemandOrderInformationBean) {
                    int orderId = TypeUtils.getInteger(facilitatorDemandOrderInformationBean.orderId, -1);
                    int orderPrice = TypeUtils.getInteger(facilitatorDemandOrderInformationBean.orderPrice, 0);
                    String priceTip = TypeUtils.getString(facilitatorDemandOrderInformationBean.priceTip, "");
                    String refundTip = TypeUtils.getString(facilitatorDemandOrderInformationBean.refundTip, "");
                    String refundReason = TypeUtils.getString(facilitatorDemandOrderInformationBean.refundReason, "");
                    Date refundCreateAt = facilitatorDemandOrderInformationBean.refundCreateAt;
                    Integer refundStatus = TypeUtils.getInteger(facilitatorDemandOrderInformationBean.refundStatus, -1);
                    long refundAutoAcceptSurplusDuration = TypeUtils.getLong(facilitatorDemandOrderInformationBean.refundAutoAcceptSurplusDuration, 0L);
                    String arbitrationTip = TypeUtils.getString(facilitatorDemandOrderInformationBean.arbitrationTip, "");

                    if (null != getView()) {
                        getView().onShowFacilitatorDemandDetailsOrderInformation(orderId, orderPrice, priceTip, refundTip, refundReason, refundCreateAt, refundStatus, refundAutoAcceptSurplusDuration, arbitrationTip);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_GET_BY_CATEGORY_ID)) {
            if (success) {
                BidInformationBean bidInformationBean = JSONObject.parseObject(data, BidInformationBean.class);

                if (null != bidInformationBean) {
                    String advantage = TypeUtils.getString(bidInformationBean.advantage, "");

                    if (null != getView()) {
                        getView().onShowAdvantage(advantage);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_BID_SAVE)) {
            if (success) {
                if (null != getView()) {
                    getView().onBidSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_BID_CHANGE_PRICE)) {
            if (success) {
                if (null != getView()) {
                    getView().onChangePriceSuccess(mPrice);
                }
            } else {
                if (null != getView()) {
                    getView().showToast("修改价格失败");
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_REFUND_APPLY_ACCEPT)) {
            if (success) {
                if (null != getView()) {
                    getView().onAgreeRefundSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("同意退款失败");
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_REFUND_APPLY_REFUSE)) {
            if (success) {
                if (null != getView()) {
                    getView().onRejectRefundSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("驳回退款失败");
                }
            }
        }
    }

}
