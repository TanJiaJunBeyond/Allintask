package com.allintask.lingdao.model.service;

import java.io.File;

import cn.tanjiajun.sdk.conn.OkHttpRequest;

/**
 * Created by TanJiaJun on 2018/2/1.
 */

public interface IServiceModel {

    OkHttpRequest fetchHotSkillsListRequest();

    OkHttpRequest selectSkillsRequest(int categoryId);

    OkHttpRequest fetchServiceCategoryListRequest();

    OkHttpRequest fetchServiceModeAndPriceModeRequest(int categoryId);

    OkHttpRequest fetchServiceIsPublishedRequest(int categoryId);

    OkHttpRequest publishServiceRequest(int userId, String categoryPropertyList, String serveWayPriceUnitList, String advantage, String introduce, String voiceTmpUrl, String format, int categoryId, int voiceDuration, String provinceCode, String cityCode);

    OkHttpRequest getIdToChineseRequest();

    OkHttpRequest fetchMyServiceListRequest(int pageNum);

    OkHttpRequest goOnlineServiceRequest(int id);

    OkHttpRequest goOfflineServiceRequest(int id);

    OkHttpRequest deleteServiceRequest(int id);

    OkHttpRequest fetchMyServiceBeanRequest(int id);

    OkHttpRequest updateServiceRequest(int id, int userId, String categoryPropertyList, String serveWayPriceUnitList, String advantage, String introduce, String voiceTmpUrl, String format, int categoryId, int voiceDuration, int voiceStatus, String provinceCode, String cityCode);

    OkHttpRequest fetchWaitBidServiceListRequest(int pageNum);

    OkHttpRequest changePriceRequest(int bidId, String price);

    OkHttpRequest fetchBuyerHasBidServiceListRequest(int pageNum, int demandId);

    OkHttpRequest fetchSellerHasBidServiceListRequest(int pageNum);

    OkHttpRequest bidRequest(int demandId, String price, String advantage, int isBook);

    OkHttpRequest fetchBuyerExpiredServiceListRequest(int pageNum, int demandId);

    OkHttpRequest fetchSellerUnderwayServiceListRequest(int pageNum);

    OkHttpRequest fetchSellerCompletedServiceListRequest(int pageNum);

    OkHttpRequest fetchSellerExpiredServiceListRequest(int pageNum);

    OkHttpRequest fetchFacilitatorDemandDetailsRequest(int demandId);

    OkHttpRequest fetchPublishedListRequest();

    OkHttpRequest fetchServiceAlbumSurplusRequest(int serveId);

    OkHttpRequest uploadVoiceRequest(int userId, File file);

    OkHttpRequest fetchRecommendServiceInformationRequest(int userId);

    OkHttpRequest fetchVoiceDemoRequest();

    OkHttpRequest fetchBidInformationRequest(int categoryId);

}
