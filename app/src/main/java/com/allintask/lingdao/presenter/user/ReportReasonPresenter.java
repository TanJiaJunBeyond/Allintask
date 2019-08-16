package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONArray;
import com.allintask.lingdao.bean.user.ReportReasonBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IReportReasonView;

import java.util.List;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/4/9.
 */

public class ReportReasonPresenter extends BasePresenter<IReportReasonView> {

    private IUserModel userModel;

    public ReportReasonPresenter() {
        userModel = new UserModel();
    }

    public void fetchReportReasonListRequest() {
        OkHttpRequest fetchReportReasonListRequest = userModel.fetchReportReasonListRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchReportReasonListRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_REPORT_USER_RECORD_LIST_REASON)) {
            if (success) {
                List<ReportReasonBean> reportReasonList = JSONArray.parseArray(data, ReportReasonBean.class);

                if (null != getView()) {
                    getView().onShowReportReasonList(reportReasonList);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
