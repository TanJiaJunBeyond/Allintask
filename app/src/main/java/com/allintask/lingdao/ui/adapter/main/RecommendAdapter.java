package com.allintask.lingdao.ui.adapter.main;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.recommend.RecommendListBean;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.utils.WindowUtils;
import com.allintask.lingdao.widget.CircleImageView;
import com.allintask.lingdao.widget.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.DensityUtils;
import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/1/4.
 */

public class RecommendAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    public RecommendAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recommend, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, int position) {
        RecommendListBean.RecommendBean recommendBean = (RecommendListBean.RecommendBean) getItem(position);

        if (null != recommendBean) {
            CircleImageView headPortraitCIV = holder.getChildView(R.id.civ_head_portrait);
            TextView specialistRecommendTv = holder.getChildView(R.id.tv_specialist_recommend);
            ImageView specialistRecommendIv = holder.getChildView(R.id.iv_specialist_recommend);
            LinearLayout serviceAlbumLL = holder.getChildView(R.id.ll_service_album);
            ImageView firstServicePhotoIv = holder.getChildView(R.id.iv_first_service_photo);
            ImageView secondServicePhotoIv = holder.getChildView(R.id.iv_second_service_photo);
            ImageView thirdServicePhotoIv = holder.getChildView(R.id.iv_third_service_photo);
            FlowLayout flowLayout = holder.getChildView(R.id.flow_layout);

            String avatarUrl = TypeUtils.getString(recommendBean.avatarUrl, "");
            String realName = TypeUtils.getString(recommendBean.realName, "");
            int officialRecommendStatus = TypeUtils.getInteger(recommendBean.officialRecommendStatus, 0);
            String officialRecommendCopyWrite = TypeUtils.getString(recommendBean.officialRecommendCopyWrite, "");
            String officialRecommendIconUrl = TypeUtils.getString(recommendBean.officialRecommendIconUrl, "");
            String loginTimeTip = TypeUtils.getString(recommendBean.loginTimeTip, "");
            List<String> serveAlbumList = recommendBean.serveAlbumList;
            String categoryName = TypeUtils.getString(recommendBean.categoryName, "");
            int viewCount = TypeUtils.getInteger(recommendBean.viewCount, -1);
            List<String> serveWayPriceUnitChineseList = recommendBean.serveWayPriceUnitChineseList;
            String advantage = TypeUtils.getString(recommendBean.advantage, "");
            List<String> categoryPropertyValueChineseList = recommendBean.categoryPropertyValueChineseList;

            String headPortraitUrl = null;

            if (!TextUtils.isEmpty(avatarUrl)) {
                headPortraitUrl = "https:" + avatarUrl;
            }

            ImageViewUtil.setImageView(context, headPortraitCIV, headPortraitUrl, R.mipmap.ic_default_avatar);

            holder.setTextView(R.id.tv_name, realName, true);

            if (officialRecommendStatus == 0) {
                specialistRecommendTv.setVisibility(View.GONE);
            } else if (officialRecommendStatus == 1) {
                specialistRecommendTv.setText(officialRecommendCopyWrite);
                specialistRecommendTv.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(officialRecommendIconUrl)) {
                specialistRecommendIv.setVisibility(View.VISIBLE);

                String imageUrl = "https:" + officialRecommendIconUrl;
                ImageViewUtil.setImageView(context, specialistRecommendIv, imageUrl, R.mipmap.ic_default);
            } else {
                specialistRecommendIv.setVisibility(View.GONE);
            }

            holder.setTextView(R.id.tv_time, loginTimeTip, true);

            if (null != serveAlbumList && serveAlbumList.size() > 0) {
                int servicePhotoWidth = (WindowUtils.getScreenWidth(context) - 2 * DensityUtils.dip2px(context, 16) - 2 * DensityUtils.dip2px(context, 3)) / 3;

                serviceAlbumLL.setVisibility(View.VISIBLE);
                List<String> serviceAlbumList = new ArrayList<>();

                for (int i = 0; i < 3; i++) {
                    if (i < serveAlbumList.size()) {
                        String servicePhotoUrl = serveAlbumList.get(i);
                        serviceAlbumList.add(servicePhotoUrl);
                    } else {
                        serviceAlbumList.add("");
                    }
                }

                for (int i = 0; i < serviceAlbumList.size(); i++) {
                    String tempServicePhotoUrl = serviceAlbumList.get(i);

                    String servicePhotoUrl = null;

                    if (!TextUtils.isEmpty(tempServicePhotoUrl)) {
                        servicePhotoUrl = "https:" + tempServicePhotoUrl;
                    }

                    if (i == 0) {
                        if (!TextUtils.isEmpty(tempServicePhotoUrl)) {
                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) firstServicePhotoIv.getLayoutParams();
                            layoutParams.width = servicePhotoWidth;
                            layoutParams.height = servicePhotoWidth;
                            firstServicePhotoIv.setLayoutParams(layoutParams);

                            ImageViewUtil.setImageView(context, firstServicePhotoIv, servicePhotoUrl, R.mipmap.ic_default);
                            firstServicePhotoIv.setVisibility(View.VISIBLE);
                        } else {
                            firstServicePhotoIv.setVisibility(View.GONE);
                        }
                    }

                    if (i == 1) {
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) secondServicePhotoIv.getLayoutParams();
                        layoutParams.width = servicePhotoWidth;
                        layoutParams.height = servicePhotoWidth;
                        secondServicePhotoIv.setLayoutParams(layoutParams);

                        if (!TextUtils.isEmpty(tempServicePhotoUrl)) {
                            ImageViewUtil.setImageView(context, secondServicePhotoIv, servicePhotoUrl, R.mipmap.ic_default);
                            secondServicePhotoIv.setVisibility(View.VISIBLE);
                        } else {
                            secondServicePhotoIv.setVisibility(View.GONE);
                        }
                    }

                    if (i == 2) {
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) thirdServicePhotoIv.getLayoutParams();
                        layoutParams.width = servicePhotoWidth;
                        layoutParams.height = servicePhotoWidth;
                        thirdServicePhotoIv.setLayoutParams(layoutParams);

                        if (!TextUtils.isEmpty(tempServicePhotoUrl)) {
                            ImageViewUtil.setImageView(context, thirdServicePhotoIv, servicePhotoUrl, R.mipmap.ic_default);
                            thirdServicePhotoIv.setVisibility(View.VISIBLE);
                        } else {
                            thirdServicePhotoIv.setVisibility(View.GONE);
                        }
                    }
                }
            } else {
                serviceAlbumLL.setVisibility(View.GONE);
            }

            holder.setTextView(R.id.tv_category_name, categoryName, true);
            holder.setTextView(R.id.tv_number_of_people_seeing, String.valueOf(viewCount) + "人看过", true);

            if (null != serveWayPriceUnitChineseList && serveWayPriceUnitChineseList.size() > 0) {
                String serviceWayPriceUnitChinese = serveWayPriceUnitChineseList.get(0);
                String[] serviceWayPriceUnitChineseArray = serviceWayPriceUnitChinese.split(":");

                for (int i = 0; i < serviceWayPriceUnitChineseArray.length; i++) {
                    if (i == 0) {
                        String serviceWay = serviceWayPriceUnitChineseArray[0];
                        holder.setTextView(R.id.tv_service_way, serviceWay, true);
                    }

                    if (i == 1) {
                        String unit = serviceWayPriceUnitChineseArray[1];
                        holder.setTextView(R.id.tv_unit, "/" + unit, true);
                    }

                    if (i == 2) {
                        String price = serviceWayPriceUnitChineseArray[2];
                        holder.setTextView(R.id.tv_price, "￥" + price, true);
                    }
                }
            }

            holder.setTextView(R.id.tv_advantage, advantage, true);

            flowLayout.setMaxRow(1);

            if (flowLayout.getChildCount() > 0) {
                flowLayout.removeAllViews();
            }

            if (null != categoryPropertyValueChineseList && categoryPropertyValueChineseList.size() > 0) {
                for (int i = 0; i < categoryPropertyValueChineseList.size(); i++) {
                    String categoryProperty = categoryPropertyValueChineseList.get(i);

                    TextView tagTv = new TextView(context);
                    tagTv.setBackgroundResource(R.drawable.shape_recommend_tag_background);
                    tagTv.setText(categoryProperty);
                    tagTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    tagTv.setPadding(15, 5, 15, 5);
                    flowLayout.addView(tagTv);
                }

                flowLayout.setVisibility(View.VISIBLE);
            } else {
                flowLayout.setVisibility(View.GONE);
            }
        }
    }

}
