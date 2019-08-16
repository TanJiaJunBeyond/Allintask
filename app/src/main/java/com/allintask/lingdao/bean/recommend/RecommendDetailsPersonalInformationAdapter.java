package com.allintask.lingdao.bean.recommend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.constant.CommonConstant;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;

import java.util.Date;

import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2018/3/28.
 */

public class RecommendDetailsPersonalInformationAdapter extends CommonRecyclerViewAdapter {

    private Context context;
    private int type;

    public RecommendDetailsPersonalInformationAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recommend_details_personal_information, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        TextView firstContentTv = holder.getChildView(R.id.tv_first_content);
        TextView secondContentTv = holder.getChildView(R.id.tv_second_content);
        TextView thirdContentTv = holder.getChildView(R.id.tv_third_content);
        TextView fourthContentTv = holder.getChildView(R.id.tv_fourth_content);

        switch (type) {
            case CommonConstant.EDUCATIONAL_EXPERIENCE:
                RecommendDetailsUserInformationBean.EducationalExperienceBean educationalExperienceBean = (RecommendDetailsUserInformationBean.EducationalExperienceBean) getItem(position);

                if (null != educationalExperienceBean) {
                    String school = TypeUtils.getString(educationalExperienceBean.school, "");
                    String major = TypeUtils.getString(educationalExperienceBean.major, "");
                    Date startAt = educationalExperienceBean.startAt;
                    Date endAt = educationalExperienceBean.endAt;
                    String educationLevelValue = TypeUtils.getString(educationalExperienceBean.educationLevelValue, "");

                    firstContentTv.setText(school);
                    secondContentTv.setText(major);

                    if (null != startAt && null != endAt) {
                        String startTime = CommonConstant.commonDateFormat.format(startAt);
                        String endTime = CommonConstant.commonDateFormat.format(endAt);

                        StringBuilder stringBuilder = new StringBuilder(startTime).append("~").append(endTime);
                        thirdContentTv.setText(stringBuilder);
                    }

                    fourthContentTv.setText(educationLevelValue);
                }
                break;

            case CommonConstant.WORK_EXPERIENCE:
                RecommendDetailsUserInformationBean.WorkExperienceBean workExperienceBean = (RecommendDetailsUserInformationBean.WorkExperienceBean) getItem(position);

                if (null != workExperienceBean) {
                    String company = TypeUtils.getString(workExperienceBean.company, "");
                    String post = TypeUtils.getString(workExperienceBean.post, "");
                    Date startAt = workExperienceBean.startAt;
                    Date endAt = workExperienceBean.endAt;

                    secondContentTv.setVisibility(View.GONE);
                    firstContentTv.setText(company);

                    if (null != startAt && null != endAt) {
                        String startTime = CommonConstant.commonDateFormat.format(startAt);
                        String endTime = CommonConstant.commonDateFormat.format(endAt);

                        StringBuilder stringBuilder = new StringBuilder(startTime).append("~").append(endTime);
                        thirdContentTv.setText(stringBuilder);
                    }

                    fourthContentTv.setText(post);
                }
                break;

            case CommonConstant.HONOR_AND_QUALIFICATION:
                RecommendDetailsUserInformationBean.HonorAndQualificationBean honorAndQualificationBean = (RecommendDetailsUserInformationBean.HonorAndQualificationBean) getItem(position);

                if (null != honorAndQualificationBean) {
                    String issuingAuthority = TypeUtils.getString(honorAndQualificationBean.issuingAuthority, "");
                    String prize = TypeUtils.getString(honorAndQualificationBean.prize, "");
                    Date gainAt = honorAndQualificationBean.gainAt;

                    secondContentTv.setVisibility(View.GONE);
                    firstContentTv.setText(issuingAuthority);

                    if (null != gainAt) {
                        String gainTime = CommonConstant.commonDateFormat.format(gainAt);
                        thirdContentTv.setText(gainTime);
                    }

                    fourthContentTv.setText(prize);
                }
                break;
        }
    }

}
