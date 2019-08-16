package com.allintask.lingdao.view.main;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/1/3.
 */

public interface IMineView extends IBaseView {

    /**
     * 设置是否正在刷新
     *
     * @param refresh 是否刷新
     */
    void setRefresh(boolean refresh);

    void showUserHeadPortraitUrl(String userHeadPortraitUrl);

    void showName(String name);

    void showGender(int gender);

    void showAge(int age);

    void showWorkExperienceYear(int workExperienceYear);

    void showMyCollectionAmount(int myCollectionAmount);

    void showIsZmrzAuthSuccess(boolean isZmrzAuthSuccess);

    void showIsExistGesturePwd(boolean isExistGesturePwd);

    void showResumeCompleteRate(int resumeCompleteRate);

}
