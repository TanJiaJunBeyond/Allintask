package com.allintask.lingdao.bean.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/3.
 */

public class ServiceWaitBidListBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public boolean hasNextPage;
    public List<ServiceWaitBidBean> list;

    public class ServiceWaitBidBean implements Serializable {

        private static final long serialVersionUID = 1L;

        public String buyerAvatarUrl;
        public String buyerName;
        public int buyerUserId;
        public int categoryId;
        public String deadlineTimeTip;
        public int demandId;
        public String demandPublishTimeTip;
        public int salaryTrusteeship;
        public int isBook;
        public String loginTimeTip;
        public int sellerUserId;
        public int serveId;
        public int serveWayId;
        public String tip;
        public int demandPrice;
        public int gender;
        public Date birthday;

    }

}
