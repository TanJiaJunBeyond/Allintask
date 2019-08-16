package com.allintask.lingdao.bean.user;

import com.allintask.lingdao.bean.demand.ImageRequestsBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TanJiaJun on 2018/3/16.
 */

public class UploadAlbumRequestBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public String describe;
    public List<ImageRequestsBean> imageRequests;

}
