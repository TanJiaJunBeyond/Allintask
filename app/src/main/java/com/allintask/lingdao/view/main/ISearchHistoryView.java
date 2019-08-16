package com.allintask.lingdao.view.main;

import com.allintask.lingdao.view.IBaseView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/1/10.
 */

public interface ISearchHistoryView extends IBaseView {

    void onShowEverybodySearchData(List<String> searchServeRecommendVos, List<String> searchDemandRecommendVos);

    void onSaveKeywordsSearchLog();

}
