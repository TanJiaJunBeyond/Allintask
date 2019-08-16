package com.allintask.lingdao.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.allintask.lingdao.R;
import com.allintask.lingdao.utils.WindowUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import cn.tanjiajun.sdk.common.utils.DensityUtils;
import cn.tanjiajun.sdk.common.utils.TypeUtils;

/**
 * Created by TanJiaJun on 2017/5/3 0003.
 */

public class NineGridLayout extends ViewGroup {

    private List<String> dataList;

    private int totalWidth;
    // 图片之间的间隔
    private int gap = 5;
    private int columns;
    private int rows;

    private DisplayImageOptions.Builder displayImageOptionsBuilder = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).showImageForEmptyUri(R.mipmap.ic_launcher).showImageOnFail(R.mipmap.ic_launcher);

    private OnImageClickListener onImageClickListener;

    public NineGridLayout(Context context) {
        super(context);

        init(context, null);
    }

    public NineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineGridLayout);

            if (null != typedArray) {
                totalWidth = typedArray.getInt(R.styleable.NineGridLayout_ngl_total_width, WindowUtils.getScreenWidth(context) - DensityUtils.dip2px(context, 80));
                gap = typedArray.getInt(R.styleable.NineGridLayout_ngl_gap, 5);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void layoutChildrenView() {
        int childrenCount = dataList.size();

        int singleWidth = (totalWidth - gap * (3 - 1)) / 3;
        int singleHeight = singleWidth;

        //根据子view数量确定高度
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = singleHeight * rows + gap * (rows - 1);
        setLayoutParams(params);

        for (int i = 0; i < childrenCount; i++) {
            ImageView imageView = (ImageView) getChildAt(i);
            String imageUrl = TypeUtils.getString(dataList.get(i), "");
            initImage(imageView, imageUrl);

            int[] position = findPosition(i);
            int left = (singleWidth + gap) * position[1];
            int top = (singleHeight + gap) * position[0];
            int right = left + singleWidth;
            int bottom = top + singleHeight;
            imageView.layout(left, top, right, bottom);
        }

    }

    private void initImage(final ImageView imageView, String url) {
        ImageLoader.getInstance().displayImage(url, imageView, displayImageOptionsBuilder.build(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                imageView.setImageResource(R.mipmap.ic_launcher);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                imageView.setImageResource(R.mipmap.ic_launcher);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_fade_in);
                imageView.setAnimation(anim);
                anim.start();
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
        });
    }

    private int[] findPosition(int childNum) {
        int[] position = new int[2];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i * columns + j) == childNum) {
                    position[0] = i;//行
                    position[1] = j;//列
                    break;
                }
            }
        }
        return position;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }


    public void setImageList(List<String> imageUrlList) {
        if (imageUrlList == null || imageUrlList.isEmpty()) {
            return;
        }

        //初始化布局
        generateChildrenLayout(imageUrlList.size());

        //这里做一个重用view的处理
        if (null == dataList) {
            int i = 0;

            while (i < imageUrlList.size()) {
                ImageView imageView = generateImageView();
                addView(imageView, generateDefaultLayoutParams());
                i++;
            }
        } else {
            int oldViewCount = dataList.size();
            int newViewCount = imageUrlList.size();

            if (oldViewCount > newViewCount) {
                removeViews(newViewCount - 1, oldViewCount - newViewCount);
            } else if (oldViewCount < newViewCount) {
                for (int i = 0; i < newViewCount - oldViewCount; i++) {
                    ImageView imageView = generateImageView();
                    addView(imageView, generateDefaultLayoutParams());
                }
            }
        }

        dataList = imageUrlList;
        layoutChildrenView();
    }


    /**
     * 根据图片个数确定行列数量
     * 对应关系如下
     * num	row	column
     * 1	   1	1
     * 2	   1	2
     * 3	   1	3
     * 4	   2	2
     * 5	   2	3
     * 6	   2	3
     * 7	   3	3
     * 8	   3	3
     * 9	   3	3
     *
     * @param length
     */
    private void generateChildrenLayout(int length) {
        if (length <= 3) {
            rows = 1;
            columns = length;
        } else if (length <= 6) {
            rows = 2;
            columns = 3;
            if (length == 4) {
                columns = 2;
            }
        } else {
            rows = 3;
            columns = 3;
        }
    }

    private ImageView generateImageView() {
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageClickListener.onImageClick(v);
            }
        });
        return imageView;
    }

    public interface OnImageClickListener {
        void onImageClick(View view);
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

}
