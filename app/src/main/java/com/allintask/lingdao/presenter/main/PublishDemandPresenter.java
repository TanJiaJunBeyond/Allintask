package com.allintask.lingdao.presenter.main;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.demand.DemandBean;
import com.allintask.lingdao.bean.demand.DemandPeriodListBean;
import com.allintask.lingdao.bean.demand.PublishDemandBean;
import com.allintask.lingdao.bean.demand.PublishPaymentDemandBean;
import com.allintask.lingdao.bean.message.UserInfoListRequestBean;
import com.allintask.lingdao.bean.message.UserInfoResponseBean;
import com.allintask.lingdao.bean.service.ServiceCategoryListBean;
import com.allintask.lingdao.bean.service.ServiceModeAndPriceModeBean;
import com.allintask.lingdao.bean.service.ServiceModeAndPriceModeListBean;
import com.allintask.lingdao.bean.user.AddressListBean;
import com.allintask.lingdao.bean.user.IsAllBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.demand.DemandModel;
import com.allintask.lingdao.model.demand.IDemandModel;
import com.allintask.lingdao.model.service.IServiceModel;
import com.allintask.lingdao.model.service.ServiceModel;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.main.IPublishDemandView;

import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/8.
 */

public class PublishDemandPresenter extends BasePresenter<IPublishDemandView> {

    private IServiceModel serviceModel;
    private IDemandModel demandModel;
    private IUserModel userModel;

    public PublishDemandPresenter() {
        serviceModel = new ServiceModel();
        demandModel = new DemandModel();
        userModel = new UserModel();
    }

    public void fetchDemandCategoryListRequest() {
        OkHttpRequest fetchServiceCategoryListRequest = demandModel.fetchDemandCategoryListRequest();
        addRequestAsyncTaskForJsonNoProgress(true, OkHttpRequestOptions.GET, fetchServiceCategoryListRequest);
    }

    public void fetchServiceModeAndPriceModeRequest(int categoryId) {
        OkHttpRequest fetchServiceModeAndPriceModeRequest = serviceModel.fetchServiceModeAndPriceModeRequest(categoryId);
        addRequestAsyncTaskForJson(true, OkHttpRequestOptions.GET, fetchServiceModeAndPriceModeRequest);
    }

    public void fetchAddressListRequest() {
        OkHttpRequest fetchAddressListRequest = userModel.fetchAddressListRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchAddressListRequest);
    }

    public void publishDemandRequest(int serveId, int isShare, int categoryId, String categoryPropertyList, int serveWayId, String bookingDate, int deliverCycleId, int budget, String introduce, String provinceCode, String cityCode) {
        int userId = UserPreferences.getInstance().getUserId();
        OkHttpRequest publishDemandRequest = demandModel.publishDemandRequest(userId, serveId, isShare, categoryId, categoryPropertyList, serveWayId, bookingDate, deliverCycleId, budget, introduce, provinceCode, cityCode);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, publishDemandRequest);
    }

    public void publishDemandV1Request(int isShare, int categoryId, String categoryPropertyList, int serveWayId, String provinceCode, String cityCode, String bookingDate, int employmentTimes, int employmentCycle, int employmentCycleUnitId, int budget, String introduce, int showEmploymentTimes) {
        OkHttpRequest publishDemandV1Request = demandModel.publishDemandV1Request(isShare, categoryId, categoryPropertyList, serveWayId, provinceCode, cityCode, bookingDate, employmentTimes, employmentCycle, employmentCycleUnitId, budget, introduce, showEmploymentTimes);
        addJsonStringRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, publishDemandV1Request);
    }

    public void publishPaymentDemandRequest(int serveId, int categoryId, String categoryPropertyList, int serveWayId, String provinceCode, String cityCode, int employmentTimes, int employmentCycle, int employmentCycleUnitId, int budget, int showEmploymentTimes) {
        OkHttpRequest publishPaymentDemandRequest = demandModel.publishPaymentDemandRequest(serveId, categoryId, categoryPropertyList, serveWayId, provinceCode, cityCode, employmentTimes, employmentCycle, employmentCycleUnitId, budget, showEmploymentTimes);
        addJsonStringRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, publishPaymentDemandRequest);
    }

    public void fetchPaymentPeriodListRequest() {
        OkHttpRequest fetchPaymentPeriodListRequest = demandModel.fetchPaymentPeriodListRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchPaymentPeriodListRequest);
    }

    public void fetchUserInfoListRequest(UserInfoListRequestBean userInfoListRequestBean) {
        OkHttpRequest fetchUserInfoListRequest = userModel.fetchUserInfoListRequest(userInfoListRequestBean);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, fetchUserInfoListRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_LIST_TO_DEMAND)) {
            if (success) {
                List<ServiceCategoryListBean> serviceCategoryList = JSONArray.parseArray(data, ServiceCategoryListBean.class);

                if (null != getView()) {
                    getView().onShowServiceCategoryList(serviceCategoryList);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
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

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_PUBLISH) || requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_V1_PUBLISH)) {
            if (success) {
                PublishDemandBean publishDemandBean = JSONObject.parseObject(data, PublishDemandBean.class);

                if (null != publishDemandBean) {
                    int demandId = TypeUtils.getInteger(publishDemandBean.demandId, -1);
                    String imMsg = TypeUtils.getString(publishDemandBean.imMsg, "");

                    if (null != getView()) {
                        getView().onPublishDemandSuccess(demandId, imMsg);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_V1_PAY_PUBLISH)) {
            if (success) {
                PublishPaymentDemandBean publishPaymentDemandBean = JSONObject.parseObject(data, PublishPaymentDemandBean.class);

                if (null != publishPaymentDemandBean) {
                    String serveBidNo = TypeUtils.getString(publishPaymentDemandBean.serveBidNo, "");
                    double totalAmount = TypeUtils.getDouble(publishPaymentDemandBean.totalAmount, 0D);

                    if (null != getView()) {
                        getView().onPublishPaymentDemandSuccess(serveBidNo, totalAmount);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DELIVER_CYCLE_LIST)) {
            if (success) {
                List<DemandPeriodListBean> demandPeriodList = JSONArray.parseArray(data, DemandPeriodListBean.class);

                if (null != getView()) {
                    getView().onShowDemandPeriodList(demandPeriodList);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_INFO_GET_USER_IM_MSG)) {
            if (success) {
                List<UserInfoResponseBean> userInfoResponseList = JSONArray.parseArray(data, UserInfoResponseBean.class);

                if (null != userInfoResponseList && userInfoResponseList.size() > 0) {
                    UserInfoResponseBean userInfoResponseBean = userInfoResponseList.get(0);

                    if (null != getView() && null != userInfoResponseBean) {
                        getView().onShowUserInfoBean(userInfoResponseBean);
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
