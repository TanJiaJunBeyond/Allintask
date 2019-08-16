package com.allintask.lingdao.view.demand;

import com.allintask.lingdao.bean.demand.ApplyForRefundReasonBean;
import com.allintask.lingdao.bean.demand.DemandCompletedListBean;
import com.allintask.lingdao.bean.demand.DemandExpiredListBean;
import com.allintask.lingdao.bean.demand.DemandInTheBiddingListBean;
import com.allintask.lingdao.bean.demand.DemandUnderwayListBean;
import com.allintask.lingdao.bean.demand.ShowDemandDetailsBean;
import com.allintask.lingdao.view.ISwipeRefreshView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/1/16.
 */

public interface IEmployerDemandDetailsView extends ISwipeRefreshView {

    void onShowDemandInTheBiddingList(List<DemandInTheBiddingListBean.DemandInTheBiddingBean> demandInTheBiddingList);

    void onShowDemandUnderwayList(List<DemandUnderwayListBean.DemandUnderwayBean> demandUnderwayList);

    void onShowDemandCompletedList(List<DemandCompletedListBean.DemandCompletedBean> demandCompletedList);

    void onShowDemandExpiredList(List<DemandExpiredListBean.DemandExpiredBean> demandExpiredList);

    void onConfirmStartWorkSuccess(int userId, int demandId);

    void onConfirmCompleteWorkSuccess(int userId, int demandId);

    void onDelayCompleteWorkSuccess(int userId, int demandId);

    void onShowDemandStatus(int demandStatus);

    void onShowUserHeadPortraitUrl(String userHeadPortraitUrl);

    void onShowCategoryName(String categoryName);

    void onShowTime(String time);

    void onShowShowDemandDetailsList(List<ShowDemandDetailsBean> showDemandDetailsList);

    void onShowAuditCodeAndOnOffLine(int auditCode, int onOffLine);

    void onShowIsShare(int isShare);

    void onShowNoServiceProvider();

    void onShowApplyForRefundReasonList(int userId, int demandId, List<ApplyForRefundReasonBean> applyForRefundReasonList);

    void onApplyForRefundSuccess(int userId, int demandId);

    void onGoOnlineDemandSuccess();

    void onGoOnlineDemandFail();

    void onGoOfflineDemandSuccess();

    void onGoOfflineDemandFail();

    void onDeleteDemandSuccess();

}
