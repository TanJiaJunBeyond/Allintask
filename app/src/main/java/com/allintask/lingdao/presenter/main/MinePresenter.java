package com.allintask.lingdao.presenter.main;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.MyDataBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.main.IMineView;

import java.util.Date;

import cn.tanjiajun.sdk.common.utils.AgeUtils;
import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/3.
 */

public class MinePresenter extends BasePresenter<IMineView> {

    private IUserModel userModel;

    public MinePresenter() {
        userModel = new UserModel();
    }

    public void fetchMyDataRequest() {
        OkHttpRequest fetchMyDataRequest = userModel.fetchMyDataRequest();
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.GET, fetchMyDataRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_MINE)) {
            if (success) {
                MyDataBean myDataBean = JSONObject.parseObject(data, MyDataBean.class);

                if (null != myDataBean) {
                    String avatarUrl = TypeUtils.getString(myDataBean.avatarUrl, "");
                    String name = TypeUtils.getString(myDataBean.name, "");
                    int gender = TypeUtils.getInteger(myDataBean.gender, -1);
                    Date birthday = myDataBean.birthday;
                    Date startWorkAt = myDataBean.startWorkAt;
                    int storeUpCount = TypeUtils.getInteger(myDataBean.storeUpCount, 0);
                    boolean zmrzAuthSuccess = TypeUtils.getBoolean(myDataBean.zmrzAuthSuccess, false);
                    boolean isExistGesturePwd = TypeUtils.getBoolean(myDataBean.isExistGesturePwd, false);
                    int resumeCompleteRate = TypeUtils.getInteger(myDataBean.resumeCompleteRate, 0);

                    if (null != getView()) {
                        String userHeadPortraitUrl = null;

                        if (!TextUtils.isEmpty(avatarUrl)) {
                            userHeadPortraitUrl = "https:" + avatarUrl;
                        }

                        getView().showUserHeadPortraitUrl(userHeadPortraitUrl);
                        getView().showName(name);
                        getView().showGender(gender);

                        if (null != birthday) {
                            int age = AgeUtils.getAge(birthday);
                            getView().showAge(age);
                        } else {
                            getView().showAge(-1);
                        }

                        if (null != startWorkAt) {
                            int workExperienceYear = AgeUtils.getAge(startWorkAt);
                            getView().showWorkExperienceYear(workExperienceYear);
                        }

//                        getView().showMyCollectionAmount(storeUpCount);
                        getView().showIsZmrzAuthSuccess(zmrzAuthSuccess);
                        getView().showIsExistGesturePwd(isExistGesturePwd);
                        getView().showResumeCompleteRate(resumeCompleteRate);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }

            if (null != getView()) {
                getView().setRefresh(false);
            }
        }
    }

}
