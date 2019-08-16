package com.allintask.lingdao.bean.service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/2/1.
 */

public class ServiceCategoryListBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public int code;
    public String exampleContext;
    public String name;
    public List<ServiceCategoryFirstBean> sub;

    public class ServiceCategoryFirstBean {

        public int code;
        public int maxSelectLen;
        public int mustSelect;
        public String name;
        public List<ServiceCategorySecondBean> sub;

        public class ServiceCategorySecondBean {

            public int code;
            public String name;

        }

    }

}
