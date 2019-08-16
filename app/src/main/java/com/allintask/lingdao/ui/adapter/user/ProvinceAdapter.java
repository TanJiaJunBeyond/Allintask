package com.allintask.lingdao.ui.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.IsAllBean;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/3/1.
 */

public class ProvinceAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    public ProvinceAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_province, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        IsAllBean isAllBean = (IsAllBean) getItem(position);

        if (null != isAllBean) {
            LinearLayout provinceLL = holder.getChildView(R.id.ll_province);
            TextView provinceTv = holder.getChildView(R.id.tv_province);

            boolean isSelected = TypeUtils.getBoolean(isAllBean.isSelected, false);
            String name = TypeUtils.getString(isAllBean.name, "");

            if (isSelected) {
                provinceLL.setBackgroundColor(context.getResources().getColor(R.color.theme_orange));
                provinceTv.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                provinceLL.setBackgroundColor(context.getResources().getColor(R.color.white));
                provinceTv.setTextColor(context.getResources().getColor(R.color.text_light_black));
            }

            provinceTv.setText(name);
        }
    }

}
