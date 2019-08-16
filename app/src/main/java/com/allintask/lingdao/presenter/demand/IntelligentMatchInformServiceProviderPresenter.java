package com.allintask.lingdao.presenter.demand;

import com.alibaba.fastjson.JSONArray;
import com.allintask.lingdao.bean.demand.IntelligentMatchInformServiceProviderBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.demand.DemandModel;
import com.allintask.lingdao.model.demand.IDemandModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.demand.IIntelligentMatchInformServiceProviderView;

import java.util.List;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/2/9.
 */

public class IntelligentMatchInformServiceProviderPresenter extends BasePresenter<IIntelligentMatchInformServiceProviderView> {

    private IDemandModel demandModel;

    public IntelligentMatchInformServiceProviderPresenter() {
        demandModel = new DemandModel();
    }

    public void fetchIntelligentMatchInformServiceProviderListRequest(int demandId) {
        OkHttpRequest fetchIntelligentMatchInformServiceProviderListRequest = demandModel.fetchIntelligentMatchInformServiceProviderListRequest(demandId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchIntelligentMatchInformServiceProviderListRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_MATCH_RADAR_LIST)) {
            if (success) {
                List<IntelligentMatchInformServiceProviderBean> intelligentMatchInformServiceProviderList = JSONArray.parseArray(data, IntelligentMatchInformServiceProviderBean.class);

                if (null != getView()) {
                    getView().onShowIntelligentMatchInformServiceProviderList(intelligentMatchInformServiceProviderList);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
