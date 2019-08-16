package com.allintask.lingdao.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.allintask.lingdao.R;

/**
 * 多状态更新视图（加载中、加载失败）
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class MultiStatusView extends FrameLayout {

    public static final int STATUS_NORMAL = 0;      // 正常状态
    public static final int STATUS_LOADING = 1;     // 加载状态，对应LoadingView
    public static final int STATUS_ERROR = 2;       // 错误状态，对应ErrorView
    public static final int STATUS_EMPTY = 3;       // 空状态, 对应EmptyView
    public static final int STATUS_NO_NETWORK = 4;  // 无网络，对应NoNetworkView

    private LayoutInflater inflater;

    private int status;
    private View mContentView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mNoNetworkView;

    public MultiStatusView(Context context) {
        this(context, null);
    }

    public MultiStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiStatusView, defStyleAttr, 0);
        this.status = typedArray.getInt(R.styleable.MultiStatusView_msv_status, STATUS_NORMAL);

        int mLoadingViewResId = typedArray.getResourceId(R.styleable.MultiStatusView_msv_loading_view, R.layout.status_view_loading);
        this.mLoadingView = inflater.inflate(mLoadingViewResId, this, false);

        int mErrorViewResId = typedArray.getResourceId(R.styleable.MultiStatusView_msv_error_view, R.layout.status_view_error);
        this.mErrorView = inflater.inflate(mErrorViewResId, this, false);

        int mEmptyViewResId = typedArray.getResourceId(R.styleable.MultiStatusView_msv_empty_view, R.layout.status_view_empty);
        this.mEmptyView = inflater.inflate(mEmptyViewResId, this, false);

        int mNoNetworkViewResId = typedArray.getResourceId(R.styleable.MultiStatusView_msv_no_network_view, R.layout.status_view_no_network);
        this.mNoNetworkView = inflater.inflate(mNoNetworkViewResId, this, false);

        typedArray.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() > 0) {
            mContentView = this.getChildAt(0);
        }

        setLoadingView(this.mLoadingView);
        setErrorView(this.mErrorView);
        setEmptyView(this.mEmptyView);
        setNoNetworkView(this.mNoNetworkView);
        switchStatusView(this.status);
    }

    /**
     * 切换状态对应视图
     *
     * @param status 目标状态
     */
    public void switchStatusView(int status) {
        this.status = status;

        switch (status) {
            case STATUS_NORMAL:
                mLoadingView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.GONE);
                mNoNetworkView.setVisibility(GONE);

                if (null != mContentView) {
                    mContentView.setVisibility(View.VISIBLE);
                }
                break;

            case STATUS_LOADING:
                mLoadingView.setVisibility(View.VISIBLE);
                mErrorView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.GONE);
                mNoNetworkView.setVisibility(GONE);

                if (null != mContentView) {
                    mContentView.setVisibility(View.GONE);
                }
                break;

            case STATUS_ERROR:
                mLoadingView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
                mNoNetworkView.setVisibility(GONE);

                if (null != mContentView) {
                    mContentView.setVisibility(View.GONE);
                }
                break;

            case STATUS_EMPTY:
                mLoadingView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
                mNoNetworkView.setVisibility(GONE);

                if (null != mContentView) {
                    mContentView.setVisibility(View.GONE);
                }
                break;

            case STATUS_NO_NETWORK:
                mLoadingView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.GONE);
                mNoNetworkView.setVisibility(VISIBLE);

                if (null != mContentView) {
                    mContentView.setVisibility(View.GONE);
                }
                break;

            default:

                break;
        }
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setLoadingView(int resId) {
        setLoadingView(inflater.inflate(resId, this, false));
    }

    public void setLoadingView(View view) {
        if (null != view) {
            if (null != this.mLoadingView) {
                this.removeView(this.mLoadingView);
            }

            addView(view);
            this.mLoadingView = view;
        }
    }

    public View getLoadingView() {
        return this.mLoadingView;
    }

    public void setErrorView(int resId) {
        setErrorView(inflater.inflate(resId, this, false));
    }

    public void setErrorView(View view) {
        if (null != view) {
            if (null != this.mErrorView) {
                this.removeView(this.mErrorView);
            }

            addView(view);
            this.mErrorView = view;
        }
    }

    public View getErrorView() {
        return this.mErrorView;
    }

    public void setEmptyView(int resId) {
        setEmptyView(inflater.inflate(resId, this, false));
    }

    public void setEmptyView(View view) {
        if (null != view) {
            if (null != this.mEmptyView) {
                this.removeView(this.mEmptyView);
            }

            addView(view);
            this.mEmptyView = view;
        }
    }

    public View getEmptyView() {
        return this.mEmptyView;
    }

    public void setNoNetworkView(View view) {
        if (null != view) {
            if (null != this.mNoNetworkView) {
                this.removeView(this.mNoNetworkView);
            }

            addView(view);
            this.mNoNetworkView = view;
        }
    }

    public View getNoNetworkView() {
        return this.mNoNetworkView;
    }

}
