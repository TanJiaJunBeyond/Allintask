package com.allintask.lingdao.view.main;

import com.allintask.lingdao.bean.user.AppUpdateBean;
import com.allintask.lingdao.bean.user.PersonalInformationBean;
import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/3/24.
 */

public interface IMainView extends IBaseView {

    void onShowPersonalInformationBean(PersonalInformationBean personalInformationBean);

    void onShowAppUpdateBean(AppUpdateBean appUpdateBean);

}
