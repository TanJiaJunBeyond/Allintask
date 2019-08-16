package com.allintask.lingdao.presenter.main;

import com.alibaba.fastjson.JSONArray;
import com.allintask.lingdao.bean.message.UserInfoListRequestBean;
import com.allintask.lingdao.bean.message.UserInfoResponseBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.main.IMessageView;

import java.util.List;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/3.
 */

public class MessagePresenter extends BasePresenter<IMessageView> {

    private IUserModel userModel;

    public MessagePresenter() {
        userModel = new UserModel();
    }

    public void fetchUserInfoListRequest(UserInfoListRequestBean userInfoListRequestBean) {
        OkHttpRequest fetchUserInfoListRequest = userModel.fetchUserInfoListRequest(userInfoListRequestBean);
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.POST, fetchUserInfoListRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_INFO_GET_USER_IM_MSG)) {
            if (success) {
                List<UserInfoResponseBean> userInfoResponseList = JSONArray.parseArray(data, UserInfoResponseBean.class);

                if (null != getView() && null != userInfoResponseList && userInfoResponseList.size() > 0) {
                    getView().onShowUserInfoList(userInfoResponseList);
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
