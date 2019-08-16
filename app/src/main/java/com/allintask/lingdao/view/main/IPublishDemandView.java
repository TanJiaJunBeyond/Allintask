package com.allintask.lingdao.view.main;

import com.allintask.lingdao.bean.demand.DemandPeriodListBean;
import com.allintask.lingdao.bean.message.UserInfoResponseBean;
import com.allintask.lingdao.bean.service.ServiceCategoryListBean;
import com.allintask.lingdao.bean.service.ServiceModeAndPriceModeBean;
import com.allintask.lingdao.bean.user.IsAllBean;
import com.allintask.lingdao.view.IBaseView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/1/8.
 */

public interface IPublishDemandView extends IBaseView {

    void onShowServiceCategoryList(List<ServiceCategoryListBean> serviceCategoryList);

    void onShowServiceModeAndPriceModeList(int demandMaxBudget, int maxEmploymentTimes, List<ServiceModeAndPriceModeBean> serviceModeAndPriceModeList);

    void onShowAddressList(List<IsAllBean> addressList);

    void onPublishDemandSuccess(int demandId, String IMMessage);

    void onPublishPaymentDemandSuccess(String serveBidNo, double totalAmount);

    void onShowDemandPeriodList(List<DemandPeriodListBean> demandPeriodList);

    void onShowUserInfoBean(UserInfoResponseBean userInfoResponseBean);

}
