package com.allintask.lingdao.bean.user;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/4/8.
 */

public class IsAllBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public String code;
    public double latitude;
    public double longitude;
    public String name;
    public String parentCode;
    public List<AddressSubBean> sub;
    public boolean isSelected;

}
