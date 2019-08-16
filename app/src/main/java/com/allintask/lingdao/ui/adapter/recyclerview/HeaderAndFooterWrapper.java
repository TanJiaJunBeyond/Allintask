package com.allintask.lingdao.ui.adapter.recyclerview;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhy on 16/6/23.
 */

public class HeaderAndFooterWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int BASE_ITEM_TYPE_HEADER = 0;
    private static final int BASE_ITEM_TYPE_FOOTER = 1;

    private SparseArrayCompat<View> headerViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> footViews = new SparseArrayCompat<>();

    private RecyclerView.Adapter mInnerAdapter;

    public HeaderAndFooterWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (headerViews.get(viewType) != null) {
            // 调整布局
            headerViews.get(viewType).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), headerViews.get(viewType));
            return holder;
        } else if (footViews.get(viewType) != null) {
            // 调整布局
            footViews.get(viewType).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), footViews.get(viewType));
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return headerViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return footViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position)) {
            return;
        }

        if (isFooterViewPos(position)) {
            return;
        }

        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }


    public void addHeaderView(View view) {
        headerViews.put(headerViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFootView(View view) {
        footViews.put(footViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public int getHeadersCount() {
        return headerViews.size();
    }

    public int getFootersCount() {
        return footViews.size();
    }

}
