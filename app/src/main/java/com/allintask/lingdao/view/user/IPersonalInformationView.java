package com.allintask.lingdao.view.user;

import com.allintask.lingdao.bean.user.PersonalInformationBean;
import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/1/18.
 */

public interface IPersonalInformationView extends IBaseView {

    void onShowPersonalInformationBean(PersonalInformationBean personalInformationBean);

    void uploadSuccess(int status, String codeId, String imageUrl);

    void uploadFail();

    void onSetUserHeadPortraitSuccess();

    void onSetNameSuccess(String name);

    void onSetBirthdaySuccess(int age);

    void onSetGenderSuccess(int gender);

    void onSetStartWorkAtSuccess(String startWorkTime);

    void onRemoveEducationExperienceSuccess();

    void onRemoveWorkExperienceSuccess();

    void onRemoveHonorAndQualificationSuccess();

}
