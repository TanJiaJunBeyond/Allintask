package com.allintask.lingdao.presenter.demand;

import com.alibaba.fastjson.JSONArray;
import com.allintask.lingdao.bean.service.HotSkillsBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.demand.DemandModel;
import com.allintask.lingdao.model.demand.IDemandModel;
import com.allintask.lingdao.model.service.IServiceModel;
import com.allintask.lingdao.model.service.ServiceModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.demand.ISelectServiceView;

import java.util.ArrayList;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/2/3.
 */

public class SelectServicePresenter extends BasePresenter<ISelectServiceView> {

    private IServiceModel serviceModel;
    private IDemandModel demandModel;
    private List<HotSkillsBean> hotSkillsList;

    public SelectServicePresenter() {
        serviceModel = new ServiceModel();
        demandModel = new DemandModel();
        hotSkillsList = new ArrayList<>();
    }

    public void fetchHotSkillsListRequest() {
        OkHttpRequest fetchHotSkillsListRequest = serviceModel.fetchHotSkillsListRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchHotSkillsListRequest);
    }

    public void selectServiceRequest(int categoryId) {
        OkHttpRequest selectServiceRequest = demandModel.selectServiceRequest(categoryId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, selectServiceRequest);
    }

    public int getServiceId(int position) {
        int serviceId = 0;

        for (int i = 0; i < hotSkillsList.size(); i++) {
            HotSkillsBean hotSkillsBean = hotSkillsList.get(i);

            if (null != hotSkillsBean) {
                int tempServiceId = TypeUtils.getInteger(hotSkillsBean.code, -1);

                if (i == position) {
                    serviceId = tempServiceId;
                }
            }
        }
        return serviceId;
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_CATEGORY_LIST_HOT)) {
            if (success) {
                List<HotSkillsBean> hotSkillsList = JSONArray.parseArray(data, HotSkillsBean.class);

                if (null != hotSkillsList && hotSkillsList.size() > 0) {
                    this.hotSkillsList.clear();
                    this.hotSkillsList.addAll(hotSkillsList);

                    List<String> serviceList = new ArrayList<>();

                    for (int i = 0; i < this.hotSkillsList.size(); i++) {
                        HotSkillsBean hotSkillsBean = this.hotSkillsList.get(i);

                        if (null != hotSkillsBean) {
                            String skills = TypeUtils.getString(hotSkillsBean.value, "");
                            serviceList.add(skills);
                        }
                    }

                    if (null != getView()) {
                        getView().onShowSelectServiceList(serviceList);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_EXPERIENCE)) {
            if (null != getView()) {
                if (success) {
                    getView().onSelectServiceSuccess();
                } else {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
