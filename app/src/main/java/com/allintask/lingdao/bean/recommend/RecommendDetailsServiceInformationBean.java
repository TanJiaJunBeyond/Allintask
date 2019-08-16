package com.allintask.lingdao.bean.recommend;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/27.
 */

public class RecommendDetailsServiceInformationBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<PersonalAlbumBean> personalAlbumList;
    public List<ServiceDetailsBean> serveDetailsList;

    public class PersonalAlbumBean {

        public int albumId;
        public String imageUrl;

    }

    public class ServiceDetailsBean {

        public String advantage;
        public String categoryIconUrl;
        public int categoryId;
        public String categoryName;
        public List<CategoryPropertyChineseBean> categoryPropertyChineseList;
        public String categorySelectIconUrl;
        public String introduce;
        public List<ServeAlbumBean> serveAlbumList;
        public int serveId;
        public List<ServeWayPriceUnitChineseBean> serveWayPriceUnitChineseList;
        public String voiceUrl;
        public int voiceDuration;
        public String province;
        public String city;

        public class CategoryPropertyChineseBean {

            public String categoryProperty;
            public List<String> values;

        }

        public class ServeAlbumBean {

            public int albumId;
            public String imageUrl;

        }

        public class ServeWayPriceUnitChineseBean {

            public int serveWayId;
            public String serveWay;
            public int price;
            public String unit;

        }

    }

}
