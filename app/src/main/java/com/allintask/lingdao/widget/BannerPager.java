package com.allintask.lingdao.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.allintask.lingdao.R;

import cn.tanjiajun.sdk.component.util.ImageViewUtil;

/**
 * Created by TanJiaJun on 2017/3/30 0030.
 */

public class BannerPager extends RelativeLayout implements View.OnClickListener {

    private String adFileName;

    public BannerPager(Context context, String adFileName) {
        super(context);

        this.adFileName = adFileName;

        init();
    }

    public BannerPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.view_banner_pager, null);
        addView(contentView);

        ImageView imageView = (ImageView) contentView.findViewById(R.id.iv_banner_page);
        ImageViewUtil.setImageView(getContext(), contentView, R.id.iv_banner_page, adFileName);

        if (null != imageView) {
            imageView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

    }

}
