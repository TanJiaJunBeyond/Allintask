package com.allintask.lingdao.presenter.user;

import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.order.IOrderModel;
import com.allintask.lingdao.model.order.OrderModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IEvaluateView;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/3/3.
 */

public class EvaluatePresenter extends BasePresenter<IEvaluateView> {

    private IOrderModel orderModel;

    public EvaluatePresenter() {
        orderModel = new OrderModel();
    }

    public void evaluateBuyerRequest(int orderId, int serveId, int buyerUserId, int sellerUserId, float overallMerit, String content) {
        OkHttpRequest evaluateBuyerRequest = orderModel.evaluateBuyerRequest(orderId, serveId, buyerUserId, sellerUserId, overallMerit, content);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, evaluateBuyerRequest);
    }

    public void evaluateSellerRequest(int orderId, int serveId, int buyerUserId, int sellerUserId, float overallMerit, float timelyComplete, float workQuality, float sincerity, String content) {
        OkHttpRequest evaluateBuyerRequest = orderModel.evaluateSellerRequest(orderId, serveId, buyerUserId, sellerUserId, overallMerit, timelyComplete, workQuality, sincerity, content);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, evaluateBuyerRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_BUYER_SAVE)) {
            if (success) {
                if (null != getView()) {
                    getView().onEvaluateBuyerSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("评价失败");
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_SELLER_SAVE)) {
            if (success) {
                if (null != getView()) {
                    getView().onEvaluateSellerSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("评价失败");
                }
            }
        }
    }

}
