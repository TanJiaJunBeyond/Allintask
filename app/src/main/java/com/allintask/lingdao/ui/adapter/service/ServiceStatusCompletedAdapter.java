package com.allintask.lingdao.ui.adapter.service;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.service.GetIdToChineseListBean;
import com.allintask.lingdao.bean.service.ServiceCompletedListBean;
import com.allintask.lingdao.constant.CommonConstant;
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

public class ServiceStatusCompletedAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    private List<GetIdToChineseListBean.GetIdToChineseBean> categoryList;
    private List<GetIdToChineseListBean.GetIdToChineseBean> serviceModeList;

    private OnClickListener onClickListener;

    public ServiceStatusCompletedAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_service_status_completed, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        ServiceCompletedListBean.ServiceCompletedBean serviceCompletedBean = (ServiceCompletedListBean.ServiceCompletedBean) getItem(position);

        if (null != serviceCompletedBean) {
            LinearLayout serviceStatusCompletedLL = holder.getChildView(R.id.ll_service_status_completed);
            CircleImageView headPortrait = holder.getChildView(R.id.civ_head_portrait);
            TextView serviceModeTv = holder.getChildView(R.id.tv_service_mode);
            ImageView trusteeshipIv = holder.getChildView(R.id.iv_trusteeship);
            TextView earnestMoneyTv = holder.getChildView(R.id.tv_trusteeship_amount);
            TextView publishTimeTv = holder.getChildView(R.id.tv_publish_time);
            LinearLayout serviceTimeLL = holder.getChildView(R.id.ll_service_time);
            TextView delayedTv = holder.getChildView(R.id.tv_delayed);
            LinearLayout heOrSheAdvantageLL = holder.getChildView(R.id.ll_he_or_she_advantage);
            LinearLayout orderNumberLL = holder.getChildView(R.id.ll_order_number);
            LinearLayout bidTimeLL = holder.getChildView(R.id.ll_bid_time);
            LinearLayout paymentPriceLL = holder.getChildView(R.id.ll_payment_price);
            TextView paymentPriceTv = holder.getChildView(R.id.tv_payment_price);
            LinearLayout contentLL = holder.getChildView(R.id.ll_content);
            LinearLayout firstLL = holder.getChildView(R.id.ll_first);
            LinearLayout secondLL = holder.getChildView(R.id.ll_second);
            TextView secondTv = holder.getChildView(R.id.tv_second);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WindowUtils.getScreenWidth(context), LinearLayout.LayoutParams.WRAP_CONTENT);
            serviceStatusCompletedLL.setLayoutParams(layoutParams);

            final int buyerUserId = TypeUtils.getInteger(serviceCompletedBean.buyerUserId, -1);
            final int sellerUserId = TypeUtils.getInteger(serviceCompletedBean.sellerUserId, -1);
            final int orderId = TypeUtils.getInteger(serviceCompletedBean.orderId, -1);
            final int serveId = TypeUtils.getInteger(serviceCompletedBean.serveId, -1);
            String avatarUrl = TypeUtils.getString(serviceCompletedBean.avatarUrl, "");
            int categoryId = TypeUtils.getInteger(serviceCompletedBean.categoryId, -1);
            String name = TypeUtils.getString(serviceCompletedBean.name, "");
            String loginTimeTip = TypeUtils.getString(serviceCompletedBean.loginTimeTip, "");
            int serveWayId = TypeUtils.getInteger(serviceCompletedBean.serveWayId, -1);
            String demandPrice = TypeUtils.getString(serviceCompletedBean.demandPrice, "");
            Date startWorkAt = serviceCompletedBean.startWorkAt;
            Date endWorkAt = serviceCompletedBean.endWorkAt;
            boolean isDelayComplete = TypeUtils.getBoolean(serviceCompletedBean.isDelayComplete, false);
            int salaryTrusteeship = TypeUtils.getInteger(serviceCompletedBean.salaryTrusteeship, 0);
            String currentStateTip = TypeUtils.getString(serviceCompletedBean.currentStateTip, "");
            String demandPublishTimeTip = TypeUtils.getString(serviceCompletedBean.demandPublishTimeTip, "");
            int orderPrice = TypeUtils.getInteger(serviceCompletedBean.orderPrice, 0);
            String advantage = TypeUtils.getString(serviceCompletedBean.advantage, "");
            String orderNo = TypeUtils.getString(serviceCompletedBean.orderNo, "");
            String priceTip = TypeUtils.getString(serviceCompletedBean.priceTip, "");
            Date bidDate = serviceCompletedBean.bidDate;
            String tip = TypeUtils.getString(serviceCompletedBean.tip, "");
            final boolean showEvaluationButton = TypeUtils.getBoolean(serviceCompletedBean.showEvaluationButton, false);
            final boolean showEvaluationDetailsButton = TypeUtils.getBoolean(serviceCompletedBean.showEvaluationDetailsButton, false);

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

            holder.setTextView(R.id.tv_name, name);
            holder.setTextView(R.id.tv_time, loginTimeTip);

            if (null != serviceModeList && serviceModeList.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 0; i < serviceModeList.size(); i++) {
                    GetIdToChineseListBean.GetIdToChineseBean serviceGetIdToChineseBean = serviceModeList.get(i);

                    if (null != serviceGetIdToChineseBean) {
                        int code = TypeUtils.getInteger(serviceGetIdToChineseBean.code, 0);
                        String value = TypeUtils.getString(serviceGetIdToChineseBean.value, "");

                        if (code == serveWayId) {
                            stringBuilder.append(value).append("：￥").append(String.valueOf(demandPrice));
                            serviceModeTv.setText(stringBuilder);
                            serviceModeTv.setVisibility(View.VISIBLE);
                        }
                    }
                }

                if (TextUtils.isEmpty(stringBuilder)) {
                    serviceModeTv.setVisibility(View.GONE);
                }
            } else {
                serviceModeTv.setVisibility(View.GONE);
            }

            if (null != startWorkAt && null != endWorkAt) {
                String startWorkDateStr = CommonConstant.commonDateFormat.format(startWorkAt);
                String endWorkDateStr = CommonConstant.commonDateFormat.format(endWorkAt);
                holder.setTextView(R.id.tv_service_time, startWorkDateStr + "~" + endWorkDateStr);
            } else {
                serviceTimeLL.setVisibility(View.GONE);
            }

            if (isDelayComplete) {
                delayedTv.setVisibility(View.VISIBLE);
            } else {
                delayedTv.setVisibility(View.GONE);
            }

            if (salaryTrusteeship > 0) {
                trusteeshipIv.setVisibility(View.VISIBLE);

//                holder.setTextView(R.id.tv_trusteeship_amount, "托管金额：￥" + String.valueOf(salaryTrusteeship));
//                earnestMoneyTv.setVisibility(View.VISIBLE);
            } else {
                trusteeshipIv.setVisibility(View.GONE);
//                earnestMoneyTv.setVisibility(View.GONE);
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
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder().append("￥").append(String.valueOf(orderPrice)).append(priceTip);
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

            if (showEvaluationButton) {
                secondTv.setText(context.getString(R.string.evaluate));
                secondLL.setVisibility(View.VISIBLE);
            } else if (showEvaluationDetailsButton) {
                secondTv.setText(context.getString(R.string.see_evaluate));
                secondLL.setVisibility(View.VISIBLE);
            } else {
                secondLL.setVisibility(View.GONE);
            }

            firstLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onClickListener) {
                        onClickListener.onChatClick(buyerUserId);
                    }
                }
            });

            secondLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onClickListener) {
                        if (showEvaluationButton) {
                            onClickListener.onEvaluateClick(buyerUserId, orderId, serveId, buyerUserId, sellerUserId);
                        } else if (showEvaluationDetailsButton) {
                            onClickListener.onSeeEvaluateClick(orderId);
                        }
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

    public interface OnClickListener {

        void onChatClick(int buyerUserId);

        void onEvaluateClick(int userId, int orderId, int serviceId, int buyerUserId, int sellerUserId);

        void onSeeEvaluateClick(int orderId);

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
