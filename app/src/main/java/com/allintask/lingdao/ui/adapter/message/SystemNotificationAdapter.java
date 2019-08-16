package com.allintask.lingdao.ui.adapter.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.allintask.lingdao.R;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;

/**
 * Created by TanJiaJun on 2018/1/12.
 */

public class SystemNotificationAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    public SystemNotificationAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_system_notification, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {

    }

}
