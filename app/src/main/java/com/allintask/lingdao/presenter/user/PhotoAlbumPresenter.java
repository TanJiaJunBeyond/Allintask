package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.PhotoAlbumBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IPhotoAlbumView;

import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/2/24.
 */

public class PhotoAlbumPresenter extends BasePresenter<IPhotoAlbumView> {

    private IUserModel userModel;

    private int albumId;
    private int imageId;

    public PhotoAlbumPresenter() {
        userModel = new UserModel();
    }

    public void fetchAlbumDetailsRequest(int albumId) {
        this.albumId = albumId;
        OkHttpRequest fetchAlbumDetailsRequest = userModel.fetchAlbumDetailsRequest(this.albumId);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchAlbumDetailsRequest);
    }

    public void deletePhotoAlbumRequest(int albumId, int imageId, String qualityUrl) {
        this.albumId = albumId;
        this.imageId = imageId;

        OkHttpRequest deletePhotoAlbumRequest = userModel.deletePhotoAlbumRequest(this.albumId, this.imageId, qualityUrl);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.PUT, deletePhotoAlbumRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_ALBUM_GET + "/" + albumId)) {
            if (success) {
                PhotoAlbumBean photoAlbumBean = JSONObject.parseObject(data, PhotoAlbumBean.class);

                if (null != photoAlbumBean) {
                    int imageCount = TypeUtils.getInteger(photoAlbumBean.imageCount, 0);
                    List<PhotoAlbumBean.ImageBean> imageList = photoAlbumBean.images;
                    String describe = TypeUtils.getString(photoAlbumBean.describe, "");

                    if (null != getView()) {
                        getView().onShowImageCountAndImageListAndPhotoDescription(imageCount, imageList, describe);
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showToast(errorMessage);
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_ALBUM_DELETE + "/" + albumId + "/" + imageId)) {
            if (success) {
                if (null != getView()) {
                    getView().onDeleteImageSuccess(albumId, imageId);
                }
            } else {
                if (null != getView()) {
                    getView().showToast("删除图片失败");
                }
            }
        }
    }

}
