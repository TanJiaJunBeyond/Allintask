package com.allintask.lingdao.presenter.main;

import com.alibaba.fastjson.JSONObject;
import com.allintask.lingdao.bean.recommend.RecommendDataBean;
import com.allintask.lingdao.constant.ServiceAPIConstant;
import com.allintask.lingdao.model.user.IUserModel;
import com.allintask.lingdao.model.user.UserModel;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.main.ISearchHistoryView;

import java.util.List;

import cn.tanjiajun.sdk.conn.OkHttpRequest;
import cn.tanjiajun.sdk.conn.OkHttpRequestOptions;

/**
 * Created by TanJiaJun on 2018/1/10.
 */

public class SearchHistoryPresenter extends BasePresenter<ISearchHistoryView> {

    private IUserModel userModel;

    public SearchHistoryPresenter() {
        userModel = new UserModel();
    }

    public void fetchRecommendDataRequest() {
        OkHttpRequest fetchRecommendDataRequest = userModel.fetchRecommendDataRequest();
        addRequestAsyncTaskForJson(true, OkHttpRequestOptions.GET, fetchRecommendDataRequest);
    }

    public void saveKeyWordsSearchLogRequest(int type, String keywords) {
        OkHttpRequest saveKeyWordsSearchLogRequest = userModel.saveKeyWordsSearchLogRequest(type, keywords);
        addJsonStringRequestAsyncTaskForJsonNoProgress(false, OkHttpRequestOptions.POST, saveKeyWordsSearchLogRequest);
    }

    @Override
    protected void onResponseAsyncTaskRender(String requestId, boolean success, int errorCode, String errorMessage, String data) {
        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_GET_NAVIGATION_DATA)) {
            if (success) {
                RecommendDataBean recommendDataBean = JSONObject.parseObject(data, RecommendDataBean.class);

                if (null != recommendDataBean) {
                    RecommendDataBean.SearchRecommendVo searchRecommendVo = recommendDataBean.searchRecommendVo;

                    if (null != searchRecommendVo) {
                        List<String> searchServeRecommendVos = searchRecommendVo.searchServeRecommendVos;
                        List<String> searchDemandRecommendVos = searchRecommendVo.searchDemandRecommendVos;

                        if (null != getView()) {
                            getView().onShowEverybodySearchData(searchServeRecommendVos, searchDemandRecommendVos);
                        }
                    }
                }
            }
        }

        if (requestId.equals(ServiceAPIConstant.REQUEST_API_NAME_GOODS_SOLR_SAVE_KEYWORDS_SEARCH_LOG)) {
            if (null != getView()) {
                getView().onSaveKeywordsSearchLog();
            }
        }
    }

}
