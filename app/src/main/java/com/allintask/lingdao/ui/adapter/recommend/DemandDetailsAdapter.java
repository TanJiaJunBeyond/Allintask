package com.allintask.lingdao.ui.adapter.recommend;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.demand.ShowDemandDetailsBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/3/12.
 */

public class DemandDetailsAdapter extends CommonRecyclerViewAdapter {

    private static final int ITEM_DEMAND_DETAILS = 0;
    private static final int ITEM_DEMAND_DETAILS_ARROW = 1;

    private Context context;
    private List<ShowDemandDetailsBean> showDemandDetailsList;

    private boolean isShowMore = false;
    private int showMorePosition;

    public DemandDetailsAdapter(Context context) {
        this.context = context;
        showDemandDetailsList = new ArrayList<>();
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = null;

        switch (viewType) {
            case ITEM_DEMAND_DETAILS:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_demand_details, parent, false));
                break;

            case ITEM_DEMAND_DETAILS_ARROW:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_demand_details_arrow, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        switch (getItemViewType(position)) {
            case ITEM_DEMAND_DETAILS:
                onBindDemandDetailsItemView(holder, position);
                break;

            case ITEM_DEMAND_DETAILS_ARROW:
                onBindDemandDetailsArrowItemView(holder);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mList.size()) {
            return ITEM_DEMAND_DETAILS_ARROW;
        } else {
            return ITEM_DEMAND_DETAILS;
        }
    }

    private void onBindDemandDetailsItemView(CommonRecyclerViewHolder holder, int position) {
        ShowDemandDetailsBean showDemandDetailsBean = (ShowDemandDetailsBean) getItem(position);

        if (null != showDemandDetailsBean) {
            TextView titleTv = holder.getChildView(R.id.tv_title);
            final TextView contentTv = holder.getChildView(R.id.tv_content);

            String title = TypeUtils.getString(showDemandDetailsBean.title, "");
            String content = TypeUtils.getString(showDemandDetailsBean.content, "");
            int color = TypeUtils.getInteger(showDemandDetailsBean.color, -1);

            titleTv.setText(title);

            contentTv.post(new Runnable() {
                @Override
                public void run() {
                    int contentLineCount = contentTv.getLineCount();

                    if (contentLineCount == 1) {
                        contentTv.setGravity(Gravity.RIGHT);
                    } else {
                        contentTv.setGravity(Gravity.LEFT);
                    }
                }
            });

            if (color != -1) {
                if (color == context.getResources().getColor(R.color.text_orange)) {
                    titleTv.setTextColor(context.getResources().getColor(R.color.text_orange));
                    contentTv.setTextColor(context.getResources().getColor(R.color.text_orange));
                } else {
                    contentTv.setTextColor(color);
                }
            }

            contentTv.setText(content);
        }
    }

    private void onBindDemandDetailsArrowItemView(CommonRecyclerViewHolder holder) {
        RelativeLayout demandDetailsArrowRL = holder.getChildView(R.id.rl_demand_details_arrow);
        final ImageView arrowIv = holder.getChildView(R.id.iv_arrow);

        if (isShowMore) {
            arrowIv.setBackgroundResource(R.mipmap.ic_arrow_up);
        } else {
            arrowIv.setBackgroundResource(R.mipmap.ic_arrow_down);
        }

        demandDetailsArrowRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowMore) {
                    isShowMore = false;
                    arrowIv.setBackgroundResource(R.mipmap.ic_arrow_down);

                    for (int i = showDemandDetailsList.size() - 1; i >= showMorePosition; i--) {
                        mList.remove(i);
                    }

                    notifyDataSetChanged();
                } else {
                    isShowMore = true;
                    arrowIv.setBackgroundResource(R.mipmap.ic_arrow_up);

                    showMorePosition = mList.size();

                    for (int i = mList.size(); i < showDemandDetailsList.size(); i++) {
                        ShowDemandDetailsBean showDemandDetailsBean = showDemandDetailsList.get(i);
                        mList.add(showDemandDetailsBean);
                    }

                    notifyDataSetChanged();
                }
            }
        });
    }

    public void setShowDemandDetailsList(List<ShowDemandDetailsBean> showDemandDetailsList) {
        if (null != showDemandDetailsList && showDemandDetailsList.size() > 0) {
            this.showDemandDetailsList.clear();
            this.showDemandDetailsList.addAll(showDemandDetailsList);
        }
    }

    public void setIsShowMore(boolean isShowMore) {
        this.isShowMore = isShowMore;
    }

}
