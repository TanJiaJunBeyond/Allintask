package com.allintask.lingdao.ui.adapter.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewAdapter;
import com.allintask.lingdao.ui.adapter.recyclerview.CommonRecyclerViewHolder;
import com.allintask.lingdao.utils.WindowUtils;
import com.hyphenate.util.DensityUtil;

import cn.tanjiajun.sdk.common.utils.TypeUtils;
import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2018/2/5.
 */

public class PhotoAdapter extends CommonRecyclerViewAdapter {

    private Context context;

    private OnDeletePhotoListener onDeletePhotoListener;

    public PhotoAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonRecyclerViewHolder holder = new CommonRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_photo, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        onBindItemView(holder, position);
    }

    private void onBindItemView(CommonRecyclerViewHolder holder, final int position) {
        ImageView photoIv = holder.getChildView(R.id.iv_photo);
        ImageView deleteTv = holder.getChildView(R.id.iv_delete);

        int width = (WindowUtils.getScreenWidth(context) - DensityUtil.dip2px(context, 45)) / 3 - 45;
        photoIv.setLayoutParams(new LinearLayout.LayoutParams(width, width));

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) deleteTv.getLayoutParams();
        layoutParams.leftMargin = width - DensityUtil.dip2px(context, 12);
        deleteTv.setLayoutParams(layoutParams);

        if (null != mList && mList.size() > 0) {
            if (position <= mList.size() - 1) {
                deleteTv.setVisibility(View.VISIBLE);

                String imageUrl = "file:/" + TypeUtils.getString(mList.get(position), "");
                ImageViewUtil.setImageView(context, holder.getItemView(), R.id.iv_photo, imageUrl);
            } else {
                deleteTv.setVisibility(View.GONE);
                photoIv.setImageResource(R.mipmap.ic_photo_add);
            }
        } else {
            deleteTv.setVisibility(View.GONE);
            photoIv.setImageResource(R.mipmap.ic_photo_add);
        }

        deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onDeletePhotoListener) {
                    onDeletePhotoListener.onDeletePhoto(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        int listSize = mList.size();

        if (listSize == 0) {
            return 1;
        } else if (listSize < 9) {
            return listSize + 1;
        } else {
            return listSize;
        }
    }

    public interface OnDeletePhotoListener {
        void onDeletePhoto(int position);
    }

    public void setOnDeletePhotoListener(OnDeletePhotoListener onDeletePhotoListener) {
        this.onDeletePhotoListener = onDeletePhotoListener;
    }

}
