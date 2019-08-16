package com.allintask.lingdao.view.user;

import com.allintask.lingdao.bean.recommend.RecommendDemandBean;
import com.allintask.lingdao.bean.user.MyCollectListBean;
import com.allintask.lingdao.view.ISwipeRefreshView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/2/26.
 */

public interface IMyCollectionView extends ISwipeRefreshView {

    void onShowMyCollectList(List<MyCollectListBean.MyCollectBean> myCollectList);

    void onShowMyCollectionDemandList(List<RecommendDemandBean> recommendDemandList);

}
