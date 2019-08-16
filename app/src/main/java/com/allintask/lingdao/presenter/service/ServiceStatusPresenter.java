package com.allintask.lingdao.presenter.service;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.service.BidInformationBean;
import com.allintask.lingdao.bean.service.GetIdToChineseListBean;
import com.allintask.lingdao.bean.service.ServiceCompletedListBean;
import com.allintask.lingdao.bean.service.ServiceExpiredListBean;
import com.allintask.lingdao.bean.service.ServiceHasBidListBean;
import com.allintask.lingdao.bean.service.ServiceUnderwayListBean;
import com.allintask.lingdao.bean.service.ServiceWaitBidListBean;
import com.allintask.lingdao.bean.user.CheckBasicPersonalInformationBean;
import com.allintask.lingdao.bean.user.MyDataBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.service.IServiceModel;
import com.allintask.lingdao.model.service.ServiceModel;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.service.IServiceStatusView;

import java.util.ArrayList;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/15.
 */

public class ServiceStatusPresenter extends BasePresenter<IServiceStatusView> {

    private IServiceModel serviceModel;
    private IUserModel userModel;
    private int serviceStatus;
    private List<ServiceWaitBidListBean.ServiceWaitBidBean> serviceWaitBidList;
    private List<ServiceHasBidListBean.ServiceHasBidBean> serviceHasBidList;
    private List<ServiceUnderwayListBean.ServiceUnderwayBean> serviceUnderwayList;
    private List<ServiceCompletedListBean.ServiceCompletedBean> serviceCompletedList;
    private List<ServiceExpiredListBean.ServiceExpiredBean> serviceExpiredList;

    private int pageNumber = CommonConstant.DEFAULT_PAGE_NUMBER;
    private int position = 0;
    private int userId = -1;
    private int demandId = -1;
    private String mPrice;

    public ServiceStatusPresenter(int serviceStatus) {
        serviceModel = new ServiceModel();
        userModel = new UserModel();
        this.serviceStatus = serviceStatus;

        switch (serviceStatus) {
            case CommonConstant.SERVICE_STATUS_WAIT_BID:
                serviceWaitBidList = new ArrayList<>();
                break;

            case CommonConstant.SERVICE_STATUS_HAS_BID:
                serviceHasBidList = new ArrayList<>();
                break;

            case CommonConstant.SERVICE_STATUS_UNDERWAY:
                serviceUnderwayList = new ArrayList<>();
                break;

            case CommonConstant.SERVICE_STATUS_COMPLETED:
                serviceCompletedList = new ArrayList<>();
                break;

            case CommonConstant.SERVICE_STATUS_EXPIRED:
                serviceExpiredList = new ArrayList<>();
                break;
        }
    }

    public void getIdToChineseRequest() {
        OkHttpRequest getIdToChineseRequest = serviceModel.getIdToChineseRequest();
        addRequestAsyncTaskForJsonNoProgress(true, OkHttpRequestOptions.GET, getIdToChineseRequest);
    }

    private void fetchWaitBidServiceListRequest(boolean isRefresh) {
        if (isRefresh) {
            serviceWaitBidList.clear();
        }

        OkHttpRequest fetchWaitBidServiceListRequest = serviceModel.fetchWaitBidServiceListRequest(pageNumber);
        addRequestAsyncTaskForJsonNoProgress(true, OkHttpRequestOptions.GET, fetchWaitBidServiceListRequest);
    }

    private void fetchSellerHasBidServiceListRequest(boolean isRefresh) {
        if (isRefresh) {
            serviceHasBidList.clear();
        }

        OkHttpRequest fetchSellerHasBidServiceListRequest = serviceModel.fetchSellerHasBidServiceListRequest(pageNumber);
        addRequestAsyncTaskForJsonNoProgress(true, OkHttpRequestOptions.GET, fetchSellerHasBidServiceListRequest);
    }

    private void fetchSellerUnderwayServiceListRequest(boolean isRefresh) {
        if (isRefresh) {
            serviceUnderwayList.clear();
        }

        OkHttpRequest fetchSellerUnderwayServiceListRequest = serviceModel.fetchSellerUnderwayServiceListRequest(pageNumber);
        addRequestAsyncTaskForJsonNoProgress(true, OkHttpRequestOptions.GET, fetchSellerUnderwayServiceListRequest);
    }

    private void fetchSellerCompletedServiceListRequest(boolean isRefresh) {
        if (isRefresh) {
            serviceCompletedList.clear();
        }

        OkHttpRequest fetchSellerCompletedServiceListRequest = serviceModel.fetchSellerCompletedServiceListRequest(pageNumber);
        addRequestAsyncTaskForJsonNoProgress(true, OkHttpRequestOptions.GET, fetchSellerCompletedServiceListRequest);
    }

    private void fetchSellerExpiredServiceListRequest(boolean isRefresh) {
        if (isRefresh) {
            serviceExpiredList.clear();
        }

        OkHttpRequest fetchSellerExpiredServiceListRequest = serviceModel.fetchSellerExpiredServiceListRequest(pageNumber);
        addRequestAsyncTaskForJsonNoProgress(true, OkHttpRequestOptions.GET, fetchSellerExpiredServiceListRequest);
    }

    public void refresh() {
        if (!getView().isLoadingMore()) {
            pageNumber = CommonConstant.DEFAULT_PAGE_NUMBER;
            getView().setRefresh(true);

            switch (serviceStatus) {
                case CommonConstant.SERVICE_STATUS_WAIT_BID:
                    fetchWaitBidServiceListRequest(true);
                    break;

                case CommonConstant.SERVICE_STATUS_HAS_BID:
                    fetchSellerHasBidServiceListRequest(true);
                    break;

                case CommonConstant.SERVICE_STATUS_UNDERWAY:
                    fetchSellerUnderwayServiceListRequest(true);
                    break;

                case CommonConstant.SERVICE_STATUS_COMPLETED:
                    fetchSellerCompletedServiceListRequest(true);
                    break;

                case CommonConstant.SERVICE_STATUS_EXPIRED:
                    fetchSellerExpiredServiceListRequest(true);
                    break;
            }
        } else {
            getView().setRefresh(false);
        }
    }

    public void loadMore() {
        if (!getView().isRefreshing()) {
            getView().setLoadMore(true);

            switch (serviceStatus) {
                case CommonConstant.SERVICE_STATUS_WAIT_BID:
                    fetchWaitBidServiceListRequest(false);
                    break;

                case CommonConstant.SERVICE_STATUS_HAS_BID:
                    fetchSellerHasBidServiceListRequest(false);
                    break;

                case CommonConstant.SERVICE_STATUS_UNDERWAY:
                    fetchSellerUnderwayServiceListRequest(false);
                    break;

                case CommonConstant.SERVICE_STATUS_COMPLETED:
                    fetchSellerCompletedServiceListRequest(false);
                    break;

                case CommonConstant.SERVICE_STATUS_EXPIRED:
                    fetchSellerExpiredServiceListRequest(false);
                    break;
            }
        } else {
            getView().setLoadMore(false);
        }
    }

    public void fetchBidInformationRequest(int position, int categoryId) {
        this.position = position;

        OkHttpRequest fetchBidInformationRequest = serviceModel.fetchBidInformationRequest(categoryId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchBidInformationRequest);
    }

    public void bidRequest(int buyerUserId, int demandId, String price, String advantage, int isBook) {
        userId = buyerUserId;
        this.demandId = demandId;
        OkHttpRequest saveBidRequest = serviceModel.bidRequest(demandId, price, advantage, isBook);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, saveBidRequest);
    }

    public void changePriceRequest(int userId, int demandId, int bidId, String price) {
        this.userId = userId;
        this.demandId = demandId;
        mPrice = price;
        OkHttpRequest changePriceRequest = serviceModel.changePriceRequest(bidId, price);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, changePriceRequest);
    }

    public void checkBasicPersonalInformationWholeRequest() {
        OkHttpRequest checkBasicPersonalInformationWholeRequest = userModel.checkBasicPersonalInformationWholeRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, checkBasicPersonalInformationWholeRequest);
    }

    public void fetchMyDataRequest() {
        OkHttpRequest fetchMyDataRequest = userModel.fetchMyDataRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchMyDataRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_GET_ID_TO_CHINESE)) {
            if (success) {
                GetIdToChineseListBean getIdToChineseListBean = JSONObject.parseObject(data, GetIdToChineseListBean.class);

                if (null != getIdToChineseListBean) {
                    if (null != getView()) {
                        getView().onShowGetIdToChineseListBean(getIdToChineseListBean);
                    }
                } else {
                    switch (serviceStatus) {
                        case CommonConstant.SERVICE_STATUS_WAIT_BID:
                            if (null == this.serviceWaitBidList || this.serviceWaitBidList.size() <= 0) {
                                if (null != getView()) {
                                    getView().showEmptyView();
                                }
                            }
                            break;

                        case CommonConstant.SERVICE_STATUS_HAS_BID:
                            if (null == this.serviceHasBidList || this.serviceHasBidList.size() <= 0) {
                                if (null != getView()) {
                                    getView().showEmptyView();
                                }
                            }
                            break;

                        case CommonConstant.SERVICE_STATUS_UNDERWAY:
                            if (null == this.serviceUnderwayList || this.serviceUnderwayList.size() <= 0) {
                                if (null != getView()) {
                                    getView().showEmptyView();
                                }
                            }
                            break;

                        case CommonConstant.SERVICE_STATUS_COMPLETED:
                            if (null == this.serviceCompletedList || this.serviceCompletedList.size() <= 0) {
                                if (null != getView()) {
                                    getView().showEmptyView();
                                }
                            }
                            break;

                        case CommonConstant.SERVICE_STATUS_EXPIRED:
                            if (null == this.serviceExpiredList || this.serviceExpiredList.size() <= 0) {
                                if (null != getView()) {
                                    getView().showEmptyView();
                                }
                            }
                            break;
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_MATCH_WAITING_BID_LIST)) {
            if (success) {
                ServiceWaitBidListBean serviceWaitBidListBean = JSONObject.parseObject(data, ServiceWaitBidListBean.class);

                if (null != serviceWaitBidListBean) {
                    List<ServiceWaitBidListBean.ServiceWaitBidBean> serviceWaitBidList = serviceWaitBidListBean.list;

                    if (null != serviceWaitBidList && serviceWaitBidList.size() > 0) {
                        this.serviceWaitBidList.addAll(serviceWaitBidList);
                        pageNumber++;
                    }

                    if (null != getView()) {
                        getView().onShowServiceWaitBidList(this.serviceWaitBidList);
                    }
                } else if (null == this.serviceWaitBidList || this.serviceWaitBidList.size() <= 0) {
                    if (null != getView()) {
                        getView().showEmptyView();
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showEmptyView();
                    getView().showToast(errorMessage);
                }
            }

            if (null != getView()) {
                getView().setRefresh(false);
                getView().setLoadMore(false);
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_BID_LIST_TO_SELLER_VALID)) {
            if (success) {
                ServiceHasBidListBean serviceHasBidListBean = JSONObject.parseObject(data, ServiceHasBidListBean.class);

                if (null != serviceHasBidListBean) {
                    List<ServiceHasBidListBean.ServiceHasBidBean> serviceHasBidList = serviceHasBidListBean.list;

                    if (null != serviceHasBidList && serviceHasBidList.size() > 0) {
                        this.serviceHasBidList.addAll(serviceHasBidList);
                        pageNumber++;
                    }

                    if (null != getView()) {
                        getView().onShowServiceHasBidList(this.serviceHasBidList);
                    }
                } else if (null == this.serviceHasBidList || this.serviceHasBidList.size() <= 0) {
                    if (null != getView()) {
                        getView().showEmptyView();
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showEmptyView();
                    getView().showToast(errorMessage);
                }
            }

            if (null != getView()) {
                getView().setRefresh(false);
                getView().setLoadMore(false);
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_LIST_TO_SELLER_ING)) {
            if (success) {
                ServiceUnderwayListBean serviceUnderwayListBean = JSONObject.parseObject(data, ServiceUnderwayListBean.class);

                if (null != serviceUnderwayListBean) {
                    List<ServiceUnderwayListBean.ServiceUnderwayBean> serviceUnderwayList = serviceUnderwayListBean.list;

                    if (null != serviceUnderwayList && serviceUnderwayList.size() > 0) {
                        this.serviceUnderwayList.addAll(serviceUnderwayList);
                        pageNumber++;
                    }

                    if (null != getView()) {
                        getView().onShowServiceUnderwayList(this.serviceUnderwayList);
                    }
                } else if (null == this.serviceUnderwayList || this.serviceUnderwayList.size() <= 0) {
                    if (null != getView()) {
                        getView().showEmptyView();
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showEmptyView();
                    getView().showToast(errorMessage);
                }
            }

            if (null != getView()) {
                getView().setRefresh(false);
                getView().setLoadMore(false);
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_LIST_TO_SELLER_COMPLETE)) {
            if (success) {
                ServiceCompletedListBean serviceCompletedListBean = JSONObject.parseObject(data, ServiceCompletedListBean.class);

                if (null != serviceCompletedListBean) {
                    List<ServiceCompletedListBean.ServiceCompletedBean> serviceCompletedList = serviceCompletedListBean.list;

                    if (null != serviceCompletedList && serviceCompletedList.size() > 0) {
                        this.serviceCompletedList.addAll(serviceCompletedList);
                        pageNumber++;
                    }

                    if (null != getView()) {
                        getView().onShowServiceCompletedList(this.serviceCompletedList);
                    }
                } else if (null == this.serviceCompletedList || this.serviceCompletedList.size() <= 0) {
                    if (null != getView()) {
                        getView().showEmptyView();
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showEmptyView();
                    getView().showToast(errorMessage);
                }
            }

            if (null != getView()) {
                getView().setRefresh(false);
                getView().setLoadMore(false);
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_BID_LIST_TO_SELLER_EXPIRE)) {
            if (success) {
                ServiceExpiredListBean serviceExpiredListBean = JSONObject.parseObject(data, ServiceExpiredListBean.class);

                if (null != serviceExpiredListBean) {
                    List<ServiceExpiredListBean.ServiceExpiredBean> serviceExpiredList = serviceExpiredListBean.list;

                    if (null != serviceExpiredList && serviceExpiredList.size() > 0) {
                        this.serviceExpiredList.addAll(serviceExpiredList);
                        pageNumber++;
                    }

                    if (null != getView()) {
                        getView().onShowServiceExpiredList(this.serviceExpiredList);
                    }
                } else if (null == this.serviceExpiredList || this.serviceExpiredList.size() <= 0) {
                    if (null != getView()) {
                        getView().showEmptyView();
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showEmptyView();
                    getView().showToast(errorMessage);
                }
            }

            if (null != getView()) {
                getView().setRefresh(false);
                getView().setLoadMore(false);
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_GET_BY_CATEGORY_ID)) {
            if (success) {
                BidInformationBean bidInformationBean = JSONObject.parseObject(data, BidInformationBean.class);

                if (null != bidInformationBean) {
                    String advantage = TypeUtils.getString(bidInformationBean.advantage, "");

                    if (null != getView()) {
                        getView().onShowAdvantage(position, advantage);
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
                    getView().onBidSuccess(userId, demandId);
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
                    getView().onChangePriceSuccess(userId, demandId, mPrice);
                }
            } else {
                if (null != getView()) {
                    getView().showToast("修改价格失败");
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_CHECK_BASIC_MSG)) {
            if (success) {
                CheckBasicPersonalInformationBean checkBasicPersonalInformationBean = JSONObject.parseObject(data, CheckBasicPersonalInformationBean.class);

                if (null != checkBasicPersonalInformationBean) {
                    boolean isComplete = TypeUtils.getBoolean(checkBasicPersonalInformationBean.isComplete, false);

                    if (null != getView()) {
                        if (isComplete) {
                            getView().onShowCompletedBasicPersonalInformation();
                        } else {
                            getView().onShowBasicPersonalInformationBean(checkBasicPersonalInformationBean);
                        }
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast("请求失败");
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MINE)) {
            if (success) {
                MyDataBean myDataBean = JSONObject.parseObject(data, MyDataBean.class);

                if (null != myDataBean) {
                    boolean zmrzAuthSuccess = TypeUtils.getBoolean(myDataBean.zmrzAuthSuccess, false);

                    if (null != getView()) {
                        getView().showIsZmrzAuthSuccess(zmrzAuthSuccess);
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
