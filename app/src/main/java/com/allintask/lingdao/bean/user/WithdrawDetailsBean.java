package com.allintask.lingdao.bean.user;

import java.io.Serializable;

/**
 * Created by TanJiaJun on 2018/5/8.
 */

public class WithdrawDetailsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public String bankCardDetails;
    public int bankCardId;
    public double canWithdraw;
    public boolean isCollectWithdrawPoundage;
    public String payPwdInputErrorTip;
    public String realName;
    public Integer surplusPayPwdInputCount;
    public int withdrawLowPrice;
    public double withdrawRate;
    public String withdrawRateTip;

}
