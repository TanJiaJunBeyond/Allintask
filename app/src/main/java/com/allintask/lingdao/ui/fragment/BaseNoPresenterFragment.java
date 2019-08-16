package com.allintask.lingdao.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by TanJiaJun on 2017/3/30 0030.
 */

public abstract class BaseNoPresenterFragment extends Fragment {

    protected Context mContext;
    protected View mContentView;
    private ProgressDialog baseProgressDialog;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(getLayoutResId(), container, false);
        unbinder = ButterKnife.bind(this, mContentView);

        this.init(savedInstanceState);

        return mContentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (null == getActivity()) {
            this.mContext = context;
        } else {
            this.mContext = getActivity();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (null != unbinder) {
            unbinder.unbind();
        }
    }

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

    public void showProgressDialog() {
        if (null == baseProgressDialog) {
            baseProgressDialog = new ProgressDialog(mContext);
        }

        if (null != baseProgressDialog && !baseProgressDialog.isShowing()) {
            baseProgressDialog.setMessage("加载中");
            baseProgressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (null != baseProgressDialog && baseProgressDialog.isShowing()) {
            baseProgressDialog.dismiss();
        }
    }

    /*****
     * showToast
     *****/

    protected void showToast(int resId) {
        showToast(getString(resId));
    }

    public void showToast(CharSequence msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    protected void showLongToast(int resId) {
        showLongToast(getString(resId));
    }

    public void showLongToast(CharSequence msg) {
        showToast(msg, Toast.LENGTH_LONG);
    }

    private void showToast(CharSequence msg, int duration) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(mContext, msg, duration).show();
        }
    }

    /**
     * 获得布局文件的Resource_ID
     *
     * @return
     */
    protected abstract int getLayoutResId();

    /**
     * 初始化数据、布局等
     */
    protected abstract void init(Bundle savedInstanceState);

}
