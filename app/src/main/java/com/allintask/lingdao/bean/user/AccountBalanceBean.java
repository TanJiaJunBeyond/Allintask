package com.allintask.lingdao.bean.user;

import java.io.Serializable;

/**
 * Created by TanJiaJun on 2018/3/31.
 */

public class AccountBalanceBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public double advanceRecharge;
    public double canWithdraw;
    public boolean isShowRechargeButton;
    public boolean isShowWithdrawButton;
    public double totalAmount;
    public double withdrawLowPrice;
    public double withdrawRate;

}
