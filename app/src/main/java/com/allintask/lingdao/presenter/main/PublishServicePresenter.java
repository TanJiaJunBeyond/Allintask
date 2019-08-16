package com.allintask.lingdao.presenter.main;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.service.GetIdToChineseListBean;
import com.allintask.lingdao.bean.service.MyServiceBean;
import com.allintask.lingdao.bean.service.ServiceCategoryListBean;
import com.allintask.lingdao.bean.service.ServiceIsPublishedBean;
import com.allintask.lingdao.bean.service.ServiceModeAndPriceModeBean;
import com.allintask.lingdao.bean.service.ServiceModeAndPriceModeListBean;
import com.allintask.lingdao.bean.service.VoiceDemoBean;
import com.allintask.lingdao.bean.user.AddressListBean;
import com.allintask.lingdao.bean.user.CheckPersonalInformationBean;
import com.allintask.lingdao.bean.user.IsAllBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.service.IServiceModel;
import com.allintask.lingdao.model.service.ServiceModel;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.main.IPublishServiceView;

import java.io.File;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/5.
 */

public class PublishServicePresenter extends BasePresenter<IPublishServiceView> {

    private IServiceModel serviceModel;
    private IUserModel userModel;

    private int id;
    private int serviceId;

    public PublishServicePresenter() {
        serviceModel = new ServiceModel();
        userModel = new UserModel();
    }

    public void fetchServiceCategoryListRequest() {
        OkHttpRequest fetchServiceCategoryListRequest = serviceModel.fetchServiceCategoryListRequest();
        addRequestAsyncTaskForJson(true, OkHttpRequestOptions.GET, fetchServiceCategoryListRequest);
    }

    public void fetchServiceModeAndPriceModeRequest(int categoryId) {
        OkHttpRequest fetchServiceModeAndPriceModeRequest = serviceModel.fetchServiceModeAndPriceModeRequest(categoryId);
        addRequestAsyncTaskForJson(true, OkHttpRequestOptions.GET, fetchServiceModeAndPriceModeRequest);
    }

    public void fetchAddressListRequest() {
        OkHttpRequest fetchAddressListRequest = userModel.fetchAddressListRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchAddressListRequest);
    }

    public void fetchServiceIsPublishedRequest(int categoryId) {
        OkHttpRequest fetchServiceIsPublishedRequest = serviceModel.fetchServiceIsPublishedRequest(categoryId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchServiceIsPublishedRequest);
    }

    public void checkUploadIsSuccessRequest(String flagId) {
        OkHttpRequest checkUploadIsSuccessRequest = userModel.checkUploadIsSuccessRequest(flagId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, checkUploadIsSuccessRequest);
    }

    public void uploadVoiceRequest(File file) {
        int userId = UserPreferences.getInstance().getUserId();
        OkHttpRequest uploadVoiceRequest = serviceModel.uploadVoiceRequest(userId, file);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, uploadVoiceRequest);
    }

    public void publishServiceRequest(String categoryPropertyList, String serveWayPriceUnitList, String advantage, String introduce, String voiceTmpUrl, String format, int categoryId, int voiceDuration, String provinceCode, String cityCode) {
        int userId = UserPreferences.getInstance().getUserId();
        OkHttpRequest publishServiceRequest = serviceModel.publishServiceRequest(userId, categoryPropertyList, serveWayPriceUnitList, advantage, introduce, voiceTmpUrl, format, categoryId, voiceDuration, provinceCode, cityCode);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, publishServiceRequest);
    }

    public void getIdToChineseRequest() {
        OkHttpRequest getIdToChineseRequest = serviceModel.getIdToChineseRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, getIdToChineseRequest);
    }

    public void fetchMyServiceBeanRequest(int id) {
        this.id = id;
        OkHttpRequest fetchMyServiceBeanRequest = serviceModel.fetchMyServiceBeanRequest(id);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchMyServiceBeanRequest);
    }

    public void updateServiceRequest(int id, String categoryPropertyList, String serveWayPriceUnitList, String advantage, String introduce, String voiceTmpUrl, String format, int categoryId, int voiceDuration, int voiceStatus, String provinceCode, String cityCode) {
        int userId = UserPreferences.getInstance().getUserId();
        OkHttpRequest updateServiceRequest = serviceModel.updateServiceRequest(id, userId, categoryPropertyList, serveWayPriceUnitList, advantage, introduce, voiceTmpUrl, format, categoryId, voiceDuration, voiceStatus, provinceCode, cityCode);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.PUT, updateServiceRequest);
    }

    public void checkPersonalInformationWholeRequest(int serviceId) {
        this.serviceId = serviceId;

        OkHttpRequest checkPersonalInformationWholeRequest = userModel.checkPersonalInformationWholeRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, checkPersonalInformationWholeRequest);
    }

    public void fetchServiceAlbumSurplusRequest(int serviceId) {
        this.serviceId = serviceId;

        OkHttpRequest fetchServiceAlbumSurplusRequest = serviceModel.fetchServiceAlbumSurplusRequest(serviceId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchServiceAlbumSurplusRequest);
    }

    public void fetchVoiceDemoRequest() {
        OkHttpRequest fetchVoiceDemoRequest = serviceModel.fetchVoiceDemoRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchVoiceDemoRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_LIST_TO_SERVE)) {
            if (success) {
                List<ServiceCategoryListBean> serviceCategoryList = JSONArray.parseArray(data, ServiceCategoryListBean.class);

                if (null != getView()) {
                    getView().onShowServiceCategoryList(serviceCategoryList);
                }
            }
        } else {
            if (null != getView()) {
                getView().showToast(errorMessage);
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_V1_LIST_SERVE_WAY_AND_PRICE_UNIT)) {
            if (success) {
                ServiceModeAndPriceModeListBean serviceModeAndPriceModeListBean = JSONObject.parseObject(data, ServiceModeAndPriceModeListBean.class);

                if (null != serviceModeAndPriceModeListBean) {
                    Integer demandMaxBudget = serviceModeAndPriceModeListBean.demandMaxBudget;
                    Integer maxEmploymentTimes = serviceModeAndPriceModeListBean.maxEmploymentTimes;
                    List<ServiceModeAndPriceModeBean> serviceModeAndPriceModeList = serviceModeAndPriceModeListBean.serveWayAndPriceUnitVos;

                    if (null == demandMaxBudget) {
                        demandMaxBudget = 0;
                    }

                    if (null == maxEmploymentTimes) {
                        maxEmploymentTimes = 0;
                    }

                    if (null != getView()) {
                        getView().onShowServiceModeAndPriceModeList(demandMaxBudget, maxEmploymentTimes, serviceModeAndPriceModeList);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GENERAL_DATA_ADDRESS_LIST)) {
            if (success) {
                AddressListBean addressListBean = JSONObject.parseObject(data, AddressListBean.class);

                if (null != addressListBean) {
                    List<IsAllBean> isAllList = addressListBean.isAll;

                    if (null != getView()) {
                        getView().onShowAddressList(isAllList);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

//        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_FILE_OSS_A_LI_YUN_IMAGE_URL_RETURN)) {
//            if (success) {
//                List<CheckUploadIsSuccessBean> checkUploadIsSuccessList = JSONArray.parseArray(data, CheckUploadIsSuccessBean.class);
//
//                int status = -1;
//                String codeId = null;
//                String imgUrl = null;
//
//                if (null != checkUploadIsSuccessList && checkUploadIsSuccessList.size() > 0) {
//                    CheckUploadIsSuccessBean checkUploadIsSuccessBean = checkUploadIsSuccessList.get(0);
//
//                    if (null != checkUploadIsSuccessBean) {
//                        status = TypeUtils.getInteger(checkUploadIsSuccessBean.status, -1);
//                        codeId = TypeUtils.getString(checkUploadIsSuccessBean.codeId, "");
//                        imgUrl = TypeUtils.getString(checkUploadIsSuccessBean.imgUrl, "");
//                    }
//                }
//
//                if (null != getView()) {
//                    getView().uploadSuccess(status, codeId, imgUrl);
//                }
//            } else {
//                if (null != getView()) {
//                    getView().uploadFail();
//                }
//            }
//        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_LIST_SERVE_IS_PUBLISH)) {
            if (success) {
                ServiceIsPublishedBean serviceIsPublishedBean = JSONObject.parseObject(data, ServiceIsPublishedBean.class);

                if (null != serviceIsPublishedBean) {
                    boolean isPublished = TypeUtils.getBoolean(serviceIsPublishedBean.isPublish, false);
                    int serveId = TypeUtils.getInteger(serviceIsPublishedBean.serveId, -1);

                    if (null != getView()) {
                        getView().onShowIsPublished(isPublished, serveId);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_FILE_OSS_VOICE_UPLOAD)) {
            if (success) {
                if (null != getView()) {
                    getView().uploadVoiceSuccess(data);
                }
            } else {
                if (null != getView()) {
                    getView().uploadVoiceFail();
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_LIST_SERVE_PUBLISH)) {
            if (success) {
                int serviceId = Integer.valueOf(data);

                if (null != getView()) {
                    getView().onPublishServiceSuccess(serviceId);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }

            if (null != getView()) {
                getView().dismissProgressDialog();
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_GET_ID_TO_CHINESE)) {
            if (success) {
                GetIdToChineseListBean getIdToChineseListBean = JSONObject.parseObject(data, GetIdToChineseListBean.class);

                if (null != getIdToChineseListBean && null != getView()) {
                    getView().onShowGetIdToChineseListBean(getIdToChineseListBean);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_GET + "/" + String.valueOf(id))) {
            if (success) {
                MyServiceBean myServiceBean = JSONObject.parseObject(data, MyServiceBean.class);

                if (null != getView()) {
                    getView().onShowMyServiceBean(myServiceBean);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_UPDATE)) {
            if (success) {
                if (null != getView()) {
                    getView().onUpdateServiceSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }

            if (null != getView()) {
                getView().dismissProgressDialog();
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_CHECK_PERSONAL_INFO)) {
            if (success) {
                CheckPersonalInformationBean checkPersonalInformationBean = JSONObject.parseObject(data, CheckPersonalInformationBean.class);

                if (null != checkPersonalInformationBean) {
                    boolean isExistUserEduInfo = TypeUtils.getBoolean(checkPersonalInformationBean.isExistUserEduInfo, false);
                    boolean isExistUserWorkInfo = TypeUtils.getBoolean(checkPersonalInformationBean.isExistUserWorkInfo, false);
                    boolean isExistUserHonorInfo = TypeUtils.getBoolean(checkPersonalInformationBean.isExistUserHonorInfo, false);

                    if (null != getView()) {
                        getView().onCheckPersonalInformationSuccess(isExistUserEduInfo, isExistUserWorkInfo, isExistUserHonorInfo);
                    }
                }
            } else {
                if (null != getView()) {
                    fetchServiceAlbumSurplusRequest(serviceId);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_GET_SERVE_ALBUM_SURPLUS)) {
            if (success) {
                Integer servicePhotoAlbumSurplusAmount = Integer.valueOf(data);

                if (null != getView()) {
                    if (null == servicePhotoAlbumSurplusAmount || servicePhotoAlbumSurplusAmount <= 0) {
                        getView().onShowServicePhotoAlbumSurplusAmount(0);
                    } else {
                        getView().onShowServicePhotoAlbumSurplusAmount(servicePhotoAlbumSurplusAmount);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().onFetchServicePhotoAlbumFail();
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_VOICE_EXAMPLE_GET_SERVE_VOICE_EXAMPLE)) {
            if (success) {
                VoiceDemoBean voiceDemoBean = JSONObject.parseObject(data, VoiceDemoBean.class);

                if (null != voiceDemoBean) {
                    int serveVoiceExampleDuration = TypeUtils.getInteger(voiceDemoBean.serveVoiceExampleDuration, -1);
                    String serveVoiceExampleUrl = TypeUtils.getString(voiceDemoBean.serveVoiceExampleUrl, "");

                    if (null != getView()) {
                        getView().onFetchVoiceDemoSuccess(serveVoiceExampleDuration, serveVoiceExampleUrl);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().onFetchVoiceDemoFail();
                }
            }
        }
    }

}
