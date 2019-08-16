package com.allintask.lingdao.bean.recommend;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/27.
 */

public class EvaluationListBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public boolean hasNextPage;
    public int total;
    public List<EvaluationBean> list;

    public class EvaluationBean {

        public String avatarUrl;
        public String buyerName;
        public String content;
        public Date createAt;
        public long overallMerit;

    }

}
