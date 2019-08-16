package com.allintask.lingdao.presenter.recommend;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.recommend.EvaluationListBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.order.IOrderModel;
import com.allintask.lingdao.model.order.OrderModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.recommend.IAllEvaluateView;

import java.util.ArrayList;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/3/3.
 */

public class AllEvaluatePresenter extends BasePresenter<IAllEvaluateView> {

    private IOrderModel orderModel;
    private List<EvaluationListBean.EvaluationBean> evaluationList;

    private int userId;
    private boolean hasNextPage;

    public AllEvaluatePresenter() {
        orderModel = new OrderModel();
        evaluationList = new ArrayList<>();
    }

    private void fetchRecommendEvaluateListRequest(boolean isRefresh, int userId, int pageNumber) {
        this.userId = userId;

        if (isRefresh) {
            evaluationList.clear();
        }

        OkHttpRequest fetchRecommendEvaluateListRequest = orderModel.fetchRecommendEvaluateListRequest(this.userId, pageNumber);
        addRequestAsyncTaskForJson(false, OkHttpRequestOptions.GET, fetchRecommendEvaluateListRequest);
    }

    public void refresh(int userId) {
        this.userId = userId;

        if (!getView().isLoadingMore()) {
            getView().setRefresh(true);
            fetchRecommendEvaluateListRequest(true, this.userId, CommonConstant.DEFAULT_PAGE_NUMBER);
        } else {
            getView().setRefresh(false);
        }
    }

    public void loadMore(int userId) {
        this.userId = userId;

        if (!getView().isRefreshing() && hasNextPage) {
            getView().setLoadMore(true);
            fetchRecommendEvaluateListRequest(false, this.userId, evaluationList.size());
        } else {
            getView().setLoadMore(false);
        }
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_ORDER_CENTER_EVALUATION_TO_SELLER_LIST + "/" + String.valueOf(userId))) {
            if (success) {
                EvaluationListBean evaluationListBean = JSONObject.parseObject(data, EvaluationListBean.class);

                if (null != evaluationListBean) {
                    hasNextPage = TypeUtils.getBoolean(evaluationListBean.hasNextPage, false);
                    List<EvaluationListBean.EvaluationBean> evaluationList = evaluationListBean.list;

                    if (null != evaluationList && evaluationList.size() > 0) {
                        this.evaluationList.addAll(evaluationList);
                    }

                    if (null != getView()) {
                        getView().onShowEvaluateList(this.evaluationList);
                    }
                } else if (null == this.evaluationList || this.evaluationList.size() <= 0) {
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
