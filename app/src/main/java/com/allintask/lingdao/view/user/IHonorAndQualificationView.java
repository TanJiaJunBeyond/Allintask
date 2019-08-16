package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/2/7.
 */

public interface IHonorAndQualificationView extends IBaseView {

    void onShowAwardsOrCertificate(String awardsOrCertificate);

    void onShowIssuingAuthority(String issuingAuthority);

    void onShowAcquisitionTime(String acquisitionTime);

    void onCompileHonorAndQualificationSuccess();

    void onRemoveHonorAndQualificationSuccess();

}
