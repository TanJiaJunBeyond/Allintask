package com.allintask.lingdao.presenter.demand;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.demand.CompileDemandBean;
import com.allintask.lingdao.bean.service.GetIdToChineseListBean;
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
import com.allintask.lingdao.view.demand.ICompileDemandView;

import java.util.Date;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/6/1.
 */

public class CompileDemandPresenter extends BasePresenter<ICompileDemandView> {

    private IDemandModel demandModel;
    private IServiceModel serviceModel;
    private IUserModel userModel;

    private int mDemandId;

    public CompileDemandPresenter() {
        demandModel = new DemandModel();
        serviceModel = new ServiceModel();
        userModel = new UserModel();
    }

    public void getIdToChineseRequest() {
        OkHttpRequest getIdToChineseRequest = serviceModel.getIdToChineseRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, getIdToChineseRequest);
    }

    public void fetchDemandCategoryListRequest() {
        OkHttpRequest fetchServiceCategoryListRequest = demandModel.fetchDemandCategoryListRequest();
        addRequestAsyncTaskForJson(true, OkHttpRequestOptions.GET, fetchServiceCategoryListRequest);
    }

    public void fetchDemandRequest(int demandId) {
        mDemandId = demandId;
        OkHttpRequest fetchDemandRequest = demandModel.fetchDemandRequest(mDemandId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchDemandRequest);
    }

    public void fetchServiceModeAndPriceModeRequest(int categoryId) {
        OkHttpRequest fetchServiceModeAndPriceModeRequest = serviceModel.fetchServiceModeAndPriceModeRequest(categoryId);
        addRequestAsyncTaskForJson(true, OkHttpRequestOptions.GET, fetchServiceModeAndPriceModeRequest);
    }

    public void fetchAddressListRequest() {
        OkHttpRequest fetchAddressListRequest = userModel.fetchAddressListRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchAddressListRequest);
    }

    public void updateDemandRequest(int demandId, int categoryId, String categoryPropertyList, int serveWayId, String provinceCode, String cityCode, String bookingDate, int employmentTimes, int employmentCycle, int employmentCycleUnitId, int budget, String introduce, int showEmploymentTimes) {
        OkHttpRequest updateDemandRequest = demandModel.updateDemandRequest(demandId, categoryId, categoryPropertyList, serveWayId, provinceCode, cityCode, bookingDate, employmentTimes, employmentCycle, employmentCycleUnitId, budget, introduce, showEmploymentTimes);
        addJsonStringRequestAsyncTaskForJson(false, OkHttpRequestOptions.PUT, updateDemandRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
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

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_V1_GET + "/" + String.valueOf(mDemandId))) {
            if (success) {
                CompileDemandBean compileDemandBean = JSONObject.parseObject(data, CompileDemandBean.class);

                if (null != compileDemandBean) {
                    int categoryId = TypeUtils.getInteger(compileDemandBean.categoryId, -1);
                    String categoryName = TypeUtils.getString(compileDemandBean.categoryName, "");
                    String categoryPropertyList = TypeUtils.getString(compileDemandBean.categoryPropertyList, "");
                    int serveWayId = TypeUtils.getInteger(compileDemandBean.serveWayId, -1);
                    String provinceCode = TypeUtils.getString(compileDemandBean.provinceCode, "");
                    String cityCode = TypeUtils.getString(compileDemandBean.cityCode, "");
                    Date bookingDate = compileDemandBean.bookingDate;
                    Integer employmentCycle = compileDemandBean.employmentCycle;
                    String employmentCycleUnit = TypeUtils.getString(compileDemandBean.employmentCycleUnit, "");
                    boolean isTrusteeship = TypeUtils.getBoolean(compileDemandBean.isTrusteeship, false);
                    int budget = TypeUtils.getInteger(compileDemandBean.budget, 0);
                    String introduce = TypeUtils.getString(compileDemandBean.introduce, "");
                    int showEmploymentTimes = TypeUtils.getInteger(compileDemandBean.showEmploymentTimes, 0);

                    if (null == employmentCycle) {
                        employmentCycle = 0;
                    }

                    if (null != getView()) {
                        getView().onShowDemandData(categoryId, categoryPropertyList, categoryName, serveWayId, provinceCode, cityCode, bookingDate, employmentCycle, employmentCycleUnit, isTrusteeship, budget, introduce, showEmploymentTimes);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showEmptyView();
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
                    getView().showEmptyView();
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

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_V1_UPDATE)) {
            if (success) {
                if (null != getView()) {
                    getView().onCompileDemandSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
