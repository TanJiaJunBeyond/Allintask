package com.allintask.lingdao.ui.adapter.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.service.ServiceManagementHeaderBean;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/1/15.
 */

public class ServiceManagementHeaderAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    public ServiceManagementHeaderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_service_management_header, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        ServiceManagementHeaderBean serviceManagementHeaderBean = (ServiceManagementHeaderBean) getItem(position);

        if (null != serviceManagementHeaderBean) {
            ImageView iconIv = holder.getChildView(R.id.iv_icon);

            String imageUrl = TypeUtils.getString(serviceManagementHeaderBean.imageUrl, "");
            String title = TypeUtils.getString(serviceManagementHeaderBean.title, "");

            ImageViewUtil.setImageView(context, iconIv, imageUrl, R.mipmap.ic_default, true);
            holder.setTextView(R.id.tv_title, title);
        }
    }

}
