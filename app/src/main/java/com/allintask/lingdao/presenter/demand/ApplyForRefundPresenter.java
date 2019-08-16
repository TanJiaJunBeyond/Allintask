package com.allintask.lingdao.presenter.demand;

import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.order.IOrderModel;
import com.allintask.lingdao.model.order.OrderModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.demand.IApplyForRefundView;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/3/10.
 */

public class ApplyForRefundPresenter extends BasePresenter<IApplyForRefundView> {

    private IOrderModel orderModel;

    public ApplyForRefundPresenter() {
        orderModel = new OrderModel();
    }

    public void applyRefundRequest(int orderId, int workStatus, String refundReason, String refundAmount) {
        OkHttpRequest applyRefundRequest = orderModel.applyForRefundRequest(orderId, workStatus, refundReason, refundAmount);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, applyRefundRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_REFUND_APPLY_SAVE)) {
            if (success) {
                if (null != getView()) {
                    getView().onApplyForRefundSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
