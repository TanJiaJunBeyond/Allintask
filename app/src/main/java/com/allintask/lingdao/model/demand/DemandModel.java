package com.allintask.lingdao.model.demand;

import android.text.TextUtils;

import com.allintask.lingdao.constant.ApiKey;
import com.allintask.lingdao.constant.ServiceAPIConstant;

import cn.tanjiajun.sdk.conn.OkHttpRequest;

/**
 * Created by TanJiaJun on 2018/2/3.
 */

public class DemandModel implements IDemandModel {

    @Override
    public OkHttpRequest selectServiceRequest(int categoryId) {
        OkHttpRequest selectServiceRequest = new OkHttpRequest();
        selectServiceRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_EXPERIENCE);
        selectServiceRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_EXPERIENCE);
        selectServiceRequest.addRequestFormParam(ApiKey.DEMAND_CATEGORY_ID, String.valueOf(categoryId));
        return selectServiceRequest;
    }

    @Override
    public OkHttpRequest publishDemandRequest(int userId, int serveId, int isShare, int categoryId, String categoryPropertyList, int serveWayId, String bookingDate, int deliverCycleId, int budget, String introduce, String provinceCode, String cityCode) {
        OkHttpRequest publishDemandRequest = new OkHttpRequest();
        publishDemandRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_PUBLISH);
        publishDemandRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_PUBLISH);
        publishDemandRequest.addRequestFormParam(ApiKey.COMMON_USER_ID, String.valueOf(userId));

        if (serveId != -1) {
            publishDemandRequest.addRequestFormParam(ApiKey.DEMAND_SERVE_ID, String.valueOf(serveId));
        }

        if (isShare != -1) {
            publishDemandRequest.addRequestFormParam(ApiKey.DEMAND_IS_SHARE, String.valueOf(isShare));
        }

        if (categoryId != -1) {
            publishDemandRequest.addRequestFormParam(ApiKey.DEMAND_CATEGORY_ID, String.valueOf(categoryId));
        }

        if (!TextUtils.isEmpty(categoryPropertyList)) {
            publishDemandRequest.addRequestFormParam(ApiKey.DEMAND_CATEGORY_PROPERTY_LIST, categoryPropertyList);
        }

        publishDemandRequest.addRequestFormParam(ApiKey.DEMAND_SERVE_WAY_ID, String.valueOf(serveWayId));
        publishDemandRequest.addRequestFormParam(ApiKey.DEMAND_BOOKING_DATE, bookingDate);
        publishDemandRequest.addRequestFormParam(ApiKey.DEMAND_DELIVER_CYCLE_ID, String.valueOf(deliverCycleId));
        publishDemandRequest.addRequestFormParam(ApiKey.DEMAND_BUDGET, String.valueOf(budget));

        if (!TextUtils.isEmpty(introduce)) {
            publishDemandRequest.addRequestFormParam(ApiKey.DEMAND_INTRODUCE, introduce);
        }

        if (!TextUtils.isEmpty(provinceCode)) {
            publishDemandRequest.addRequestFormParam(ApiKey.DEMAND_PROVINCE_CODE, provinceCode);
        }

        if (!TextUtils.isEmpty(cityCode)) {
            publishDemandRequest.addRequestFormParam(ApiKey.DEMAND_CITY_CODE, cityCode);
        }
        return publishDemandRequest;
    }

    @Override
    public OkHttpRequest fetchPaymentPeriodListRequest() {
        OkHttpRequest fetchPaymentPeriodRequest = new OkHttpRequest();
        fetchPaymentPeriodRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DELIVER_CYCLE_LIST);
        fetchPaymentPeriodRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DELIVER_CYCLE_LIST);
        return fetchPaymentPeriodRequest;
    }

    @Override
    public OkHttpRequest fetchDemandManageListRequest(String tag, int pageNum) {
        OkHttpRequest fetchDemandManageListRequest = new OkHttpRequest();
        fetchDemandManageListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_GET_DEMAND_MANAGE_LIST + "/" + tag);
        fetchDemandManageListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_GET_DEMAND_MANAGE_LIST + "/" + tag);
        fetchDemandManageListRequest.addRequestFormParam(ApiKey.COMMON_PAGE_NUM, String.valueOf(pageNum));
        return fetchDemandManageListRequest;
    }

    @Override
    public OkHttpRequest fetchDemandDetailsRequest(int demandId) {
        OkHttpRequest fetchDemandDetailsRequest = new OkHttpRequest();
        fetchDemandDetailsRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_DETAILS_TO_BUYER);
        fetchDemandDetailsRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_DETAILS_TO_BUYER);
        fetchDemandDetailsRequest.addRequestFormParam(ApiKey.DEMAND_DEMAND_ID, String.valueOf(demandId));
        return fetchDemandDetailsRequest;
    }

    @Override
    public OkHttpRequest fetchIntelligentMatchInformServiceProviderListRequest(int demandId) {
        OkHttpRequest fetchIntelligentMatchInformServiceProviderListRequest = new OkHttpRequest();
        fetchIntelligentMatchInformServiceProviderListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_MATCH_RADAR_LIST);
        fetchIntelligentMatchInformServiceProviderListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_MATCH_RADAR_LIST);
        fetchIntelligentMatchInformServiceProviderListRequest.addRequestFormParam(ApiKey.DEMAND_DEMAND_ID, String.valueOf(demandId));
        return fetchIntelligentMatchInformServiceProviderListRequest;
    }

    @Override
    public OkHttpRequest applyForRefundRequest(int orderId, String reasonTag, int reasonId) {
        OkHttpRequest applyForRefundRequest = new OkHttpRequest();
        applyForRefundRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_OPERATE_REFUND);
        applyForRefundRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_OPERATE_REFUND);
        applyForRefundRequest.addRequestFormParam(ApiKey.DEMAND_ORDER_ID, String.valueOf(orderId));
        applyForRefundRequest.addRequestFormParam(ApiKey.DEMAND_REASON_TAG, reasonTag);
        applyForRefundRequest.addRequestFormParam(ApiKey.DEMAND_REASON_ID, String.valueOf(reasonId));
        return applyForRefundRequest;
    }

    @Override
    public OkHttpRequest fetchRecommendDemandDetailsRequest(int demandId) {
        OkHttpRequest fetchRecommendDemandDetailsRequest = new OkHttpRequest();
        fetchRecommendDemandDetailsRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_DETAILS);
        fetchRecommendDemandDetailsRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_DETAILS);
        fetchRecommendDemandDetailsRequest.addRequestFormParam(ApiKey.DEMAND_DEMAND_ID, String.valueOf(demandId));
        return fetchRecommendDemandDetailsRequest;
    }

    @Override
    public OkHttpRequest fetchRecommendDemandDetailsUserInformationRequest(int userId) {
        OkHttpRequest fetchRecommendDemandDetailsUserInformationRequest = new OkHttpRequest();
        fetchRecommendDemandDetailsUserInformationRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_INFO_TO_DEMAND_DETAILS + "/" + String.valueOf(userId));
        fetchRecommendDemandDetailsUserInformationRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_INFO_TO_DEMAND_DETAILS + "/" + String.valueOf(userId));
        return fetchRecommendDemandDetailsUserInformationRequest;
    }

    @Override
    public OkHttpRequest goOnlineDemandRequest(int id) {
        OkHttpRequest goOnlineDemandRequest = new OkHttpRequest();
        goOnlineDemandRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_GO_ONLINE + "/" + String.valueOf(id));
        goOnlineDemandRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_GO_ONLINE + "/" + String.valueOf(id));
        return goOnlineDemandRequest;
    }

    @Override
    public OkHttpRequest goOfflineDemandRequest(int id) {
        OkHttpRequest goOfflineDemandRequest = new OkHttpRequest();
        goOfflineDemandRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_GO_OFFLINE + "/" + String.valueOf(id));
        goOfflineDemandRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_GO_OFFLINE + "/" + String.valueOf(id));
        return goOfflineDemandRequest;
    }

    @Override
    public OkHttpRequest deleteDemandRequest(int id) {
        OkHttpRequest deleteDemandRequest = new OkHttpRequest();
        deleteDemandRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_DELETE + "/" + String.valueOf(id));
        deleteDemandRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_DELETE + "/" + String.valueOf(id));
        return deleteDemandRequest;
    }

    @Override
    public OkHttpRequest fetchDemandCategoryListRequest() {
        OkHttpRequest fetchDemandCategoryListRequest = new OkHttpRequest();
        fetchDemandCategoryListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_LIST_TO_DEMAND);
        fetchDemandCategoryListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_LIST_TO_DEMAND);
        return fetchDemandCategoryListRequest;
    }

    @Override
    public OkHttpRequest publishDemandV1Request(int isShare, int categoryId, String categoryPropertyList, int serveWayId, String provinceCode, String cityCode, String bookingDate, int employmentTimes, int employmentCycle, int employmentCycleUnitId, int budget, String introduce, int showEmploymentTimes) {
        OkHttpRequest publishDemandV1Request = new OkHttpRequest();
        publishDemandV1Request.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_V1_PUBLISH);
        publishDemandV1Request.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_V1_PUBLISH);

        if (isShare != -1) {
            publishDemandV1Request.addRequestFormParam(ApiKey.DEMAND_IS_SHARE, String.valueOf(isShare));
        }

        if (categoryId != -1) {
            publishDemandV1Request.addRequestFormParam(ApiKey.DEMAND_CATEGORY_ID, String.valueOf(categoryId));
        }

        if (!TextUtils.isEmpty(categoryPropertyList)) {
            publishDemandV1Request.addRequestFormParam(ApiKey.DEMAND_CATEGORY_PROPERTY_LIST, categoryPropertyList);
        }

        publishDemandV1Request.addRequestFormParam(ApiKey.DEMAND_SERVE_WAY_ID, String.valueOf(serveWayId));

        if (!TextUtils.isEmpty(provinceCode)) {
            publishDemandV1Request.addRequestFormParam(ApiKey.DEMAND_PROVINCE_CODE, provinceCode);
        }

        if (!TextUtils.isEmpty(cityCode)) {
            publishDemandV1Request.addRequestFormParam(ApiKey.DEMAND_CITY_CODE, cityCode);
        }

        publishDemandV1Request.addRequestFormParam(ApiKey.DEMAND_BOOKING_DATE, bookingDate);
        publishDemandV1Request.addRequestFormParam(ApiKey.DEMAND_EMPLOYMENT_TIMES, String.valueOf(employmentTimes));
        publishDemandV1Request.addRequestFormParam(ApiKey.DEMAND_EMPLOYMENT_CYCLE, String.valueOf(employmentCycle));
        publishDemandV1Request.addRequestFormParam(ApiKey.DEMAND_EMPLOYMENT_CYCLE_UNIT_ID, String.valueOf(employmentCycleUnitId));
        publishDemandV1Request.addRequestFormParam(ApiKey.DEMAND_BUDGET, String.valueOf(budget));

        if (!TextUtils.isEmpty(introduce)) {
            publishDemandV1Request.addRequestFormParam(ApiKey.DEMAND_INTRODUCE, introduce);
        }

        publishDemandV1Request.addRequestFormParam(ApiKey.DEMAND_SHOW_EMPLOYMENT_TIMES, String.valueOf(showEmploymentTimes));
        return publishDemandV1Request;
    }

    @Override
    public OkHttpRequest fetchDemandRequest(int id) {
        OkHttpRequest fetchDemandRequest = new OkHttpRequest();
        fetchDemandRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_V1_GET + "/" + String.valueOf(id));
        fetchDemandRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_V1_GET + "/" + String.valueOf(id));
        return fetchDemandRequest;
    }

    @Override
    public OkHttpRequest updateDemandRequest(int demandId, int categoryId, String categoryPropertyList, int serveWayId, String provinceCode, String cityCode, String bookingDate, int employmentTimes, int employmentCycle, int employmentCycleUnitId, int budget, String introduce, int showEmploymentTimes) {
        OkHttpRequest updateDemandRequest = new OkHttpRequest();
        updateDemandRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_V1_UPDATE);
        updateDemandRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_V1_UPDATE);
        updateDemandRequest.addRequestFormParam(ApiKey.DEMAND_DEMAND_ID, String.valueOf(demandId));
        updateDemandRequest.addRequestFormParam(ApiKey.DEMAND_CATEGORY_ID, String.valueOf(categoryId));

        if (!TextUtils.isEmpty(categoryPropertyList)) {
            updateDemandRequest.addRequestFormParam(ApiKey.DEMAND_CATEGORY_PROPERTY_LIST, categoryPropertyList);
        }

        updateDemandRequest.addRequestFormParam(ApiKey.DEMAND_SERVE_WAY_ID, String.valueOf(serveWayId));

        if (!TextUtils.isEmpty(provinceCode)) {
            updateDemandRequest.addRequestFormParam(ApiKey.DEMAND_PROVINCE_CODE, provinceCode);
        }

        if (!TextUtils.isEmpty(cityCode)) {
            updateDemandRequest.addRequestFormParam(ApiKey.DEMAND_CITY_CODE, cityCode);
        }

        updateDemandRequest.addRequestFormParam(ApiKey.DEMAND_BOOKING_DATE, bookingDate);
        updateDemandRequest.addRequestFormParam(ApiKey.DEMAND_EMPLOYMENT_TIMES, String.valueOf(employmentTimes));
        updateDemandRequest.addRequestFormParam(ApiKey.DEMAND_EMPLOYMENT_CYCLE, String.valueOf(employmentCycle));
        updateDemandRequest.addRequestFormParam(ApiKey.DEMAND_EMPLOYMENT_CYCLE_UNIT_ID, String.valueOf(employmentCycleUnitId));
        updateDemandRequest.addRequestFormParam(ApiKey.DEMAND_BUDGET, String.valueOf(budget));
        updateDemandRequest.addRequestFormParam(ApiKey.DEMAND_INTRODUCE, introduce);
        updateDemandRequest.addRequestFormParam(ApiKey.DEMAND_SHOW_EMPLOYMENT_TIMES, String.valueOf(showEmploymentTimes));
        return updateDemandRequest;
    }

    @Override
    public OkHttpRequest publishPaymentDemandRequest(int serveId, int categoryId, String categoryPropertyList, int serveWayId, String provinceCode, String cityCode, int employmentTimes, int employmentCycle, int employmentCycleUnitId, int budget, int showEmploymentTimes) {
        OkHttpRequest publishPaymentDemandRequest = new OkHttpRequest();
        publishPaymentDemandRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_V1_PAY_PUBLISH);
        publishPaymentDemandRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_V1_PAY_PUBLISH);
        publishPaymentDemandRequest.addRequestFormParam(ApiKey.DEMAND_SERVE_ID, String.valueOf(serveId));
        publishPaymentDemandRequest.addRequestFormParam(ApiKey.DEMAND_CATEGORY_ID, String.valueOf(categoryId));

        if (!TextUtils.isEmpty(categoryPropertyList)) {
            publishPaymentDemandRequest.addRequestFormParam(ApiKey.DEMAND_CATEGORY_PROPERTY_LIST, categoryPropertyList);
        }

        publishPaymentDemandRequest.addRequestFormParam(ApiKey.DEMAND_SERVE_WAY_ID, String.valueOf(serveWayId));

        if (!TextUtils.isEmpty(provinceCode)) {
            publishPaymentDemandRequest.addRequestFormParam(ApiKey.DEMAND_PROVINCE_CODE, provinceCode);
        }

        if (!TextUtils.isEmpty(cityCode)) {
            publishPaymentDemandRequest.addRequestFormParam(ApiKey.DEMAND_CITY_CODE, cityCode);
        }

        publishPaymentDemandRequest.addRequestFormParam(ApiKey.DEMAND_EMPLOYMENT_TIMES, String.valueOf(employmentTimes));
        publishPaymentDemandRequest.addRequestFormParam(ApiKey.DEMAND_EMPLOYMENT_CYCLE, String.valueOf(employmentCycle));
        publishPaymentDemandRequest.addRequestFormParam(ApiKey.DEMAND_EMPLOYMENT_CYCLE_UNIT_ID, String.valueOf(employmentCycleUnitId));
        publishPaymentDemandRequest.addRequestFormParam(ApiKey.DEMAND_BUDGET, String.valueOf(budget));
        publishPaymentDemandRequest.addRequestFormParam(ApiKey.DEMAND_SHOW_EMPLOYMENT_TIMES, String.valueOf(showEmploymentTimes));
        return publishPaymentDemandRequest;
    }

}
