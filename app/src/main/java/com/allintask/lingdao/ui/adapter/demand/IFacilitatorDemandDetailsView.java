package com.allintask.lingdao.ui.adapter.demand;

import com.allintask.lingdao.bean.service.ShowFacilitatorDemandDetailsBean;
import com.allintask.lingdao.view.IBaseView;

import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/1/17.
 */

public interface IFacilitatorDemandDetailsView extends IBaseView {

    void onShowFacilitatorDemandDetailsOrderInformation(int orderId, int orderPrice, String priceTip, String refundTip, String refundReason, Date refundCreateAt, int refundStatus, long refundAutoAcceptDuration, String arbitrationTip);

    void onShowCategoryId(int categoryId);

    void onShowShowFacilitatorDemandDetailsList(List<ShowFacilitatorDemandDetailsBean> showFacilitatorDemandDetailsList);

    void onShowAdvantage(String advantage);

    void onBidSuccess();

    void onChangePriceSuccess(String price);

    void onAgreeRefundSuccess();

    void onRejectRefundSuccess();

}
