package com.allintask.lingdao.ui.adapter.user;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.bean.user.MyPhotoAlbumListBean;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.utils.MonthUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/2/14.
 */

public class MyPhotoAlbumAdapter extends CommonRecyclerViewAdapter {

    private static final int ITEM_MY_PHOTO_ALBUM_A_PHOTO = 0;
    private static final int ITEM_MY_PHOTO_ALBUM_TWO_PHOTO = 1;
    private static final int ITEM_MY_PHOTO_ALBUM_THREE_PHOTO = 2;
    private static final int ITEM_MY_PHOTO_ALBUM = 3;

    private Context context;

    public MyPhotoAlbumAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = null;

        switch (viewType) {
            case ITEM_MY_PHOTO_ALBUM_A_PHOTO:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_photo_album_a_photo, parent, false));
                break;

            case ITEM_MY_PHOTO_ALBUM_TWO_PHOTO:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_photo_album_two_photos, parent, false));
                break;

            case ITEM_MY_PHOTO_ALBUM_THREE_PHOTO:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_photo_album_three_photos, parent, false));
                break;

            case ITEM_MY_PHOTO_ALBUM:
                holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_photo_album, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        switch (holder.getItemViewType()) {
            case ITEM_MY_PHOTO_ALBUM_A_PHOTO:
                onBindMyPhotoAlbumAPhotoItemView(holder, position);
                break;

            case ITEM_MY_PHOTO_ALBUM_TWO_PHOTO:
                onBindMyPhotoAlbumTwoPhotosItemView(holder, position);
                break;

            case ITEM_MY_PHOTO_ALBUM_THREE_PHOTO:
                onBindMyPhotoAlbumThreePhotosItemView(holder, position);
                break;

            case ITEM_MY_PHOTO_ALBUM:
                onBindMyPhotoAlbumItemView(holder, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int itemViewType = super.getItemViewType(position);

        MyPhotoAlbumListBean.MyPhotoAlbumBean myPhotoAlbumBean = (MyPhotoAlbumListBean.MyPhotoAlbumBean) getItem(position);

        if (null != myPhotoAlbumBean) {
            List<MyPhotoAlbumListBean.MyPhotoAlbumBean.ImagesBean> imagesList = myPhotoAlbumBean.images;

            if (null != imagesList && imagesList.size() > 0) {
                int imagesListSize = imagesList.size();

                if (imagesListSize == 1) {
                    itemViewType = ITEM_MY_PHOTO_ALBUM_A_PHOTO;
                } else if (imagesListSize == 2) {
                    itemViewType = ITEM_MY_PHOTO_ALBUM_TWO_PHOTO;
                } else if (imagesListSize == 3) {
                    itemViewType = ITEM_MY_PHOTO_ALBUM_THREE_PHOTO;
                } else if (imagesListSize >= 4) {
                    itemViewType = ITEM_MY_PHOTO_ALBUM;
                }
            }
        }
        return itemViewType;
    }

    private void onBindMyPhotoAlbumAPhotoItemView(CommonRecyclerViewHolder holder, int position) {
        MyPhotoAlbumListBean.MyPhotoAlbumBean myPhotoAlbumBean = (MyPhotoAlbumListBean.MyPhotoAlbumBean) getItem(position);

        if (null != myPhotoAlbumBean) {
            TextView dayTv = holder.getChildView(R.id.tv_day);
            TextView monthTv = holder.getChildView(R.id.tv_month);
            ImageView photoIv = holder.getChildView(R.id.iv_photo);

            Date createAt = myPhotoAlbumBean.createAt;
            List<MyPhotoAlbumListBean.MyPhotoAlbumBean.ImagesBean> imagesList = myPhotoAlbumBean.images;
            String describe = TypeUtils.getString(myPhotoAlbumBean.describe, "");
            String title = TypeUtils.getString(myPhotoAlbumBean.title, "");
            boolean isShowDate = TypeUtils.getBoolean(myPhotoAlbumBean.isShowDate, false);

            if (null != createAt) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(createAt);
                int month = calendar.get(Calendar.MONTH) + 1;
                int date = calendar.get(Calendar.DATE);

                String monthChinese = MonthUtils.getMonthChinese(month);
                monthTv.setText(monthChinese);

                dayTv.setText(String.valueOf(date));
            }

            if (isShowDate) {
                dayTv.setVisibility(View.VISIBLE);
                monthTv.setVisibility(View.VISIBLE);
            } else {
                dayTv.setVisibility(View.INVISIBLE);
                monthTv.setVisibility(View.INVISIBLE);
            }

            if (null != imagesList && imagesList.size() > 0) {
                MyPhotoAlbumListBean.MyPhotoAlbumBean.ImagesBean imagesBean = imagesList.get(0);

                if (null != imagesBean) {
                    String qualityUrl = TypeUtils.getString(imagesBean.qualityUrl, "");
                    String imageUrl = null;

                    if (!TextUtils.isEmpty(qualityUrl)) {
                        imageUrl = "https:" + qualityUrl;
                    }

                    ImageViewUtil.setImageView(context, photoIv, imageUrl, R.mipmap.ic_default);
                }
            }

            holder.setTextView(R.id.tv_photo_description, describe);
            holder.setTextView(R.id.tv_title, title);
        }
    }

    private void onBindMyPhotoAlbumTwoPhotosItemView(CommonRecyclerViewHolder holder, int position) {
        MyPhotoAlbumListBean.MyPhotoAlbumBean myPhotoAlbumBean = (MyPhotoAlbumListBean.MyPhotoAlbumBean) getItem(position);

        if (null != myPhotoAlbumBean) {
            TextView dayTv = holder.getChildView(R.id.tv_day);
            TextView monthTv = holder.getChildView(R.id.tv_month);
            ImageView firstPhotoIv = holder.getChildView(R.id.iv_first_photo);
            ImageView secondPhotoIv = holder.getChildView(R.id.iv_second_photo);

            Date createAt = myPhotoAlbumBean.createAt;
            List<MyPhotoAlbumListBean.MyPhotoAlbumBean.ImagesBean> imagesList = myPhotoAlbumBean.images;
            String describe = TypeUtils.getString(myPhotoAlbumBean.describe, "");
            String title = TypeUtils.getString(myPhotoAlbumBean.title, "");
            boolean isShowDate = TypeUtils.getBoolean(myPhotoAlbumBean.isShowDate, false);

            if (null != createAt) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(createAt);
                int month = calendar.get(Calendar.MONTH) + 1;
                int date = calendar.get(Calendar.DATE);

                String monthChinese = MonthUtils.getMonthChinese(month);
                monthTv.setText(monthChinese);

                dayTv.setText(String.valueOf(date));
            }

            if (isShowDate) {
                dayTv.setVisibility(View.VISIBLE);
                monthTv.setVisibility(View.VISIBLE);
            } else {
                dayTv.setVisibility(View.INVISIBLE);
                monthTv.setVisibility(View.INVISIBLE);
            }

            if (null != imagesList && imagesList.size() > 0) {
                MyPhotoAlbumListBean.MyPhotoAlbumBean.ImagesBean firstImagesBean = imagesList.get(0);
                MyPhotoAlbumListBean.MyPhotoAlbumBean.ImagesBean secondImagesBean = imagesList.get(1);

                if (null != firstImagesBean) {
                    String qualityUrl = TypeUtils.getString(firstImagesBean.qualityUrl, "");
                    String imageUrl = null;

                    if (!TextUtils.isEmpty(qualityUrl)) {
                        imageUrl = "https:" + qualityUrl;
                    }

                    ImageViewUtil.setImageView(context, firstPhotoIv, imageUrl, R.mipmap.ic_default);
                }

                if (null != secondImagesBean) {
                    String qualityUrl = TypeUtils.getString(secondImagesBean.qualityUrl, "");
                    String imageUrl = null;

                    if (!TextUtils.isEmpty(qualityUrl)) {
                        imageUrl = "https:" + qualityUrl;
                    }

                    ImageViewUtil.setImageView(context, secondPhotoIv, imageUrl, R.mipmap.ic_default);
                }
            }

            holder.setTextView(R.id.tv_photo_description, describe);
            holder.setTextView(R.id.tv_title, title);
        }
    }

    private void onBindMyPhotoAlbumThreePhotosItemView(CommonRecyclerViewHolder holder, int position) {
        MyPhotoAlbumListBean.MyPhotoAlbumBean myPhotoAlbumBean = (MyPhotoAlbumListBean.MyPhotoAlbumBean) getItem(position);

        if (null != myPhotoAlbumBean) {
            TextView dayTv = holder.getChildView(R.id.tv_day);
            TextView monthTv = holder.getChildView(R.id.tv_month);
            ImageView firstPhotoIv = holder.getChildView(R.id.iv_first_photo);
            ImageView secondPhotoIv = holder.getChildView(R.id.iv_second_photo);
            ImageView thirdPhotoIv = holder.getChildView(R.id.iv_third_photo);

            Date createAt = myPhotoAlbumBean.createAt;
            List<MyPhotoAlbumListBean.MyPhotoAlbumBean.ImagesBean> imagesList = myPhotoAlbumBean.images;
            String describe = TypeUtils.getString(myPhotoAlbumBean.describe, "");
            String title = TypeUtils.getString(myPhotoAlbumBean.title, "");
            boolean isShowDate = TypeUtils.getBoolean(myPhotoAlbumBean.isShowDate, false);

            if (null != createAt) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(createAt);
                int month = calendar.get(Calendar.MONTH) + 1;
                int date = calendar.get(Calendar.DATE);

                String monthChinese = MonthUtils.getMonthChinese(month);
                monthTv.setText(monthChinese);

                dayTv.setText(String.valueOf(date));
            }

            if (isShowDate) {
                dayTv.setVisibility(View.VISIBLE);
                monthTv.setVisibility(View.VISIBLE);
            } else {
                dayTv.setVisibility(View.INVISIBLE);
                monthTv.setVisibility(View.INVISIBLE);
            }

            if (null != imagesList && imagesList.size() > 0) {
                MyPhotoAlbumListBean.MyPhotoAlbumBean.ImagesBean firstImagesBean = imagesList.get(0);
                MyPhotoAlbumListBean.MyPhotoAlbumBean.ImagesBean secondImagesBean = imagesList.get(1);
                MyPhotoAlbumListBean.MyPhotoAlbumBean.ImagesBean thirdImagesBean = imagesList.get(2);

                if (null != firstImagesBean) {
                    String qualityUrl = TypeUtils.getString(firstImagesBean.qualityUrl, "");
                    String imageUrl = null;

                    if (!TextUtils.isEmpty(qualityUrl)) {
                        imageUrl = "https:" + qualityUrl;
                    }

                    ImageViewUtil.setImageView(context, firstPhotoIv, imageUrl, R.mipmap.ic_default);
                }

                if (null != secondImagesBean) {
                    String qualityUrl = TypeUtils.getString(secondImagesBean.qualityUrl, "");
                    String imageUrl = null;

                    if (!TextUtils.isEmpty(qualityUrl)) {
                        imageUrl = "https:" + qualityUrl;
                    }

                    ImageViewUtil.setImageView(context, secondPhotoIv, imageUrl, R.mipmap.ic_default);
                }

                if (null != thirdImagesBean) {
                    String qualityUrl = TypeUtils.getString(thirdImagesBean.qualityUrl, "");
                    String imageUrl = null;

                    if (!TextUtils.isEmpty(qualityUrl)) {
                        imageUrl = "https:" + qualityUrl;
                    }

                    ImageViewUtil.setImageView(context, thirdPhotoIv, imageUrl, R.mipmap.ic_default);
                }
            }

            holder.setTextView(R.id.tv_photo_description, describe);
            holder.setTextView(R.id.tv_title, title);
        }
    }

    private void onBindMyPhotoAlbumItemView(CommonRecyclerViewHolder holder, int position) {
        MyPhotoAlbumListBean.MyPhotoAlbumBean myPhotoAlbumBean = (MyPhotoAlbumListBean.MyPhotoAlbumBean) getItem(position);

        if (null != myPhotoAlbumBean) {
            TextView dayTv = holder.getChildView(R.id.tv_day);
            TextView monthTv = holder.getChildView(R.id.tv_month);
            ImageView firstPhotoIv = holder.getChildView(R.id.iv_first_photo);
            ImageView secondPhotoIv = holder.getChildView(R.id.iv_second_photo);
            ImageView thirdPhotoIv = holder.getChildView(R.id.iv_third_photo);
            ImageView fourthPhotoIv = holder.getChildView(R.id.iv_fourth_photo);
            TextView photoNumberTv = holder.getChildView(R.id.tv_photo_number);

            Date createAt = myPhotoAlbumBean.createAt;
            List<MyPhotoAlbumListBean.MyPhotoAlbumBean.ImagesBean> imagesList = myPhotoAlbumBean.images;
            String describe = TypeUtils.getString(myPhotoAlbumBean.describe, "");
            String title = TypeUtils.getString(myPhotoAlbumBean.title, "");
            boolean isShowDate = TypeUtils.getBoolean(myPhotoAlbumBean.isShowDate, false);

            if (null != createAt) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(createAt);
                int month = calendar.get(Calendar.MONTH) + 1;
                int date = calendar.get(Calendar.DATE);

                String monthChinese = MonthUtils.getMonthChinese(month);
                monthTv.setText(monthChinese);

                dayTv.setText(String.valueOf(date));
            }

            if (isShowDate) {
                dayTv.setVisibility(View.VISIBLE);
                monthTv.setVisibility(View.VISIBLE);
            } else {
                dayTv.setVisibility(View.INVISIBLE);
                monthTv.setVisibility(View.INVISIBLE);
            }

            if (null != imagesList && imagesList.size() > 0) {
                MyPhotoAlbumListBean.MyPhotoAlbumBean.ImagesBean firstImagesBean = imagesList.get(0);
                MyPhotoAlbumListBean.MyPhotoAlbumBean.ImagesBean secondImagesBean = imagesList.get(1);
                MyPhotoAlbumListBean.MyPhotoAlbumBean.ImagesBean thirdImagesBean = imagesList.get(2);
                MyPhotoAlbumListBean.MyPhotoAlbumBean.ImagesBean fourthImagesBean = imagesList.get(3);

                if (null != firstImagesBean) {
                    String qualityUrl = TypeUtils.getString(firstImagesBean.qualityUrl, "");
                    String imageUrl = null;

                    if (!TextUtils.isEmpty(qualityUrl)) {
                        imageUrl = "https:" + qualityUrl;
                    }

                    ImageViewUtil.setImageView(context, firstPhotoIv, imageUrl, R.mipmap.ic_default);
                }

                if (null != secondImagesBean) {
                    String qualityUrl = TypeUtils.getString(secondImagesBean.qualityUrl, "");
                    String imageUrl = null;

                    if (!TextUtils.isEmpty(qualityUrl)) {
                        imageUrl = "https:" + qualityUrl;
                    }

                    ImageViewUtil.setImageView(context, secondPhotoIv, imageUrl, R.mipmap.ic_default);
                }

                if (null != thirdImagesBean) {
                    String qualityUrl = TypeUtils.getString(thirdImagesBean.qualityUrl, "");
                    String imageUrl = null;

                    if (!TextUtils.isEmpty(qualityUrl)) {
                        imageUrl = "https:" + qualityUrl;
                    }

                    ImageViewUtil.setImageView(context, thirdPhotoIv, imageUrl, R.mipmap.ic_default);
                }

                if (null != fourthImagesBean) {
                    String qualityUrl = TypeUtils.getString(fourthImagesBean.qualityUrl, "");
                    String imageUrl = null;

                    if (!TextUtils.isEmpty(qualityUrl)) {
                        imageUrl = "https:" + qualityUrl;
                    }

                    ImageViewUtil.setImageView(context, fourthPhotoIv, imageUrl, R.mipmap.ic_default);
                }
            }

            holder.setTextView(R.id.tv_photo_description, describe);

            if (null != imagesList && imagesList.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder("总共（").append(String.valueOf(imagesList.size())).append("）张");
                photoNumberTv.setText(stringBuilder);
            }

            holder.setTextView(R.id.tv_title, title);
        }
    }

}
