package com.allintask.lingdao.ui.adapter.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintask.lingdao.AllintaskApplication;
import com.allintask.lingdao.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import cn.tanjiajun.sdk.component.custom.image.imageloader.DefaultImageLoadingListener;
import cn.tanjiajun.sdk.component.custom.image.imageloader.IImageOnLoadingCompleteListener;

/**
 * Created by TanJiaJun on 2016/4/2.
 */

public class CommonRecyclerViewHolder extends RecyclerView.ViewHolder {

    private View mItemView;
    private Context mContext;
    private SparseArray<View> childViewArray; // 替代HashMap<int resId, View childView>

    private int defaultImage = 0;
    private DisplayImageOptions.Builder displayImageOptionsBuilder = new DisplayImageOptions.Builder().showImageForEmptyUri(R.mipmap.ic_launcher).showImageOnFail(R.mipmap.ic_launcher).cacheInMemory().cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565);

    public CommonRecyclerViewHolder(View itemView) {
        this(itemView, AllintaskApplication.getInstance().getApplicationContext());
    }

    public CommonRecyclerViewHolder(View itemView, Context context) {
        super(itemView);
        this.mItemView = itemView;
        this.mContext = context;
        childViewArray = new SparseArray<>();
    }

    public View getItemView() {
        return this.mItemView;
    }

    public <T extends View> T getChildView(int resId) {
        View childView = childViewArray.get(resId);
        if (null == childView) {
            if (null != mItemView) {
                childView = mItemView.findViewById(resId);
                childViewArray.put(resId, childView);
            }
        }
        return (T) childView;
    }

    /*******************************
     * 设置TextView
     *******************************/
    public CommonRecyclerViewHolder setTextView(int viewId, String text) {
        return setTextView(viewId, text, false);
    }

    public CommonRecyclerViewHolder setTextView(int viewId, String text, boolean isEmptyGone) {
        TextView tv = getChildView(viewId);

        if (null != tv) {
            if (!TextUtils.isEmpty(text)) {
                tv.setText(text);
                tv.setVisibility(View.VISIBLE);
            } else {
                if (isEmptyGone) {
                    tv.setVisibility(View.GONE);
                } else {
                    tv.setText(null);
                }
            }
        }
        return this;
    }

    public CommonRecyclerViewHolder setTextView(int viewId, String text, int color) {
        TextView tv = getChildView(viewId);
        if (null != tv) {
            if (!TextUtils.isEmpty(text)) {
                tv.setText(text);
                tv.setTextColor(color);
            }
        }
        return this;
    }

    /*******************************
     * 设置ImageView
     *******************************/
    public void setImageView(int imageViewId, int imageResId) {
        ImageView imageView = getChildView(imageViewId);
        if (null != imageView && null != mContext) {
            imageView.setImageResource(imageResId);
        }
    }

    public void setImageView(int imageViewId, String imageUri) {
        setImageView(imageViewId, imageUri, 0, false, null);
    }

    public void setImageView(int imageViewId, String imageUri, boolean isOpenLoadAnimation) {
        setImageView(imageViewId, imageUri, 0, isOpenLoadAnimation, null);
    }

    public void setImageView(int imageViewId, String imageUri, IImageOnLoadingCompleteListener iImageOnLoadingCompleteListener) {
        setImageView(imageViewId, imageUri, 0, false, iImageOnLoadingCompleteListener);
    }

    public void setImageView(int imageViewId, String imageUri, int defaultImage) {
        setImageView(imageViewId, imageUri, defaultImage, false, null);
    }

    public void setImageView(int imageViewId, String imageUri, int defaultImage, boolean isOpenLoadAnimation) {
        setImageView(imageViewId, imageUri, defaultImage, isOpenLoadAnimation, null);
    }

    public void setImageView(int imageViewId, String imageUri, int defaultImage, IImageOnLoadingCompleteListener iImageOnLoadingCompleteListener) {
        setImageView(imageViewId, imageUri, defaultImage, false, iImageOnLoadingCompleteListener);
    }

    private void setImageView(int imageViewId, String imageUri, int defaultImage, boolean isOpenLoadAnimation, IImageOnLoadingCompleteListener iImageOnLoadingCompleteListener) {
        ImageView imageView = getChildView(imageViewId);
        if (null != imageView && mContext != null) {
            if (defaultImage != 0) {
                displayImageOptionsBuilder.showImageOnFail(defaultImage).showImageForEmptyUri(defaultImage);

                Drawable defaultDrawable = mContext.getResources().getDrawable(defaultImage);
                if (null != defaultDrawable) {
                    imageView.setImageDrawable(defaultDrawable);
                }
            } else {
                if (0 != this.defaultImage) {
                    displayImageOptionsBuilder.showImageOnFail(this.defaultImage).showImageForEmptyUri(this.defaultImage);
                }
            }

            DefaultImageLoadingListener defaultImageLoadingListener = null;
            if (null == iImageOnLoadingCompleteListener) {
                defaultImageLoadingListener = new DefaultImageLoadingListener(mContext, imageView, isOpenLoadAnimation);
            } else {
                defaultImageLoadingListener = new DefaultImageLoadingListener(mContext, imageView, isOpenLoadAnimation, iImageOnLoadingCompleteListener);
            }
            ImageLoader.getInstance().displayImage(imageUri, imageView, displayImageOptionsBuilder.build(), defaultImageLoadingListener);
        }
    }

    public void setDefaultImage(int defaultImage) {
        this.defaultImage = defaultImage;
    }

}
