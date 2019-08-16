package com.allintask.lingdao.view.demand;

import com.allintask.lingdao.bean.demand.DemandStatusListBean;
import com.allintask.lingdao.bean.user.CheckBasicPersonalInformationBean;
import com.allintask.lingdao.view.ISwipeRefreshView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/1/15.
 */

public interface IDemandStatusView extends ISwipeRefreshView {

    void onShowDemandStatusList(List<DemandStatusListBean.DemandStatusBean> demandStatusList);

    void onShowCompletedBasicPersonalInformation();

    void onShowBasicPersonalInformationBean(CheckBasicPersonalInformationBean checkBasicPersonalInformationBean);

}
