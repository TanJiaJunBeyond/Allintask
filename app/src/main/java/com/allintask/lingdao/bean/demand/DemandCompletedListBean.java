package com.allintask.lingdao.bean.demand;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/13.
 */

public class DemandCompletedListBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public boolean hasNextPage;
    public List<DemandCompletedBean> list;

    public class DemandCompletedBean {

        public String avatarUrl;
        public String bidAdvantage;
        public Date bidDate;
        public Date birthday;
        public int buyerUserId;
        public String currentStateTip;
        public String delayCompleteTip;
        public Date endWorkAt;
        public int gender;
        public String loginTimeTip;
        public String name;
        public int orderId;
        public String orderNo;
        public int orderStatus;
        public String priceTip;
        public int sellerUserId;
        public boolean showEvaluationButton;
        public boolean showEvaluationDetailsButton;
        public Date startWorkAt;
        public String tip;
        public int totalPrice;
        public Date userStartWorkAt;
        public int serveId;
        public int demandId;
        public boolean isSelected;

    }

}
