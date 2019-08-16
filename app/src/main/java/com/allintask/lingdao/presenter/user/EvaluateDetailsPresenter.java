package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.EvaluateDetailsBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.order.IOrderModel;
import com.allintask.lingdao.model.order.OrderModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IEvaluateDetailsView;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/3/23.
 */

public class EvaluateDetailsPresenter extends BasePresenter<IEvaluateDetailsView> {

    private IOrderModel orderModel;

    public EvaluateDetailsPresenter() {
        orderModel = new OrderModel();
    }

    public void fetchBuyerEvaluateDetailsRequest(int orderId) {
        OkHttpRequest fetchBuyerEvaluateDetailsRequest = orderModel.fetchBuyerEvaluateDetailsRequest(orderId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchBuyerEvaluateDetailsRequest);
    }

    public void fetchSellerEvaluateDetailsRequest(int orderId) {
        OkHttpRequest fetchSellerEvaluateDetailsRequest = orderModel.fetchSellerEvaluateDetailsRequest(orderId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchSellerEvaluateDetailsRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_BUYER_DETAILS) || requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_SELLER_DETAILS)) {
            if (success) {
                EvaluateDetailsBean evaluateDetailsBean = JSONObject.parseObject(data, EvaluateDetailsBean.class);

                if (null != evaluateDetailsBean) {
                    float toBuyerOverallMerit = TypeUtils.getFloat(evaluateDetailsBean.toBuyerOverallMerit, 0F);
                    String toBuyerContent = TypeUtils.getString(evaluateDetailsBean.toBuyerContent, "");
                    float toSellerOverallMerit = TypeUtils.getFloat(evaluateDetailsBean.toSellerOverallMerit, 0F);
                    float toSellerTimelyComplete = TypeUtils.getFloat(evaluateDetailsBean.toSellerTimelyComplete, 0F);
                    float toSellerWorkQuality = TypeUtils.getFloat(evaluateDetailsBean.toSellerWorkQuality, 0F);
                    float toSellerSincerity = TypeUtils.getFloat(evaluateDetailsBean.toSellerSincerity, 0F);
                    String toSellerContent = TypeUtils.getString(evaluateDetailsBean.toSellerContent, "");

                    if (null != getView()) {
                        getView().onShowEvaluateDetails(toBuyerOverallMerit, toBuyerContent, toSellerOverallMerit, toSellerTimelyComplete, toSellerWorkQuality, toSellerSincerity, toSellerContent);
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
