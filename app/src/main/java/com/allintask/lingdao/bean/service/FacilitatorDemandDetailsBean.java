package com.allintask.lingdao.bean.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/17.
 */

public class FacilitatorDemandDetailsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public String avatarUrl;
    public Date birthday;
    public Date bookingDate;
    public int budget;
    public String categoryIconUrl;
    public int categoryId;
    public String categoryName;
    public List<CategoryPropertyChineseBean> categoryPropertyChineseList;
    public Date createAt;
    public String deliverCycleValue;
    public int demandId;
    public int demandStatus;
    public int salaryTrusteeship;
    public int expireDuration;
    public String expiredTip;
    public int gender;
    public String introduce;
    public int isBook;
    public String loginTimeTip;
    public String name;
    public String serveWay;
    public String province;
    public String city;

    public class CategoryPropertyChineseBean {

        public String categoryProperty;
        public List<String> values;

    }

}
