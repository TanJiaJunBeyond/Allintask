package com.allintask.lingdao.bean.user;

import java.io.Serializable;

/**
 * Created by TanJiaJun on 2018/3/23.
 */

public class EvaluateDetailsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public int orderId;
    public String toBuyerContent;
    public float toBuyerOverallMerit;
    public String toSellerContent;
    public float toSellerOverallMerit;
    public float toSellerSincerity;
    public float toSellerTimelyComplete;
    public float toSellerWorkQuality;

}
