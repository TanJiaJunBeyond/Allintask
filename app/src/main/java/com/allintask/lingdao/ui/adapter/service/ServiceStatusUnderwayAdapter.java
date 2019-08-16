package com.allintask.lingdao.ui.adapter.service;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.service.GetIdToChineseListBean;
import com.allintask.lingdao.bean.service.ServiceUnderwayListBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.activity.message.ChatActivity;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.utils.WindowUtils;
import com.allintask.lingdao.widget.CircleImageView;

import java.util.Date;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/1/16.
 */

public class ServiceStatusUnderwayAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    private List<GetIdToChineseListBean.GetIdToChineseBean> categoryList;
    private List<GetIdToChineseListBean.GetIdToChineseBean> serviceModeList;

    public ServiceStatusUnderwayAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_service_status_underway, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        ServiceUnderwayListBean.ServiceUnderwayBean serviceUnderwayBean = (ServiceUnderwayListBean.ServiceUnderwayBean) getItem(position);

        if (null != serviceUnderwayBean) {
            LinearLayout serviceStatusUnderwayLL = holder.getChildView(R.id.ll_service_status_underway);
            CircleImageView headPortrait = holder.getChildView(R.id.civ_head_portrait);
            TextView nameTv = holder.getChildView(R.id.tv_name);
            TextView timeTv = holder.getChildView(R.id.tv_time);
            TextView serviceModeTv = holder.getChildView(R.id.tv_service_mode);
            ImageView trusteeshipIv = holder.getChildView(R.id.iv_trusteeship);
            TextView earnestMoneyTv = holder.getChildView(R.id.tv_trusteeship_amount);
            TextView statusTv = holder.getChildView(R.id.tv_status);
            TextView publishTimeTv = holder.getChildView(R.id.tv_publish_time);
            LinearLayout heOrSheAdvantageLL = holder.getChildView(R.id.ll_he_or_she_advantage);
            LinearLayout orderNumberLL = holder.getChildView(R.id.ll_order_number);
            LinearLayout bidTimeLL = holder.getChildView(R.id.ll_bid_time);
            LinearLayout paymentPriceLL = holder.getChildView(R.id.ll_payment_price);
            TextView paymentPriceTv = holder.getChildView(R.id.tv_payment_price);
            LinearLayout contentLL = holder.getChildView(R.id.ll_content);
            LinearLayout chatLL = holder.getChildView(R.id.ll_chat);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WindowUtils.getScreenWidth(context), LinearLayout.LayoutParams.WRAP_CONTENT);
            serviceStatusUnderwayLL.setLayoutParams(layoutParams);

            final int buyerUserId = TypeUtils.getInteger(serviceUnderwayBean.buyerUserId, -1);
            int serveId = TypeUtils.getInteger(serviceUnderwayBean.serveId, -1);
            String avatarUrl = TypeUtils.getString(serviceUnderwayBean.avatarUrl, "");
            int categoryId = TypeUtils.getInteger(serviceUnderwayBean.categoryId, -1);
            String name = TypeUtils.getString(serviceUnderwayBean.name, "");
            String loginTimeTip = TypeUtils.getString(serviceUnderwayBean.loginTimeTip, "");
            int serveWayId = TypeUtils.getInteger(serviceUnderwayBean.serveWayId, -1);
            String demandPrice = TypeUtils.getString(serviceUnderwayBean.demandPrice, "");
            int salaryTrusteeship = TypeUtils.getInteger(serviceUnderwayBean.salaryTrusteeship, 0);
            String currentStateTip = TypeUtils.getString(serviceUnderwayBean.currentStateTip, "");
            String demandPublishTimeTip = TypeUtils.getString(serviceUnderwayBean.demandPublishTimeTip, "");
            int orderPrice = TypeUtils.getInteger(serviceUnderwayBean.orderPrice, 0);
            String advantage = TypeUtils.getString(serviceUnderwayBean.advantage, "");
            String orderNo = TypeUtils.getString(serviceUnderwayBean.orderNo, "");
            String priceTip = TypeUtils.getString(serviceUnderwayBean.priceTip, "");
            Date bidDate = serviceUnderwayBean.bidDate;
            String tip = TypeUtils.getString(serviceUnderwayBean.tip, "");

            String imageUrl = null;

            if (!TextUtils.isEmpty(avatarUrl)) {
                imageUrl = "https:" + avatarUrl;
            }

            ImageViewUtil.setImageView(context, headPortrait, imageUrl, R.mipmap.ic_default_avatar);

            if (null != categoryList && categoryList.size() > 0) {
                for (int i = 0; i < categoryList.size(); i++) {
                    GetIdToChineseListBean.GetIdToChineseBean serviceGetIdToChineseBean = categoryList.get(i);

                    if (null != serviceGetIdToChineseBean) {
                        int code = TypeUtils.getInteger(serviceGetIdToChineseBean.code, 0);
                        String value = TypeUtils.getString(serviceGetIdToChineseBean.value, "");

                        if (code == categoryId) {
                            holder.setTextView(R.id.tv_service_name, value);
                        }
                    }
                }
            }

            if (!TextUtils.isEmpty(name)) {
                nameTv.setText(name);
                nameTv.setVisibility(View.VISIBLE);
            } else {
                nameTv.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(loginTimeTip)) {
                timeTv.setText(loginTimeTip);
                timeTv.setVisibility(View.VISIBLE);
            } else {
                timeTv.setVisibility(View.GONE);
            }

            StringBuilder stringBuilder = new StringBuilder();

            if (null != serviceModeList && serviceModeList.size() > 0) {
                for (int i = 0; i < serviceModeList.size(); i++) {
                    GetIdToChineseListBean.GetIdToChineseBean serviceGetIdToChineseBean = serviceModeList.get(i);

                    if (null != serviceGetIdToChineseBean) {
                        int code = TypeUtils.getInteger(serviceGetIdToChineseBean.code, 0);
                        String value = TypeUtils.getString(serviceGetIdToChineseBean.value, "");

                        if (code == serveWayId) {
                            stringBuilder.append(value);
                        }
                    }
                }

                stringBuilder.append("：￥").append(String.valueOf(demandPrice));
                serviceModeTv.setText(stringBuilder);
                serviceModeTv.setVisibility(View.VISIBLE);
            } else {
                serviceModeTv.setVisibility(View.GONE);
            }

            if (salaryTrusteeship > 0) {
                trusteeshipIv.setVisibility(View.VISIBLE);

//                holder.setTextView(R.id.tv_trusteeship_amount, "托管金额：￥" + String.valueOf(salaryTrusteeship));
//                earnestMoneyTv.setVisibility(View.VISIBLE);
            } else {
                trusteeshipIv.setVisibility(View.GONE);
//                earnestMoneyTv.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(currentStateTip)) {
                holder.setTextView(R.id.tv_status, currentStateTip);
                statusTv.setVisibility(View.VISIBLE);
            } else {
                statusTv.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(demandPublishTimeTip)) {
                holder.setTextView(R.id.tv_publish_time, demandPublishTimeTip);
                publishTimeTv.setVisibility(View.VISIBLE);
            } else {
                publishTimeTv.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(advantage)) {
                holder.setTextView(R.id.tv_he_or_she_advantage_content, advantage);
                heOrSheAdvantageLL.setVisibility(View.VISIBLE);
            } else {
                heOrSheAdvantageLL.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(orderNo)) {
                holder.setTextView(R.id.tv_order_number, orderNo);
                orderNumberLL.setVisibility(View.VISIBLE);
            } else {
                orderNumberLL.setVisibility(View.GONE);
            }

            if (null != bidDate) {
                String bidDateStr = CommonConstant.commonTimeFormat.format(bidDate);
                holder.setTextView(R.id.tv_bid_time, bidDateStr);

                bidTimeLL.setVisibility(View.VISIBLE);
            } else {
                bidTimeLL.setVisibility(View.GONE);
            }

            if (orderPrice != 0) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("￥");
                spannableStringBuilder.append(String.valueOf(orderPrice)).append(priceTip);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.text_red)), String.valueOf(orderPrice).length() + 1, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                paymentPriceTv.setText(spannableStringBuilder);

                paymentPriceLL.setVisibility(View.VISIBLE);
            } else {
                paymentPriceLL.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(tip)) {
                tip = tip.replace(CommonConstant.NEWLINE, "\n");
                holder.setTextView(R.id.tv_content, tip);
                contentLL.setVisibility(View.VISIBLE);
            } else {
                contentLL.setVisibility(View.GONE);
            }

            chatLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (buyerUserId != -1) {
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra(CommonConstant.EXTRA_USER_ID, String.valueOf(buyerUserId));
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public void setCategoryList(List<GetIdToChineseListBean.GetIdToChineseBean> categoryList) {
        this.categoryList = categoryList;
    }

    public void setServiceModeList(List<GetIdToChineseListBean.GetIdToChineseBean> serviceModeList) {
        this.serviceModeList = serviceModeList;
    }

}
