package com.allintask.lingdao.bean.pay;

import java.io.Serializable;

/**
 * Created by TanJiaJun on 2018/3/9.
 */

public class PaymentMethodBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public String code;
    public String value;
    public boolean isSelected;

}
