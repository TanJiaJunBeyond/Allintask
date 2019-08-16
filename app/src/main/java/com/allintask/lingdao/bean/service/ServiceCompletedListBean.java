package com.allintask.lingdao.bean.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/13.
 */

public class ServiceCompletedListBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public boolean hasNextPage;
    public List<ServiceCompletedBean> list;

    public class ServiceCompletedBean implements Serializable {

        private static final long serialVersionUID = 1L;

        public String advantage;
        public String avatarUrl;
        public Date bidDate;
        public Date birthday;
        public int buyerUserId;
        public int categoryId;
        public String currentStateTip;
        public int demandId;
        public int demandPrice;
        public String demandPublishTimeTip;
        public int salaryTrusteeship;
        public Date endWorkAt;
        public int gender;
        public boolean isDelayComplete;
        public String loginTimeTip;
        public String name;
        public int orderId;
        public String orderNo;
        public int orderPrice;
        public String priceTip;
        public int sellerUserId;
        public int serveId;
        public int serveWayId;
        public boolean showEvaluationButton;
        public boolean showEvaluationDetailsButton;
        public Date startWorkAt;
        public String tip;
        public String buyerAvatarUrl;
        public String buyerName;
        public int isBook;

    }

}
