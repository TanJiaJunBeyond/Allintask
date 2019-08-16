package com.allintask.lingdao.bean.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/2/5.
 */

public class MyServiceListBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public boolean hasNextPage;
    public List<MyServiceBean> list;

    public class MyServiceBean implements Serializable {

        private static final long serialVersionUID = 1L;

        public int id;
        public String advantage;
        public int categoryId;
        public String categoryPropertyList;
        public String format;
        public String introduce;
        public String serveWayPriceUnitList;
        public int userId;
        public String voiceSrcUrl;
        public String voiceTmpUrl;
        public int onOffLine;
        public int auditCode;
        public Date changeAt;
        public int consummate;
        public Integer serveAlbumAmount;

    }

}
