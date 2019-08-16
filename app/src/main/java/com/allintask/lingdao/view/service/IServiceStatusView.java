package com.allintask.lingdao.view.service;

import com.allintask.lingdao.bean.service.GetIdToChineseListBean;
import com.allintask.lingdao.bean.service.ServiceCompletedListBean;
import com.allintask.lingdao.bean.service.ServiceExpiredListBean;
import com.allintask.lingdao.bean.service.ServiceHasBidListBean;
import com.allintask.lingdao.bean.service.ServiceUnderwayListBean;
import com.allintask.lingdao.bean.service.ServiceWaitBidListBean;
import com.allintask.lingdao.bean.user.CheckBasicPersonalInformationBean;
import com.allintask.lingdao.view.ISwipeRefreshView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/1/15.
 */

public interface IServiceStatusView extends ISwipeRefreshView {

    void onShowGetIdToChineseListBean(GetIdToChineseListBean getIdToChineseListBean);

    void onShowServiceWaitBidList(List<ServiceWaitBidListBean.ServiceWaitBidBean> serviceWaitBidList);

    void onShowServiceHasBidList(List<ServiceHasBidListBean.ServiceHasBidBean> serviceHasBidList);

    void onShowServiceUnderwayList(List<ServiceUnderwayListBean.ServiceUnderwayBean> serviceUnderwayList);

    void onShowServiceCompletedList(List<ServiceCompletedListBean.ServiceCompletedBean> serviceCompletedList);

    void onShowServiceExpiredList(List<ServiceExpiredListBean.ServiceExpiredBean> serviceExpiredList);

    void onShowAdvantage(int position, String advantage);

    void onBidSuccess(int userId, int demandId);

    void onChangePriceSuccess(int userId, int demandId, String price);

    void onShowCompletedBasicPersonalInformation();

    void onShowBasicPersonalInformationBean(CheckBasicPersonalInformationBean checkBasicPersonalInformationBean);

    void showIsZmrzAuthSuccess(boolean isZmrzAuthSuccess);

}
