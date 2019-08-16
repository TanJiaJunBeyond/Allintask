package com.allintask.lingdao.presenter.service;

import com.allintask.lingdao.model.service.IServiceModel;
import com.allintask.lingdao.model.service.ServiceModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.service.IServiceCategoryView;

/**
 * Created by TanJiaJun on 2018/2/26.
 */

public class ServiceCategoryPresenter extends BasePresenter<IServiceCategoryView> {

    private IServiceModel serviceModel;

    public ServiceCategoryPresenter() {
        serviceModel = new ServiceModel();
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {

    }

}
