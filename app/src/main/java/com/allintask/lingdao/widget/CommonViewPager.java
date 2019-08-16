package com.allintask.lingdao.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TanJiaJun on 2016/10/25.
 */

public class CommonViewPager extends ViewPager {

    private Map<Integer, Integer> map;

    private int mCurrentPage;

    private OnCommonViewPagerListener mOnCommonViewPagerListener;

    public CommonViewPager(Context context) {
        super(context);

        init();
    }

    public CommonViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        map = new HashMap<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;

        if (mCurrentPage < map.size()) {
            height = map.get(mCurrentPage);
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (null != mOnCommonViewPagerListener) {
            mOnCommonViewPagerListener.onViewPagerDispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 在切换tab的时候，重置ViewPager的高度
     *
     * @param current
     */
    public void resetHeight(int current) {
        this.mCurrentPage = current;

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
        if (mCurrentPage < map.size()) {
            if (null == layoutParams) {
                layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, map.get(current));
            } else {
                layoutParams.height = map.get(current);
            }

            setLayoutParams(layoutParams);
        }
    }

    /**
     * 获取、存储每一个tab的高度，在需要的时候显示存储的高度
     *
     * @param current tab的position
     * @param height  当前tab的高度
     */
    public void addHeight(int current, int height) {
        map.put(current, height);
    }

    public interface OnCommonViewPagerListener {

        void onViewPagerDispatchTouchEvent(MotionEvent motionEvent);

    }

    public void setOnCommonViewPagerListener(OnCommonViewPagerListener onCommonViewPagerListener) {
        mOnCommonViewPagerListener = onCommonViewPagerListener;
    }

}
