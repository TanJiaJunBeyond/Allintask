package com.allintask.lingdao.bean.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/31.
 */

public class TransactionRecordListBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<TransactionRecordBean> list;

    public class TransactionRecordBean {

        public double totalAmount;
        public String tradeRemark;
        public int tradeType;
        public Date createAt;

    }

}
