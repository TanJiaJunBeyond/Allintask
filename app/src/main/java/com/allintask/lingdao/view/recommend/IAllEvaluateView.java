package com.allintask.lingdao.view.recommend;

import com.allintask.lingdao.bean.recommend.EvaluationListBean;
import com.allintask.lingdao.view.ISwipeRefreshView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/3.
 */

public interface IAllEvaluateView extends ISwipeRefreshView {

    void onShowEvaluateList(List<EvaluationListBean.EvaluationBean> evaluationList);

}
