package com.allintask.lingdao.view.service;

import com.allintask.lingdao.bean.service.MyServiceListBean;
import com.allintask.lingdao.bean.service.GetIdToChineseListBean;
import com.allintask.lingdao.view.ISwipeRefreshView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/2/5.
 */

public interface IMyServiceView extends ISwipeRefreshView {

    void onShowResumeCompleteRate(int resumeCompleteRate);

    void onShowGetIdToChineseListBean(GetIdToChineseListBean getIdToChineseListBean);

    void onShowMyServiceList(List<MyServiceListBean.MyServiceBean> myServiceList);

    void onDeleteServiceSuccess(int position);

    void onGoOnlineSuccess(int position);

    void onGoOfflineSuccess(int position);

}
