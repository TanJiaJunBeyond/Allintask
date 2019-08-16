package com.allintask.lingdao.view.user;

import com.allintask.lingdao.bean.recommend.RecommendDemandBean;
import com.allintask.lingdao.bean.recommend.RecommendListBean;
import com.allintask.lingdao.bean.user.AdvancedFilterBean;
import com.allintask.lingdao.bean.user.CategoryBean;
import com.allintask.lingdao.bean.user.DemandAdvancedFilterBean;
import com.allintask.lingdao.bean.user.IsAllBean;
import com.allintask.lingdao.view.ISwipeRefreshView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/1.
 */

public interface ISearchView extends ISwipeRefreshView {

    void onShowCategoryList(List<CategoryBean> categoryList);

    void onShowAddressList(List<IsAllBean> addressList);

    void onShowAdvancedFilterBean(AdvancedFilterBean advancedFilterBean);

    void onShowDemandAdvancedFilterBean(DemandAdvancedFilterBean demandAdvancedFilterBean);

    void onShowRecommendList(List<RecommendListBean.RecommendBean> recommendList);

    void onShowRecommendDemandList(List<RecommendDemandBean> recommendDemandList);

    void onSaveKeywordsSearchLog();

}
