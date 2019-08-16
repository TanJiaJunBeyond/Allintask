package com.allintask.lingdao.presenter;

import com.allintask.lingdao.view.IBaseView;

/**
 * Created by TanJiaJun on 2017/3/30 0030.
 */
public interface IBasePresenter<V extends IBaseView> {

    void attachView(V view);

    void detachView();

    boolean isViewAttached();

    V getView();

}
