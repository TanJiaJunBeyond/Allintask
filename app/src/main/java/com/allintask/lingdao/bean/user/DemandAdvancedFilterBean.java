package com.allintask.lingdao.bean.user;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/4/3.
 */

public class DemandAdvancedFilterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<ServeWayCodeAndValuesBean> serveWayCodeAndValues;
    public List<TrusteeshipCodeAndValuesBean> trusteeshipCodeAndValues;
    public List<BudgetRangeVosBean> budgetRangeVos;
    public List<DeliverCycleCodeAndValuesBean> deliverCycleCodeAndValues;
    public List<SortCodeAndValuesBean> sortCodeAndValues;

    public class ServeWayCodeAndValuesBean {

        public int code;
        public String value;
        public boolean isSelected;

    }

    public class TrusteeshipCodeAndValuesBean {

        public int code;
        public String value;
        public boolean isSelected;

    }

    public class BudgetRangeVosBean {

        public int maxPrice;
        public int minPrice;
        public String value;
        public boolean isSelected;

    }

    public class DeliverCycleCodeAndValuesBean {

        public int code;
        public String value;
        public boolean isSelected;

    }

    public class SortCodeAndValuesBean {

        public int code;
        public String value;
        public boolean isSelected;

    }

}
