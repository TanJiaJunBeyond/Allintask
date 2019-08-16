package com.allintask.lingdao.bean.demand;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/7.
 */

public class DemandUnderwayListBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public boolean hasNextPage;
    public List<DemandUnderwayBean> list;

    public class DemandUnderwayBean {

        public String avatarUrl;
        public String bidAdvantage;
        public Date bidDate;
        public int buyerUserId;
        public String currentStateTip;
        public Date endWorkAt;
        public boolean isCanShowApplyArbitrationButton;
        public boolean isCanShowApplyRefundButton;
        public boolean isDelayComplete;
        public String loginTimeTip;
        public String name;
        public int orderId;
        public String orderNo;
        public int orderStatus;
        public String refundTip;
        public int sellerUserId;
        public Date startWorkAt;
        public String tip;
        public int totalPrice;
        public int gender;
        public Date birthday;
        public int delayDuration;
        public boolean showEvaluationButton;
        public boolean showEvaluationDetailsButton;
        public int serveId;
        public int demandId;
        public boolean isSelected;

    }

}
