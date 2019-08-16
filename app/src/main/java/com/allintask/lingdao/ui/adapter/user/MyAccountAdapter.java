package com.allintask.lingdao.ui.adapter.user;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.TransactionRecordListBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;

import java.text.DecimalFormat;
import java.util.Date;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/2/26.
 */

public class MyAccountAdapter extends CommonRecyclerViewAdapter {

    private static final int ITEM_MY_ACCOUNT_HEADER = 0;
    private static final int ITEM_MY_ACCOUNT = 1;

    private Context context;

    private double accountBalance;
    private double advanceRecharge;
    private double canWithdraw;
    private boolean isShowRechargeButton = false;
    private boolean isShowWithdrawButton = false;
    private double withdrawLowPrice;
    private double withdrawRate;

    private OnClickListener onClickListener;

    public MyAccountAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = null;

        switch (viewType) {
            case ITEM_MY_ACCOUNT_HEADER:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_account_header, parent, false));
                break;

            case ITEM_MY_ACCOUNT:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_account, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        switch (getItemViewType(position)) {
            case ITEM_MY_ACCOUNT_HEADER:
                onBindMyAccountHeaderItemView(holder);
                break;

            case ITEM_MY_ACCOUNT:
                onBindMyAccountItemView(holder, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_MY_ACCOUNT_HEADER;
        } else {
            return ITEM_MY_ACCOUNT;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    private void onBindMyAccountHeaderItemView(CommonRecyclerViewHolder holder) {
        final TextView accountBalanceTv = holder.getChildView(R.id.tv_account_balance);
        TextView myIncomeTv = holder.getChildView(R.id.tv_my_income);
        TextView myRechargeTv = holder.getChildView(R.id.tv_my_recharge);
        RelativeLayout rechargeRL = holder.getChildView(R.id.rl_recharge);
        RelativeLayout withdrawDepositRL = holder.getChildView(R.id.rl_withdraw_deposit);

        final DecimalFormat decimalFormat = new DecimalFormat("#0.00");

        ValueAnimator valueAnimator = ValueAnimator.ofFloat((float) accountBalance);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                String accountBalance = decimalFormat.format(animatedValue);
                accountBalanceTv.setText(accountBalance);
            }
        });

        StringBuilder canWithdrawSB = new StringBuilder(context.getString(R.string.my_income)).append("￥").append(decimalFormat.format(canWithdraw));
        myIncomeTv.setText(canWithdrawSB);

        StringBuilder canRechargeSB = new StringBuilder(context.getString(R.string.my_recharge)).append("￥").append(decimalFormat.format(advanceRecharge));
        myRechargeTv.setText(canRechargeSB);

        if (isShowRechargeButton) {
            rechargeRL.setVisibility(View.VISIBLE);
        } else {
            rechargeRL.setVisibility(View.GONE);
        }

        if (isShowWithdrawButton) {
            withdrawDepositRL.setVisibility(View.VISIBLE);
        } else {
            withdrawDepositRL.setVisibility(View.GONE);
        }

        withdrawDepositRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onClickListener) {
                    onClickListener.onWithdrawDepositClick();
                }
            }
        });
    }

    private void onBindMyAccountItemView(CommonRecyclerViewHolder holder, int position) {
        TransactionRecordListBean.TransactionRecordBean transactionRecordBean = (TransactionRecordListBean.TransactionRecordBean) getItem(position - 1);

        if (null != transactionRecordBean) {
            TextView timeTv = holder.getChildView(R.id.tv_time);
            TextView moneyTv = holder.getChildView(R.id.tv_money);

            String tradeRemark = TypeUtils.getString(transactionRecordBean.tradeRemark, "");
            Date createAt = transactionRecordBean.createAt;
            int tradeType = TypeUtils.getInteger(transactionRecordBean.tradeType, -1);
            double totalAmount = TypeUtils.getDouble(transactionRecordBean.totalAmount, 0D);

            holder.setTextView(R.id.tv_remark, tradeRemark, true);

            if (null != createAt) {
                String createTime = CommonConstant.commonTimeFormat.format(createAt);
                timeTv.setText(createTime);
                timeTv.setVisibility(View.VISIBLE);
            } else {
                timeTv.setVisibility(View.GONE);
            }

            DecimalFormat decimalFormat = new DecimalFormat("#0.00");
            String totalAmountString = decimalFormat.format(totalAmount);

            if (tradeType == 0) {
                StringBuilder stringBuilder = new StringBuilder("-").append(totalAmountString).append("元");
                moneyTv.setText(stringBuilder);
                moneyTv.setTextColor(context.getResources().getColor(R.color.theme_orange));
                moneyTv.setVisibility(View.VISIBLE);
            } else if (tradeType == 1) {
                StringBuilder stringBuilder = new StringBuilder("+").append(totalAmountString).append("元");
                moneyTv.setText(stringBuilder);
                moneyTv.setTextColor(context.getResources().getColor(R.color.text_red));
                moneyTv.setVisibility(View.VISIBLE);
            } else {
                moneyTv.setVisibility(View.GONE);
            }
        }
    }

    public void setAccountData(double accountBalance, double canWithdraw, double advanceRecharge, boolean isShowRechargeButton, boolean isShowWithdrawButton, double withdrawLowPrice, double withdrawRate) {
        this.accountBalance = accountBalance;
        this.canWithdraw = canWithdraw;
        this.advanceRecharge = advanceRecharge;
        this.isShowRechargeButton = isShowRechargeButton;
        this.isShowWithdrawButton = isShowWithdrawButton;
        this.withdrawLowPrice = withdrawLowPrice;
        this.withdrawRate = withdrawRate;

        notifyDataSetChanged();
    }

    public interface OnClickListener {

        void onWithdrawDepositClick();

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
