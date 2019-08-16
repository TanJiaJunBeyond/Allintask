package com.allintask.lingdao.ui.adapter.recommend;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.recommend.EvaluationListBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.widget.CircleImageView;

import java.util.Date;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/3/3.
 */

public class EvaluateAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    public EvaluateAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_evaluate, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        EvaluationListBean.EvaluationBean evaluationBean = (EvaluationListBean.EvaluationBean) getItem(position);

        if (null != evaluationBean) {
            CircleImageView headPortraitCIV = holder.getChildView(R.id.civ_head_portrait);
            LinearLayout overallMeritLL = holder.getChildView(R.id.ll_overall_merit);
            ImageView firstStarIv = holder.getChildView(R.id.iv_first_star);
            ImageView secondStarIv = holder.getChildView(R.id.iv_second_star);
            ImageView thirdStarIv = holder.getChildView(R.id.iv_third_star);
            ImageView fourthStarIv = holder.getChildView(R.id.iv_fourth_star);
            ImageView fifthStarIv = holder.getChildView(R.id.iv_fifth_star);

            String avatarUrl = TypeUtils.getString(evaluationBean.avatarUrl, "");
            String buyerName = TypeUtils.getString(evaluationBean.buyerName, "");
            String content = TypeUtils.getString(evaluationBean.content, "");
            long overallMerit = TypeUtils.getLong(evaluationBean.overallMerit, 0L);
            Date createAt = evaluationBean.createAt;

            String headPortraitUrl = null;

            if (!TextUtils.isEmpty(avatarUrl)) {
                headPortraitUrl = "https:" + avatarUrl;
            }

            ImageViewUtil.setImageView(context, headPortraitCIV, headPortraitUrl, R.mipmap.ic_default_avatar);

            holder.setTextView(R.id.tv_name, buyerName);
            holder.setTextView(R.id.tv_content, content);

            if (overallMerit == 0L) {
                overallMeritLL.setVisibility(View.GONE);
            } else {
                if (overallMerit == 1L) {
                    firstStarIv.setBackgroundResource(R.mipmap.ic_selected_small_star);
                    secondStarIv.setBackgroundResource(R.mipmap.ic_unselected_small_star);
                    thirdStarIv.setBackgroundResource(R.mipmap.ic_unselected_small_star);
                    fourthStarIv.setBackgroundResource(R.mipmap.ic_unselected_small_star);
                    fifthStarIv.setBackgroundResource(R.mipmap.ic_unselected_small_star);
                } else if (overallMerit == 2L) {
                    firstStarIv.setBackgroundResource(R.mipmap.ic_selected_small_star);
                    secondStarIv.setBackgroundResource(R.mipmap.ic_selected_small_star);
                    thirdStarIv.setBackgroundResource(R.mipmap.ic_unselected_small_star);
                    fourthStarIv.setBackgroundResource(R.mipmap.ic_unselected_small_star);
                    fifthStarIv.setBackgroundResource(R.mipmap.ic_unselected_small_star);
                } else if (overallMerit == 3L) {
                    firstStarIv.setBackgroundResource(R.mipmap.ic_selected_small_star);
                    secondStarIv.setBackgroundResource(R.mipmap.ic_selected_small_star);
                    thirdStarIv.setBackgroundResource(R.mipmap.ic_selected_small_star);
                    fourthStarIv.setBackgroundResource(R.mipmap.ic_unselected_small_star);
                    fifthStarIv.setBackgroundResource(R.mipmap.ic_unselected_small_star);
                } else if (overallMerit == 4L) {
                    firstStarIv.setBackgroundResource(R.mipmap.ic_selected_small_star);
                    secondStarIv.setBackgroundResource(R.mipmap.ic_selected_small_star);
                    thirdStarIv.setBackgroundResource(R.mipmap.ic_selected_small_star);
                    fourthStarIv.setBackgroundResource(R.mipmap.ic_selected_small_star);
                    fifthStarIv.setBackgroundResource(R.mipmap.ic_unselected_small_star);
                } else if (overallMerit == 5L) {
                    firstStarIv.setBackgroundResource(R.mipmap.ic_selected_small_star);
                    secondStarIv.setBackgroundResource(R.mipmap.ic_selected_small_star);
                    thirdStarIv.setBackgroundResource(R.mipmap.ic_selected_small_star);
                    fourthStarIv.setBackgroundResource(R.mipmap.ic_selected_small_star);
                    fifthStarIv.setBackgroundResource(R.mipmap.ic_selected_small_star);
                }
            }

            if (null != createAt) {
                String createTime = CommonConstant.dateFormat.format(createAt);
                holder.setTextView(R.id.tv_date, createTime);
            }
        }
    }

}
