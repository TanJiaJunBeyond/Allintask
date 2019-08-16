package com.allintask.lingdao.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.allintask.lingdao.R;

import java.util.ArrayList;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.DensityUtils;

/**
 * Created by TanJiaJun on 2017/4/14 0014.
 */

public class MarqueeView extends ViewFlipper {

    private Context mContext;
    private List<String> noticesList;
    private boolean isSetAnimDuration = false;

    private int interval = 2000;
    private int animDuration = 500;
    private int textSize = 14;
    private int textColor = 0xffffffff;

    private OnNoticesListener onNoticesListener;

    public MarqueeView(Context context) {
        super(context);

        init(context, null, 0);
    }

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;

        if (noticesList == null) {
            noticesList = new ArrayList<>();
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MarqueeViewStyle, defStyleAttr, 0);
        interval = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvInterval, interval);
        isSetAnimDuration = typedArray.hasValue(R.styleable.MarqueeViewStyle_mvAnimDuration);
        animDuration = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvAnimDuration, animDuration);

        if (typedArray.hasValue(R.styleable.MarqueeViewStyle_mvTextSize)) {
            textSize = (int) typedArray.getDimension(R.styleable.MarqueeViewStyle_mvTextSize, textSize);
            textSize = DensityUtils.px2dip(mContext, textSize);
        }

        textColor = typedArray.getColor(R.styleable.MarqueeViewStyle_mvTextColor, textColor);
        typedArray.recycle();

        setFlipInterval(interval);

        Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_in);

        if (isSetAnimDuration) animIn.setDuration(animDuration);
        setInAnimation(animIn);

        Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_out);

        if (isSetAnimDuration) animOut.setDuration(animDuration);
        setOutAnimation(animOut);
    }

    // 根据公告字符串启动轮播
    public void startWithText(final String notice) {
        if (TextUtils.isEmpty(notice)) return;

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                startWithFixedWidth(notice, getWidth());
            }
        });
    }

    // 根据公告字符串列表启动轮播
    public void startWithList(List<String> noticesList) {
        setNotices(noticesList);
        start();
    }

    // 根据宽度和公告字符串启动轮播
    private void startWithFixedWidth(String notice, int width) {
        int noticeLength = notice.length();
        int dpW = DensityUtils.px2dip(mContext, width);
        int limit = dpW / textSize;

        if (dpW == 0) {
            throw new RuntimeException("Please set MarqueeView width !");
        }

        if (noticeLength <= limit) {
            noticesList.add(notice);
        } else {
            int size = noticeLength / limit + (noticeLength % limit != 0 ? 1 : 0);

            for (int i = 0; i < size; i++) {
                int startIndex = i * limit;
                int endIndex = ((i + 1) * limit >= noticeLength ? noticeLength : (i + 1) * limit);
                noticesList.add(notice.substring(startIndex, endIndex));
            }
        }
        start();
    }

    // 启动轮播
    public boolean start() {
        if (noticesList == null || noticesList.size() == 0) return false;

        removeAllViews();

        for (int i = 0; i < noticesList.size(); i++) {
            String notice = noticesList.get(i);

            TextView textView = createTextView(notice);
            addView(textView);

            if (null != onNoticesListener) {
                onNoticesListener.notices(i, textView);
            }
        }

        if (noticesList.size() > 1) {
            startFlipping();
        }
        return true;
    }

    // 创建ViewFlipper下的TextView
    private TextView createTextView(String text) {
        TextView tv = new TextView(mContext);
        tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        tv.setText(text);
        tv.setTextColor(textColor);
        tv.setTextSize(textSize);
        return tv;
    }

    public List<String> getNotices() {
        return noticesList;
    }

    public void setNotices(List<String> noticesList) {
        this.noticesList = noticesList;
    }

    public interface OnNoticesListener {
        void notices(int position, TextView textView);
    }

    public void setOnNoticesClickListener(OnNoticesListener onNoticesListener) {
        this.onNoticesListener = onNoticesListener;
    }

}
