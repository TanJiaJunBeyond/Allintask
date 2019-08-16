package com.allintask.lingdao.bean.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/2/7.
 */

public class PersonalInformationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public Integer avatarImageId;
    public String avatarImageUrl;
    public Date birthday;
    public int gender;
    public String realName;
    public boolean realNameLock;
    public Date startWorkAt;
    public List<EducationalExperienceBean> userEduInfos;
    public List<WorkExperienceBean> userWorkInfos;
    public List<HonorAndQualificationBean> userHonorInfos;

    public class EducationalExperienceBean {

        public int id;
        public int educationLevelId;
        public Date endAt;
        public String major;
        public String school;
        public Date startAt;
        public int userId;

    }

    public class WorkExperienceBean {

        public int id;
        public String company;
        public Date endAt;
        public String post;
        public Date startAt;
        public int userId;

    }

    public class HonorAndQualificationBean {

        public int id;
        public Date gainAt;
        public String issuingAuthority;
        public String prize;
        public int userId;

    }

}
