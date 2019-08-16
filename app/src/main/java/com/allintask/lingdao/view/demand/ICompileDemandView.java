package com.allintask.lingdao.view.demand;

import com.allintask.lingdao.bean.service.GetIdToChineseListBean;
import com.allintask.lingdao.bean.service.ServiceCategoryListBean;
import com.allintask.lingdao.bean.service.ServiceModeAndPriceModeBean;
import com.allintask.lingdao.bean.user.IsAllBean;
import com.allintask.lingdao.view.IBaseView;

import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/6/1.
 */

public interface ICompileDemandView extends IBaseView {

    void onShowAddressList(List<IsAllBean> addressList);

    void onShowGetIdToChineseListBean(GetIdToChineseListBean getIdToChineseListBean);

    void onShowServiceCategoryList(List<ServiceCategoryListBean> serviceCategoryList);

    void onShowDemandData(int categoryId, String categoryPropertyList, String categoryName, int serveWayId, String provinceCode, String cityCode, Date bookingDate, int employmentCycle, String employmentCycleUnit, boolean isTrusteeship, int budget, String introduce, int showEmploymentTimes);

    void onShowServiceModeAndPriceModeList(int demandMaxBudget, int maxEmploymentTimes, List<ServiceModeAndPriceModeBean> serviceModeAndPriceModeList);

    void onCompileDemandSuccess();

}
