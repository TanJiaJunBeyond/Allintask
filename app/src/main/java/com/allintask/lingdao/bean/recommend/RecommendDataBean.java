package com.allintask.lingdao.bean.recommend;

import com.allintask.lingdao.bean.user.AdvancedFilterBean;
import com.allintask.lingdao.bean.user.BannerBean;
import com.allintask.lingdao.bean.user.DemandAdvancedFilterBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/6/17.
 */

public class RecommendDataBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<BannerBean> bannerResponseList;
    public RecommendGridListBean homePageIconVo;
    public AdvancedFilterBean advancedFilterServeVo;
    public DemandAdvancedFilterBean advancedFilterDemandVo;
    public SearchRecommendVo searchRecommendVo;

    public class SearchRecommendVo {

        public List<String> searchServeRecommendVos;
        public List<String> searchDemandRecommendVos;

    }

}
