package com.allintask.lingdao.bean.user;

import java.io.Serializable;

/**
 * Created by TanJiaJun on 2018/3/31.
 */

public class SaveBankCardRequestBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public String bankCardNo;
    public String ghtBankCode;
    public ImageRequestBean imageRequest;

}
