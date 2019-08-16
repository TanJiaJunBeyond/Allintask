package com.allintask.lingdao.model.service;

import android.text.TextUtils;

import com.allintask.lingdao.constant.ApiKey;
import com.allintask.lingdao.constant.ServiceAPIConstant;

import java.io.File;

import cn.tanjiajun.sdk.conn.OkHttpRequest;

/**
 * Created by TanJiaJun on 2018/2/1.
 */

public class ServiceModel implements IServiceModel {

    @Override
    public OkHttpRequest fetchHotSkillsListRequest() {
        OkHttpRequest fetchHotSkillsListRequest = new OkHttpRequest();
        fetchHotSkillsListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_LIST_HOT);
        fetchHotSkillsListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_LIST_HOT);
        return fetchHotSkillsListRequest;
    }

    @Override
    public OkHttpRequest selectSkillsRequest(int categoryId) {
        OkHttpRequest selectServiceRequest = new OkHttpRequest();
        selectServiceRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_EXPERIENCE);
        selectServiceRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_EXPERIENCE);
        selectServiceRequest.addRequestFormParam(ApiKey.SERVICE_CATEGORY_ID, String.valueOf(categoryId));
        return selectServiceRequest;
    }

    @Override
    public OkHttpRequest fetchServiceCategoryListRequest() {
        OkHttpRequest fetchServiceCategoryListRequest = new OkHttpRequest();
        fetchServiceCategoryListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_LIST_TO_SERVE);
        fetchServiceCategoryListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_LIST_TO_SERVE);
        return fetchServiceCategoryListRequest;
    }

    @Override
    public OkHttpRequest fetchServiceModeAndPriceModeRequest(int categoryId) {
        OkHttpRequest fetchServiceModeAndPriceModeRequest = new OkHttpRequest();
        fetchServiceModeAndPriceModeRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_V1_LIST_SERVE_WAY_AND_PRICE_UNIT);
        fetchServiceModeAndPriceModeRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_V1_LIST_SERVE_WAY_AND_PRICE_UNIT);
        fetchServiceModeAndPriceModeRequest.addRequestFormParam(ApiKey.SERVICE_CATEGORY_ID, String.valueOf(categoryId));
        return fetchServiceModeAndPriceModeRequest;
    }

    @Override
    public OkHttpRequest fetchServiceIsPublishedRequest(int categoryId) {
        OkHttpRequest fetchServiceIsPublishedRequest = new OkHttpRequest();
        fetchServiceIsPublishedRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_LIST_SERVE_IS_PUBLISH);
        fetchServiceIsPublishedRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_LIST_SERVE_IS_PUBLISH);
        fetchServiceIsPublishedRequest.addRequestFormParam(ApiKey.SERVICE_CATEGORY_ID, String.valueOf(categoryId));
        return fetchServiceIsPublishedRequest;
    }

    @Override
    public OkHttpRequest publishServiceRequest(int userId, String categoryPropertyList, String serveWayPriceUnitList, String advantage, String introduce, String voiceTmpUrl, String format, int categoryId, int voiceDuration, String provinceCode, String cityCode) {
        OkHttpRequest publishServiceRequest = new OkHttpRequest();
        publishServiceRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_LIST_SERVE_PUBLISH);
        publishServiceRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_LIST_SERVE_PUBLISH);
        publishServiceRequest.addRequestFormParam(ApiKey.SERVICE_USER_ID, String.valueOf(userId));

        if (!TextUtils.isEmpty(categoryPropertyList)) {
            publishServiceRequest.addRequestFormParam(ApiKey.SERVICE_CATEGORY_PROPERTY_LIST, categoryPropertyList);
        }

        publishServiceRequest.addRequestFormParam(ApiKey.SERVICE_SERVE_WAY_PRICE_UNIT_LIST, serveWayPriceUnitList);
        publishServiceRequest.addRequestFormParam(ApiKey.SERVICE_ADVANTAGE, advantage);
        publishServiceRequest.addRequestFormParam(ApiKey.SERVICE_INTRODUCE, introduce);

        if (!TextUtils.isEmpty(voiceTmpUrl)) {
            publishServiceRequest.addRequestFormParam(ApiKey.SERVICE_VOICE_TMP_URL, voiceTmpUrl);
        }

        if (!TextUtils.isEmpty(format)) {
            publishServiceRequest.addRequestFormParam(ApiKey.SERVICE_FORMAT, format);
        }

        publishServiceRequest.addRequestFormParam(ApiKey.SERVICE_CATEGORY_ID, String.valueOf(categoryId));

        if (voiceDuration != -1) {
            publishServiceRequest.addRequestFormParam(ApiKey.SERVICE_VOICE_DURATION, String.valueOf(voiceDuration));
        }

        if (!TextUtils.isEmpty(provinceCode)) {
            publishServiceRequest.addRequestFormParam(ApiKey.SERVICE_PROVINCE_CODE, provinceCode);
        }

        if (!TextUtils.isEmpty(cityCode)) {
            publishServiceRequest.addRequestFormParam(ApiKey.SERVICE_CITY_CODE, cityCode);
        }
        return publishServiceRequest;
    }

    @Override
    public OkHttpRequest getIdToChineseRequest() {
        OkHttpRequest getIdToChineseRequest = new OkHttpRequest();
        getIdToChineseRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_GET_ID_TO_CHINESE);
        getIdToChineseRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_GET_ID_TO_CHINESE);
        return getIdToChineseRequest;
    }

    @Override
    public OkHttpRequest fetchMyServiceListRequest(int pageNum) {
        OkHttpRequest fetchMyServiceListRequest = new OkHttpRequest();
        fetchMyServiceListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_MY_SERVE_LIST);
        fetchMyServiceListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_MY_SERVE_LIST);
        fetchMyServiceListRequest.addRequestFormParam(ApiKey.COMMON_PAGE_NUM, String.valueOf(pageNum));
        return fetchMyServiceListRequest;
    }

    @Override
    public OkHttpRequest goOnlineServiceRequest(int id) {
        OkHttpRequest goOnlineServiceRequest = new OkHttpRequest();
        goOnlineServiceRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_GO_ONLINE + "/" + String.valueOf(id));
        goOnlineServiceRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_GO_ONLINE + "/" + String.valueOf(id));
        goOnlineServiceRequest.addRequestFormParam(ApiKey.COMMON_ID, String.valueOf(id));
        return goOnlineServiceRequest;
    }

    @Override
    public OkHttpRequest goOfflineServiceRequest(int id) {
        OkHttpRequest goOfflineServiceRequest = new OkHttpRequest();
        goOfflineServiceRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_GO_OFFLINE + "/" + String.valueOf(id));
        goOfflineServiceRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_GO_OFFLINE + "/" + String.valueOf(id));
        goOfflineServiceRequest.addRequestFormParam(ApiKey.COMMON_ID, String.valueOf(id));
        return goOfflineServiceRequest;
    }

    @Override
    public OkHttpRequest deleteServiceRequest(int id) {
        OkHttpRequest deleteServiceRequest = new OkHttpRequest();
        deleteServiceRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_DELETE + "/" + String.valueOf(id));
        deleteServiceRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_DELETE + "/" + String.valueOf(id));
        deleteServiceRequest.addRequestFormParam(ApiKey.COMMON_ID, String.valueOf(id));
        return deleteServiceRequest;
    }

    @Override
    public OkHttpRequest fetchMyServiceBeanRequest(int id) {
        OkHttpRequest fetchMyServiceBeanRequest = new OkHttpRequest();
        fetchMyServiceBeanRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_GET + "/" + String.valueOf(id));
        fetchMyServiceBeanRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_GET + "/" + String.valueOf(id));
        return fetchMyServiceBeanRequest;
    }

    @Override
    public OkHttpRequest updateServiceRequest(int id, int userId, String categoryPropertyList, String serveWayPriceUnitList, String advantage, String introduce, String voiceTmpUrl, String format, int categoryId, int voiceDuration, int voiceStatus, String provinceCode, String cityCode) {
        OkHttpRequest updateServiceRequest = new OkHttpRequest();
        updateServiceRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_UPDATE);
        updateServiceRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_UPDATE);
        updateServiceRequest.addRequestFormParam(ApiKey.COMMON_ID, String.valueOf(id));
        updateServiceRequest.addRequestFormParam(ApiKey.SERVICE_USER_ID, String.valueOf(userId));

        if (!TextUtils.isEmpty(categoryPropertyList)) {
            updateServiceRequest.addRequestFormParam(ApiKey.SERVICE_CATEGORY_PROPERTY_LIST, categoryPropertyList);
        }

        updateServiceRequest.addRequestFormParam(ApiKey.SERVICE_SERVE_WAY_PRICE_UNIT_LIST, serveWayPriceUnitList);
        updateServiceRequest.addRequestFormParam(ApiKey.SERVICE_ADVANTAGE, advantage);
        updateServiceRequest.addRequestFormParam(ApiKey.SERVICE_INTRODUCE, introduce);

        if (!TextUtils.isEmpty(voiceTmpUrl)) {
            updateServiceRequest.addRequestFormParam(ApiKey.SERVICE_VOICE_TMP_URL, voiceTmpUrl);
        }

        if (!TextUtils.isEmpty(format)) {
            updateServiceRequest.addRequestFormParam(ApiKey.SERVICE_FORMAT, format);
        }

        updateServiceRequest.addRequestFormParam(ApiKey.SERVICE_CATEGORY_ID, String.valueOf(categoryId));

        if (voiceDuration != -1) {
            updateServiceRequest.addRequestFormParam(ApiKey.SERVICE_VOICE_DURATION, String.valueOf(voiceDuration));
        }

        updateServiceRequest.addRequestFormParam(ApiKey.SERVICE_VOICE_STATUS, String.valueOf(voiceStatus));
        updateServiceRequest.addRequestFormParam(ApiKey.SERVICE_PROVINCE_CODE, provinceCode);
        updateServiceRequest.addRequestFormParam(ApiKey.SERVICE_CITY_CODE, cityCode);
        return updateServiceRequest;
    }

    @Override
    public OkHttpRequest fetchWaitBidServiceListRequest(int pageNum) {
        OkHttpRequest fetchWaitBidServiceListRequest = new OkHttpRequest();
        fetchWaitBidServiceListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_MATCH_WAITING_BID_LIST);
        fetchWaitBidServiceListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_MATCH_WAITING_BID_LIST);
        fetchWaitBidServiceListRequest.addRequestFormParam(ApiKey.COMMON_PAGE_NUM, String.valueOf(pageNum));
        return fetchWaitBidServiceListRequest;
    }

    @Override
    public OkHttpRequest changePriceRequest(int bidId, String price) {
        OkHttpRequest changePriceRequest = new OkHttpRequest();
        changePriceRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_BID_CHANGE_PRICE);
        changePriceRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_BID_CHANGE_PRICE);
        changePriceRequest.addRequestFormParam(ApiKey.SERVICE_BID_ID, String.valueOf(bidId));
        changePriceRequest.addRequestFormParam(ApiKey.SERVICE_PRICE, price);
        return changePriceRequest;
    }

    @Override
    public OkHttpRequest fetchBuyerHasBidServiceListRequest(int pageNum, int demandId) {
        OkHttpRequest fetchBuyerHasBidServiceListRequest = new OkHttpRequest();
        fetchBuyerHasBidServiceListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_MAME_GOODS_SERVE_BID_LIST_TO_BUYER_VALID);
        fetchBuyerHasBidServiceListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_MAME_GOODS_SERVE_BID_LIST_TO_BUYER_VALID);
        fetchBuyerHasBidServiceListRequest.addRequestFormParam(ApiKey.COMMON_PAGE_NUM, String.valueOf(pageNum));
        fetchBuyerHasBidServiceListRequest.addRequestFormParam(ApiKey.SERVICE_DEMAND_ID, String.valueOf(demandId));
        return fetchBuyerHasBidServiceListRequest;
    }

    @Override
    public OkHttpRequest fetchSellerHasBidServiceListRequest(int pageNum) {
        OkHttpRequest fetchSellerHasBidServiceListRequest = new OkHttpRequest();
        fetchSellerHasBidServiceListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_BID_LIST_TO_SELLER_VALID);
        fetchSellerHasBidServiceListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_BID_LIST_TO_SELLER_VALID);
        fetchSellerHasBidServiceListRequest.addRequestFormParam(ApiKey.COMMON_PAGE_NUM, String.valueOf(pageNum));
        return fetchSellerHasBidServiceListRequest;
    }

    @Override
    public OkHttpRequest bidRequest(int demandId, String price, String advantage, int isBook) {
        OkHttpRequest saveBidRequest = new OkHttpRequest();
        saveBidRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_BID_SAVE);
        saveBidRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_BID_SAVE);
        saveBidRequest.addRequestFormParam(ApiKey.SERVICE_DEMAND_ID, String.valueOf(demandId));
        saveBidRequest.addRequestFormParam(ApiKey.SERVICE_PRICE, price);
        saveBidRequest.addRequestFormParam(ApiKey.SERVICE_ADVANTAGE, advantage);
        saveBidRequest.addRequestFormParam(ApiKey.SERVICE_IS_BOOK, String.valueOf(isBook));
        return saveBidRequest;
    }

    @Override
    public OkHttpRequest fetchBuyerExpiredServiceListRequest(int pageNum, int demandId) {
        OkHttpRequest fetchBuyerExpiredServiceListRequest = new OkHttpRequest();
        fetchBuyerExpiredServiceListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_BID_LIST_TO_BUYER_EXPIRE);
        fetchBuyerExpiredServiceListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_BID_LIST_TO_BUYER_EXPIRE);
        fetchBuyerExpiredServiceListRequest.addRequestFormParam(ApiKey.COMMON_PAGE_NUM, String.valueOf(pageNum));
        fetchBuyerExpiredServiceListRequest.addRequestFormParam(ApiKey.SERVICE_DEMAND_ID, String.valueOf(demandId));
        return fetchBuyerExpiredServiceListRequest;
    }

    @Override
    public OkHttpRequest fetchSellerUnderwayServiceListRequest(int pageNum) {
        OkHttpRequest fetchSellerUnderwayServiceListRequest = new OkHttpRequest();
        fetchSellerUnderwayServiceListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_LIST_TO_SELLER_ING);
        fetchSellerUnderwayServiceListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_LIST_TO_SELLER_ING);
        fetchSellerUnderwayServiceListRequest.addRequestFormParam(ApiKey.COMMON_PAGE_NUM, String.valueOf(pageNum));
        return fetchSellerUnderwayServiceListRequest;
    }

    @Override
    public OkHttpRequest fetchSellerCompletedServiceListRequest(int pageNum) {
        OkHttpRequest fetchSellerCompletedServiceListRequest = new OkHttpRequest();
        fetchSellerCompletedServiceListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_LIST_TO_SELLER_COMPLETE);
        fetchSellerCompletedServiceListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_ORDER_LIST_TO_SELLER_COMPLETE);
        fetchSellerCompletedServiceListRequest.addRequestFormParam(ApiKey.COMMON_PAGE_NUM, String.valueOf(pageNum));
        return fetchSellerCompletedServiceListRequest;
    }

    @Override
    public OkHttpRequest fetchSellerExpiredServiceListRequest(int pageNum) {
        OkHttpRequest fetchSellerExpiredServiceListRequest = new OkHttpRequest();
        fetchSellerExpiredServiceListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_BID_LIST_TO_SELLER_EXPIRE);
        fetchSellerExpiredServiceListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_BID_LIST_TO_SELLER_EXPIRE);
        fetchSellerExpiredServiceListRequest.addRequestFormParam(ApiKey.COMMON_PAGE_NUM, String.valueOf(pageNum));
        return fetchSellerExpiredServiceListRequest;
    }

    @Override
    public OkHttpRequest fetchFacilitatorDemandDetailsRequest(int demandId) {
        OkHttpRequest fetchFacilitatorDemandDetailsRequest = new OkHttpRequest();
        fetchFacilitatorDemandDetailsRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_DETAILS_TO_SELLER);
        fetchFacilitatorDemandDetailsRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_DETAILS_TO_SELLER);
        fetchFacilitatorDemandDetailsRequest.addRequestFormParam(ApiKey.SERVICE_DEMAND_ID, String.valueOf(demandId));
        return fetchFacilitatorDemandDetailsRequest;
    }

    @Override
    public OkHttpRequest fetchPublishedListRequest() {
        OkHttpRequest fetchPublishedListRequest = new OkHttpRequest();
        fetchPublishedListRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_LIST_PUBLISH);
        fetchPublishedListRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_LIST_PUBLISH);
        return fetchPublishedListRequest;
    }

    @Override
    public OkHttpRequest fetchServiceAlbumSurplusRequest(int serveId) {
        OkHttpRequest fetchServiceAlbumSurplusRequest = new OkHttpRequest();
        fetchServiceAlbumSurplusRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_GET_SERVE_ALBUM_SURPLUS);
        fetchServiceAlbumSurplusRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_GET_SERVE_ALBUM_SURPLUS);
        fetchServiceAlbumSurplusRequest.addRequestFormParam(ApiKey.SERVICE_SERVE_ID, String.valueOf(serveId));
        return fetchServiceAlbumSurplusRequest;
    }

    @Override
    public OkHttpRequest uploadVoiceRequest(int userId, File file) {
        OkHttpRequest uploadVoiceRequest = new OkHttpRequest();
        uploadVoiceRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_FILE_OSS_VOICE_UPLOAD);
        uploadVoiceRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_FILE_OSS_VOICE_UPLOAD);
        uploadVoiceRequest.addRequestFormParam(ApiKey.COMMON_USER_ID, String.valueOf(userId));
        uploadVoiceRequest.addRequestFileTypeParam(ApiKey.SERVICE_FILE, file);
        return uploadVoiceRequest;
    }

    @Override
    public OkHttpRequest fetchRecommendServiceInformationRequest(int userId) {
        OkHttpRequest fetchRecommendServiceInformationRequest = new OkHttpRequest();
        fetchRecommendServiceInformationRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_LIST_TO_SERVE_DETAILS + "/" + String.valueOf(userId));
        fetchRecommendServiceInformationRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_LIST_TO_SERVE_DETAILS + "/" + String.valueOf(userId));
        return fetchRecommendServiceInformationRequest;
    }

    @Override
    public OkHttpRequest fetchVoiceDemoRequest() {
        OkHttpRequest fetchVoiceDemoRequest = new OkHttpRequest();
        fetchVoiceDemoRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_VOICE_EXAMPLE_GET_SERVE_VOICE_EXAMPLE);
        fetchVoiceDemoRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_VOICE_EXAMPLE_GET_SERVE_VOICE_EXAMPLE);
        return fetchVoiceDemoRequest;
    }

    @Override
    public OkHttpRequest fetchBidInformationRequest(int categoryId) {
        OkHttpRequest fetchBidInformationRequest = new OkHttpRequest();
        fetchBidInformationRequest.setAPIPath(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_GET_BY_CATEGORY_ID);
        fetchBidInformationRequest.setRequestID(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_GET_BY_CATEGORY_ID);
        fetchBidInformationRequest.addRequestFormParam(ApiKey.SERVICE_CATEGORY_ID, String.valueOf(categoryId));
        return fetchBidInformationRequest;
    }

}
