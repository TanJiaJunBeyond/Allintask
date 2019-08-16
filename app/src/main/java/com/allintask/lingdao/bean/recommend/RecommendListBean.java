package com.allintask.lingdao.bean.recommend;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/29.
 */

public class RecommendListBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<RecommendBean> solrServes;

    public class RecommendBean {

        public String advantage;
        public String avatarUrl;
        public int categoryId;
        public String categoryName;
        public List<String> categoryPropertyValueChineseList;
        public String id;
        public String loginTimeTip;
        public String realName;
        public List<String> serveAlbumList;
        public int serveId;
        public List<String> serveWayPriceUnitChineseList;
        public int status;
        public int userId;
        public int viewCount;
        public int officialRecommendStatus;
        public String officialRecommendCopyWrite;
        public String officialRecommendIconUrl;

    }

}
