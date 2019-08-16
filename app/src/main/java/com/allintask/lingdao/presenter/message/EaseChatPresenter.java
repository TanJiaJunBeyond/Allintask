package com.allintask.lingdao.presenter.message;

import com.alibaba.fastjson.JSONArray;
import com.allintask.lingdao.bean.message.UserInfoListRequestBean;
import com.allintask.lingdao.bean.message.UserInfoResponseBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.message.IEaseChatView;

import java.util.List;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/3/24.
 */

public class EaseChatPresenter extends BasePresenter<IEaseChatView> {

    private IUserModel userModel;

    public EaseChatPresenter() {
        userModel = new UserModel();
    }

    public void fetchUserInfoListRequest(UserInfoListRequestBean userInfoListRequestBean) {
        OkHttpRequest fetchUserInfoListRequest = userModel.fetchUserInfoListRequest(userInfoListRequestBean);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, fetchUserInfoListRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_INFO_GET_USER_IM_MSG)) {
            if (success) {
                List<UserInfoResponseBean> userInfoResponseList = JSONArray.parseArray(data, UserInfoResponseBean.class);

                if (null != userInfoResponseList && userInfoResponseList.size() > 0) {
                    UserInfoResponseBean userInfoResponseBean = userInfoResponseList.get(0);

                    if (null != getView() && null != userInfoResponseBean) {
                        getView().onShowUserInfoBean(userInfoResponseBean);
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
