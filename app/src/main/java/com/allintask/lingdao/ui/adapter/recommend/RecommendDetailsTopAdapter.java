package com.allintask.lingdao.ui.adapter.recommend;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.recommend.RecommendDetailsTopBean;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/1/4.
 */

public class RecommendDetailsTopAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    public RecommendDetailsTopAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recommend_details_top, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        RecommendDetailsTopBean recommendDetailsTopBean = (RecommendDetailsTopBean) getItem(position);

        if (null != recommendDetailsTopBean) {
            ImageView myServiceIv = holder.getChildView(R.id.iv_my_service);
            TextView myServiceTv = holder.getChildView(R.id.tv_my_service);
            View selectedView = holder.getChildView(R.id.view_selected);

            String tempUnSelectedImageUrl = TypeUtils.getString(recommendDetailsTopBean.unSelectedImageUrl, "");
            String tempSelectedImageUrl = TypeUtils.getString(recommendDetailsTopBean.selectedImageUrl, "");
            String name = TypeUtils.getString(recommendDetailsTopBean.name, "");
            boolean isSelected = TypeUtils.getBoolean(recommendDetailsTopBean.isSelected, false);

            String unSelectedImageUrl = null;

            if (!TextUtils.isEmpty(tempUnSelectedImageUrl)) {
                unSelectedImageUrl = "https:" + tempUnSelectedImageUrl;
            }

            String selectedImageUrl = null;

            if (!TextUtils.isEmpty(tempSelectedImageUrl)) {
                selectedImageUrl = "https:" + tempSelectedImageUrl;
            }

            if (isSelected) {
                ImageViewUtil.setImageView(context, myServiceIv, selectedImageUrl, R.mipmap.ic_default);
                myServiceTv.setTextColor(context.getResources().getColor(R.color.theme_orange));
                selectedView.setVisibility(View.VISIBLE);
            } else {
                ImageViewUtil.setImageView(context, myServiceIv, unSelectedImageUrl, R.mipmap.ic_default);
                myServiceTv.setTextColor(context.getResources().getColor(R.color.text_dark_gray));
                selectedView.setVisibility(View.GONE);
            }

            myServiceTv.setText(name);
        }
    }

}
