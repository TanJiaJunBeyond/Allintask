package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.HonorAndQualificationBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.utils.UserPreferences;
import com.allintask.lingdao.view.user.IHonorAndQualificationView;

import java.util.Date;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/2/7.
 */

public class HonorAndQualificationPresenter extends BasePresenter<IHonorAndQualificationView> {

    private IUserModel userModel;

    private int honorAndQualificationId;

    public HonorAndQualificationPresenter() {
        userModel = new UserModel();
    }

    public void fetchHonorAndQualificationRequest(int honorAndQualificationId) {
        this.honorAndQualificationId = honorAndQualificationId;
        OkHttpRequest fetchHonorAndQualificationRequest = userModel.fetchHonorAndQualificationRequest(this.honorAndQualificationId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchHonorAndQualificationRequest);
    }

    public void compileHonorAndQualificationRequest(int id, String prize, String issuingAuthority, String gainAt) {
        int userId = UserPreferences.getInstance().getUserId();
        OkHttpRequest compileHonorAndQualificationRequest = userModel.compileHonorAndQualificationRequest(userId, id, prize, issuingAuthority, gainAt);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.PUT, compileHonorAndQualificationRequest);
    }

    public void removeHonorAndQualificationRequest(int honorAndQualificationId) {
        this.honorAndQualificationId = honorAndQualificationId;
        OkHttpRequest removeHonorAndQualificationRequest = userModel.removeHonorAndQualificationRequest(this.honorAndQualificationId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.DELETE, removeHonorAndQualificationRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_HONOR_INFO + "/" + String.valueOf(honorAndQualificationId))) {
            if (success) {
                HonorAndQualificationBean honorAndQualificationBean = JSONObject.parseObject(data, HonorAndQualificationBean.class);

                if (null != honorAndQualificationBean) {
                    String awardsOrCertificate = TypeUtils.getString(honorAndQualificationBean.prize, "");
                    String issuingAuthority = TypeUtils.getString(honorAndQualificationBean.issuingAuthority, "");
                    Date acquisitionTimeDate = honorAndQualificationBean.gainAt;

                    String acquisitionTime = null;

                    if (null != acquisitionTimeDate) {
                        acquisitionTime = CommonConstant.commonDateFormat.format(acquisitionTimeDate);
                    }

                    if (null != getView()) {
                        getView().onShowAwardsOrCertificate(awardsOrCertificate);
                        getView().onShowIssuingAuthority(issuingAuthority);
                        getView().onShowAcquisitionTime(acquisitionTime);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_HONOR_INFO_UPDATE)) {
            if (success) {
                if (null != getView()) {
                    getView().onCompileHonorAndQualificationSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_HONOR_INFO_DELETE + "/" + String.valueOf(honorAndQualificationId))) {
            if (success) {
                if (null != getView()) {
                    getView().onRemoveHonorAndQualificationSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }
    }

}
