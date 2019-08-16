package com.allintask.lingdao.utils;

import com.allintask.lingdao.bean.user.MyPhotoAlbumListBean;
import com.allintask.lingdao.bean.user.SearchInformationBean;

import java.util.List;
import java.util.Set;

/**
 * 临时数据缓存区
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class TemporaryDataCachePool {

    private static TemporaryDataCachePool instance;

    private List<SearchInformationBean> searchInformationList;
    private List<MyPhotoAlbumListBean.MyPhotoAlbumBean.ImagesBean> imageList;

    private TemporaryDataCachePool() {

    }

    public static TemporaryDataCachePool getInstance() {
        if (null == instance) {
            instance = new TemporaryDataCachePool();
        }
        return instance;
    }

    public List<SearchInformationBean> getSearchInformationList() {
        return searchInformationList;
    }

    public void setSearchInformationList(List<SearchInformationBean> searchInformationList) {
        this.searchInformationList = searchInformationList;
    }

    public List<MyPhotoAlbumListBean.MyPhotoAlbumBean.ImagesBean> getImageList() {
        return imageList;
    }

    public void setImageList(List<MyPhotoAlbumListBean.MyPhotoAlbumBean.ImagesBean> imageList) {
        this.imageList = imageList;
    }

}
