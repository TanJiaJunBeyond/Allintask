package com.allintask.lingdao.bean.recommend;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/6/19.
 */

public class RecommendDemandBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public String avatarUrl;
    public Date birthday;
    public String bookingDateTip;
    public Integer budget;
    public int categoryId;
    public String categoryName;
    public List<String> categoryPropertyValueChineseList;
    public String city;
    public String deliverCycleName;
    public int demandId;
    public int gender;
    public int isTrusteeship;
    public String loginTimeTip;
    public String province;
    public String realName;
    public String serveWayName;
    public int userId;
    public int viewCount;
    public int demandStatus;
    public Integer bidCount;
    public String introduce;

}
