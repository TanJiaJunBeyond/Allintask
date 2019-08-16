package com.allintask.lingdao.bean.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/30.
 */

public class MyCollectListBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<MyCollectBean> list;

    public class MyCollectBean {

        public String avatarUrl;
        public int beUserId;
        public Date birthday;
        public int distance;
        public int gender;
        public String name;
        public Date startWorkAt;

    }

}
