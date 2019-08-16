package com.allintask.lingdao.ui.adapter.user;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.CategoryBean;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.utils.WindowUtils;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/2/26.
 */

public class CategoryAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    public CategoryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        CategoryBean categoryBean = (CategoryBean) getItem(position);

        if (null != categoryBean) {
            RelativeLayout categoryRL = holder.getChildView(R.id.rl_category);
            TextView categoryTv = holder.getChildView(R.id.tv_category);
            ImageView tickIv = holder.getChildView(R.id.iv_tick);

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) categoryRL.getLayoutParams();
            layoutParams.width = WindowUtils.getScreenWidth(context);
            categoryRL.setLayoutParams(layoutParams);

            boolean isSelected = TypeUtils.getBoolean(categoryBean.isSelected, false);
            String name = TypeUtils.getString(categoryBean.name, "");

            Paint paint = categoryTv.getPaint();

            if (isSelected) {
                categoryTv.setTextColor(context.getResources().getColor(R.color.black));

                if (null != paint) {
                    paint.setFakeBoldText(true);
                }

                tickIv.setVisibility(View.VISIBLE);
            } else {
                categoryTv.setTextColor(context.getResources().getColor(R.color.text_light_black));

                if (null != paint) {
                    paint.setFakeBoldText(false);
                }

                tickIv.setVisibility(View.GONE);
            }

            categoryTv.setText(name);
        }
    }

}
