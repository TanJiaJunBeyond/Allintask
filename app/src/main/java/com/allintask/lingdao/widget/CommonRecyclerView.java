package com.allintask.lingdao.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by TanJiaJun on 2017/3/30 0030.
 */

public class CommonRecyclerView extends RecyclerView {

    private Context context;

    private LayoutManager defaultLayoutManager;
    private ItemAnimator defaultItemAnimator;
    private ItemDecoration defaultItemDecoration;

    public CommonRecyclerView(Context context) {
        this(context, null);
    }

    public CommonRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.defaultLayoutManager = linearLayoutManager;
        this.defaultItemAnimator = new DefaultItemAnimator();

        this.setLayoutManager(this.defaultLayoutManager);
        this.setItemAnimator(this.defaultItemAnimator);
        this.setHasFixedSize(true);
    }

    public LayoutManager getLayoutManager() {
        return defaultLayoutManager;
    }

    public void addHeaderView(int resId) {
        addHeaderView(View.inflate(context, resId, null));
    }

    public void addHeaderView(View view) {
        if (null != view) {

        }
    }

    public void addFooterView(int resId) {
        addFooterView(View.inflate(context, resId, null));
    }

    public void addFooterView(View view) {
        if (null != view) {

        }
    }

    public void isLoadindgMore() {

    }

    public void isRefreshing() {

    }

    public void setRefresh(boolean isRefresh) {

    }

    public void setLoadMore(boolean isLoadMore) {

    }

}
