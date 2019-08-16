package com.allintask.lingdao.bean.service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/6/8.
 */

public class ServiceModeAndPriceModeListBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public Integer demandMaxBudget;
    public Integer maxEmploymentTimes;
    public List<ServiceModeAndPriceModeBean> serveWayAndPriceUnitVos;

}
