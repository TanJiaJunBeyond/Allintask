package com.allintask.lingdao.bean.service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/2/5.
 */

public class GetIdToChineseListBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<GetIdToChineseBean> category;
    public List<GetIdToChineseBean> priceUnit;
    public List<GetIdToChineseBean> serveWay;

    public class GetIdToChineseBean {

        public int code;
        public String value;

    }

}
