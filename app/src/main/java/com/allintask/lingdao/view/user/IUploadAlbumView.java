package com.allintask.lingdao.view.user;

import com.allintask.lingdao.bean.service.PublishedBean;
import com.allintask.lingdao.bean.user.CheckUploadIsSuccessBean;
import com.allintask.lingdao.view.IBaseView;

import java.util.List;

/**
 * Created by TanJiaJun on 2018/2/5.
 */

public interface IUploadAlbumView extends IBaseView {

    void onShowPublishedList(List<PublishedBean> publishedList);

    void onShowCheckUploadIsSuccessList(List<CheckUploadIsSuccessBean> checkUploadIsSuccessList);

    void onUploading();

    void onUploadFail();

    void onUploadAlbumSuccess();

}
