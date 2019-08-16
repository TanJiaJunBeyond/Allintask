package com.allintask.lingdao.presenter.main;

import com.allintask.lingdao.model.service.IServiceModel;
import com.allintask.lingdao.model.service.ServiceModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.main.IServiceManagementView;

/**
 * Created by TanJiaJun on 2018/1/3.
 */

public class ServiceManagementPresenter extends BasePresenter<IServiceManagementView> {

    private IServiceModel serviceModel;

    public ServiceManagementPresenter() {
        serviceModel = new ServiceModel();
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {

    }

}
