package com.allintask.lingdao.ui.adapter.service;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.service.GetIdToChineseListBean;
import com.allintask.lingdao.bean.service.ServiceExpiredListBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.activity.message.ChatActivity;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.utils.WindowUtils;
import com.allintask.lingdao.widget.CircleImageView;

import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/1/16.
 */

public class ServiceStatusExpiredAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    private List<GetIdToChineseListBean.GetIdToChineseBean> categoryList;
    private List<GetIdToChineseListBean.GetIdToChineseBean> serviceModeList;

    public ServiceStatusExpiredAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_service_status_expired, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        ServiceExpiredListBean.ServiceExpiredBean serviceExpiredBean = (ServiceExpiredListBean.ServiceExpiredBean) getItem(position);

        if (null != serviceExpiredBean) {
            LinearLayout serviceStatusExpiredLL = holder.getChildView(R.id.ll_service_status_expired);
            CircleImageView headPortrait = holder.getChildView(R.id.civ_head_portrait);
            TextView serviceModeTv = holder.getChildView(R.id.tv_service_mode);
            ImageView trusteeshipIv = holder.getChildView(R.id.iv_trusteeship);
            TextView publishTimeTv = holder.getChildView(R.id.tv_publish_time);
            LinearLayout contentLL = holder.getChildView(R.id.ll_content);
            LinearLayout chatLL = holder.getChildView(R.id.ll_chat);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WindowUtils.getScreenWidth(context), LinearLayout.LayoutParams.WRAP_CONTENT);
            serviceStatusExpiredLL.setLayoutParams(layoutParams);

            final int buyerUserId = TypeUtils.getInteger(serviceExpiredBean.buyerUserId, -1);
            String avatarUrl = TypeUtils.getString(serviceExpiredBean.avatarUrl, "");
            int categoryId = TypeUtils.getInteger(serviceExpiredBean.categoryId, -1);
            String name = TypeUtils.getString(serviceExpiredBean.name, "");
            String loginTimeTip = TypeUtils.getString(serviceExpiredBean.loginTimeTip, "");
            int serveWayId = TypeUtils.getInteger(serviceExpiredBean.serveWayId, -1);
            String demandPrice = TypeUtils.getString(serviceExpiredBean.demandPrice, "");
            int salaryTrusteeship = TypeUtils.getInteger(serviceExpiredBean.salaryTrusteeship, 0);
            String demandPublishTimeTip = TypeUtils.getString(serviceExpiredBean.demandPublishTimeTip, "");
            String tip = TypeUtils.getString(serviceExpiredBean.tip, "");

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

            if (salaryTrusteeship > 0) {
                trusteeshipIv.setVisibility(View.VISIBLE);
            } else {
                trusteeshipIv.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(demandPublishTimeTip)) {
                holder.setTextView(R.id.tv_publish_time, demandPublishTimeTip);
                publishTimeTv.setVisibility(View.VISIBLE);
            } else {
                publishTimeTv.setVisibility(View.GONE);
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
