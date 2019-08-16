package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/2/7.
 */

public interface IEducationalExperienceView extends IBaseView {

    void onShowEducationalInstitution(String educationalInstitution);

    void onShowMajor(String major);

    void onShowEducationalBackgroundId(int educationalBackgroundId);

    void onShowEducationalBackground(String educationalBackground);

    void onShowStartTime(String startTime);

    void onShowEndTime(String endTime);

    void onCompileEducationExperienceSuccess();

    void onRemoveEducationExperienceSuccess();

}
