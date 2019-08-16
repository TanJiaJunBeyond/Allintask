package com.allintask.lingdao.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.allintask.lingdao.R;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.IBaseView;
import com.allintask.lingdao.widget.MultiStatusView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by TanJiaJun on 2017/3/30 0030.
 */

public abstract class BaseFragment<V extends IBaseView, T extends BasePresenter<V>> extends Fragment implements IBaseView {

    @Nullable
    @BindView(R.id.swipe_refresh_status_layout)
    protected MultiStatusView mSwipeRefreshStatusLayout;

    protected Context mContext;
    protected View mContentView;
    private ProgressDialog baseProgressDialog;
    protected T mPresenter;

    private Unbinder unbinder;
    private Toast toast;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (null == getActivity()) {
            this.mContext = context;
        } else {
            this.mContext = getActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(getLayoutResId(), container, false);
        unbinder = ButterKnife.bind(this, mContentView);
        mPresenter = CreatePresenter();

        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }

        this.init(savedInstanceState);

        return mContentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (null != unbinder) {
            unbinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != mPresenter) {
            mPresenter.detachView();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    /**
     * 获得布局文件的Resource_ID
     *
     * @return
     */
    protected abstract int getLayoutResId();

    /**
     * 创建presenter
     */
    protected abstract T CreatePresenter();

    /**
     * 初始化数据、布局等
     */
    protected abstract void init(Bundle savedInstanceState);

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showContentView() {
        showStatusView(MultiStatusView.STATUS_NORMAL);
    }

    @Override
    public void showLoadingView() {
        showStatusView(MultiStatusView.STATUS_LOADING);
    }

    @Override
    public void showErrorView() {
        showStatusView(MultiStatusView.STATUS_ERROR);
    }

    @Override
    public void showEmptyView() {
        showStatusView(MultiStatusView.STATUS_EMPTY);
    }

    @Override
    public void showNoNetworkView() {
        showStatusView(MultiStatusView.STATUS_NO_NETWORK);
    }

    private void showStatusView(int status) {
        if (null != mSwipeRefreshStatusLayout) {
            mSwipeRefreshStatusLayout.switchStatusView(status);
        }
    }

    @Override
    public void showProgressDialog(String msg) {
        if (null == baseProgressDialog) {
            baseProgressDialog = new ProgressDialog(mContext);
        }

        if (null != baseProgressDialog && !baseProgressDialog.isShowing()) {
            if (!TextUtils.isEmpty(msg)) {
                baseProgressDialog.setMessage(msg);
            }

            baseProgressDialog.show();
        }
    }

    @Override
    public void showProgressDialog() {
        if (null == baseProgressDialog) {
            baseProgressDialog = new ProgressDialog(mContext);
        }

        if (null != baseProgressDialog && !baseProgressDialog.isShowing()) {
            baseProgressDialog.setMessage("加载中");
            baseProgressDialog.show();
        }
    }

    @Override
    public void dismissProgressDialog() {
        if (null != baseProgressDialog && baseProgressDialog.isShowing()) {
            baseProgressDialog.dismiss();
        }
    }

    public ProgressDialog getProgressDialog() {
        return baseProgressDialog;
    }

    /*****
     * showToast
     *****/

    protected void showToast(int resId) {
        showToast(getString(resId));
    }

    @Override
    public void showToast(CharSequence msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    protected void showLongToast(int resId) {
        showLongToast(getString(resId));
    }

    @Override
    public void showLongToast(CharSequence msg) {
        showToast(msg, Toast.LENGTH_LONG);
    }

    private void showToast(CharSequence msg, int duration) {
        if (!TextUtils.isEmpty(msg)) {
            if (toast != null) {
                toast.cancel();
            }

            toast = Toast.makeText(mContext, msg, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    @Override
    public Context getParentContext() {
        return mContext;
    }

    public View getContentView() {
        return mContentView;
    }

}
