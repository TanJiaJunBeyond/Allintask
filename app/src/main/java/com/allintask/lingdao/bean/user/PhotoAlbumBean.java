package com.allintask.lingdao.bean.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/30.
 */

public class PhotoAlbumBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public int albumId;
    public Date createAt;
    public String describe;
    public int imageCount;
    public List<ImageBean> images;
    public String title;

    public class ImageBean {

        public int imageId;
        public String qualityUrl;

    }

}
