package com.allintask.lingdao.ui.adapter.service;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.service.GetIdToChineseListBean;
import com.allintask.lingdao.bean.service.ServiceWaitBidListBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.activity.message.ChatActivity;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.utils.WindowUtils;
import com.allintask.lingdao.widget.BidToMakeMoneyAtOnceDialog;
import com.allintask.lingdao.widget.CircleImageView;

import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/1/15.
 */

public class ServiceStatusWaitBidAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    private List<GetIdToChineseListBean.GetIdToChineseBean> categoryList;
    private List<GetIdToChineseListBean.GetIdToChineseBean> serviceModeList;

    private BidToMakeMoneyAtOnceDialog bidToMakeMoneyAtOnceDialog;

    private OnClickListener onClickListener;

    public ServiceStatusWaitBidAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_service_status_wait_bid, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, final int position) {
        ServiceWaitBidListBean.ServiceWaitBidBean serviceWaitBidBean = (ServiceWaitBidListBean.ServiceWaitBidBean) getItem(position);

        if (null != serviceWaitBidBean) {
            LinearLayout serviceStatusWaitBidLL = holder.getChildView(R.id.ll_service_status_wait_bid);
            CircleImageView headPortraitCIV = holder.getChildView(R.id.civ_head_portrait);
            TextView nameTv = holder.getChildView(R.id.tv_name);
            TextView timeTv = holder.getChildView(R.id.tv_time);
            TextView bookTv = holder.getChildView(R.id.tv_book);
            TextView serviceModeTv = holder.getChildView(R.id.tv_service_mode);
            ImageView trusteeshipIv = holder.getChildView(R.id.iv_trusteeship);
            TextView earnestMoneyTv = holder.getChildView(R.id.tv_trusteeship_amount);
            TextView publishTimeTv = holder.getChildView(R.id.tv_publish_time);
            TextView deadTimeTv = holder.getChildView(R.id.tv_deadline_time);
            LinearLayout contentLL = holder.getChildView(R.id.ll_content);
            LinearLayout chatLL = holder.getChildView(R.id.ll_chat);
            LinearLayout bidToMakeMoneyLL = holder.getChildView(R.id.ll_bid_to_make_money);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WindowUtils.getScreenWidth(context), LinearLayout.LayoutParams.WRAP_CONTENT);
            serviceStatusWaitBidLL.setLayoutParams(layoutParams);

            final int buyerUserId = TypeUtils.getInteger(serviceWaitBidBean.buyerUserId, -1);
            String buyerAvatarUrl = TypeUtils.getString(serviceWaitBidBean.buyerAvatarUrl, "");
            int serveId = TypeUtils.getInteger(serviceWaitBidBean.serveId, -1);
            final int categoryId = TypeUtils.getInteger(serviceWaitBidBean.categoryId, -1);
            String buyName = TypeUtils.getString(serviceWaitBidBean.buyerName, "");
            int isBook = TypeUtils.getInteger(serviceWaitBidBean.isBook, 0);
            String loginTimeTip = TypeUtils.getString(serviceWaitBidBean.loginTimeTip, "");
            int serveWayId = TypeUtils.getInteger(serviceWaitBidBean.serveWayId, -1);
            int demandPrice = TypeUtils.getInteger(serviceWaitBidBean.demandPrice, 0);
            int salaryTrusteeship = TypeUtils.getInteger(serviceWaitBidBean.salaryTrusteeship, 0);
            String demandPublishTimeTip = TypeUtils.getString(serviceWaitBidBean.demandPublishTimeTip, "");
            String deadlineTimeTip = TypeUtils.getString(serviceWaitBidBean.deadlineTimeTip, "");
            String tip = TypeUtils.getString(serviceWaitBidBean.tip, "");

            String imageUrl = null;

            if (!TextUtils.isEmpty(buyerAvatarUrl)) {
                imageUrl = "https:" + buyerAvatarUrl;
            }

            ImageViewUtil.setImageView(context, headPortraitCIV, imageUrl, R.mipmap.ic_default_avatar);

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

            if (!TextUtils.isEmpty(buyName)) {
                nameTv.setText(buyName);
                nameTv.setVisibility(View.VISIBLE);
            } else {
                nameTv.setVisibility(View.GONE);
            }

            if (isBook == 0) {
                bookTv.setVisibility(View.GONE);
            } else if (isBook == 1) {
                bookTv.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(loginTimeTip)) {
                timeTv.setText(loginTimeTip);
                timeTv.setVisibility(View.VISIBLE);
            } else {
                timeTv.setVisibility(View.GONE);
            }

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

            if (!TextUtils.isEmpty(deadlineTimeTip)) {
                holder.setTextView(R.id.tv_deadline_time, deadlineTimeTip);
                deadTimeTv.setVisibility(View.VISIBLE);
            } else {
                deadTimeTv.setVisibility(View.GONE);
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

            bidToMakeMoneyLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onClickListener) {
                        onClickListener.onBidToMakeMoney(position, categoryId);
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

    public void showBidToMakeMoneyAtOnceDialog(final int position, String advantage) {
        bidToMakeMoneyAtOnceDialog = new BidToMakeMoneyAtOnceDialog(context, advantage);

        Window window = bidToMakeMoneyAtOnceDialog.getWindow();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.CENTER;

        if (null != window) {
            window.setAttributes(layoutParams);
        }

        bidToMakeMoneyAtOnceDialog.show();
        bidToMakeMoneyAtOnceDialog.setOnClickListener(new BidToMakeMoneyAtOnceDialog.OnClickListener() {
            @Override
            public void onCloseClick() {
                if (null != bidToMakeMoneyAtOnceDialog && bidToMakeMoneyAtOnceDialog.isShowing()) {
                    bidToMakeMoneyAtOnceDialog.dismiss();
                }
            }

            @Override
            public void onBidAtOnceClick(String myOffer, String myAdvantage) {
                if (null != onClickListener) {
                    ServiceWaitBidListBean.ServiceWaitBidBean serviceWaitBidBean = (ServiceWaitBidListBean.ServiceWaitBidBean) getItem(position);

                    if (null != serviceWaitBidBean) {
                        int buyerUserId = TypeUtils.getInteger(serviceWaitBidBean.buyerUserId, -1);
                        int serveId = TypeUtils.getInteger(serviceWaitBidBean.serveId, -1);
                        int demandId = TypeUtils.getInteger(serviceWaitBidBean.demandId, -1);
                        int isBook = TypeUtils.getInteger(serviceWaitBidBean.isBook, -1);

                        onClickListener.onBidAtOnceClick(bidToMakeMoneyAtOnceDialog, buyerUserId, serveId, demandId, myOffer, myAdvantage, isBook);
                    }
                }
            }
        });
    }

    public void dismissBidToMakeMoneyAtOnceDialog() {
        if (null != bidToMakeMoneyAtOnceDialog && bidToMakeMoneyAtOnceDialog.isShowing()) {
            bidToMakeMoneyAtOnceDialog.dismiss();
        }
    }

    public interface OnClickListener {

        void onBidToMakeMoney(int position, int categoryId);

        void onBidAtOnceClick(BidToMakeMoneyAtOnceDialog bidToMakeMoneyAtOnceDialog, int buyerUserId, int serveId, int demandId, String myOffer, String myAdvantage, int isBook);

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
