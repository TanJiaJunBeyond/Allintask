package com.allintask.lingdao.bean.demand;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/10.
 */

public class ApplyForArbitramentRequestBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public int orderId;
    public int operateReasonId;
    public String arbitrationDescribe;
    public List<ImageRequestsBean> imageRequests;

}
