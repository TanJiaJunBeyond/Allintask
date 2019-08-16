package com.allintask.lingdao.view.user;

import com.allintask.lingdao.bean.user.MyPhotoAlbumListBean;
import com.allintask.lingdao.view.ISwipeRefreshView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/2/14.
 */

public interface IMyPhotoAlbumView extends ISwipeRefreshView {

    void onShowMyPhotoAlbumList(List<MyPhotoAlbumListBean.MyPhotoAlbumBean> myPhotoAlbumList);

}
