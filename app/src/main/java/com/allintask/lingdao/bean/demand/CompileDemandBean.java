package com.allintask.lingdao.bean.demand;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by TanJiaJun on 2018/6/2.
 */

public class CompileDemandBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public Date bookingDate;
    public int budget;
    public int categoryId;
    public String categoryName;
    public String categoryPropertyList;
    public String cityCode;
    public String deadlineTimeTip;
    public Integer employmentCycle;
    public String employmentCycleUnit;
    public int id;
    public String introduce;
    public int isShare;
    public String provinceCode;
    public int serveId;
    public int serveWayId;
    public int userId;
    public boolean isTrusteeship;
    public int showEmploymentTimes;

}
