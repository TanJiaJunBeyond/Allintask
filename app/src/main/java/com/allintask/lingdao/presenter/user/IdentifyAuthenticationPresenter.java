package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.ZhiMaCreditAuthenticationDataBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IIdentifyAuthenticationView;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/6/15.
 */

public class IdentifyAuthenticationPresenter extends BasePresenter<IIdentifyAuthenticationView> {

    private IUserModel userModel;

    public IdentifyAuthenticationPresenter() {
        userModel = new UserModel();
    }

    public void getZhiMaCreditUrlRequest(String certName, String certNo) {
        OkHttpRequest getZhiMaCreditUrlRequest = userModel.getZhiMaCreditAuthenticationUrlRequest(certName, certNo);
        addJsonStringRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, getZhiMaCreditUrlRequest);
    }

    public void fetchZhiMaCreditResultRequest(String sign, String params) {
        OkHttpRequest fetchZhiMaCreditResultRequest = userModel.fetchZhiMaCreditAuthenticationResultRequest(sign, params);
        addJsonStringRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, fetchZhiMaCreditResultRequest);
    }

    public void fetchZhiMaCreditAuthenticationDataRequest() {
        OkHttpRequest fetchZhiMaCreditAuthenticationDataRequest = userModel.fetchZhiMaCreditAuthenticationDataRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchZhiMaCreditAuthenticationDataRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_ZMRZ_GENERATE_ZMRZ_URL)) {
            if (success) {
                if (null != getView()) {
                    getView().onGetZhiMaCreditAuthenticationUrlSuccess(data);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_ZMRZ_ANALYSIS_RESULTS)) {
            if (success) {
                Boolean tempPassed = Boolean.valueOf(data);
                boolean passed;

                if (null == tempPassed) {
                    passed = false;
                } else {
                    passed = tempPassed;
                }

                if (null != getView()) {
                    getView().onShowIdentifyAuthenticationData(passed);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_ZMRZ_GET_LAST_DATA)) {
            if (success) {
                ZhiMaCreditAuthenticationDataBean zhiMaCreditAuthenticationDataBean = JSONObject.parseObject(data, ZhiMaCreditAuthenticationDataBean.class);

                if (null != zhiMaCreditAuthenticationDataBean) {
                    String realName = TypeUtils.getString(zhiMaCreditAuthenticationDataBean.realName, "");
                    String idCardNo = TypeUtils.getString(zhiMaCreditAuthenticationDataBean.idCardNo, "");

                    if (null != getView()) {
                        getView().onShowZhiMaCreditAuthenticationData(realName, idCardNo);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
