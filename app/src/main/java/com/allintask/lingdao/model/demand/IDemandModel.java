package com.allintask.lingdao.model.demand;

import cn.tanjiajun.sdk.conn.OkHttpRequest;

/**
 * Created by TanJiaJun on 2018/2/3.
 */

public interface IDemandModel {

    OkHttpRequest selectServiceRequest(int categoryId);

    OkHttpRequest publishDemandRequest(int userId, int serveId, int isShare, int categoryId, String categoryPropertyList, int serveWayId, String bookingDate, int deliverCycleId, int budget, String introduce, String provinceCode, String cityCode);

    OkHttpRequest fetchPaymentPeriodListRequest();

    OkHttpRequest fetchDemandManageListRequest(String tag, int pageNum);

    OkHttpRequest fetchDemandDetailsRequest(int demandId);

    OkHttpRequest fetchIntelligentMatchInformServiceProviderListRequest(int demandId);

    OkHttpRequest applyForRefundRequest(int orderId, String reasonTag, int reasonId);

    OkHttpRequest fetchRecommendDemandDetailsRequest(int demandId);

    OkHttpRequest fetchRecommendDemandDetailsUserInformationRequest(int userId);

    OkHttpRequest goOnlineDemandRequest(int id);

    OkHttpRequest goOfflineDemandRequest(int id);

    OkHttpRequest deleteDemandRequest(int id);

    OkHttpRequest fetchDemandCategoryListRequest();

    OkHttpRequest publishDemandV1Request(int isShare, int categoryId, String categoryPropertyList, int serveWayId, String provinceCode, String cityCode, String bookingDate, int employmentTimes, int employmentCycle, int employmentCycleUnitId, int budget, String introduce, int showEmploymentTimes);

    OkHttpRequest fetchDemandRequest(int id);

    OkHttpRequest updateDemandRequest(int demandId, int categoryId, String categoryPropertyList, int serveWayId, String provinceCode, String cityCode, String bookingDate, int employmentTimes, int employmentCycle, int employmentCycleUnitId, int budget, String introduce, int showEmploymentTimes);

    OkHttpRequest publishPaymentDemandRequest(int serveId, int categoryId, String categoryPropertyList, int serveWayId, String provinceCode, String cityCode, int employmentTimes, int employmentCycle, int employmentCycleUnitId, int budget, int showEmploymentTimes);

}
