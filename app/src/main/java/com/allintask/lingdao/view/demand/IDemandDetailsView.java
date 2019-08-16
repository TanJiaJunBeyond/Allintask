package com.allintask.lingdao.view.demand;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/5/3.
 */

public interface IDemandDetailsView extends IBaseView {

    /**
     * 设置是否正在刷新
     *
     * @param refresh 是否刷新
     */
    void setRefresh(boolean refresh);

    void onShowUserId(int userId);

    void onShowCategoryId(int categoryId);

    void onShowIsCollected(boolean isCollected);

    void onShowCategoryName(String categoryName);

    void onShowDemandStatus(int demandStatus);

    void onShowHasBidAmount(int hasBidAmount);

    void onShowHasPayAmount(int hasPayAmount);

    void onShowHasBrowseAmount(int hasBrowseAmount);

    void onShowPublishTip(String publishTip);

    void onShowExpireTip(String expireTip);

    void onShowServiceWay(String serviceWay);

    void onShowBudget(int budget);

    void onShowIsTrusteeship(int isTrusteeship);

    void onShowCategoryPropertyChineseString(String categoryPropertyChineseString);

    void onShowAddress(String address);

    void onShowSubscribeStartTime(String subscribeStartTime);

    void onShowDeliveryCycle(String deliveryCycle);

    void onShowDemandIntroduction(String demandIntroduction);

    void onShowIsHasBid(boolean isHasBid);

    void onShowIsBidServe(boolean isBidServe);

    void onShowUserHeadPortraitUrl(String userHeadPortraitUrl);

    void onShowName(String name);

    void onShowTime(String time);

    void onShowAdvantageAndIsBook(String advantage, int isBook);

    void onBidSuccess(int userId, int demandId);

    void onCollectDemandSuccess();

    void onCancelCollectDemandSuccess();

}
