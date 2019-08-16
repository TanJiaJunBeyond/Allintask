package com.allintask.lingdao.view.main;

import com.allintask.lingdao.bean.user.CheckBasicPersonalInformationBean;
import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/4/2.
 */

public interface IDemandManagementView extends IBaseView {

    void onShowCompletedBasicPersonalInformation();

    void onShowBasicPersonalInformationBean(CheckBasicPersonalInformationBean checkBasicPersonalInformationBean);

}
