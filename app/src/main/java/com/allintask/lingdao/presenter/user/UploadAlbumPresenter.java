package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONArray;
import com.allintask.lingdao.bean.service.PublishedBean;
import com.allintask.lingdao.bean.user.CheckUploadIsSuccessBean;
import com.allintask.lingdao.bean.user.UploadAlbumRequestBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.service.IServiceModel;
import com.allintask.lingdao.model.service.ServiceModel;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IUploadAlbumView;

import java.util.ArrayList;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/2/5.
 */

public class UploadAlbumPresenter extends BasePresenter<IUploadAlbumView> {

    private IServiceModel serviceModel;
    private IUserModel userModel;

    private int uploadAlbumListSize;

    public UploadAlbumPresenter() {
        serviceModel = new ServiceModel();
        userModel = new UserModel();
    }

    public void fetchPublishedListRequest() {
        OkHttpRequest fetchPublishedListRequest = serviceModel.fetchPublishedListRequest();
        addRequestAsyncTaskForJson(true, OkHttpRequestOptions.GET, fetchPublishedListRequest);
    }

    public void checkUploadIsSuccessRequest(String flagId, int uploadAlbumListSize) {
        this.uploadAlbumListSize = uploadAlbumListSize;
        OkHttpRequest checkUploadIsSuccessRequest = userModel.checkUploadIsSuccessRequest(flagId);
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.POST, checkUploadIsSuccessRequest);
    }

    public void savePhotoAlbumListRequest(String albumRequestJson) {
        OkHttpRequest savePhotoAlbumListRequest = userModel.savePhotoAlbumListRequest(albumRequestJson);
        addRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.POST, savePhotoAlbumListRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SERVE_LIST_PUBLISH)) {
            if (success) {
                List<PublishedBean> publishedList = JSONArray.parseArray(data, PublishedBean.class);

                if (null != getView()) {
                    getView().onShowPublishedList(publishedList);
                }
            }
        } else {
            if (null != getView()) {
                getView().showToast(errorMessage);
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_FILE_OSS_A_LI_YUN_IMAGE_URL_RETURN)) {
            if (success) {
                List<CheckUploadIsSuccessBean> checkUploadIsSuccessList = JSONArray.parseArray(data, CheckUploadIsSuccessBean.class);

                if (null != checkUploadIsSuccessList && checkUploadIsSuccessList.size() > 0) {
                    int status = -1;
                    List<Integer> statusList = new ArrayList<>();

                    for (int i = 0; i < checkUploadIsSuccessList.size(); i++) {
                        CheckUploadIsSuccessBean checkUploadIsSuccessBean = checkUploadIsSuccessList.get(i);

                        if (null != checkUploadIsSuccessBean) {
                            int tempStatus = TypeUtils.getInteger(checkUploadIsSuccessBean.status, -1);
                            statusList.add(tempStatus);
                        }
                    }

                    for (int i = 0; i < statusList.size(); i++) {
                        int tempStatus = statusList.get(i);

                        if (tempStatus == -1) {
                            status = -1;
                            break;
                        } else if (tempStatus == 0) {
                            status = 0;
                            break;
                        } else {
                            status = 1;
                        }
                    }

                    if (null != getView()) {
                        if (checkUploadIsSuccessList.size() == uploadAlbumListSize) {
                            if (status == -1) {
                                getView().onUploadFail();
                            } else if (status == 0) {
                                getView().onUploading();
                            } else {
                                getView().onShowCheckUploadIsSuccessList(checkUploadIsSuccessList);
                            }
                        } else {
                            getView().onUploading();
                        }
                    }
                }
            } else {
                if (null != getView()) {
                    getView().onUploadFail();
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_ALBUM_SAVE_LIST)) {
            if (success) {
                if (null != getView()) {
                    getView().onUploadAlbumSuccess();
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
    }

}
