package com.allintask.lingdao.ui.adapter.recommend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.recommend.RecommendDetailsServiceInformationBean;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/3/28.
 */

public class RecommendDetailsServiceWayPriceUnitAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    public RecommendDetailsServiceWayPriceUnitAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recommend_details_service_way_price_unit, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        RecommendDetailsServiceInformationBean.ServiceDetailsBean.ServeWayPriceUnitChineseBean serveWayPriceUnitChineseBean = (RecommendDetailsServiceInformationBean.ServiceDetailsBean.ServeWayPriceUnitChineseBean) getItem(position);

        if (null != serveWayPriceUnitChineseBean) {
            String serveWay = TypeUtils.getString(serveWayPriceUnitChineseBean.serveWay, "");
            int price = TypeUtils.getInteger(serveWayPriceUnitChineseBean.price, 0);
            String unit = TypeUtils.getString(serveWayPriceUnitChineseBean.unit, "");

            holder.setTextView(R.id.tv_title, serveWay);
            holder.setTextView(R.id.tv_content, "￥" + String.valueOf(price) + "/" + unit);
        }
    }

}
