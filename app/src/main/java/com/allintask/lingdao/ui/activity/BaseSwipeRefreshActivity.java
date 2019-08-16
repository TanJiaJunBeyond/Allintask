package com.allintask.lingdao.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.allintask.lingdao.R;
import com.allintask.lingdao.presenter.BasePresenter;
import com.allintask.lingdao.view.IBaseView;
import com.allintask.lingdao.view.ISwipeRefreshView;
import com.allintask.lingdao.widget.CommonRecyclerView;

import butterknife.BindView;

/**
 * Activity基类，支持列表的下拉刷新和上拉加载
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public abstract class BaseSwipeRefreshActivity<V extends IBaseView, T extends BasePresenter<V>> extends BaseActivity<V, T> implements SwipeRefreshLayout.OnRefreshListener, ISwipeRefreshView {

    @BindView(R.id.swipe_refresh_layout)
    protected SwipeRefreshLayout swipe_refresh_layout;
    @BindView(R.id.recycler_view)
    protected CommonRecyclerView recycler_view;

    /**
     * 是否正在刷新
     */
    private boolean isRefreshing;

    /**
     * 是否正常加载
     */
    private boolean isLoadingMore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initSwipeRefreshLayout();
        initRecyclerView();
    }

    private void initSwipeRefreshLayout() {
        if (null != swipe_refresh_layout) {
            swipe_refresh_layout.setColorSchemeResources(R.color.refresh_orange);
            swipe_refresh_layout.setOnRefreshListener(this);
        }
    }

    private void initRecyclerView() {
        if (null != recycler_view) {
            recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    int totalItemCount;
                    int visibleItemCount;
                    int lastVisibleItemPosition;
                    RecyclerView.LayoutManager mLayoutManager = recyclerView.getLayoutManager();

                    if (mLayoutManager instanceof LinearLayoutManager) { // 线性布局
                        LinearLayoutManager mLinearLayoutManager = (LinearLayoutManager) mLayoutManager;
                        lastVisibleItemPosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                        visibleItemCount = mLayoutManager.getChildCount();
                        totalItemCount = mLayoutManager.getItemCount();

                        if (!isLoadingMore && lastVisibleItemPosition == (totalItemCount - 1) && newState == RecyclerView.SCROLL_STATE_IDLE && visibleItemCount > 0) {
                            isLoadingMore = true;
                            onLoadMore();
                        }
                    } else if (mLayoutManager instanceof StaggeredGridLayoutManager) { // 瀑布流布局
                        StaggeredGridLayoutManager mStaggeredGridLayoutManager = (StaggeredGridLayoutManager) mLayoutManager;
                        int spanCount = mStaggeredGridLayoutManager.getSpanCount();
                        int[] lastVisibleItemPositionArray = mStaggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(new int[spanCount]);
                        lastVisibleItemPosition = lastVisibleItemPositionArray[spanCount - 1];
                        totalItemCount = mStaggeredGridLayoutManager.getItemCount();

                        if (!isLoadingMore && lastVisibleItemPosition == (totalItemCount - 1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                            isLoadingMore = true;
                            onLoadMore();
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        }
    }

    protected abstract void onLoadMore();

    protected abstract void onSwipeRefresh();

    @Override
    public void onRefresh() {
        if (!this.isRefreshing) {
            this.isRefreshing = true;
            onSwipeRefresh();
        }
    }

    @Override
    public boolean isRefreshing() {
        return this.isRefreshing;
    }

    @Override
    public void setRefresh(boolean refresh) {
        if (this.isRefreshing != refresh) {
            this.isRefreshing = refresh;
            swipe_refresh_layout.setRefreshing(refresh);
        }
    }

    @Override
    public boolean isLoadingMore() {
        return this.isLoadingMore;
    }

    @Override
    public void setLoadMore(boolean loadMore) {
        this.isLoadingMore = loadMore;
    }

}
