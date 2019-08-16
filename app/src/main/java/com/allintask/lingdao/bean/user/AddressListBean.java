package com.allintask.lingdao.bean.user;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/29.
 */

public class AddressListBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<IsAllBean> isAll;
    public List<IsHotBean> isHot;

}
