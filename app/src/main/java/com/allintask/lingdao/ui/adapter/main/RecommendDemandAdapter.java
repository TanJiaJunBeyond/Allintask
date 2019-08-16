package com.allintask.lingdao.ui.adapter.main;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.recommend.RecommendDemandBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.widget.CircleImageView;
import com.allintask.lingdao.widget.FlowLayout;

import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/5/2.
 */

public class RecommendDemandAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    public RecommendDemandAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recommend_demand, parent, false));
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        RecommendDemandBean recommendDemandBean = (RecommendDemandBean) getItem(position);

        if (null != recommendDemandBean) {
            TextView statusTv = holder.getChildView(R.id.tv_status);
            LinearLayout priceAndUnitLL = holder.getChildView(R.id.ll_price_and_unit);
            ImageView trusteeshipIv = holder.getChildView(R.id.iv_trusteeship);
            LinearLayout workWayLL = holder.getChildView(R.id.ll_work_way);
            LinearLayout siteLL = holder.getChildView(R.id.ll_site);
            FlowLayout flowLayout = holder.getChildView(R.id.flow_layout);
            CircleImageView headPortraitCIV = holder.getChildView(R.id.civ_head_portrait);

            String categoryName = TypeUtils.getString(recommendDemandBean.categoryName, "");
            String serveWayName = TypeUtils.getString(recommendDemandBean.serveWayName, "");
            Integer budget = recommendDemandBean.budget;
            int isTrusteeship = TypeUtils.getInteger(recommendDemandBean.isTrusteeship, 0);
            String city = TypeUtils.getString(recommendDemandBean.city, "");
            String introduce = TypeUtils.getString(recommendDemandBean.introduce, "");
            String bookingDateTip = TypeUtils.getString(recommendDemandBean.bookingDateTip, "");
            String deliverCycleName = TypeUtils.getString(recommendDemandBean.deliverCycleName, "");
            List<String> categoryPropertyValueChineseList = recommendDemandBean.categoryPropertyValueChineseList;
            String avatarUrl = TypeUtils.getString(recommendDemandBean.avatarUrl, "");
            String realName = TypeUtils.getString(recommendDemandBean.realName, "");
            String loginTimeTip = TypeUtils.getString(recommendDemandBean.loginTimeTip, "");
            int demandStatus = TypeUtils.getInteger(recommendDemandBean.demandStatus, -1);
            Integer bidCount = recommendDemandBean.bidCount;

            if (null == bidCount) {
                bidCount = 0;
            }

            holder.setTextView(R.id.tv_category_name, categoryName, true);

            String status = null;

            if (demandStatus == CommonConstant.DEMAND_DETAILS_STATUS_IN_THE_BIDDING) {
                status = context.getString(R.string.demand_status_in_the_bidding);
                statusTv.setBackgroundResource(R.drawable.shape_recommend_tag_orange_background);
            } else if (demandStatus == CommonConstant.DEMAND_DETAILS_STATUS_UNDERWAY) {
                status = context.getString(R.string.demand_status_underway);
                statusTv.setBackgroundResource(R.drawable.shape_recommend_tag_orange_background);
            } else if (demandStatus == CommonConstant.DEMAND_DETAILS_STATUS_COMPLETED) {
                status = context.getString(R.string.demand_status_completed);
                statusTv.setBackgroundResource(R.drawable.shape_recommend_tag_gray_background);
            } else if (demandStatus == CommonConstant.DEMAND_DETAILS_STATUS_EXPIRED) {
                status = context.getString(R.string.demand_status_expired);
                statusTv.setBackgroundResource(R.drawable.shape_recommend_tag_gray_background);
            }

            if (!TextUtils.isEmpty(status)) {
                statusTv.setText(status);
                statusTv.setVisibility(View.VISIBLE);
            } else {
                statusTv.setVisibility(View.GONE);
            }

            if (null != budget && budget != 0) {
                holder.setTextView(R.id.tv_price, String.valueOf(budget));
                priceAndUnitLL.setVisibility(View.VISIBLE);
            } else {
                priceAndUnitLL.setVisibility(View.GONE);
            }

            if (isTrusteeship == 1) {
                trusteeshipIv.setVisibility(View.VISIBLE);
            } else {
                trusteeshipIv.setVisibility(View.GONE);
            }

//            StringBuilder stringBuilder = new StringBuilder();
//
//            if (!TextUtils.isEmpty(city)) {
//                stringBuilder.append(city).append("\r\r|\r\r");
//            }
//
//            if (!TextUtils.isEmpty(bookingDateTip)) {
//                stringBuilder.append(bookingDateTip).append("\r\r|\r\r");
//            }
//
//            if (!TextUtils.isEmpty(deliverCycleName)) {
//                stringBuilder.append(deliverCycleName);
//            }
//
//            holder.setTextView(R.id.tv_content, stringBuilder.toString(), true);

            holder.setTextView(R.id.tv_apply_amount, String.valueOf(bidCount) + "人");

            if (!TextUtils.isEmpty(serveWayName)) {
                holder.setTextView(R.id.tv_work_way, serveWayName);
                workWayLL.setVisibility(View.VISIBLE);
            } else {
                workWayLL.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(city)) {
                holder.setTextView(R.id.tv_site, city);
            } else {
                holder.setTextView(R.id.tv_site, context.getString(R.string.unlimited));
            }

            holder.setTextView(R.id.tv_demand_introduction, introduce, true);

//            flowLayout.setMaxRow(1);
//
//            if (flowLayout.getChildCount() > 0) {
//                flowLayout.removeAllViews();
//            }
//
//            if (null != categoryPropertyValueChineseList && categoryPropertyValueChineseList.size() > 0) {
//                for (int i = 0; i < categoryPropertyValueChineseList.size(); i++) {
//                    String categoryProperty = categoryPropertyValueChineseList.get(i);
//
//                    TextView tagTv = new TextView(context);
//                    tagTv.setBackgroundResource(R.drawable.shape_recommend_tag_background);
//                    tagTv.setText(categoryProperty);
//                    tagTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
//                    tagTv.setPadding(15, 5, 15, 5);
//                    flowLayout.addView(tagTv);
//                }
//
//                flowLayout.setVisibility(View.VISIBLE);
//            } else {
//                flowLayout.setVisibility(View.GONE);
//            }

            String headPortraitUrl = null;

            if (!TextUtils.isEmpty(avatarUrl)) {
                headPortraitUrl = "https:" + avatarUrl;
            }

            ImageViewUtil.setImageView(context, headPortraitCIV, headPortraitUrl, R.mipmap.ic_default_avatar);

            holder.setTextView(R.id.tv_name, realName, true);
            holder.setTextView(R.id.tv_time, loginTimeTip, true);
        }
    }

}
