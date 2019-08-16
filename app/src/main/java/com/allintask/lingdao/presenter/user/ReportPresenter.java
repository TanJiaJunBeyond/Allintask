package com.allintask.lingdao.presenter.user;

import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IReportView;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/4/9.
 */

public class ReportPresenter extends BasePresenter<IReportView> {

    private IUserModel userModel;

    public ReportPresenter() {
        userModel = new UserModel();
    }

    public void saveReportRequest(int beUserId, String reportReasonIdList, String content) {
        OkHttpRequest saveReportRequest = userModel.saveReportRequest(beUserId, reportReasonIdList, content);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, saveReportRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_REPORT_USER_RECORD_SAVE)) {
            if (success) {
                if (null != getView()) {
                    getView().onReportSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("举报失败");
                }
            }
        }
    }

}
