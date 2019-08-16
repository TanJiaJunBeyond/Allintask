package com.allintask.lingdao.bean.user;

import java.io.Serializable;

/**
 * Created by TanJiaJun on 2018/4/1.
 */

public class ModifyBankCardRequestBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public int bankId;
    public String bankCardNo;
    public String ghtBankCode;
    public ImageRequestBean imageRequest;

}
