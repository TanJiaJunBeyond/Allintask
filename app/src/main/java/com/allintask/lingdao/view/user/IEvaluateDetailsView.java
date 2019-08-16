package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/3/23.
 */

public interface IEvaluateDetailsView extends IBaseView {

    void onShowEvaluateDetails(float buyerOverallMerit, String buyerEvaluateContent, float sellerOverallMerit, float sellerCompleteOnTime, float sellerWorkQuality, float sellerServiceIntegrity, String sellerEvaluateContent);

}
