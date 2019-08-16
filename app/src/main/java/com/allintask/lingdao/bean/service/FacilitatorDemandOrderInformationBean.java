package com.allintask.lingdao.bean.service;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by TanJiaJun on 2018/3/18.
 */

public class FacilitatorDemandOrderInformationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public String arbitrationTip;
    public int orderId;
    public int orderPrice;
    public String priceTip;
    public long refundAutoAcceptSurplusDuration;
    public Date refundCreateAt;
    public String refundReason;
    public Integer refundStatus;
    public String refundTip;

}
