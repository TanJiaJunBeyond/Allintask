package com.allintask.lingdao.ui.adapter.demand;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.demand.DemandCompletedListBean;
import com.allintask.lingdao.bean.demand.DemandExpiredListBean;
import com.allintask.lingdao.bean.demand.DemandInTheBiddingListBean;
import com.allintask.lingdao.bean.demand.DemandUnderwayListBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.activity.message.ChatActivity;
import com.allintask.lingdao.ui.activity.recommend.RecommendDetailsActivity;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.utils.WindowUtils;
import com.allintask.lingdao.widget.CircleImageView;

import java.util.Date;

import cn.tanjiajun.sdk.common.utils.AgeUtils;
import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.DialogUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/1/16.
 */

public class EmployerDemandDetailsAdapter extends CommonRecyclerViewAdapter {

    private Context context;
    private int demandStatus;
    private int earnestMoney;

    private OnClickListener onClickListener;

    public EmployerDemandDetailsAdapter(Context context, int earnestMoney) {
        this.context = context;
        this.earnestMoney = earnestMoney;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_employer_demand_details, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        LinearLayout employerDemandDetailsLL = holder.getChildView(R.id.ll_employer_demand_details);
//        ImageView newIv = holder.getChildView(R.id.iv_new);
        CircleImageView headPortraitCIV = holder.getChildView(R.id.civ_head_portrait);
        TextView nameTv = holder.getChildView(R.id.tv_name);
        ImageView genderIv = holder.getChildView(R.id.iv_gender);
        TextView ageTv = holder.getChildView(R.id.tv_age);
        TextView timeTv = holder.getChildView(R.id.tv_time);
        TextView statusTv = holder.getChildView(R.id.tv_status);
        LinearLayout bidPriceLL = holder.getChildView(R.id.ll_bid_price);
        LinearLayout advantageLL = holder.getChildView(R.id.ll_advantage);
        LinearLayout orderNumberLL = holder.getChildView(R.id.ll_order_number);
        LinearLayout bidTimeLL = holder.getChildView(R.id.ll_bid_time);
        LinearLayout workTimeLL = holder.getChildView(R.id.ll_work_time);
        Button delayDayBtn = holder.getChildView(R.id.btn_delay_day);
        TextView delayCompletedTv = holder.getChildView(R.id.tv_delay_completed);
        LinearLayout paymentPriceLL = holder.getChildView(R.id.ll_payment_price);
        TextView paymentPriceTv = holder.getChildView(R.id.tv_payment_price);
        Button applyForRefundBtn = holder.getChildView(R.id.btn_apply_for_refund);
        LinearLayout arbitrationLL = holder.getChildView(R.id.ll_arbitration);
        TextView hintTv = holder.getChildView(R.id.tv_hint);
        Button applyForArbitramentBtn = holder.getChildView(R.id.btn_apply_for_arbitrament);
        final TextView contentTv = holder.getChildView(R.id.tv_content);
        LinearLayout firstLL = holder.getChildView(R.id.ll_first);
        LinearLayout secondLL = holder.getChildView(R.id.ll_second);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WindowUtils.getScreenWidth(context), LinearLayout.LayoutParams.WRAP_CONTENT);
        employerDemandDetailsLL.setLayoutParams(layoutParams);

        switch (demandStatus) {
            case CommonConstant.DEMAND_STATUS_IN_THE_BIDDING:
                DemandInTheBiddingListBean.DemandInTheBiddingBean demandInTheBiddingBean = (DemandInTheBiddingListBean.DemandInTheBiddingBean) getItem(position);

                if (null != demandInTheBiddingBean) {
                    final int sellerUserId = TypeUtils.getInteger(demandInTheBiddingBean.sellerUserId, -1);
                    final int demandId = TypeUtils.getInteger(demandInTheBiddingBean.demandId, -1);
                    final int bidId = TypeUtils.getInteger(demandInTheBiddingBean.bidId, -1);
                    String avatarUrl = TypeUtils.getString(demandInTheBiddingBean.avatarUrl, "");
                    final String name = TypeUtils.getString(demandInTheBiddingBean.name, "");
                    int gender = TypeUtils.getInteger(demandInTheBiddingBean.gender, 0);
                    Date birthdayDate = demandInTheBiddingBean.birthday;
                    String loginTimeTip = TypeUtils.getString(demandInTheBiddingBean.loginTimeTip, "");
                    String currentStateTip = TypeUtils.getString(demandInTheBiddingBean.currentStateTip, "");
                    final int price = TypeUtils.getInteger(demandInTheBiddingBean.price, 0);
                    String advantage = TypeUtils.getString(demandInTheBiddingBean.advantage, "");
                    Date bidDate = demandInTheBiddingBean.bidDate;
                    String tip = TypeUtils.getString(demandInTheBiddingBean.tip, "");

//                    newIv.setVisibility(View.VISIBLE);
                    bidPriceLL.setVisibility(View.GONE);
                    advantageLL.setVisibility(View.GONE);
                    bidTimeLL.setVisibility(View.GONE);
                    orderNumberLL.setVisibility(View.GONE);
                    workTimeLL.setVisibility(View.GONE);
                    paymentPriceLL.setVisibility(View.GONE);
                    arbitrationLL.setVisibility(View.GONE);
                    secondLL.setVisibility(View.VISIBLE);

                    String imageUrl = null;

                    if (!TextUtils.isEmpty(avatarUrl)) {
                        imageUrl = "https:" + avatarUrl;
                    }

                    ImageViewUtil.setImageView(context, headPortraitCIV, imageUrl, R.mipmap.ic_default_avatar);

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
                        genderIv.setBackgroundResource(R.mipmap.ic_female_white);
                        genderIv.setVisibility(View.VISIBLE);
                    } else {
                        genderIv.setVisibility(View.GONE);
                    }

                    if (null != birthdayDate) {
                        int age = AgeUtils.getAge(birthdayDate);
                        ageTv.setText(String.valueOf(age) + "岁");
                        ageTv.setVisibility(View.VISIBLE);
                    } else {
                        ageTv.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(loginTimeTip)) {
                        holder.setTextView(R.id.tv_time, loginTimeTip);
                        timeTv.setVisibility(View.VISIBLE);
                    } else {
                        timeTv.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(currentStateTip)) {
                        statusTv.setText(currentStateTip);
                        statusTv.setVisibility(View.VISIBLE);
                    } else {
                        statusTv.setVisibility(View.GONE);
                    }

//                    if (price > 0) {
//                        holder.setTextView(R.id.tv_bid_price, "￥" + price);
//                        bidPriceLL.setVisibility(View.VISIBLE);
//                    } else {
//                        bidPriceLL.setVisibility(View.GONE);
//                    }

//                    if (!TextUtils.isEmpty(advantage)) {
//                        holder.setTextView(R.id.tv_advantage, advantage);
//                        advantageLL.setVisibility(View.VISIBLE);
//                    } else {
//                        advantageLL.setVisibility(View.GONE);
//                    }

//                    if (null != bidDate) {
//                        String date = CommonConstant.commonTimeFormat.format(bidDate);
//                        holder.setTextView(R.id.tv_bid_time, date);
//                        bidTimeLL.setVisibility(View.VISIBLE);
//                    } else {
//                        bidTimeLL.setVisibility(View.GONE);
//                    }

                    if (!TextUtils.isEmpty(tip)) {
                        tip = tip.replace(CommonConstant.NEWLINE, tip);
                        contentTv.setText(tip);
                        contentTv.setVisibility(View.VISIBLE);
                    } else {
                        contentTv.setVisibility(View.GONE);
                    }

                    holder.setTextView(R.id.tv_first, context.getString(R.string.chat));
                    holder.setTextView(R.id.tv_second, context.getString(R.string.pay));

                    firstLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_USER_ID, String.valueOf(sellerUserId));
                            context.startActivity(intent);
                        }
                    });

                    secondLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != onClickListener) {
                                onClickListener.onPayClick(sellerUserId, bidId, name, price, earnestMoney);
                            }
                        }
                    });

                    holder.getItemView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, RecommendDetailsActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_USER_ID, sellerUserId);
                            context.startActivity(intent);
                        }
                    });
                }
                break;

            case CommonConstant.DEMAND_STATUS_UNDERWAY:
                DemandUnderwayListBean.DemandUnderwayBean demandUnderwayBean = (DemandUnderwayListBean.DemandUnderwayBean) getItem(position);

                if (null != demandUnderwayBean) {
                    final int demandId = TypeUtils.getInteger(demandUnderwayBean.demandId, -1);
                    final int sellerUserId = TypeUtils.getInteger(demandUnderwayBean.sellerUserId, -1);
                    final int buyerUserId = TypeUtils.getInteger(demandUnderwayBean.buyerUserId, -1);
                    final int orderStatus = TypeUtils.getInteger(demandUnderwayBean.orderStatus, CommonConstant.ORDER_STATUS_ABNORMAL);
                    final int orderId = TypeUtils.getInteger(demandUnderwayBean.orderId, -1);
                    final int serveId = TypeUtils.getInteger(demandUnderwayBean.serveId, -1);
                    String avatarUrl = TypeUtils.getString(demandUnderwayBean.avatarUrl, "");
                    String name = TypeUtils.getString(demandUnderwayBean.name, "");
                    int gender = TypeUtils.getInteger(demandUnderwayBean.gender, 0);
                    Date birthdayDate = demandUnderwayBean.birthday;
                    String loginTimeTip = TypeUtils.getString(demandUnderwayBean.loginTimeTip, "");
                    String currentStateTip = TypeUtils.getString(demandUnderwayBean.currentStateTip, "");
                    String advantage = TypeUtils.getString(demandUnderwayBean.bidAdvantage, "");
                    String orderNo = TypeUtils.getString(demandUnderwayBean.orderNo, "");
                    Date bidDate = demandUnderwayBean.bidDate;
                    final Date startWorkAt = demandUnderwayBean.startWorkAt;
                    Date endWorkAt = demandUnderwayBean.endWorkAt;
                    boolean isDelayComplete = TypeUtils.getBoolean(demandUnderwayBean.isDelayComplete, false);
                    int delayDuration = TypeUtils.getInteger(demandUnderwayBean.delayDuration, 0);
                    final int totalPrice = TypeUtils.getInteger(demandUnderwayBean.totalPrice, 0);
                    boolean isCanShowApplyRefundButton = TypeUtils.getBoolean(demandUnderwayBean.isCanShowApplyRefundButton, false);
                    String refundTip = TypeUtils.getString(demandUnderwayBean.refundTip, "");
                    boolean isCanShowApplyArbitrationButton = TypeUtils.getBoolean(demandUnderwayBean.isCanShowApplyArbitrationButton, false);
                    String tip = TypeUtils.getString(demandUnderwayBean.tip, "");
                    final boolean showEvaluationButton = TypeUtils.getBoolean(demandUnderwayBean.showEvaluationButton, false);
                    final boolean showEvaluationDetailsButton = TypeUtils.getBoolean(demandUnderwayBean.showEvaluationDetailsButton, false);

//                    newIv.setVisibility(View.GONE);
                    bidPriceLL.setVisibility(View.GONE);
                    advantageLL.setVisibility(View.GONE);
                    bidTimeLL.setVisibility(View.GONE);
                    orderNumberLL.setVisibility(View.VISIBLE);
                    workTimeLL.setVisibility(View.GONE);
                    paymentPriceLL.setVisibility(View.VISIBLE);
                    arbitrationLL.setVisibility(View.GONE);

                    String imageUrl = null;

                    if (!TextUtils.isEmpty(avatarUrl)) {
                        imageUrl = "https:" + avatarUrl;
                    }

                    ImageViewUtil.setImageView(context, headPortraitCIV, imageUrl, R.mipmap.ic_default_avatar);

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
                        genderIv.setBackgroundResource(R.mipmap.ic_female_white);
                        genderIv.setVisibility(View.VISIBLE);
                    } else {
                        genderIv.setVisibility(View.GONE);
                    }

                    if (null != birthdayDate) {
                        int age = AgeUtils.getAge(birthdayDate);
                        ageTv.setText(String.valueOf(age) + "岁");
                        ageTv.setVisibility(View.VISIBLE);
                    } else {
                        ageTv.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(loginTimeTip)) {
                        holder.setTextView(R.id.tv_time, loginTimeTip);
                        timeTv.setVisibility(View.VISIBLE);
                    } else {
                        timeTv.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(currentStateTip)) {
                        statusTv.setText(currentStateTip);
                        statusTv.setVisibility(View.VISIBLE);
                    } else {
                        statusTv.setVisibility(View.GONE);
                    }

//                    if (!TextUtils.isEmpty(advantage)) {
//                        holder.setTextView(R.id.tv_advantage, advantage);
//                        advantageLL.setVisibility(View.VISIBLE);
//                    } else {
//                        advantageLL.setVisibility(View.GONE);
//                    }

                    if (!TextUtils.isEmpty(orderNo)) {
                        holder.setTextView(R.id.tv_order_number, orderNo);
                        orderNumberLL.setVisibility(View.VISIBLE);
                    } else {
                        orderNumberLL.setVisibility(View.GONE);
                    }

//                    if (null != bidDate) {
//                        String date = CommonConstant.commonTimeFormat.format(bidDate);
//                        holder.setTextView(R.id.tv_bid_time, date);
//                        bidTimeLL.setVisibility(View.VISIBLE);
//                    } else {
//                        bidTimeLL.setVisibility(View.GONE);
//                    }

                    if (null != startWorkAt && null != endWorkAt) {
                        String startWorkDataStr = CommonConstant.commonDateFormat.format(startWorkAt);
                        String endWorkDataStr = CommonConstant.commonDateFormat.format(endWorkAt);
                        holder.setTextView(R.id.tv_work_time, startWorkDataStr + "~" + endWorkDataStr);

                        if (isDelayComplete) {
                            delayDayBtn.setVisibility(View.GONE);
                            delayCompletedTv.setVisibility(View.VISIBLE);
                        } else {
                            if (orderStatus == CommonConstant.ORDER_STATUS_WAIT_FOR_COMPLETE) {
                                delayDayBtn.setText("延长" + delayDuration + "天");
                                delayDayBtn.setVisibility(View.VISIBLE);
                            } else {
                                delayDayBtn.setVisibility(View.GONE);
                            }

                            delayCompletedTv.setVisibility(View.GONE);
                        }

                        workTimeLL.setVisibility(View.VISIBLE);
                    } else {
                        workTimeLL.setVisibility(View.GONE);
                    }

                    if (totalPrice >= 0) {
                        holder.setTextView(R.id.tv_payment_price, "￥" + String.valueOf(totalPrice));
                        paymentPriceTv.setVisibility(View.VISIBLE);
                    } else {
                        paymentPriceTv.setVisibility(View.GONE);
                    }

                    String hint = refundTip.replace(CommonConstant.NEWLINE, "\n");

                    if (!TextUtils.isEmpty(hint)) {
                        hintTv.setText(hint);
                        hintTv.setVisibility(View.VISIBLE);
                    } else {
                        hintTv.setVisibility(View.GONE);
                    }

                    if (isCanShowApplyArbitrationButton) {
                        applyForArbitramentBtn.setVisibility(View.VISIBLE);
                    } else {
                        applyForArbitramentBtn.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(hint) | isCanShowApplyArbitrationButton) {
                        arbitrationLL.setVisibility(View.VISIBLE);
                    } else {
                        arbitrationLL.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(tip)) {
                        tip = tip.replace(CommonConstant.NEWLINE, "\n");
                        holder.setTextView(R.id.tv_content, tip);
                    }

                    holder.setTextView(R.id.tv_first, context.getString(R.string.chat));

                    if (orderStatus == CommonConstant.ORDER_STATUS_ABNORMAL) {
                        applyForRefundBtn.setVisibility(View.GONE);
                        secondLL.setVisibility(View.GONE);
                    } else if (orderStatus == CommonConstant.ORDER_STATUS_WAIT_FOR_START) {
                        if (isCanShowApplyRefundButton) {
                            applyForRefundBtn.setVisibility(View.VISIBLE);
                        } else {
                            applyForRefundBtn.setVisibility(View.GONE);
                        }

                        secondLL.setVisibility(View.VISIBLE);
                        holder.setTextView(R.id.tv_second, context.getString(R.string.confirm_start));
                    } else if (orderStatus == CommonConstant.ORDER_STATUS_WAIT_FOR_COMPLETE) {
                        if (isCanShowApplyRefundButton) {
                            applyForRefundBtn.setVisibility(View.VISIBLE);
                        } else {
                            applyForRefundBtn.setVisibility(View.GONE);
                        }

                        secondLL.setVisibility(View.VISIBLE);
                        holder.setTextView(R.id.tv_second, context.getString(R.string.confirm_complete));
                    } else if (orderStatus == CommonConstant.ORDER_STATUS_COMPLETE) {
                        applyForRefundBtn.setVisibility(View.GONE);
                        secondLL.setVisibility(View.GONE);

                        if (showEvaluationButton) {
                            holder.setTextView(R.id.tv_second, context.getString(R.string.evaluate));
                            secondLL.setVisibility(View.VISIBLE);
                        } else if (showEvaluationDetailsButton) {
                            holder.setTextView(R.id.tv_second, context.getString(R.string.see_evaluate));
                            secondLL.setVisibility(View.VISIBLE);
                        }
                    }

                    delayDayBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogUtils.showAlertDialog(context, "确认延长完成时间", "你有一次延长完成时间的机会，可以延长7天时间。", context.getString(R.string.confirm_delay), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    if (null != onClickListener) {
                                        onClickListener.onDelayCompleteWorkClick(sellerUserId, orderId);
                                    }
                                }
                            }, context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    });

                    applyForRefundBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != onClickListener) {
                                onClickListener.onApplyForRefundClick(sellerUserId, orderStatus, orderId, totalPrice);
                            }
                        }
                    });

                    applyForArbitramentBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != onClickListener) {
                                onClickListener.onApplyForArbitramentClick(sellerUserId, orderId);
                            }
                        }
                    });

                    firstLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_USER_ID, String.valueOf(sellerUserId));
                            context.startActivity(intent);
                        }
                    });

                    secondLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (orderStatus == CommonConstant.ORDER_STATUS_WAIT_FOR_START) {
                                DialogUtils.showAlertDialog(context, "确认开始服务", "从当前确认算起，按照项目周期计算结束时间。", context.getString(R.string.confirm_start), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        if (null != onClickListener) {
                                            onClickListener.onConfirmStartWorkClick(sellerUserId, orderId, startWorkAt);
                                        }
                                    }
                                }, context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                            } else if (orderStatus == CommonConstant.ORDER_STATUS_WAIT_FOR_COMPLETE) {
                                DialogUtils.showAlertDialog(context, context.getString(R.string.confirm_complete), "服务商完成工作就可以确认完成了！", context.getString(R.string.confirm_complete), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        if (null != onClickListener) {
                                            onClickListener.onConfirmCompleteWorkClick(sellerUserId, orderId);
                                        }
                                    }
                                }, context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                            } else if (orderStatus == CommonConstant.ORDER_STATUS_COMPLETE) {
                                if (null != onClickListener) {
                                    if (showEvaluationButton) {
                                        onClickListener.onEvaluateClick(sellerUserId, orderId, serveId, buyerUserId, sellerUserId);
                                    } else if (showEvaluationDetailsButton) {
                                        onClickListener.onSeeEvaluateClick(sellerUserId, orderId);
                                    }
                                }
                            }
                        }
                    });

                    holder.getItemView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, RecommendDetailsActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_USER_ID, sellerUserId);
                            context.startActivity(intent);
                        }
                    });
                }
                break;

            case CommonConstant.DEMAND_STATUS_COMPLETED:
                final DemandCompletedListBean.DemandCompletedBean demandCompletedBean = (DemandCompletedListBean.DemandCompletedBean) getItem(position);

                if (null != demandCompletedBean) {
                    final int demandId = TypeUtils.getInteger(demandCompletedBean.demandId, -1);
                    final int sellerUserId = TypeUtils.getInteger(demandCompletedBean.sellerUserId, -1);
                    final int buyerUserId = TypeUtils.getInteger(demandCompletedBean.buyerUserId, -1);
                    final int orderStatus = TypeUtils.getInteger(demandCompletedBean.orderStatus, CommonConstant.ORDER_STATUS_ABNORMAL);
                    final int orderId = TypeUtils.getInteger(demandCompletedBean.orderId, -1);
                    final int serveId = TypeUtils.getInteger(demandCompletedBean.serveId, -1);
                    String avatarUrl = TypeUtils.getString(demandCompletedBean.avatarUrl, "");
                    String name = TypeUtils.getString(demandCompletedBean.name, "");
                    int gender = TypeUtils.getInteger(demandCompletedBean.gender, 0);
                    Date birthdayDate = demandCompletedBean.birthday;
                    String loginTimeTip = TypeUtils.getString(demandCompletedBean.loginTimeTip, "");
                    String currentStateTip = TypeUtils.getString(demandCompletedBean.currentStateTip, "");
                    String advantage = TypeUtils.getString(demandCompletedBean.bidAdvantage, "");
                    String orderNo = TypeUtils.getString(demandCompletedBean.orderNo, "");
                    Date bidDate = demandCompletedBean.bidDate;
                    final Date startWorkAt = demandCompletedBean.startWorkAt;
                    Date endWorkAt = demandCompletedBean.endWorkAt;
                    String delayCompleteTip = TypeUtils.getString(demandCompletedBean.delayCompleteTip, "");
                    int totalPrice = TypeUtils.getInteger(demandCompletedBean.totalPrice, 0);
                    String priceTip = TypeUtils.getString(demandCompletedBean.priceTip, "");
                    String tip = TypeUtils.getString(demandCompletedBean.tip, "");
                    final boolean showEvaluationButton = TypeUtils.getBoolean(demandCompletedBean.showEvaluationButton, false);
                    final boolean showEvaluationDetailsButton = TypeUtils.getBoolean(demandCompletedBean.showEvaluationDetailsButton, false);

                    bidPriceLL.setVisibility(View.GONE);
                    advantageLL.setVisibility(View.GONE);
                    bidTimeLL.setVisibility(View.GONE);
                    orderNumberLL.setVisibility(View.VISIBLE);
                    workTimeLL.setVisibility(View.GONE);
                    paymentPriceLL.setVisibility(View.VISIBLE);
                    arbitrationLL.setVisibility(View.GONE);

                    String imageUrl = null;

                    if (!TextUtils.isEmpty(avatarUrl)) {
                        imageUrl = "https:" + avatarUrl;
                    }

                    ImageViewUtil.setImageView(context, headPortraitCIV, imageUrl, R.mipmap.ic_default_avatar);

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
                        genderIv.setBackgroundResource(R.mipmap.ic_female_white);
                        genderIv.setVisibility(View.VISIBLE);
                    } else {
                        genderIv.setVisibility(View.GONE);
                    }

                    if (null != birthdayDate) {
                        int age = AgeUtils.getAge(birthdayDate);
                        ageTv.setText(String.valueOf(age) + "岁");
                        ageTv.setVisibility(View.VISIBLE);
                    } else {
                        ageTv.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(loginTimeTip)) {
                        holder.setTextView(R.id.tv_time, loginTimeTip);
                        timeTv.setVisibility(View.VISIBLE);
                    } else {
                        timeTv.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(currentStateTip)) {
                        statusTv.setText(currentStateTip);
                        statusTv.setVisibility(View.VISIBLE);
                    } else {
                        statusTv.setVisibility(View.GONE);
                    }

//                    if (!TextUtils.isEmpty(advantage)) {
//                        holder.setTextView(R.id.tv_advantage, advantage);
//                        advantageLL.setVisibility(View.VISIBLE);
//                    } else {
//                        advantageLL.setVisibility(View.GONE);
//                    }

                    if (!TextUtils.isEmpty(orderNo)) {
                        holder.setTextView(R.id.tv_order_number, orderNo);
                        orderNumberLL.setVisibility(View.VISIBLE);
                    } else {
                        orderNumberLL.setVisibility(View.GONE);
                    }

                    if (null != startWorkAt && null != endWorkAt) {
                        String startWorkDataStr = CommonConstant.commonTimeFormat.format(startWorkAt);
                        String endWorkDataStr = CommonConstant.commonTimeFormat.format(endWorkAt);
                        holder.setTextView(R.id.tv_work_time, startWorkDataStr + "~" + endWorkDataStr);

                        workTimeLL.setVisibility(View.VISIBLE);
                    } else {
                        workTimeLL.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(delayCompleteTip)) {
                        delayDayBtn.setText(delayCompleteTip);
                        delayDayBtn.setVisibility(View.VISIBLE);
                    } else {
                        delayDayBtn.setVisibility(View.GONE);
                    }

                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("（");
                    spannableStringBuilder.append(priceTip).append("）");
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.text_red)), 0, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.setTextView(R.id.tv_payment_price, "￥" + String.valueOf(totalPrice) + spannableStringBuilder.toString());

                    if (!TextUtils.isEmpty(tip)) {
                        tip = tip.replace(CommonConstant.NEWLINE, "\n");
                        holder.setTextView(R.id.tv_content, tip);
                    }

                    holder.setTextView(R.id.tv_first, "聊一聊");

                    if (showEvaluationButton) {
                        holder.setTextView(R.id.tv_second, context.getString(R.string.evaluate_service));
                        secondLL.setVisibility(View.VISIBLE);
                    } else if (showEvaluationDetailsButton) {
                        holder.setTextView(R.id.tv_second, context.getString(R.string.see_evaluate));
                        secondLL.setVisibility(View.VISIBLE);
                    } else {
                        secondLL.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(tip)) {
                        tip = tip.replace(CommonConstant.NEWLINE, "\n");
                        contentTv.setText(tip);
                        contentTv.setVisibility(View.VISIBLE);
                    } else {
                        contentTv.setVisibility(View.GONE);
                    }

                    firstLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_USER_ID, String.valueOf(sellerUserId));
                            context.startActivity(intent);
                        }
                    });

                    secondLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != onClickListener) {
                                if (showEvaluationButton) {
                                    onClickListener.onEvaluateClick(sellerUserId, orderId, serveId, buyerUserId, sellerUserId);
                                } else if (showEvaluationDetailsButton) {
                                    onClickListener.onSeeEvaluateClick(sellerUserId, orderId);
                                }
                            }
                        }
                    });

                    holder.getItemView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, RecommendDetailsActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_USER_ID, sellerUserId);
                            context.startActivity(intent);
                        }
                    });
                }
                break;

            case CommonConstant.DEMAND_STATUS_EXPIRED:
                DemandExpiredListBean.DemandExpiredBean demandExpiredBean = (DemandExpiredListBean.DemandExpiredBean) getItem(position);

                if (null != demandExpiredBean) {
                    final int sellerUserId = TypeUtils.getInteger(demandExpiredBean.sellerUserId, -1);
                    String avatarUrl = TypeUtils.getString(demandExpiredBean.avatarUrl, "");
                    String name = TypeUtils.getString(demandExpiredBean.name, "");
                    int gender = TypeUtils.getInteger(demandExpiredBean.gender, 0);
                    Date birthdayDate = demandExpiredBean.birthday;
                    String loginTimeTip = TypeUtils.getString(demandExpiredBean.loginTimeTip, "");
                    String currentStateTip = TypeUtils.getString(demandExpiredBean.currentStateTip, "");
                    String advantage = TypeUtils.getString(demandExpiredBean.advantage, "");
                    Date bidDate = demandExpiredBean.bidDate;

//                    newIv.setVisibility(View.GONE);
                    bidPriceLL.setVisibility(View.GONE);
                    advantageLL.setVisibility(View.GONE);
                    bidTimeLL.setVisibility(View.GONE);
                    orderNumberLL.setVisibility(View.GONE);
                    workTimeLL.setVisibility(View.GONE);
                    paymentPriceLL.setVisibility(View.GONE);
                    arbitrationLL.setVisibility(View.GONE);

                    String imageUrl = null;

                    if (!TextUtils.isEmpty(avatarUrl)) {
                        imageUrl = "https:" + avatarUrl;
                    }

                    ImageViewUtil.setImageView(context, headPortraitCIV, imageUrl, R.mipmap.ic_default_avatar);

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
                        genderIv.setBackgroundResource(R.mipmap.ic_female_white);
                        genderIv.setVisibility(View.VISIBLE);
                    } else {
                        genderIv.setVisibility(View.GONE);
                    }

                    if (null != birthdayDate) {
                        int age = AgeUtils.getAge(birthdayDate);
                        ageTv.setText(String.valueOf(age) + "岁");
                        ageTv.setVisibility(View.VISIBLE);
                    } else {
                        ageTv.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(loginTimeTip)) {
                        holder.setTextView(R.id.tv_time, loginTimeTip);
                        timeTv.setVisibility(View.VISIBLE);
                    } else {
                        timeTv.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(currentStateTip)) {
                        statusTv.setText(currentStateTip);
                        statusTv.setVisibility(View.VISIBLE);
                    } else {
                        statusTv.setVisibility(View.GONE);
                    }

//                    if (!TextUtils.isEmpty(advantage)) {
//                        holder.setTextView(R.id.tv_advantage, advantage);
//                        advantageLL.setVisibility(View.VISIBLE);
//                    } else {
//                        advantageLL.setVisibility(View.GONE);
//                    }

//                    if (null != bidDate) {
//                        String date = CommonConstant.commonTimeFormat.format(bidDate);
//                        holder.setTextView(R.id.tv_bid_time, date);
//
//                        bidTimeLL.setVisibility(View.VISIBLE);
//                    } else {
//                        bidTimeLL.setVisibility(View.GONE);
//                    }

                    holder.setTextView(R.id.tv_first, context.getString(R.string.chat));

                    contentTv.setVisibility(View.GONE);
                    secondLL.setVisibility(View.GONE);

                    firstLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_USER_ID, String.valueOf(sellerUserId));
                            context.startActivity(intent);
                        }
                    });

                    holder.getItemView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, RecommendDetailsActivity.class);
                            intent.putExtra(CommonConstant.EXTRA_USER_ID, sellerUserId);
                            context.startActivity(intent);
                        }
                    });
                }
                break;
        }
    }

    public void setDemandStatus(int demandStatus) {
        this.demandStatus = demandStatus;
    }

    public interface OnClickListener {

        void onPayClick(int userId, int serviceId, String sellerName, int paymentMoney, int earnestMoney);

        void onApplyForRefundClick(int userId, int orderStatus, int orderId, int orderPrice);

        void onApplyForArbitramentClick(int userId, int orderId);

        void onConfirmStartWorkClick(int userId, int orderId, Date startWorkDate);

        void onConfirmCompleteWorkClick(int userId, int orderId);

        void onDelayCompleteWorkClick(int userId, int orderId);

        void onEvaluateClick(int userId, int orderId, int serviceId, int buyerUserId, int sellerUserId);

        void onSeeEvaluateClick(int userId, int orderId);

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
