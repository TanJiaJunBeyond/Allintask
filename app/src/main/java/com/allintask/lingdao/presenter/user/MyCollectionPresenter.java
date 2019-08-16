package com.allintask.lingdao.presenter.user;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.AllintaskApplication;
import com.allintask.lingdao.bean.recommend.RecommendDemandBean;
import com.allintask.lingdao.bean.user.MyCollectListBean;
import com.allintask.lingdao.bean.user.MyCollectionDemandBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.user.IMyCollectionView;

import java.util.ArrayList;
import java.util.List;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/2/26.
 */

public class MyCollectionPresenter extends BasePresenter<IMyCollectionView> {

    private int mMyCollectStatus;
    private IUserModel userModel;
    private List<MyCollectListBean.MyCollectBean> mMyCollectList;
    private List<RecommendDemandBean> mRecommendDemandList;

    private int pageNumber = CommonConstant.DEFAULT_PAGE_NUMBER;

    public MyCollectionPresenter(int myCollectStatus) {
        mMyCollectStatus = myCollectStatus;
        userModel = new UserModel();

        if (myCollectStatus == CommonConstant.MY_COLLECTION_SERVICE) {
            mMyCollectList = new ArrayList<>();
        } else if (myCollectStatus == CommonConstant.MY_COLLECTION_DEMAND) {
            mRecommendDemandList = new ArrayList<>();
        }
    }

    private void fetchMyCollectListRequest(boolean isRefresh) {
        if (isRefresh) {
            mMyCollectList.clear();
        }

        OkHttpRequest fetchMyCollectListRequest = userModel.fetchMyCollectListRequest(pageNumber);
        addRequestAsyncTaskForJsonNoProgress(true, OkHttpRequestOptions.GET, fetchMyCollectListRequest);
    }

    private void fetchMyCollectionDemandListRequest(boolean isRefresh) {
        if (isRefresh) {
            mRecommendDemandList.clear();
        }

        OkHttpRequest fetchMyCollectionDemandListRequest = userModel.fetchMyCollectionDemandListRequest(pageNumber);
        addRequestAsyncTaskForJsonNoProgress(true, OkHttpRequestOptions.GET, fetchMyCollectionDemandListRequest);
    }

    public void refresh() {
        if (!getView().isLoadingMore()) {
            pageNumber = CommonConstant.DEFAULT_PAGE_NUMBER;
            getView().setRefresh(true);

            if (mMyCollectStatus == CommonConstant.MY_COLLECTION_SERVICE) {
                fetchMyCollectListRequest(true);
            } else if (mMyCollectStatus == CommonConstant.MY_COLLECTION_DEMAND) {
                fetchMyCollectionDemandListRequest(true);
            }
        } else {
            getView().setRefresh(false);
        }
    }

    public void loadMore() {
        if (!getView().isRefreshing()) {
            getView().setLoadMore(true);

            if (mMyCollectStatus == CommonConstant.MY_COLLECTION_SERVICE) {
                fetchMyCollectListRequest(false);
            } else if (mMyCollectStatus == CommonConstant.MY_COLLECTION_DEMAND) {
                fetchMyCollectionDemandListRequest(false);
            }
        } else {
            getView().setLoadMore(false);
        }
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_USER_CENTER_STORE_UP_LIST)) {
            if (success) {
                MyCollectListBean myCollectListBean = JSONObject.parseObject(data, MyCollectListBean.class);

                if (null != myCollectListBean) {
                    List<MyCollectListBean.MyCollectBean> myCollectList = myCollectListBean.list;

                    if (null != myCollectList && myCollectList.size() > 0) {
                        mMyCollectList.addAll(myCollectList);
                        pageNumber++;
                    }
                } else if (null == mMyCollectList || mMyCollectList.size() <= 0) {
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
                getView().onShowMyCollectList(mMyCollectList);
            }

            if (null != getView()) {
                getView().setRefresh(false);
                getView().setLoadMore(false);
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_DEMAND_STORE_UP_LIST)) {
            if (success) {
                MyCollectionDemandBean myCollectionDemandBean = JSONObject.parseObject(data, MyCollectionDemandBean.class);

                if (null != myCollectionDemandBean) {
                    List<RecommendDemandBean> recommendDemandList = myCollectionDemandBean.list;

                    if (null != recommendDemandList && recommendDemandList.size() > 0) {
                        mRecommendDemandList.addAll(recommendDemandList);
                        pageNumber++;
                    }
                } else if (null == mRecommendDemandList || mRecommendDemandList.size() <= 0) {
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
                getView().onShowMyCollectionDemandList(mRecommendDemandList);
            }

            if (null != getView()) {
                getView().setRefresh(false);
                getView().setLoadMore(false);
            }
        }
    }

}
