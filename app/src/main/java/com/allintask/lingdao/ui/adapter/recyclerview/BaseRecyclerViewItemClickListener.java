package com.allintask.lingdao.ui.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;

/**
 * Created by TanJiaJun on 2018/6/28.
 */

public interface BaseRecyclerViewItemClickListener {

    void setOnItemClick(RecyclerView.ViewHolder holder, int position);

}