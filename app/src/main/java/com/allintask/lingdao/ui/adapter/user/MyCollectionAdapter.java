package com.allintask.lingdao.ui.adapter.user;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.MyCollectListBean;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.widget.CircleImageView;

import java.text.DecimalFormat;
import java.util.Date;

import cn.tanjiajun.sdk.common.utils.AgeUtils;
import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/2/26.
 */

public class MyCollectionAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    public MyCollectionAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_collection, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        MyCollectListBean.MyCollectBean myCollectBean = (MyCollectListBean.MyCollectBean) getItem(position);

        if (null != myCollectBean) {
            CircleImageView headPortraitCIV = holder.getChildView(R.id.civ_head_portrait);
            ImageView genderIv = holder.getChildView(R.id.iv_gender);
            TextView ageTv = holder.getChildView(R.id.tv_age);
            TextView workExperienceTv = holder.getChildView(R.id.tv_work_experience_year);
            TextView distanceTv = holder.getChildView(R.id.tv_distance);

            String avatarUrl = TypeUtils.getString(myCollectBean.avatarUrl, "");
            String name = TypeUtils.getString(myCollectBean.name, "");
            int gender = TypeUtils.getInteger(myCollectBean.gender, -1);
            Date birthday = myCollectBean.birthday;
            Date startWorkAt = myCollectBean.startWorkAt;
            int distance = TypeUtils.getInteger(myCollectBean.distance, 0);

            if (!TextUtils.isEmpty(avatarUrl)) {
                String headPortraitUrl = "https:" + avatarUrl;
                ImageViewUtil.setImageView(context, headPortraitCIV, headPortraitUrl, R.mipmap.ic_default_avatar);
                headPortraitCIV.setVisibility(View.VISIBLE);
            } else {
                headPortraitCIV.setVisibility(View.GONE);
            }

            holder.setTextView(R.id.tv_name, name, true);

            if (gender == CommonConstant.MALE) {
                genderIv.setImageResource(R.mipmap.ic_male);
                genderIv.setVisibility(View.VISIBLE);
            } else if (gender == CommonConstant.FEMALE) {
                genderIv.setImageResource(R.mipmap.ic_female);
                genderIv.setVisibility(View.VISIBLE);
            } else {
                genderIv.setVisibility(View.GONE);
            }

            if (null != birthday) {
                int age = AgeUtils.getAge(birthday);
                ageTv.setText(String.valueOf(age) + "岁");
                ageTv.setVisibility(View.VISIBLE);
            } else {
                ageTv.setVisibility(View.GONE);
            }

            if (null != startWorkAt) {
                int workExperienceYear = AgeUtils.getAge(startWorkAt);
                workExperienceTv.setText(String.valueOf(workExperienceYear) + "年经验");
                workExperienceTv.setVisibility(View.VISIBLE);
            } else {
                workExperienceTv.setVisibility(View.GONE);
            }

            String tempDistance = String.valueOf(distance);
            int distanceLength = tempDistance.length();

            if (distanceLength >= 4) {
                double distanceDouble = distance / 1000D;
                DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                String distanceString = decimalFormat.format(distanceDouble) + "km";
                distanceTv.setText(distanceString);
            } else {
                distanceTv.setText(String.valueOf(distance) + "m");
            }
        }
    }

}
