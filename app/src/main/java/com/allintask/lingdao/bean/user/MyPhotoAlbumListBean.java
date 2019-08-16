package com.allintask.lingdao.bean.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/2/14.
 */

public class MyPhotoAlbumListBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public boolean hasNextPage;
    public List<MyPhotoAlbumBean> list;

    public class MyPhotoAlbumBean {

        public int albumId;
        public Date createAt;
        public String describe;
        public int imageCount;
        public List<ImagesBean> images;
        public String title;
        public boolean isShowDate;

        public class ImagesBean implements Serializable {

            public int imageId;
            public String qualityUrl;

        }

    }

}
