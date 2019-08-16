package com.allintask.lingdao.model.order;

import com.allintask.lingdao.bean.demand.ApplyForArbitramentRequestBean;

import java.util.Date;

import cn.tanjiajun.sdk.conn.OkHttpRequest;

/**
 * Created by TanJiaJun on 2018/3/7.
 */

public interface IOrderModel {

    OkHttpRequest applyArbitramentRequest(ApplyForArbitramentRequestBean applyForArbitramentRequestBean);

    OkHttpRequest fetchBuyerUnderwayServiceList(int pageNum, int demandId);

    OkHttpRequest buyerConfirmCompleteWorkRequest(int orderId);

    OkHttpRequest buyerConfirmStartWorkRequest(int orderId, Date startWorkAt);

    OkHttpRequest buyerDelayCompleteWorkRequest(int orderId);

    OkHttpRequest evaluateBuyerRequest(int orderId, int serveId, int buyerUserId, int sellerUserId, float overallMerit, String content);

    OkHttpRequest evaluateSellerRequest(int orderId, int serveId, int buyerUserId, int sellerUserId, float overallMerit, float timelyComplete, float workQuality, float sincerity, String content);

    OkHttpRequest acceptApplyRefundRequest(int orderId);

    OkHttpRequest refuseApplyRefundRequest(int orderId);

    OkHttpRequest applyForRefundRequest(int orderId, int workStatus, String refundReason, String refundAmount);

    OkHttpRequest fetchBuyerCompletedServiceListRequest(int pageNum, int demandId);

    OkHttpRequest fetchFacilitatorDemandOrderInformationRequest(int orderId);

    OkHttpRequest fetchArbitrationReasonListRequest();

    OkHttpRequest fetchRefundReasonListRequest();

    OkHttpRequest fetchBuyerEvaluateDetailsRequest(int orderId);

    OkHttpRequest fetchSellerEvaluateDetailsRequest(int orderId);

    OkHttpRequest fetchRecommendEvaluateRequest(int userId);

    OkHttpRequest fetchRecommendEvaluateListRequest(int userId, int pageNum);

}
