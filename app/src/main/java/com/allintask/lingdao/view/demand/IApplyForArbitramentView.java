package com.allintask.lingdao.view.demand;

import com.allintask.lingdao.bean.demand.ArbitramentReasonBean;
import com.allintask.lingdao.bean.user.CheckUploadIsSuccessBean;
import com.allintask.lingdao.view.IBaseView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/10.
 */

public interface IApplyForArbitramentView extends IBaseView {

    void onShowArbitramentReasonList(List<ArbitramentReasonBean> arbitramentReasonList);

    void onShowCheckUploadIsSuccessList(List<CheckUploadIsSuccessBean> checkUploadIsSuccessList);

    void onUploading();

    void onUploadFail();

    void onApplyForArbitramentSuccess();

}
