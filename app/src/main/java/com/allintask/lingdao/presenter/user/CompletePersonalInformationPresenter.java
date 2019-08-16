package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONArray;
import com.allintask.lingdao.bean.user.CheckUploadIsSuccessBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.ICompletePersonalInformationView;

import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/2.
 */

public class CompletePersonalInformationPresenter extends BasePresenter<ICompletePersonalInformationView> {

    private IUserModel userModel;

    public CompletePersonalInformationPresenter() {
        userModel = new UserModel();
    }

    public void checkUploadIsSuccessRequest(String flagId) {
        OkHttpRequest checkUploadIsSuccessRequest = userModel.checkUploadIsSuccessRequest(flagId);
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.POST, checkUploadIsSuccessRequest);
    }

    public void completePersonalInformationRequest(String tmpUrl, int width, int height, String format, int cutStartX, int cutStartY, int cutWidth, int cutHeight, String name, int gender, String birthday) {
        OkHttpRequest completePersonalInformationRequest = userModel.completePersonalInformationRequest(tmpUrl, width, height, format, cutStartX, cutStartY, cutWidth, cutHeight, name, gender, birthday);
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.POST, completePersonalInformationRequest);
    }

    public void updateBasicPersonalInformationRequest(int imageId, String tmpUrl, int width, int height, String format, int cutStartX, int cutStartY, int cutWidth, int cutHeight, String name, int gender, String birthday) {
        OkHttpRequest updateBasicPersonalInformationRequest = userModel.updateBasicPersonalInformationRequest(imageId, tmpUrl, width, height, format, cutStartX, cutStartY, cutWidth, cutHeight, name, gender, birthday);
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.PUT, updateBasicPersonalInformationRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_FILE_OSS_A_LI_YUN_IMAGE_URL_RETURN)) {
            if (success) {
                List<CheckUploadIsSuccessBean> checkUploadIsSuccessList = JSONArray.parseArray(data, CheckUploadIsSuccessBean.class);

                int status = -1;
                String codeId = null;
                String imgUrl = null;

                if (null != checkUploadIsSuccessList && checkUploadIsSuccessList.size() > 0) {
                    CheckUploadIsSuccessBean checkUploadIsSuccessBean = checkUploadIsSuccessList.get(0);

                    if (null != checkUploadIsSuccessBean) {
                        status = TypeUtils.getInteger(checkUploadIsSuccessBean.status, -1);
                        codeId = TypeUtils.getString(checkUploadIsSuccessBean.codeId, "");
                        imgUrl = TypeUtils.getString(checkUploadIsSuccessBean.imgUrl, "");
                    }
                }

                if (null != getView()) {
                    getView().uploadSuccess(status, codeId, imgUrl);
                }
            } else {
                if (null != getView()) {
                    getView().uploadFail();
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_SET_BASIS)) {
            if (success) {
                if (null != getView()) {
                    getView().onCompletePersonalInformationSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }

            if (null != getView()) {
                getView().dismissProgressDialog();
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_USER_UPDATE_BASIS)) {
            if (success) {
                if (null != getView()) {
                    getView().onUpdatePersonalInformationSuccess();
                }
            } else {
                if (null != getView()) {
                    getView().showToast("完善个人资料失败");
                }
            }
        }
    }

}
