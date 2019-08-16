package com.allintask.lingdao.ui.adapter.service;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.service.GetIdToChineseListBean;
import com.allintask.lingdao.bean.service.ServiceHasBidListBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.activity.message.ChatActivity;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.utils.WindowUtils;
import com.allintask.lingdao.widget.CircleImageView;
import com.allintask.lingdao.widget.ModifyServicePriceDialog;

import java.util.Date;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/1/15.
 */

public class ServiceStatusHasBidAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    private List<GetIdToChineseListBean.GetIdToChineseBean> categoryList;
    private List<GetIdToChineseListBean.GetIdToChineseBean> serviceModeList;

    private ModifyServicePriceDialog modifyServicePriceDialog;

    private OnClickListener onClickListener;

    public ServiceStatusHasBidAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_service_status_has_bid, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        ServiceHasBidListBean.ServiceHasBidBean serviceHasBidBean = (ServiceHasBidListBean.ServiceHasBidBean) getItem(position);

        if (null != serviceHasBidBean) {
            LinearLayout serviceStatusHasBidLL = holder.getChildView(R.id.ll_service_status_has_bid);
            CircleImageView headPortrait = holder.getChildView(R.id.civ_head_portrait);
            TextView nameTv = holder.getChildView(R.id.tv_name);
            TextView timeTv = holder.getChildView(R.id.tv_time);
            TextView serviceModeTv = holder.getChildView(R.id.tv_service_mode);
            ImageView trusteeshipIv = holder.getChildView(R.id.iv_trusteeship);
            TextView earnestMoneyTv = holder.getChildView(R.id.tv_trusteeship_amount);
            TextView publishTimeTv = holder.getChildView(R.id.tv_publish_time);
            LinearLayout bidPriceLL = holder.getChildView(R.id.ll_bid_price);
            TextView bidPriceTv = holder.getChildView(R.id.tv_bid_price);
            LinearLayout heOrSheAdvantageLL = holder.getChildView(R.id.ll_he_or_she_advantage);
            LinearLayout bidTimeLL = holder.getChildView(R.id.ll_bid_time);
            LinearLayout contentLL = holder.getChildView(R.id.ll_content);
            LinearLayout chatLL = holder.getChildView(R.id.ll_chat);
            LinearLayout modifyPriceLL = holder.getChildView(R.id.ll_modify_price);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WindowUtils.getScreenWidth(context), LinearLayout.LayoutParams.WRAP_CONTENT);
            serviceStatusHasBidLL.setLayoutParams(layoutParams);

            final int buyerUserId = TypeUtils.getInteger(serviceHasBidBean.buyerUserId, -1);
            final int demandId = TypeUtils.getInteger(serviceHasBidBean.demandId, -1);
            int serveId = TypeUtils.getInteger(serviceHasBidBean.serveId, -1);
            final int bidId = TypeUtils.getInteger(serviceHasBidBean.bidId, -1);
            String avatarUrl = TypeUtils.getString(serviceHasBidBean.avatarUrl, "");
            int categoryId = TypeUtils.getInteger(serviceHasBidBean.categoryId, -1);
            String name = TypeUtils.getString(serviceHasBidBean.name, "");
            String loginTimeTip = TypeUtils.getString(serviceHasBidBean.loginTimeTip, "");
            int serveWayId = TypeUtils.getInteger(serviceHasBidBean.serveWayId, -1);
            String demandPrice = TypeUtils.getString(serviceHasBidBean.demandPrice, "");
            int salaryTrusteeship = TypeUtils.getInteger(serviceHasBidBean.salaryTrusteeship, 0);
            String demandPublishTimeTip = TypeUtils.getString(serviceHasBidBean.demandPublishTimeTip, "");
            final int price = TypeUtils.getInteger(serviceHasBidBean.price, 0);
            String currentStateTip = TypeUtils.getString(serviceHasBidBean.currentStateTip, "");
            String advantage = TypeUtils.getString(serviceHasBidBean.advantage, "");
            Date bidDate = serviceHasBidBean.bidDate;
            String tip = TypeUtils.getString(serviceHasBidBean.tip, "");

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

            holder.setTextView(R.id.tv_status, currentStateTip, true);

            if (!TextUtils.isEmpty(demandPublishTimeTip)) {
                holder.setTextView(R.id.tv_publish_time, demandPublishTimeTip);
                publishTimeTv.setVisibility(View.VISIBLE);
            } else {
                publishTimeTv.setVisibility(View.GONE);
            }

            if (price != 0) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("￥");
                spannableStringBuilder.append(String.valueOf(price)).append("（等待支付）");
                spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.text_red)), String.valueOf(price).length() + 1, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                bidPriceTv.setText(spannableStringBuilder);

                bidPriceLL.setVisibility(View.VISIBLE);
            } else {
                bidPriceLL.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(advantage)) {
                holder.setTextView(R.id.tv_he_or_she_advantage_content, advantage);
                heOrSheAdvantageLL.setVisibility(View.VISIBLE);
            } else {
                heOrSheAdvantageLL.setVisibility(View.GONE);
            }

            if (null != bidDate) {
                String bidDateStr = CommonConstant.commonTimeFormat.format(bidDate);
                holder.setTextView(R.id.tv_bid_time, bidDateStr);

                bidTimeLL.setVisibility(View.VISIBLE);
            } else {
                bidTimeLL.setVisibility(View.GONE);
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

            if (bidId != -1 && price != 0) {
                modifyPriceLL.setVisibility(View.VISIBLE);
            } else {
                modifyPriceLL.setVisibility(View.GONE);
            }

            modifyPriceLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showModifyServicePriceDialog(buyerUserId, demandId, bidId, String.valueOf(price));
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

    private void showModifyServicePriceDialog(final int userId, final int demandId, final int bidId, String originalPrice) {
        modifyServicePriceDialog = new ModifyServicePriceDialog(context, originalPrice);

        Window window = modifyServicePriceDialog.getWindow();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        modifyServicePriceDialog.show();
        modifyServicePriceDialog.setOnClickListener(new ModifyServicePriceDialog.OnClickListener() {
            @Override
            public void onConfirmModifyClick(String modifyPrice) {
                if (!TextUtils.isEmpty(modifyPrice)) {
                    if (null != onClickListener) {
                        onClickListener.onClickListener(userId, demandId, bidId, modifyPrice);
                    }
                } else {
                    Toast.makeText(context, "修改的价格不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void dismissModifyServicePriceDialog() {
        if (null != modifyServicePriceDialog) {
            modifyServicePriceDialog.dismiss();
        }
    }

    public interface OnClickListener {

        void onClickListener(int userId, int demandId, int bidId, String price);

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
