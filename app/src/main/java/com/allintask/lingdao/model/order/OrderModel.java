package com.allintask.lingdao.model.order;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.allintask.lingdao.bean.demand.ApplyForArbitramentRequestBean;
import com.allintask.lingdao.constant.ApiKey;
import com.allintask.lingdao.constant.ServiceAPIConstant;

import java.util.Date;

import cn.tanjiajun.sdk.conn.OkHttpRequest;

/**
 * Created by TanJiaJun on 2018/3/7.
 */

public class OrderModel implements IOrderModel {

    @Override
    public OkHttpRequest applyArbitramentRequest(ApplyForArbitramentRequestBean applyForArbitramentRequestBean) {
        OkHttpRequest applyArbitramentRequest = new OkHttpRequest();
        applyArbitramentRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ARBITRATION_APPLY_SAVE);
        applyArbitramentRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ARBITRATION_APPLY_SAVE);
        applyArbitramentRequest.addRequestFormParam(ApiKey.ORDER_ARBITRATION_APPLY_JSON, JSONObject.toJSONString(applyForArbitramentRequestBean, SerializerFeature.DisableCircularReferenceDetect));
        return applyArbitramentRequest;
    }

    @Override
    public OkHttpRequest fetchBuyerUnderwayServiceList(int pageNum, int demandId) {
        OkHttpRequest fetchBuyerUnderwayServiceList = new OkHttpRequest();
        fetchBuyerUnderwayServiceList.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_LIST_TO_BUYER_ING);
        fetchBuyerUnderwayServiceList.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_LIST_TO_BUYER_ING);
        fetchBuyerUnderwayServiceList.addRequestFormParam(ApiKey.COMMON_PAGE_NUM, String.valueOf(pageNum));
        fetchBuyerUnderwayServiceList.addRequestFormParam(ApiKey.ORDER_DEMAND_ID, String.valueOf(demandId));
        return fetchBuyerUnderwayServiceList;
    }

    @Override
    public OkHttpRequest buyerConfirmCompleteWorkRequest(int orderId) {
        OkHttpRequest buyerConfirmCompleteWorkRequest = new OkHttpRequest();
        buyerConfirmCompleteWorkRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_OPERATE_CONFIRM_COMPLETE_WORK);
        buyerConfirmCompleteWorkRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_OPERATE_CONFIRM_COMPLETE_WORK);
        buyerConfirmCompleteWorkRequest.addRequestFormParam(ApiKey.ORDER_ORDER_ID, String.valueOf(orderId));
        return buyerConfirmCompleteWorkRequest;
    }

    @Override
    public OkHttpRequest buyerConfirmStartWorkRequest(int orderId, Date startWorkAt) {
        OkHttpRequest buyerConfirmStartWorkRequest = new OkHttpRequest();
        buyerConfirmStartWorkRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_OPERATE_CONFIRM_START_WORK);
        buyerConfirmStartWorkRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_OPERATE_CONFIRM_START_WORK);
        buyerConfirmStartWorkRequest.addRequestFormParam(ApiKey.ORDER_ORDER_ID, String.valueOf(orderId));
        buyerConfirmStartWorkRequest.addRequestFormParam(ApiKey.ORDER_START_WORK_AT, String.valueOf(startWorkAt));
        return buyerConfirmStartWorkRequest;
    }

    @Override
    public OkHttpRequest buyerDelayCompleteWorkRequest(int orderId) {
        OkHttpRequest buyerDelayCompleteWorkRequest = new OkHttpRequest();
        buyerDelayCompleteWorkRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_OPERATE_DELAY_COMPLETE_WORD);
        buyerDelayCompleteWorkRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_OPERATE_DELAY_COMPLETE_WORD);
        buyerDelayCompleteWorkRequest.addRequestFormParam(ApiKey.ORDER_ORDER_ID, String.valueOf(orderId));
        return buyerDelayCompleteWorkRequest;
    }

    @Override
    public OkHttpRequest evaluateBuyerRequest(int orderId, int serveId, int buyerUserId, int sellerUserId, float overallMerit, String content) {
        OkHttpRequest evaluateBuyerRequest = new OkHttpRequest();
        evaluateBuyerRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_BUYER_SAVE);
        evaluateBuyerRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_BUYER_SAVE);
        evaluateBuyerRequest.addRequestFormParam(ApiKey.ORDER_ORDER_ID, String.valueOf(orderId));
        evaluateBuyerRequest.addRequestFormParam(ApiKey.ORDER_SERVE_ID, String.valueOf(serveId));
        evaluateBuyerRequest.addRequestFormParam(ApiKey.ORDER_BUYER_USER_ID, String.valueOf(buyerUserId));
        evaluateBuyerRequest.addRequestFormParam(ApiKey.ORDER_SELLER_USER_ID, String.valueOf(sellerUserId));
        evaluateBuyerRequest.addRequestFormParam(ApiKey.ORDER_OVERALL_MERIT, String.valueOf(overallMerit));
        evaluateBuyerRequest.addRequestFormParam(ApiKey.ORDER_CONTENT, content);
        return evaluateBuyerRequest;
    }

    @Override
    public OkHttpRequest evaluateSellerRequest(int orderId, int serveId, int buyerUserId, int sellerUserId, float overallMerit, float timelyComplete, float workQuality, float sincerity, String content) {
        OkHttpRequest evaluateSellerRequest = new OkHttpRequest();
        evaluateSellerRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_SELLER_SAVE);
        evaluateSellerRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_SELLER_SAVE);
        evaluateSellerRequest.addRequestFormParam(ApiKey.ORDER_ORDER_ID, String.valueOf(orderId));
        evaluateSellerRequest.addRequestFormParam(ApiKey.ORDER_SERVE_ID, String.valueOf(serveId));
        evaluateSellerRequest.addRequestFormParam(ApiKey.ORDER_SELLER_USER_ID, String.valueOf(sellerUserId));
        evaluateSellerRequest.addRequestFormParam(ApiKey.ORDER_OVERALL_MERIT, String.valueOf(overallMerit));
        evaluateSellerRequest.addRequestFormParam(ApiKey.ORDER_TIMELY_COMPLETE, String.valueOf(timelyComplete));
        evaluateSellerRequest.addRequestFormParam(ApiKey.ORDER_WORK_QUALITY, String.valueOf(workQuality));
        evaluateSellerRequest.addRequestFormParam(ApiKey.ORDER_SINCERITY, String.valueOf(sincerity));
        evaluateSellerRequest.addRequestFormParam(ApiKey.ORDER_CONTENT, content);
        return evaluateSellerRequest;
    }

    @Override
    public OkHttpRequest acceptApplyRefundRequest(int orderId) {
        OkHttpRequest acceptApplyRefundRequest = new OkHttpRequest();
        acceptApplyRefundRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_REFUND_APPLY_ACCEPT);
        acceptApplyRefundRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_REFUND_APPLY_ACCEPT);
        acceptApplyRefundRequest.addRequestFormParam(ApiKey.ORDER_ORDER_ID, String.valueOf(orderId));
        return acceptApplyRefundRequest;
    }

    @Override
    public OkHttpRequest refuseApplyRefundRequest(int orderId) {
        OkHttpRequest refuseApplyRefundRequest = new OkHttpRequest();
        refuseApplyRefundRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_REFUND_APPLY_REFUSE);
        refuseApplyRefundRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_REFUND_APPLY_REFUSE);
        refuseApplyRefundRequest.addRequestFormParam(ApiKey.ORDER_ORDER_ID, String.valueOf(orderId));
        return refuseApplyRefundRequest;
    }

    @Override
    public OkHttpRequest applyForRefundRequest(int orderId, int workStatus, String refundReason, String refundAmount) {
        OkHttpRequest applyRefundRequest = new OkHttpRequest();
        applyRefundRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_REFUND_APPLY_SAVE);
        applyRefundRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_REFUND_APPLY_SAVE);
        applyRefundRequest.addRequestFormParam(ApiKey.ORDER_ORDER_ID, String.valueOf(orderId));
        applyRefundRequest.addRequestFormParam(ApiKey.ORDER_WORK_STATUS, String.valueOf(workStatus));
        applyRefundRequest.addRequestFormParam(ApiKey.ORDER_REFUND_REASON, refundReason);
        applyRefundRequest.addRequestFormParam(ApiKey.ORDER_REFUND_AMOUNT, refundAmount);
        return applyRefundRequest;
    }

    @Override
    public OkHttpRequest fetchBuyerCompletedServiceListRequest(int pageNum, int demandId) {
        OkHttpRequest fetchBuyerCompletedServiceListRequest = new OkHttpRequest();
        fetchBuyerCompletedServiceListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_LIST_TO_BUYER_COMPLETE);
        fetchBuyerCompletedServiceListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_LIST_TO_BUYER_COMPLETE);
        fetchBuyerCompletedServiceListRequest.addRequestFormParam(ApiKey.COMMON_PAGE_NUM, String.valueOf(pageNum));
        fetchBuyerCompletedServiceListRequest.addRequestFormParam(ApiKey.ORDER_DEMAND_ID, String.valueOf(demandId));
        return fetchBuyerCompletedServiceListRequest;
    }

    @Override
    public OkHttpRequest fetchFacilitatorDemandOrderInformationRequest(int orderId) {
        OkHttpRequest fetchFacilitatorDemandOrderInformationRequest = new OkHttpRequest();
        fetchFacilitatorDemandOrderInformationRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_MSG_TO_DEMAND);
        fetchFacilitatorDemandOrderInformationRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_MSG_TO_DEMAND);
        fetchFacilitatorDemandOrderInformationRequest.addRequestFormParam(ApiKey.ORDER_ORDER_ID, String.valueOf(orderId));
        return fetchFacilitatorDemandOrderInformationRequest;
    }

    @Override
    public OkHttpRequest fetchArbitrationReasonListRequest() {
        OkHttpRequest fetchArbitrationReasonListRequest = new OkHttpRequest();
        fetchArbitrationReasonListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_OPERATE_REASON_LIST_BUYER_ARBITRATION_APPLY);
        fetchArbitrationReasonListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_OPERATE_REASON_LIST_BUYER_ARBITRATION_APPLY);
        return fetchArbitrationReasonListRequest;
    }

    @Override
    public OkHttpRequest fetchRefundReasonListRequest() {
        OkHttpRequest fetchRefundReasonListRequest = new OkHttpRequest();
        fetchRefundReasonListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_OPERATE_REASON_LIST_BUYER_CANCEL_BEFORE_START_WORK);
        fetchRefundReasonListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_OPERATE_REASON_LIST_BUYER_CANCEL_BEFORE_START_WORK);
        return fetchRefundReasonListRequest;
    }

    @Override
    public OkHttpRequest fetchBuyerEvaluateDetailsRequest(int orderId) {
        OkHttpRequest fetchBuyerEvaluateDetailsRequest = new OkHttpRequest();
        fetchBuyerEvaluateDetailsRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_BUYER_DETAILS);
        fetchBuyerEvaluateDetailsRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_BUYER_DETAILS);
        fetchBuyerEvaluateDetailsRequest.addRequestFormParam(ApiKey.ORDER_ORDER_ID, String.valueOf(orderId));
        return fetchBuyerEvaluateDetailsRequest;
    }

    @Override
    public OkHttpRequest fetchSellerEvaluateDetailsRequest(int orderId) {
        OkHttpRequest fetchSellerEvaluateDetailsRequest = new OkHttpRequest();
        fetchSellerEvaluateDetailsRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_SELLER_DETAILS);
        fetchSellerEvaluateDetailsRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_SELLER_DETAILS);
        fetchSellerEvaluateDetailsRequest.addRequestFormParam(ApiKey.ORDER_ORDER_ID, String.valueOf(orderId));
        return fetchSellerEvaluateDetailsRequest;
    }

    @Override
    public OkHttpRequest fetchRecommendEvaluateRequest(int userId) {
        OkHttpRequest fetchRecommendEvaluateRequest = new OkHttpRequest();
        fetchRecommendEvaluateRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_SELLER_TO_SERVE_DETAILS + "/" + String.valueOf(userId));
        fetchRecommendEvaluateRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_SELLER_TO_SERVE_DETAILS + "/" + String.valueOf(userId));
        return fetchRecommendEvaluateRequest;
    }

    @Override
    public OkHttpRequest fetchRecommendEvaluateListRequest(int userId, int pageNum) {
        OkHttpRequest fetchRecommendEvaluateListRequest = new OkHttpRequest();
        fetchRecommendEvaluateListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_SELLER_LIST + "/" + String.valueOf(userId));
        fetchRecommendEvaluateListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_SELLER_LIST + "/" + String.valueOf(userId));
        fetchRecommendEvaluateListRequest.addRequestFormParam(ApiKey.COMMON_PAGE_NUM, String.valueOf(pageNum));
        return fetchRecommendEvaluateListRequest;
    }

}
