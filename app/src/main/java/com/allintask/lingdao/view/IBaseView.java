package com.allintask.lingdao.view;

import android.content.Context;

/**
 * 父类视图接口
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public interface IBaseView {

    /**
     * 显示主视图
     */
    void showContentView();

    /**
     * 显示加载视图
     */
    void showLoadingView();

    /**
     * 显示错误视图
     */
    void showErrorView();

    /**
     * 显示空值视图
     */
    void showEmptyView();

    /**
     * 显示无网络视图
     */
    void showNoNetworkView();

    /**
     * 显示进度对话框
     *
     * @param msg
     */
    void showProgressDialog(String msg);

    /**
     * 显示进度对话框（默认加载中）
     */
    void showProgressDialog();

    /**
     * 隐藏进度对话框
     */
    void dismissProgressDialog();

    /**
     * 显示Toast
     *
     * @param msg
     */
    void showToast(CharSequence msg);

    void showLongToast(CharSequence msg);

    Context getParentContext();

}
