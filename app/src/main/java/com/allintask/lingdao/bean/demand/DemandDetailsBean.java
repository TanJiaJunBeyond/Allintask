package com.allintask.lingdao.bean.demand;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/12.
 */

public class DemandDetailsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public Date bookingDate;
    public int budget;
    public String categoryIconUrl;
    public String categoryName;
    public List<CategoryPropertyChineseListBean> categoryPropertyChineseList;
    public Date createAt;
    public String deliverCycleValue;
    public int demandId;
    public int demandStatus;
    public int salaryTrusteeship;
    public int expireDuration;
    public String expiredTip;
    public String introduce;
    public String serveWay;
    public String province;
    public String city;
    public int auditCode;
    public int onOffLine;
    public int isShare;

    public class CategoryPropertyChineseListBean {

        public String categoryProperty;
        public List<String> values;

    }

}
