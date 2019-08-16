package com.allintask.lingdao.ui.adapter.recommend;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.recommend.RecommendGridBean;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/6/2.
 */

public class RecommendGridAdapter extends CommonRecyclerViewAdapter {

    private Context mContext;

    public RecommendGridAdapter(Context context) {
        mContext = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommonRecyclerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recommend_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    public void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        RecommendGridBean recommendGridBean = (RecommendGridBean) getItem(position);

        if (null != recommendGridBean) {
            ImageView iconIv = holder.getChildView(R.id.iv_icon);

            String homeIconUrl = TypeUtils.getString(recommendGridBean.homeIconUrl, "");
            String categoryName = TypeUtils.getString(recommendGridBean.categoryName, "");

            String imageUrl = null;

            if (!TextUtils.isEmpty(homeIconUrl)) {
                imageUrl = "https:" + homeIconUrl;
            }

            ImageViewUtil.setImageView(mContext, iconIv, imageUrl, R.mipmap.ic_default, true);
            holder.setTextView(R.id.tv_text, categoryName);
        }
    }

}
