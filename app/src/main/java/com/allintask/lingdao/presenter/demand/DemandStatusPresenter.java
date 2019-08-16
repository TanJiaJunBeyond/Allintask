package com.allintask.lingdao.presenter.demand;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.demand.DemandStatusListBean;
import com.allintask.lingdao.bean.user.CheckBasicPersonalInformationBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.demand.DemandModel;
import com.allintask.lingdao.model.demand.IDemandModel;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.demand.IDemandStatusView;

import java.util.ArrayList;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/15.
 */

public class DemandStatusPresenter extends BasePresenter<IDemandStatusView> {

    private int demandStatus;
    private IDemandModel demandModel;
    private IUserModel userModel;
    private List<DemandStatusListBean.DemandStatusBean> demandStatusList;

    private String tag;
    private int pageNumber = CommonConstant.DEFAULT_PAGE_NUMBER;

    public DemandStatusPresenter(int demandStatus) {
        this.demandStatus = demandStatus;
        demandModel = new DemandModel();
        userModel = new UserModel();
        demandStatusList = new ArrayList<>();
    }

    private void fetchDemandManageListRequest(boolean isRefresh) {
        if (isRefresh) {
            demandStatusList.clear();
        }

        switch (demandStatus) {
            case CommonConstant.DEMAND_STATUS_IN_THE_BIDDING:
                tag = CommonConstant.DEMAND_STATUS_IN_THE_BIDDING_STRING;
                break;

            case CommonConstant.DEMAND_STATUS_UNDERWAY:
                tag = CommonConstant.DEMAND_STATUS_UNDERWAY_STRING;
                break;

            case CommonConstant.DEMAND_STATUS_COMPLETED:
                tag = CommonConstant.DEMAND_STATUS_COMPLETED_STRING;
                break;

            case CommonConstant.DEMAND_STATUS_EXPIRED:
                tag = CommonConstant.DEMAND_STATUS_EXPIRED_STRING;
                break;
        }

        if (!TextUtils.isEmpty(tag)) {
            OkHttpRequest fetchDemandManageListRequest = demandModel.fetchDemandManageListRequest(tag, pageNumber);
            addRequestAsyncTaskForJsonNoProgress(true, OkHttpRequestOptions.GET, fetchDemandManageListRequest);
        }
    }

    public void refresh() {
        if (!getView().isLoadingMore()) {
            pageNumber = CommonConstant.DEFAULT_PAGE_NUMBER;
            getView().setRefresh(true);
            fetchDemandManageListRequest(true);
        } else {
            getView().setRefresh(false);
        }
    }

    public void loadMore() {
        if (!getView().isRefreshing()) {
            getView().setLoadMore(true);
            fetchDemandManageListRequest(false);
        } else {
            getView().setLoadMore(false);
        }
    }

    public void checkBasicPersonalInformationWholeRequest() {
        OkHttpRequest checkBasicPersonalInformationWholeRequest = userModel.checkBasicPersonalInformationWholeRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, checkBasicPersonalInformationWholeRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_GET_DEMAND_MANAGE_LIST + "/" + tag)) {
            if (success) {
                DemandStatusListBean demandStatusListBean = JSONObject.parseObject(data, DemandStatusListBean.class);

                if (null != demandStatusListBean) {
                    List<DemandStatusListBean.DemandStatusBean> demandStatusList = demandStatusListBean.list;

                    if (null != demandStatusList && demandStatusList.size() > 0) {
                        this.demandStatusList.addAll(demandStatusList);
                        pageNumber++;
                    }
                } else if (null == this.demandStatusList || this.demandStatusList.size() <= 0) {
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
                getView().onShowDemandStatusList(this.demandStatusList);
            }

            if (null != getView()) {
                getView().setRefresh(false);
                getView().setLoadMore(false);
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
    }

}
