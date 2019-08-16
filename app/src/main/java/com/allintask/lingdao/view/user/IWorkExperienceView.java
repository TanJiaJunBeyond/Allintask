package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/2/7.
 */

public interface IWorkExperienceView extends IBaseView {

    void onShowWorkUnit(String workUnit);

    void onShowPosition(String position);

    void onShowStartTime(String startTime);

    void onShowEndTime(String endTime);

    void onCompileWorkExperienceSuccess();

    void onRemoveWorkExperienceSuccess();

}
