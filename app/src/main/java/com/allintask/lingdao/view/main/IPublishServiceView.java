package com.allintask.lingdao.view.main;

import com.allintask.lingdao.bean.service.GetIdToChineseListBean;
import com.allintask.lingdao.bean.service.MyServiceBean;
import com.allintask.lingdao.bean.service.ServiceCategoryListBean;
import com.allintask.lingdao.bean.service.ServiceModeAndPriceModeBean;
import com.allintask.lingdao.bean.user.IsAllBean;
import com.allintask.lingdao.view.IBaseView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/1/5.
 */

public interface IPublishServiceView extends IBaseView {

    void onShowServiceCategoryList(List<ServiceCategoryListBean> serviceCategoryList);

    void onShowServiceModeAndPriceModeList(int demandMaxBudget, int maxEmploymentTimes, List<ServiceModeAndPriceModeBean> serviceModeAndPriceModeList);

    void onShowAddressList(List<IsAllBean> addressList);

    void onShowIsPublished(boolean isPublished, int serviceId);

    void uploadVoiceSuccess(String voiceUrl);

    void uploadVoiceFail();

    void onPublishServiceSuccess(int serviceId);

    void onShowGetIdToChineseListBean(GetIdToChineseListBean getIdToChineseListBean);

    void onShowMyServiceBean(MyServiceBean myServiceBean);

    void onUpdateServiceSuccess();

    void onCheckPersonalInformationSuccess(boolean isExistEducationalExperience, boolean isExistWorkExperience, boolean isExistHonorAndQualification);

    void onShowServicePhotoAlbumSurplusAmount(int servicePhotoAlbumSurplusAmount);

    void onFetchServicePhotoAlbumFail();

    void onFetchVoiceDemoSuccess(int voiceDemoDuration, String voiceDemoUrl);

    void onFetchVoiceDemoFail();

}
