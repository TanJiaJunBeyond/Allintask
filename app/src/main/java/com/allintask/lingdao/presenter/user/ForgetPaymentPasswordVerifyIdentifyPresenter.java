package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.GetHiddenUserDataBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IForgetPaymentPasswordVerifyIdentifyView;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/7/5.
 */

public class ForgetPaymentPasswordVerifyIdentifyPresenter extends BasePresenter<IForgetPaymentPasswordVerifyIdentifyView> {

    private IUserModel userModel;

    public ForgetPaymentPasswordVerifyIdentifyPresenter() {
        userModel = new UserModel();
    }

    public void getHiddenUserDataRequest() {
        OkHttpRequest getHiddenUserDataRequest = userModel.getHiddenUserDataRequest();
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, getHiddenUserDataRequest);
    }

    public void checkIdentifyCardRequest(String idCardNo) {
        OkHttpRequest checkIdentifyCardRequest = userModel.checkIdentifyCardRequest(idCardNo);
        addJsonStringRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, checkIdentifyCardRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SECURITY_MANAGE_GET_HIDDEN_USER_DATA)) {
            if (success) {
                GetHiddenUserDataBean getHiddenUserDataBean = JSONObject.parseObject(data, GetHiddenUserDataBean.class);

                if (null != getHiddenUserDataBean) {
                    String hiddenName = TypeUtils.getString(getHiddenUserDataBean.hiddenName, "");

                    if (null != getView()) {
                        getView().onShowHiddenName(hiddenName);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_ZMRZ_CHECK_ID_CARD_NO)) {
            if (success) {
                if (null != getView()) {
                    getView().onShowVerifyIdentifyCardSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().onShowVerifyIdentifyCardFail(errorMessage);
                }
            }
        }
    }

}
