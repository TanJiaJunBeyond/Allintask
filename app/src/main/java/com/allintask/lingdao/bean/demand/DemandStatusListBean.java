package com.allintask.lingdao.bean.demand;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/7.
 */

public class DemandStatusListBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public boolean hasNextPage;
    public List<DemandStatusBean> list;

    public class DemandStatusBean {

        public int bidBuyCount;
        public int budget;
        public String categoryName;
        public int demandId;
        public String tip;
        public List<UserMsgVosBean> userMsgVos;

        public class UserMsgVosBean {

            public String avatarUrl;
            public int userId;

        }

    }

}
