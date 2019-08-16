package com.allintask.lingdao.view.main;

import com.allintask.lingdao.bean.recommend.RecommendDemandBean;
import com.allintask.lingdao.bean.recommend.RecommendGridBean;
import com.allintask.lingdao.bean.recommend.RecommendListBean;
import com.allintask.lingdao.bean.user.AdvancedFilterBean;
import com.allintask.lingdao.bean.user.BannerBean;
import com.allintask.lingdao.bean.user.CategoryBean;
import com.allintask.lingdao.bean.user.CheckBasicPersonalInformationBean;
import com.allintask.lingdao.bean.user.DemandAdvancedFilterBean;
import com.allintask.lingdao.bean.user.IsAllBean;
import com.allintask.lingdao.view.IBaseView;
import com.allintask.lingdao.view.ISwipeRefreshView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/1/3.
 */

public interface IRecommendView extends ISwipeRefreshView {

    void onShowCompletedBasicPersonalInformation();

    void onShowBasicPersonalInformationBean(CheckBasicPersonalInformationBean checkBasicPersonalInformationBean);

    void onShowBannerList(List<BannerBean> bannerList);

    void onShowAddressList(List<IsAllBean> addressList);

    void onShowCategoryList(List<CategoryBean> categoryList);

    void onShowRecommendGridList(List<RecommendGridBean> serveHomeIconsList, List<RecommendGridBean> demandHomeIconsList);

    void onShowAdvancedFilterBean(AdvancedFilterBean advancedFilterBean);

    void onShowDemandAdvancedFilterBean(DemandAdvancedFilterBean demandAdvancedFilterBean);

    void onShowRecommendList(int recommendFiltrateStatus, List<RecommendListBean.RecommendBean> allRecommendList, List<RecommendListBean.RecommendBean> recommendList);

    void onShowRecommendDemandList(int recommendFiltrateStatus, List<RecommendDemandBean> allRecommendDemandList, List<RecommendDemandBean> recommendDemandList);

}
