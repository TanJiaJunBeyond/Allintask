package com.allintask.lingdao.bean.user;

import java.io.Serializable;

/**
 * Created by TanJiaJun on 2018/4/9.
 */

public class AddressSubBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public String code;
    public double latitude;
    public double longitude;
    public String name;
    public String parentCode;
    public boolean isSelected;

}