package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.PhoneNumberHomeLocationBean;
import com.allintask.lingdao.bean.user.PhoneNumberHomeLocationListBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IPhoneNumberHomeLocationView;

import java.util.List;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/22.
 */

public class PhoneNumberHomeLocationPresenter extends BasePresenter<IPhoneNumberHomeLocationView> {

    private IUserModel userModel;

    public PhoneNumberHomeLocationPresenter() {
        userModel = new UserModel();
    }

    public void fetchPhoneNumberLocationListRequest() {
        OkHttpRequest fetchPhoneNumberLocationListRequest = userModel.fetchPhoneNumberHomeLocationListRequest();
        addRequestAsyncTaskForJsonNoProgress(true, OkHttpRequestOptions.GET, fetchPhoneNumberLocationListRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_MOBILE_COUNTRY_CODE_LIST)) {
            if (success) {
                PhoneNumberHomeLocationListBean phoneNumberHomeLocationListBean = JSONObject.parseObject(data, PhoneNumberHomeLocationListBean.class);

                if (null != phoneNumberHomeLocationListBean) {
                    List<PhoneNumberHomeLocationBean> isHotList = phoneNumberHomeLocationListBean.isHot;
                    List<PhoneNumberHomeLocationBean> isAllList = phoneNumberHomeLocationListBean.isAll;

                    if (null != getView()) {
                        getView().onShowPhoneNumberHomeLocationList(isHotList, isAllList);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showEmptyView();
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
