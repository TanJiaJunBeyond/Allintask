package com.allintask.lingdao.presenter.demand;

import com.alibaba.fastjson.JSONArray;
import com.allintask.lingdao.bean.demand.ApplyForArbitramentRequestBean;
import com.allintask.lingdao.bean.demand.ArbitramentReasonBean;
import com.allintask.lingdao.bean.user.CheckUploadIsSuccessBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.order.IOrderModel;
import com.allintask.lingdao.model.order.OrderModel;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.demand.IApplyForArbitramentView;

import java.util.ArrayList;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/3/10.
 */

public class ApplyForArbitramentPresenter extends BasePresenter<IApplyForArbitramentView> {

    private IOrderModel orderModel;
    private IUserModel userModel;

    private int uploadAlbumListSize;

    public ApplyForArbitramentPresenter() {
        orderModel = new OrderModel();
        userModel = new UserModel();
    }

    public void fetchArbitrationReasonListRequest() {
        OkHttpRequest fetchArbitrationReasonListRequest = orderModel.fetchArbitrationReasonListRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchArbitrationReasonListRequest);
    }

    public void checkUploadIsSuccessRequest(String flagId, int uploadAlbumListSize) {
        this.uploadAlbumListSize = uploadAlbumListSize;
        OkHttpRequest checkUploadIsSuccessRequest = userModel.checkUploadIsSuccessRequest(flagId);
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.POST, checkUploadIsSuccessRequest);
    }

    public void applyArbitramentRequest(ApplyForArbitramentRequestBean applyForArbitramentRequestBean) {
        OkHttpRequest applyArbitramentRequest = orderModel.applyArbitramentRequest(applyForArbitramentRequestBean);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, applyArbitramentRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_OPERATE_REASON_LIST_BUYER_ARBITRATION_APPLY)) {
            if (success) {
                List<ArbitramentReasonBean> arbitramentReasonList = JSONArray.parseArray(data, ArbitramentReasonBean.class);

                if (null != getView()) {
                    getView().onShowArbitramentReasonList(arbitramentReasonList);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_FILE_OSS_A_LI_YUN_IMAGE_URL_RETURN)) {
            if (success) {
                List<CheckUploadIsSuccessBean> checkUploadIsSuccessList = JSONArray.parseArray(data, CheckUploadIsSuccessBean.class);

                if (null != checkUploadIsSuccessList && checkUploadIsSuccessList.size() > 0) {
                    int status = -1;
                    List<Integer> statusList = new ArrayList<>();

                    for (int i = 0; i < checkUploadIsSuccessList.size(); i++) {
                        CheckUploadIsSuccessBean checkUploadIsSuccessBean = checkUploadIsSuccessList.get(i);

                        if (null != checkUploadIsSuccessBean) {
                            int tempStatus = TypeUtils.getInteger(checkUploadIsSuccessBean.status, -1);
                            statusList.add(tempStatus);
                        }
                    }

                    for (int i = 0; i < statusList.size(); i++) {
                        int tempStatus = statusList.get(i);

                        if (tempStatus == -1) {
                            status = -1;
                            break;
                        } else if (tempStatus == 0) {
                            status = 0;
                            break;
                        } else {
                            status = 1;
                        }
                    }

                    if (null != getView()) {
                        if (checkUploadIsSuccessList.size() == uploadAlbumListSize) {
                            if (status == -1) {
                                getView().onUploadFail();
                            } else if (status == 0) {
                                getView().onUploading();
                            } else {
                                getView().onShowCheckUploadIsSuccessList(checkUploadIsSuccessList);
                            }
                        } else {
                            getView().onUploading();
                        }
                    }
                }
            } else {
                if (null != getView()) {
                    getView().onUploadFail();
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ARBITRATION_APPLY_SAVE)) {
            if (success) {
                if (null != getView()) {
                    getView().onApplyForArbitramentSuccess();
                }
            } else {
                if (null != getView()) {

                }
            }
        }
    }

}
