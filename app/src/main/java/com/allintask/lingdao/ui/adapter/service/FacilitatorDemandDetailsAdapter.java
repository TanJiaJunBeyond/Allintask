package com.allintask.lingdao.ui.adapter.service;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.service.ShowFacilitatorDemandDetailsBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.widget.CircleImageView;

import java.util.Date;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/3/18.
 */

public class FacilitatorDemandDetailsAdapter extends CommonRecyclerViewAdapter {

    private static final int ITEM_FACILITATOR_DEMAND_DETAILS_HEADER = 0;
    private static final int ITEM_FACILITATOR_DEMAND_DETAILS_AUTO_REFUND_DATE = 1;
    private static final int ITEM_FACILITATOR_DEMAND_DETAILS = 2;

    private Context context;

    private String userHeadPortraitUrl;
    private String name;
    private int gender;
    private int age = -1;
    private String time;
    private int isBook;

    private int orderId = -1;
    private int orderPrice;
    private String priceTip;
    private String refundTip;
    private String refundReason;
    private Date refundCreateAt;
    private int refundStatus = -1;
    private String arbitrationTip;

    private String autoRefundDateString;

    private OnClickListener onClickListener;

    public FacilitatorDemandDetailsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = null;

        switch (viewType) {
            case ITEM_FACILITATOR_DEMAND_DETAILS_HEADER:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_facilitator_demand_details_header, parent, false));
                break;

            case ITEM_FACILITATOR_DEMAND_DETAILS_AUTO_REFUND_DATE:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_facilitator_demand_details_auto_refund_date, parent, false));
                break;

            case ITEM_FACILITATOR_DEMAND_DETAILS:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_facilitator_demand_details, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        switch (getItemViewType(position)) {
            case ITEM_FACILITATOR_DEMAND_DETAILS_HEADER:
                onBindFacilitatorDemandDetailsHeaderItemView(holder);
                break;

            case ITEM_FACILITATOR_DEMAND_DETAILS_AUTO_REFUND_DATE:
                onBindFacilitatorDemandDetailsAutoRefundDate(holder);
                break;

            case ITEM_FACILITATOR_DEMAND_DETAILS:
                onBindFacilitatorDemandDetailsItemView(holder, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (!TextUtils.isEmpty(autoRefundDateString)) {
            if (position == 0) {
                return ITEM_FACILITATOR_DEMAND_DETAILS_HEADER;
            } else if (position == 1) {
                return ITEM_FACILITATOR_DEMAND_DETAILS_AUTO_REFUND_DATE;
            } else {
                return ITEM_FACILITATOR_DEMAND_DETAILS;
            }
        } else {
            if (position == 0) {
                return ITEM_FACILITATOR_DEMAND_DETAILS_HEADER;
            } else {
                return ITEM_FACILITATOR_DEMAND_DETAILS;
            }
        }
    }

    private void onBindFacilitatorDemandDetailsHeaderItemView(CommonRecyclerViewHolder holder) {
        CircleImageView headPortraitCIV = holder.getChildView(R.id.civ_head_portrait);
        TextView nameTv = holder.getChildView(R.id.tv_name);
        ImageView genderIv = holder.getChildView(R.id.iv_gender);
        TextView ageTv = holder.getChildView(R.id.tv_age);
        TextView timeTv = holder.getChildView(R.id.tv_time);
        LinearLayout bookLL = holder.getChildView(R.id.ll_book);
        LinearLayout paymentPriceLL = holder.getChildView(R.id.ll_payment_price);
        TextView paymentPriceTv = holder.getChildView(R.id.tv_payment_price);
        LinearLayout refundContentLL = holder.getChildView(R.id.ll_refund_content);
        LinearLayout refundReasonLL = holder.getChildView(R.id.ll_refund_reason);
        LinearLayout refundLL = holder.getChildView(R.id.ll_refund);
        Button rejectRefundBtn = holder.getChildView(R.id.btn_reject_refund);
        Button agreeRefundBtn = holder.getChildView(R.id.btn_agree_refund);
        LinearLayout arbitramentLL = holder.getChildView(R.id.ll_arbitrament);

        ImageViewUtil.setImageView(context, headPortraitCIV, userHeadPortraitUrl, R.mipmap.ic_default_avatar);

        if (!TextUtils.isEmpty(name)) {
            nameTv.setText(name);
            nameTv.setVisibility(View.VISIBLE);
        } else {
            nameTv.setVisibility(View.GONE);
        }

        if (gender == CommonConstant.MALE) {
            genderIv.setBackgroundResource(R.mipmap.ic_male);
            genderIv.setVisibility(View.VISIBLE);
        } else if (gender == CommonConstant.FEMALE) {
            genderIv.setBackgroundResource(R.mipmap.ic_female);
            genderIv.setVisibility(View.VISIBLE);
        } else {
            genderIv.setVisibility(View.GONE);
        }

        if (age != -1) {
            ageTv.setText(String.valueOf(age));
            ageTv.setVisibility(View.VISIBLE);
        } else {
            ageTv.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(time)) {
            timeTv.setText(time);
            timeTv.setVisibility(View.VISIBLE);
        } else {
            timeTv.setVisibility(View.GONE);
        }

        if (isBook == 1) {
            bookLL.setVisibility(View.VISIBLE);
        } else {
            bookLL.setVisibility(View.GONE);
        }

        if (orderPrice > 0) {
            if (TextUtils.isEmpty(priceTip)) {
                holder.setTextView(R.id.tv_payment_price, "￥" + String.valueOf(orderPrice));
            } else {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("￥");
                spannableStringBuilder.append(String.valueOf(orderPrice)).append(priceTip);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.text_red)), String.valueOf(orderPrice).length() + 1, spannableStringBuilder.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
                paymentPriceTv.setText(spannableStringBuilder);
            }

            paymentPriceLL.setVisibility(View.VISIBLE);
        } else {
            paymentPriceLL.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(refundTip)) {
            holder.setTextView(R.id.tv_refund_content, refundTip);
            refundContentLL.setVisibility(View.VISIBLE);
        } else {
            refundContentLL.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(refundReason)) {
            holder.setTextView(R.id.tv_refund_reason, refundReason);
            refundReasonLL.setVisibility(View.VISIBLE);
        } else {
            refundReasonLL.setVisibility(View.GONE);
        }

        if (refundStatus == 0) {
            refundLL.setVisibility(View.VISIBLE);
        } else {
            refundLL.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(arbitrationTip)) {
            holder.setTextView(R.id.tv_arbitrament, arbitrationTip);
            arbitramentLL.setVisibility(View.VISIBLE);
        } else {
            arbitramentLL.setVisibility(View.GONE);
        }

        rejectRefundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClickListener) {
                    onClickListener.onRejectRefundClick(orderId);
                }
            }
        });

        agreeRefundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClickListener) {
                    onClickListener.onAgreeRefundClick(orderId);
                }
            }
        });
    }

    private void onBindFacilitatorDemandDetailsAutoRefundDate(CommonRecyclerViewHolder holder) {
        holder.setTextView(R.id.tv_auto_refund_date, autoRefundDateString);
    }

    private void onBindFacilitatorDemandDetailsItemView(CommonRecyclerViewHolder holder, int position) {
        ShowFacilitatorDemandDetailsBean showFacilitatorDemandDetailsBean = (ShowFacilitatorDemandDetailsBean) getItem(position - 1);

        if (null != showFacilitatorDemandDetailsBean) {
            final TextView contentTv = holder.getChildView(R.id.tv_content);
            String title = TypeUtils.getString(showFacilitatorDemandDetailsBean.title, "");
            String content = TypeUtils.getString(showFacilitatorDemandDetailsBean.content, "");

            holder.setTextView(R.id.tv_title, title);

            contentTv.post(new Runnable() {
                @Override
                public void run() {
                    int contentLineCount = contentTv.getLineCount();

                    if (contentLineCount == 1) {
                        contentTv.setGravity(Gravity.RIGHT);
                    } else {
                        contentTv.setGravity(Gravity.LEFT);
                    }
                }
            });

            contentTv.setText(content);
        }
    }

    public void setFacilitatorDemandDetailsPersonalInformation(String userHeadPortraitUrl, String name, int gender, int age, String time, int isBook) {
        this.userHeadPortraitUrl = userHeadPortraitUrl;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.time = time;
        this.isBook = isBook;

        notifyItemChanged(0);
    }

    public void setFacilitatorDemandDetailsOrderInformation(int orderId, int orderPrice, String priceTip, String refundTip, String refundReason, Date refundCreateAt, int refundStatus, String arbitrationTip) {
        this.orderId = orderId;
        this.orderPrice = orderPrice;
        this.priceTip = priceTip;
        this.refundTip = refundTip;
        this.refundReason = refundReason;
        this.refundCreateAt = refundCreateAt;
        this.refundStatus = refundStatus;
        this.arbitrationTip = arbitrationTip;

        notifyItemChanged(0);
    }

    public void setAutoRefundDateString(String autoRefundDateString) {
        this.autoRefundDateString = autoRefundDateString;

        if (!TextUtils.isEmpty(this.autoRefundDateString)) {
            notifyItemChanged(1);
        }
    }

    public interface OnClickListener {

        void onAgreeRefundClick(int orderId);

        void onRejectRefundClick(int orderId);

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
