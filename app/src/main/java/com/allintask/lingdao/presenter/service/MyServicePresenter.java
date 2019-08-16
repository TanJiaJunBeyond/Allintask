package com.allintask.lingdao.presenter.service;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.service.GetIdToChineseListBean;
import com.allintask.lingdao.bean.service.MyServiceListBean;
import com.allintask.lingdao.bean.user.GetResumeCompleteRateBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.service.IServiceModel;
import com.allintask.lingdao.model.service.ServiceModel;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.service.IMyServiceView;

import java.util.ArrayList;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/2/5.
 */

public class MyServicePresenter extends BasePresenter<IMyServiceView> {

    private IUserModel userModel;
    private IServiceModel serviceModel;
    private List<MyServiceListBean.MyServiceBean> myServiceList;

    private int pageNumber = CommonConstant.DEFAULT_PAGE_NUMBER;

    private int position;
    private int id;

    public MyServicePresenter() {
        userModel = new UserModel();
        serviceModel = new ServiceModel();
        myServiceList = new ArrayList<>();
    }

    public void getResumeCompleteRateRequest() {
        OkHttpRequest getResumeCompleteRateRequest = userModel.getResumeCompleteRateRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, getResumeCompleteRateRequest);
    }

    public void getIdToChineseRequest() {
        OkHttpRequest getIdToChineseRequest = serviceModel.getIdToChineseRequest();
        addRequestAsyncTaskForJsonNoProgress(true, OkHttpRequestOptions.GET, getIdToChineseRequest);
    }

    private void fetchMyServiceListRequest(boolean isRefresh) {
        if (isRefresh) {
            myServiceList.clear();
        }

        OkHttpRequest fetchMyServiceListRequest = serviceModel.fetchMyServiceListRequest(pageNumber);
        addRequestAsyncTaskForJsonNoProgress(true, OkHttpRequestOptions.GET, fetchMyServiceListRequest);
    }

    public void refresh() {
        if (!getView().isLoadingMore()) {
            pageNumber = CommonConstant.DEFAULT_PAGE_NUMBER;
            getView().setRefresh(true);
            fetchMyServiceListRequest(true);
        } else {
            getView().setRefresh(false);
        }
    }

    public void loadMore() {
        if (!getView().isRefreshing()) {
            getView().setLoadMore(true);
            fetchMyServiceListRequest(false);
        } else {
            getView().setLoadMore(false);
        }
    }

    public void deleteServiceRequest(int position, int id) {
        this.position = position;
        this.id = id;

        OkHttpRequest deleteServiceRequest = serviceModel.deleteServiceRequest(id);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.DELETE, deleteServiceRequest);
    }

    public void goOnlineServiceRequest(int position, int id) {
        this.position = position;
        this.id = id;

        OkHttpRequest goOnlineServiceRequest = serviceModel.goOnlineServiceRequest(id);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.PUT, goOnlineServiceRequest);
    }

    public void goOfflineServiceRequest(int position, int id) {
        this.position = position;
        this.id = id;

        OkHttpRequest goOfflineServiceRequest = serviceModel.goOfflineServiceRequest(id);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.PUT, goOfflineServiceRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_GET_RESUME_COMPLETE_RATE)) {
            if (success) {
                GetResumeCompleteRateBean getResumeCompleteRateBean = JSONObject.parseObject(data, GetResumeCompleteRateBean.class);

                if (null != getResumeCompleteRateBean) {
                    Integer tempResumeCompleteRate = getResumeCompleteRateBean.resumeCompleteRate;
                    int resumeCompleteRate;

                    if (null == tempResumeCompleteRate) {
                        resumeCompleteRate = 0;
                    } else {
                        resumeCompleteRate = tempResumeCompleteRate;
                    }

                    if (null != getView()) {
                        getView().onShowResumeCompleteRate(resumeCompleteRate);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_GET_ID_TO_CHINESE)) {
            if (success) {
                GetIdToChineseListBean getIdToChineseListBean = JSONObject.parseObject(data, GetIdToChineseListBean.class);

                if (null != getIdToChineseListBean && null != getView()) {
                    getView().onShowGetIdToChineseListBean(getIdToChineseListBean);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_MY_SERVE_LIST)) {
            if (success) {
                MyServiceListBean myServiceListBean = JSONObject.parseObject(data, MyServiceListBean.class);

                if (null != myServiceListBean) {
                    List<MyServiceListBean.MyServiceBean> myServiceList = myServiceListBean.list;

                    if (null != myServiceList && myServiceList.size() > 0) {
                        this.myServiceList.addAll(myServiceList);
                        pageNumber++;
                    }

                    if (null != getView()) {
                        getView().onShowMyServiceList(this.myServiceList);
                    }
                } else if (null == this.myServiceList || this.myServiceList.size() <= 0) {
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

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_DELETE + "/" + String.valueOf(id))) {
            if (success) {
                if (null != getView()) {
                    getView().onDeleteServiceSuccess(position);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_GO_ONLINE + "/" + String.valueOf(id))) {
            if (success) {
                if (null != getView()) {
                    getView().onGoOnlineSuccess(position);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_GO_OFFLINE + "/" + String.valueOf(id))) {
            if (success) {
                if (null != getView()) {
                    getView().onGoOfflineSuccess(position);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
