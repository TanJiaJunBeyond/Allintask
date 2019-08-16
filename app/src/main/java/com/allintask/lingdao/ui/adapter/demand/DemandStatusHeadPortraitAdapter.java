package com.allintask.lingdao.ui.adapter.demand;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.demand.DemandStatusListBean;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.widget.CircleImageView;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/1/15.
 */

public class DemandStatusHeadPortraitAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    public DemandStatusHeadPortraitAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_demand_status_head_portrait, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        DemandStatusListBean.DemandStatusBean.UserMsgVosBean userMsgVosBean = (DemandStatusListBean.DemandStatusBean.UserMsgVosBean) getItem(position);

        if (null != userMsgVosBean) {
            CircleImageView headPortraitCIV = holder.getChildView(R.id.civ_head_portrait);

            String avatarUrl = TypeUtils.getString(userMsgVosBean.avatarUrl, "");
            String imageUrl = null;

            if (!TextUtils.isEmpty(avatarUrl)) {
                imageUrl = "https:" + avatarUrl;
            }

            ImageViewUtil.setImageView(context, headPortraitCIV, imageUrl, R.mipmap.ic_default_avatar);
        }
    }

}
