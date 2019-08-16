package com.allintask.lingdao.bean.demand;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/8.
 */

public class DemandExpiredListBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public boolean hasNextPage;
    public List<DemandExpiredBean> list;

    public class DemandExpiredBean {

        public String advantage;
        public String avatarUrl;
        public Date bidDate;
        public int bidId;
        public int bidStatus;
        public Date birthday;
        public int buyerUserId;
        public String currentStateTip;
        public int demandId;
        public int gender;
        public int isBook;
        public String loginTimeTip;
        public String name;
        public int price;
        public int sellerUserId;
        public int serveId;
        public String startWorkAt;
        public boolean isSelected;

    }

}
