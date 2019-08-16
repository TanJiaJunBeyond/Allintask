package com.allintask.lingdao.bean.user;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by TanJiaJun on 2018/3/30.
 */

public class CategoryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public int code;
    public String name;
    public List<FirstSubBean> sub;
    public boolean isSelected;

    public class FirstSubBean {

        public int code;
        public String name;
        public List<SecondSubBean> sub;
        public Set<Integer> isSelectedSet;
        public List<Integer> isSelectedCategoryIdList;

        public class SecondSubBean {

            public int code;
            public String name;

        }

    }

}
