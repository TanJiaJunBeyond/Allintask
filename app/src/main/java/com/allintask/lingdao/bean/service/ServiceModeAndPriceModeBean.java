package com.allintask.lingdao.bean.service;

import java.io.Serializable;

/**
 * Created by TanJiaJun on 2018/2/1.
 */

public class ServiceModeAndPriceModeBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public String priceUnit;
    public int priceUnitId;
    public String priceWayName;
    public int serveWayId;
    public String serveWayName;
    public String price;
    public Integer isNeedAddress;
    public Integer minValue;
    public Integer maxValue;
    public int employmentCycleUnitId;
    public String employmentCycleUnit;
    public Integer employmentCycleMinValue;
    public Integer employmentCycleMaxValue;
    public Integer priceMinValue;
    public Integer priceMaxValue;
    public int showEmploymentTimes;

}
