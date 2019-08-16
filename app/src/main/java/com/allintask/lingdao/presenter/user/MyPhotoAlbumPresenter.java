package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.user.MyPhotoAlbumListBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IMyPhotoAlbumView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/2/14.
 */

public class MyPhotoAlbumPresenter extends BasePresenter<IMyPhotoAlbumView> {

    private IUserModel userModel;
    private List<MyPhotoAlbumListBean.MyPhotoAlbumBean> myPhotoAlbumList;

    private int pageNumber = CommonConstant.DEFAULT_PAGE_NUMBER;

    public MyPhotoAlbumPresenter() {
        userModel = new UserModel();
        myPhotoAlbumList = new ArrayList<>();
    }

    private void fetchMyPhotoAlbumListRequest(boolean isRefresh, int userId) {
        if (isRefresh) {
            myPhotoAlbumList.clear();
        }

        OkHttpRequest fetchMyPhotoAlbumListRequest = userModel.fetchMyPhotoAlbumListRequest(userId, pageNumber);
        addRequestAsyncTaskForJsonNoProgress(true, OkHttpRequestOptions.GET, fetchMyPhotoAlbumListRequest);
    }

    public void refresh(int userId) {
        if (!getView().isLoadingMore()) {
            pageNumber = CommonConstant.DEFAULT_PAGE_NUMBER;
            getView().setRefresh(true);
            fetchMyPhotoAlbumListRequest(true, userId);
        } else {
            getView().setRefresh(false);
        }
    }

    public void loadMore(int userId) {
        if (!getView().isRefreshing()) {
            getView().setLoadMore(true);
            fetchMyPhotoAlbumListRequest(false, userId);
        } else {
            getView().setLoadMore(false);
        }
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_ALBUM_LIST)) {
            if (success) {
                MyPhotoAlbumListBean myPhotoAlbumListBean = JSONObject.parseObject(data, MyPhotoAlbumListBean.class);

                if (null != myPhotoAlbumListBean) {
                    List<MyPhotoAlbumListBean.MyPhotoAlbumBean> myPhotoAlbumList = myPhotoAlbumListBean.list;

                    if (null != myPhotoAlbumList && myPhotoAlbumList.size() > 0) {
                        this.myPhotoAlbumList.addAll(myPhotoAlbumList);
                        pageNumber++;
                        String tempDate = null;

                        for (int i = 0; i < this.myPhotoAlbumList.size(); i++) {
                            MyPhotoAlbumListBean.MyPhotoAlbumBean myPhotoAlbumBean = this.myPhotoAlbumList.get(i);

                            if (null != myPhotoAlbumBean) {
                                Date date = myPhotoAlbumBean.createAt;
                                String dateStr = CommonConstant.commonDateFormat.format(date);

                                if (i == 0 && null != data) {
                                    tempDate = dateStr;
                                    myPhotoAlbumBean.isShowDate = true;
                                }

                                if (i > 0) {
                                    if (dateStr.equals(tempDate)) {
                                        myPhotoAlbumBean.isShowDate = false;
                                    } else {
                                        tempDate = dateStr;
                                        myPhotoAlbumBean.isShowDate = true;
                                    }
                                }
                            }
                        }
                    }

                    if (null != getView()) {
                        getView().onShowMyPhotoAlbumList(this.myPhotoAlbumList);
                    }
                } else if (null == this.myPhotoAlbumList || this.myPhotoAlbumList.size() <= 0) {
                    if (null != getView()) {
                        getView().showEmptyView();
                    }
                }
            } else {
                if (null != getView()) {
                    getView().showEmptyView();
                    getView().showToast(errorMessage);
                }
            }

            if (null != getView()) {
                getView().setRefresh(false);
                getView().setLoadMore(false);
            }
        }
    }

}
