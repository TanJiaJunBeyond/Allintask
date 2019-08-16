package com.allintask.lingdao.view.user;

import com.allintask.lingdao.bean.user.PhotoAlbumBean;
import com.allintask.lingdao.view.IBaseView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/2/24.
 */

public interface IPhotoAlbumView extends IBaseView {

    void onShowImageCountAndImageListAndPhotoDescription(int imageCount, List<PhotoAlbumBean.ImageBean> imageList, String description);

    void onDeleteImageSuccess(int albumId, int imageId);

}
