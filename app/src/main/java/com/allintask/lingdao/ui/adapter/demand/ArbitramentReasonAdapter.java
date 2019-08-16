package com.allintask.lingdao.ui.adapter.demand;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.demand.ArbitramentReasonBean;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/3/20.
 */

public class ArbitramentReasonAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    public ArbitramentReasonAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_arbitrament_reason, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        ArbitramentReasonBean arbitramentReasonBean = (ArbitramentReasonBean) getItem(position);

        if (null != arbitramentReasonBean) {
            String name = TypeUtils.getString(arbitramentReasonBean.name, "");
            holder.setTextView(R.id.tv_arbitrament_reason, name);
        }
    }

}
