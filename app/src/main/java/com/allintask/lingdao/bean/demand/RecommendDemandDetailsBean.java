package com.allintask.lingdao.bean.demand;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/5/3.
 */

public class RecommendDemandDetailsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public Integer bidCount;
    public Date bookingDate;
    public Integer budget;
    public Integer buyCount;
    public int categoryId;
    public String categoryName;
    public List<CategoryPropertyChineseBean> categoryPropertyChineseList;
    public String city;
    public Date createAt;
    public String deliverCycleValue;
    public int demandId;
    public int demandStatus;
    public String expireTip;
    public String introduce;
    public boolean isBid;
    public boolean isStoreUp;
    public String province;
    public String publishTip;
    public String serveWay;
    public int userId;
    public Integer viewCount;
    public int isTrusteeship;
    public boolean isBidServe;

    public class CategoryPropertyChineseBean {

        public String categoryProperty;
        public List<String> values;

    }

}
