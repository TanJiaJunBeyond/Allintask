package com.allintask.lingdao.ui.adapter.main;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.demand.DemandStatusListBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.adapter.demand.DemandStatusHeadPortraitAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.utils.WindowUtils;

import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/1/15.
 */

public class DemandStatusAdapter extends CommonRecyclerViewAdapter {

    private Context context;
    private int demandStatus = 0;

    public DemandStatusAdapter(Context context, int demandStatus) {
        this.context = context;
        this.demandStatus = demandStatus;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_demand_status, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        DemandStatusListBean.DemandStatusBean demandStatusBean = (DemandStatusListBean.DemandStatusBean) getItem(position);

        if (null != demandStatusBean) {
            LinearLayout demandStatusLL = holder.getChildView(R.id.ll_demand_status);
            ImageView demandStatusIv = holder.getChildView(R.id.iv_demand_status);
            TextView bidContentTv = holder.getChildView(R.id.tv_bid_content);
            TextView noBidderTv = holder.getChildView(R.id.tv_no_bidder);
            RecyclerView recyclerView = holder.getChildView(R.id.recycler_view);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WindowUtils.getScreenWidth(context), ViewGroup.LayoutParams.WRAP_CONTENT);
            demandStatusLL.setLayoutParams(layoutParams);

            String categoryName = TypeUtils.getString(demandStatusBean.categoryName, "");
            int bidBuyCount = TypeUtils.getInteger(demandStatusBean.bidBuyCount, 0);
            int budget = TypeUtils.getInteger(demandStatusBean.budget, 0);
            String tip = TypeUtils.getString(demandStatusBean.tip, "");
            List<DemandStatusListBean.DemandStatusBean.UserMsgVosBean> userMsgVosList = demandStatusBean.userMsgVos;

            holder.setTextView(R.id.tv_service_name, categoryName);
            holder.setTextView(R.id.tv_win_the_bidding_count, String.valueOf(bidBuyCount));
            holder.setTextView(R.id.tv_budget, "￥" + String.valueOf(budget) + "预算");

            if (!TextUtils.isEmpty(tip)) {
                tip = tip.replace(CommonConstant.NEWLINE, "\n");
                holder.setTextView(R.id.tv_content, tip);
            }

            if (null != userMsgVosList && userMsgVosList.size() > 0) {
                bidContentTv.setText("已有" + String.valueOf(userMsgVosList.size()) + "位投标");
                bidContentTv.setVisibility(View.VISIBLE);
                noBidderTv.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                initRecyclerView(recyclerView, userMsgVosList);
            } else {
                bidContentTv.setVisibility(View.GONE);
                noBidderTv.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

            switch (demandStatus) {
                case CommonConstant.DEMAND_STATUS_IN_THE_BIDDING:
                    demandStatusIv.setBackgroundResource(R.mipmap.ic_demand_status_in_the_bidding);
                    break;

                case CommonConstant.DEMAND_STATUS_UNDERWAY:
                    demandStatusIv.setBackgroundResource(R.mipmap.ic_demand_status_underway);
                    break;

                case CommonConstant.DEMAND_STATUS_COMPLETED:
                    demandStatusIv.setBackgroundResource(R.mipmap.ic_demand_status_completed);
                    break;

                case CommonConstant.DEMAND_STATUS_EXPIRED:
                    demandStatusIv.setBackgroundResource(R.mipmap.ic_demand_status_expired);
                    break;
            }
        }
    }

    private void initRecyclerView(RecyclerView recyclerView, List<DemandStatusListBean.DemandStatusBean.UserMsgVosBean> userMsgVosList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        DemandStatusHeadPortraitAdapter demandStatusHeadPortraitAdapter = new DemandStatusHeadPortraitAdapter(context);
        recyclerView.setAdapter(demandStatusHeadPortraitAdapter);

        demandStatusHeadPortraitAdapter.setDateList(userMsgVosList);
    }

}
