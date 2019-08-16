package com.allintask.lingdao.bean.user;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/4/3.
 */

public class AdvancedFilterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<ServeWayCodeAndValuesBean> serveWayCodeAndValues;
    public List<SortCodeAndValuesBean> sortCodeAndValues;
    public List<UnitCodeAndValuesBean> unitCodeAndValues;

    public class ServeWayCodeAndValuesBean {

        public int code;
        public String value;
        public boolean isSelected;

    }

    public class SortCodeAndValuesBean {

        public int code;
        public String value;
        public boolean isSelected;

    }

    public class UnitCodeAndValuesBean {

        public int code;
        public String value;
        public boolean isSelected;
        public List<PriceRangeVosBean> priceRangeVos;

        public class PriceRangeVosBean {

            public int maxPrice;
            public int minPrice;
            public String value;
            public boolean isSeletecd;

        }

    }

}
