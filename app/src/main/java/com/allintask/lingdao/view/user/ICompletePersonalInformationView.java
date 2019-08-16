package com.allintask.lingdao.view.user;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2018/1/2.
 */

public interface ICompletePersonalInformationView extends IBaseView {

    void uploadSuccess(int status, String codeId, String imageUrl);

    void uploadFail();

    void onCompletePersonalInformationSuccess();

    void onUpdatePersonalInformationSuccess();

}
