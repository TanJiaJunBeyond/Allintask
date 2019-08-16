package com.allintask.lingdao.ui.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.WayBean;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/4/4.
 */

public class WayAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    public WayAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_way, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        WayBean wayBean = (WayBean) getItem(position);

        if (null != wayBean) {
            TextView wayTv = holder.getChildView(R.id.tv_way);

            String value = TypeUtils.getString(wayBean.value, "");
            boolean isSelected = TypeUtils.getBoolean(wayBean.isSelected, false);

            if (isSelected) {
                wayTv.setTextColor(context.getResources().getColor(R.color.theme_orange));
            } else {
                wayTv.setTextColor(context.getResources().getColor(R.color.text_light_black));
            }

            wayTv.setText(value);
        }
    }

}
