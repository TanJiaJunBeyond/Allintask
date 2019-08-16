package com.allintask.lingdao.bean.user;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by TanJiaJun on 2018/3/19.
 */

public class EducationalExperienceBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public String school;
    public String major;
    public int educationLevelId;
    public Date startAt;
    public Date endAt;

}
