package com.allintask.lingdao.presenter.recommend;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.recommend.RecommendDetailsServiceInformationBean;
import com.allintask.lingdao.bean.recommend.RecommendDetailsUserInformationBean;
import com.allintask.lingdao.bean.recommend.EvaluationListBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.order.IOrderModel;
import com.allintask.lingdao.model.order.OrderModel;
import com.allintask.lingdao.model.service.IServiceModel;
import com.allintask.lingdao.model.service.ServiceModel;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.recommend.IRecommendDetailsView;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/4.
 */

public class RecommendDetailsPresenter extends BasePresenter<IRecommendDetailsView> {

    private IUserModel userModel;
    private IServiceModel serviceModel;
    private IOrderModel orderModel;

    private int userId = -1;
    private int beUserId = -1;

    public RecommendDetailsPresenter() {
        userModel = new UserModel();
        serviceModel = new ServiceModel();
        orderModel = new OrderModel();
    }

    public void fetchRecommendDetailsUserInformationRequest(int userId) {
        this.userId = userId;
        OkHttpRequest fetchRecommendDetailsUserInformationRequest = userModel.fetchRecommendDetailsUserInformationRequest(this.userId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchRecommendDetailsUserInformationRequest);
    }

    public void fetchRecommendServiceInformationRequest(int userId) {
        this.userId = userId;
        OkHttpRequest fetchRecommendServiceInformationRequest = serviceModel.fetchRecommendServiceInformationRequest(this.userId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchRecommendServiceInformationRequest);
    }

    public void fetchRecommendEvaluateRequest(int userId) {
        this.userId = userId;
        OkHttpRequest fetchRecommendEvaluateRequest = orderModel.fetchRecommendEvaluateRequest(this.userId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchRecommendEvaluateRequest);
    }

    public void likeUserRequest(int beUserId) {
        OkHttpRequest likeUserRequest = userModel.likeUserRequest(beUserId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, likeUserRequest);
    }

    public void cancelLikeUserRequest(int beUserId) {
        int userId = UserPreferences.getInstance().getUserId();

        if (userId != -1) {
            this.beUserId = beUserId;
            OkHttpRequest cancelLikeUserRequest = userModel.cancelLikeUserRequest(userId, beUserId);
            addRequestAsyncTaskForJson(false, OkHttpRequestOptions.DELETE, cancelLikeUserRequest);
        }
    }

    public void collectUserRequest(int beUserId) {
        OkHttpRequest collectUserRequest = userModel.collectUserRequest(beUserId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.POST, collectUserRequest);
    }

    public void cancelCollectUserRequest(int beUserId) {
        int userId = UserPreferences.getInstance().getUserId();

        if (userId != -1) {
            this.beUserId = beUserId;
            OkHttpRequest cancelCollectUserRequest = userModel.cancelCollectUserRequest(userId, this.beUserId);
            addRequestAsyncTaskForJson(false, OkHttpRequestOptions.DELETE, cancelCollectUserRequest);
        }
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        int userId = UserPreferences.getInstance().getUserId();

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_INFO_TO_SERVE_DETAILS + "/" + String.valueOf(this.userId))) {
            String loginTimeTip = null;
            String avatarUrl = null;
            int officialRecommendStatus = 0;
            String officialRecommendIconUrl = null;
            String realName = null;
            int gender = -1;
            Date birthday = null;
            Date startWorkAt = null;
            String location = null;
            int distance = 0;
            boolean isLike = false;
            boolean isStoreUp = false;
            Integer likeCount = null;
            RecommendDetailsUserInformationBean.ViewRecordVo viewRecordVo = null;
            List<RecommendDetailsUserInformationBean.EducationalExperienceBean> educationalExperienceList = null;
            List<RecommendDetailsUserInformationBean.WorkExperienceBean> workExperienceList = null;
            List<RecommendDetailsUserInformationBean.HonorAndQualificationBean> honorAndQualificationList = null;

            if (success) {
                RecommendDetailsUserInformationBean recommendDetailsUserInformationBean = JSONObject.parseObject(data, RecommendDetailsUserInformationBean.class);

                if (null != recommendDetailsUserInformationBean) {
                    loginTimeTip = TypeUtils.getString(recommendDetailsUserInformationBean.loginTimeTip, "");
                    avatarUrl = TypeUtils.getString(recommendDetailsUserInformationBean.avatarUrl, "");
                    officialRecommendStatus = TypeUtils.getInteger(recommendDetailsUserInformationBean.officialRecommendStatus, 0);
                    officialRecommendIconUrl = TypeUtils.getString(recommendDetailsUserInformationBean.officialRecommendIconUrl, "");
                    realName = TypeUtils.getString(recommendDetailsUserInformationBean.realName, "");
                    gender = TypeUtils.getInteger(recommendDetailsUserInformationBean.gender, -1);
                    birthday = recommendDetailsUserInformationBean.birthday;
                    startWorkAt = recommendDetailsUserInformationBean.startWorkAt;
                    location = TypeUtils.getString(recommendDetailsUserInformationBean.location, "");
                    distance = TypeUtils.getInteger(recommendDetailsUserInformationBean.distance, 0);
                    isLike = TypeUtils.getBoolean(recommendDetailsUserInformationBean.isLike, false);
                    isStoreUp = TypeUtils.getBoolean(recommendDetailsUserInformationBean.isStoreUp, false);
                    likeCount = recommendDetailsUserInformationBean.likeCount;
                    viewRecordVo = recommendDetailsUserInformationBean.viewRecordVo;
                    educationalExperienceList = recommendDetailsUserInformationBean.userEduInfoList;
                    workExperienceList = recommendDetailsUserInformationBean.userWorkInfoList;
                    honorAndQualificationList = recommendDetailsUserInformationBean.userHonorInfoList;
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }

            if (null != getView()) {
                String userHeadPortraitUrl = null;

                if (!TextUtils.isEmpty(avatarUrl)) {
                    userHeadPortraitUrl = "https:" + avatarUrl;
                }

                if (null == likeCount) {
                    likeCount = 0;
                }

                getView().onShowPersonalInformation(loginTimeTip, userHeadPortraitUrl, officialRecommendStatus, officialRecommendIconUrl, realName, gender, birthday, startWorkAt, location, distance, isLike, isStoreUp, likeCount, viewRecordVo, educationalExperienceList, workExperienceList, honorAndQualificationList);
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_LIST_TO_SERVE_DETAILS + "/" + String.valueOf(this.userId))) {
            List<RecommendDetailsServiceInformationBean.PersonalAlbumBean> personalAlbumList = null;
            List<RecommendDetailsServiceInformationBean.ServiceDetailsBean> serviceDetailsList = null;

            if (success) {
                RecommendDetailsServiceInformationBean recommendDetailsServiceInformationBean = JSONObject.parseObject(data, RecommendDetailsServiceInformationBean.class);

                if (null != recommendDetailsServiceInformationBean) {
                    personalAlbumList = recommendDetailsServiceInformationBean.personalAlbumList;
                    serviceDetailsList = recommendDetailsServiceInformationBean.serveDetailsList;
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }

            if (null != getView()) {
                getView().onShowServiceInformation(personalAlbumList, serviceDetailsList);
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_SELLER_TO_SERVE_DETAILS + "/" + String.valueOf(this.userId))) {
            int total = 0;
            List<EvaluationListBean.EvaluationBean> evaluationList = null;

            if (success) {
                EvaluationListBean evaluationListBean = JSONObject.parseObject(data, EvaluationListBean.class);

                if (null != evaluationListBean) {
                    total = TypeUtils.getInteger(evaluationListBean.total, 0);
                    evaluationList = evaluationListBean.list;
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }

            if (null != getView()) {
                getView().onShowEvaluateList(total, evaluationList);
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_LIKE_SAVE)) {
            if (success) {
                if (null != getView()) {
                    getView().onLikeUserSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().onLikeUserFail();
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_LIKE_CANCEL + "/" + String.valueOf(userId) + "/" + String.valueOf(beUserId))) {
            if (success) {
                if (null != getView()) {
                    getView().onCancelLikeUserSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().onCancelLikeUserFail();
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_STORE_UP_SAVE)) {
            if (success) {
                if (null != getView()) {
                    getView().onCollectUserSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("收藏失败");
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_STORE_UP_CANCEL + "/" + String.valueOf(userId) + "/" + String.valueOf(beUserId))) {
            if (success) {
                if (null != getView()) {
                    getView().onCancelCollectUserSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("取消收藏失败");
                }
            }
        }
    }

}
