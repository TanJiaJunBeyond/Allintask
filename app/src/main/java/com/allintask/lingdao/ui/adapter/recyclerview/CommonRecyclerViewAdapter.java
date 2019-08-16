package com.allintask.lingdao.ui.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用RecyclerView.Adapter，可设置子项点击事件
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class CommonRecyclerViewAdapter<T> extends RecyclerView.Adapter<CommonRecyclerViewHolder> {

    private RecyclerItemClickListener itemClickListener;
    private RecyclerItemLongClickListener itemLongClickListener;

    protected List<T> mList;

    public CommonRecyclerViewAdapter() {
        mList = new ArrayList<>();
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(final CommonRecyclerViewHolder holder, final int position) {
        if (null != this.itemClickListener) {
            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(holder.getItemView(), position);
                }
            });
        }

        if (null != this.itemLongClickListener) {
            holder.getItemView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemLongClickListener.onItemLongClick(holder.getItemView(), position);
                    return false;
                }
            });
        }
    }

    public void setDateList(List<T> recyclerList) {
        setDateList(recyclerList, true);
    }

    public void setDateList(List<T> recyclerList, boolean clearable) {
        if (null != this.mList) {
            if (clearable) {
                this.mList.clear();
            }

            if (null != recyclerList) {
                this.mList.addAll(recyclerList);
            }

            this.notifyDataSetChanged();
        }
    }

    public T getItem(int position) {
        return (getItemCount() == 0) ? null : mList.get(position);
    }

    @Override
    public int getItemCount() {
        return (null == mList) ? 0 : mList.size();
    }

    public void setOnItemClickListener(RecyclerItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnItemLongClickListener(RecyclerItemLongClickListener listener) {
        this.itemLongClickListener = listener;
    }

}
