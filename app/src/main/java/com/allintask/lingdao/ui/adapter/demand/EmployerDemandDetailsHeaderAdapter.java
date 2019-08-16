package com.allintask.lingdao.ui.adapter.demand;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.demand.DemandCompletedListBean;
import com.allintask.lingdao.bean.demand.DemandExpiredListBean;
import com.allintask.lingdao.bean.demand.DemandInTheBiddingListBean;
import com.allintask.lingdao.bean.demand.DemandUnderwayListBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.widget.CircleImageView;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/1/16.
 */

public class EmployerDemandDetailsHeaderAdapter extends CommonRecyclerViewAdapter {

    private Context context;
    private int demandStatus;

    public EmployerDemandDetailsHeaderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_employer_demand_details_header, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        CircleImageView headPortraitCIV = holder.getChildView(R.id.civ_head_portrait);
        ImageView redDotIv = holder.getChildView(R.id.iv_red_dot);
        TextView demandStatusTv = holder.getChildView(R.id.tv_demand_status);
        ImageView lineIv = holder.getChildView(R.id.iv_line);

        switch (demandStatus) {
            case CommonConstant.DEMAND_STATUS_IN_THE_BIDDING:
                DemandInTheBiddingListBean.DemandInTheBiddingBean demandInTheBiddingBean = (DemandInTheBiddingListBean.DemandInTheBiddingBean) getItem(position);

                if (null != demandInTheBiddingBean) {
                    String avatarUrl = TypeUtils.getString(demandInTheBiddingBean.avatarUrl, "");
                    boolean isSelected = TypeUtils.getBoolean(demandInTheBiddingBean.isSelected, false);

                    demandStatusTv.setVisibility(View.GONE);

                    String imageUrl = null;

                    if (!TextUtils.isEmpty(avatarUrl)) {
                        imageUrl = "https:" + avatarUrl;
                    }

                    ImageViewUtil.setImageView(context, headPortraitCIV, imageUrl, R.mipmap.ic_default_avatar);

                    if (isSelected) {
//                        redDotIv.setVisibility(View.VISIBLE);
                        demandStatusTv.setTextColor(context.getResources().getColor(R.color.text_red));
                        lineIv.setVisibility(View.VISIBLE);
                    } else {
//                        redDotIv.setVisibility(View.GONE);
                        demandStatusTv.setTextColor(context.getResources().getColor(R.color.text_dark_gray));
                        lineIv.setVisibility(View.GONE);
                    }
                }
                break;

            case CommonConstant.DEMAND_STATUS_UNDERWAY:
                DemandUnderwayListBean.DemandUnderwayBean demandUnderwayBean = (DemandUnderwayListBean.DemandUnderwayBean) getItem(position);

                if (null != demandUnderwayBean) {
                    String avatarUrl = TypeUtils.getString(demandUnderwayBean.avatarUrl, "");
                    String currentStateTip = TypeUtils.getString(demandUnderwayBean.currentStateTip, "");
                    boolean isSelected = TypeUtils.getBoolean(demandUnderwayBean.isSelected, false);

//                    redDotIv.setVisibility(View.GONE);

                    demandStatusTv.setVisibility(View.VISIBLE);

                    String imageUrl = null;

                    if (!TextUtils.isEmpty(avatarUrl)) {
                        imageUrl = "https:" + avatarUrl;
                    }

                    ImageViewUtil.setImageView(context, headPortraitCIV, imageUrl, R.mipmap.ic_default_avatar);

                    if (isSelected) {
                        demandStatusTv.setTextColor(context.getResources().getColor(R.color.text_red));
                        lineIv.setVisibility(View.VISIBLE);
                    } else {
                        demandStatusTv.setTextColor(context.getResources().getColor(R.color.text_dark_gray));
                        lineIv.setVisibility(View.GONE);
                    }

                    demandStatusTv.setText(currentStateTip);
                }
                break;

            case CommonConstant.DEMAND_STATUS_COMPLETED:
                DemandCompletedListBean.DemandCompletedBean demandCompletedBean = (DemandCompletedListBean.DemandCompletedBean) getItem(position);

                if (null != demandCompletedBean) {
                    String avatarUrl = TypeUtils.getString(demandCompletedBean.avatarUrl, "");
                    boolean isSelected = TypeUtils.getBoolean(demandCompletedBean.isSelected, false);

                    demandStatusTv.setVisibility(View.GONE);

                    String imageUrl = null;

                    if (!TextUtils.isEmpty(avatarUrl)) {
                        imageUrl = "https:" + avatarUrl;
                    }

                    ImageViewUtil.setImageView(context, headPortraitCIV, imageUrl, R.mipmap.ic_default_avatar);

                    if (isSelected) {
//                        redDotIv.setVisibility(View.VISIBLE);
                        demandStatusTv.setTextColor(context.getResources().getColor(R.color.text_red));
                        lineIv.setVisibility(View.VISIBLE);
                    } else {
//                        redDotIv.setVisibility(View.GONE);
                        demandStatusTv.setTextColor(context.getResources().getColor(R.color.text_dark_gray));
                        lineIv.setVisibility(View.GONE);
                    }
                }
                break;

            case CommonConstant.DEMAND_STATUS_EXPIRED:
                DemandExpiredListBean.DemandExpiredBean demandExpiredBean = (DemandExpiredListBean.DemandExpiredBean) getItem(position);

                if (null != demandExpiredBean) {
                    String avatarUrl = TypeUtils.getString(demandExpiredBean.avatarUrl, "");
                    boolean isSelected = TypeUtils.getBoolean(demandExpiredBean.isSelected, false);

//                    redDotIv.setVisibility(View.GONE);
                    demandStatusTv.setVisibility(View.GONE);

                    String imageUrl = null;

                    if (!TextUtils.isEmpty(avatarUrl)) {
                        imageUrl = "https:" + avatarUrl;
                    }

                    ImageViewUtil.setImageView(context, headPortraitCIV, imageUrl, R.mipmap.ic_default_avatar);

                    if (isSelected) {
                        demandStatusTv.setTextColor(context.getResources().getColor(R.color.text_red));
                        lineIv.setVisibility(View.VISIBLE);
                    } else {
                        demandStatusTv.setTextColor(context.getResources().getColor(R.color.text_dark_gray));
                        lineIv.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    public void setDemandStatus(int demandStatus) {
        this.demandStatus = demandStatus;
    }

}
