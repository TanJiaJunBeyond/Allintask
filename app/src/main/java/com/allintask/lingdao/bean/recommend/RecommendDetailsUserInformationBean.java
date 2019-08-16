package com.allintask.lingdao.bean.recommend;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/27.
 */

public class RecommendDetailsUserInformationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public int userId;
    public String avatarUrl;
    public Date birthday;
    public int distance;
    public int gender;
    public boolean isLike;
    public boolean isStoreUp;
    public Integer likeCount;
    public String location;
    public String loginTimeTip;
    public String realName;
    public Date startWorkAt;
    public List<EducationalExperienceBean> userEduInfoList;
    public List<WorkExperienceBean> userWorkInfoList;
    public List<HonorAndQualificationBean> userHonorInfoList;
    public ViewRecordVo viewRecordVo;
    public int officialRecommendStatus;
    public String officialRecommendIconUrl;

    public class EducationalExperienceBean {

        public int educationLevelId;
        public String educationLevelValue;
        public Date endAt;
        public String major;
        public String school;
        public Date startAt;
        public int userId;

    }

    public class WorkExperienceBean {

        public String company;
        public Date endAt;
        public String post;
        public Date startAt;
        public int userId;

    }

    public class HonorAndQualificationBean {

        public Date gainAt;
        public String issuingAuthority;
        public String prize;
        public int userId;

    }

    public class ViewRecordVo {

        public int count;
        public List<String> viewUserAvatarUrls;

    }

}
