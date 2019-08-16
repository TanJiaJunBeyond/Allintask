package com.allintask.lingdao.view.recommend;

import com.allintask.lingdao.bean.recommend.RecommendDetailsServiceInformationBean;
import com.allintask.lingdao.bean.recommend.RecommendDetailsUserInformationBean;
import com.allintask.lingdao.bean.recommend.EvaluationListBean;
import com.allintask.lingdao.view.IBaseView;

import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/1/4.
 */

public interface IRecommendDetailsView extends IBaseView {

    void onShowPersonalInformation(String loginTimeTip, String avatarUrl, int officialRecommendStatus, String officialRecommendIconUrl, String realName, int gender, Date birthday, Date startWorkAt, String location, int distance, boolean isLike, boolean isStoreUp, int likeCount, RecommendDetailsUserInformationBean.ViewRecordVo viewRecordVo, List<RecommendDetailsUserInformationBean.EducationalExperienceBean> educationalExperienceList, List<RecommendDetailsUserInformationBean.WorkExperienceBean> workExperienceList, List<RecommendDetailsUserInformationBean.HonorAndQualificationBean> honorAndQualificationList);

    void onShowServiceInformation(List<RecommendDetailsServiceInformationBean.PersonalAlbumBean> personalAlbumList, List<RecommendDetailsServiceInformationBean.ServiceDetailsBean> serviceDetailsList);

    void onShowEvaluateList(int total, List<EvaluationListBean.EvaluationBean> evaluationList);

    void onLikeUserSuccess();

    void onLikeUserFail();

    void onCancelLikeUserSuccess();

    void onCancelLikeUserFail();

    void onCollectUserSuccess();

    void onCancelCollectUserSuccess();

}
