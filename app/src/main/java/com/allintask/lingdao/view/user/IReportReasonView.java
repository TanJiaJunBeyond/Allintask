package com.allintask.lingdao.view.user;

import com.allintask.lingdao.bean.user.ReportReasonBean;
import com.allintask.lingdao.view.IBaseView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/4/9.
 */

public interface IReportReasonView extends IBaseView {

    void onShowReportReasonList(List<ReportReasonBean> reportReasonList);

}
