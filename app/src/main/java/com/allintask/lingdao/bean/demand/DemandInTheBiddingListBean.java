package com.allintask.lingdao.bean.demand;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/7.
 */

public class DemandInTheBiddingListBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public boolean hasNextPage;
    public List<DemandInTheBiddingBean> list;

    public class DemandInTheBiddingBean {

        public String advantage;
        public String avatarUrl;
        public Date bidDate;
        public int bidId;
        public int bidStatus;
        public int buyerUserId;
        public int categoryId;
        public String currentStateTip;
        public int demandId;
        public int demandPrice;
        public String demandPublishTimeTip;
        public int salaryTrusteeship;
        public int isBook;
        public String loginTimeTip;
        public String name;
        public int price;
        public int sellerUserId;
        public int serveId;
        public int serveWayId;
        public String tip;
        public int gender;
        public Date birthday;
        public boolean isSelected;

    }

}
