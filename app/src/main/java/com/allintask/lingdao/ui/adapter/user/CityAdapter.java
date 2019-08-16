package com.allintask.lingdao.ui.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.AddressListBean;
import com.allintask.lingdao.bean.user.AddressSubBean;
import com.allintask.lingdao.bean.user.IsAllBean;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/3/1.
 */

public class CityAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    public CityAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_city, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        AddressSubBean subBean = (AddressSubBean) getItem(position);

        if (null != subBean) {
            RelativeLayout cityRL = holder.getChildView(R.id.rl_city);
            TextView cityTv = holder.getChildView(R.id.tv_city);

            String city = TypeUtils.getString(subBean.name, "");
            boolean isSelected = TypeUtils.getBoolean(subBean.isSelected, false);

            if (isSelected) {
                cityRL.setBackgroundColor(context.getResources().getColor(R.color.theme_orange));
                cityTv.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                cityRL.setBackgroundColor(context.getResources().getColor(R.color.white));
                cityTv.setTextColor(context.getResources().getColor(R.color.text_light_black));
            }

            cityTv.setText(city);
        }
    }

}
