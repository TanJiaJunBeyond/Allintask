package com.allintask.lingdao.widget.swipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.allintask.lingdao.R;
import com.hyphenate.util.DensityUtil;

/**
 * Created by TanJiaJun on 2017/6/20 0020.
 */

public class HorizontalSlidingView extends HorizontalScrollView {

    private TextView deleteTv;

    private int scrollWidth;

    private HorizontalSlidingViewListener mHorizontalSlidingViewListener;

    private Boolean isOpen = false;
    private Boolean once = false;


    public HorizontalSlidingView(Context context) {
        this(context, null);
    }

    public HorizontalSlidingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalSlidingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!once) {
            deleteTv = (TextView) findViewById(R.id.tv_delete);
            once = true;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {
            scrollTo(0, 0);

            //获取水平滚动条可以滑动的范围，即右侧按钮的宽度
            scrollWidth = deleteTv.getWidth() + DensityUtil.dip2px(getContext(), 16);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (null != mHorizontalSlidingViewListener) {
                    mHorizontalSlidingViewListener.onDownOrMove(this);
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                changeScrollX();
                return true;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 按滚动条被拖动距离判断关闭或打开菜单
     */
    public void changeScrollX() {
        if (getScrollX() >= (scrollWidth / 2)) {
            smoothScrollTo(scrollWidth, 0);
            isOpen = true;
            mHorizontalSlidingViewListener.onMenuIsOpen(this);
        } else {
            smoothScrollTo(0, 0);
            isOpen = false;
        }
    }

    /**
     * 打开菜单
     */
    public void openMenu() {
        if (isOpen) {
            return;
        }

        smoothScrollTo(scrollWidth, 0);
        isOpen = true;

        if (null != mHorizontalSlidingViewListener) {
            mHorizontalSlidingViewListener.onMenuIsOpen(this);
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (!isOpen) {
            return;
        }

        smoothScrollTo(0, 0);
        isOpen = false;
    }

    public interface HorizontalSlidingViewListener {

        void onMenuIsOpen(View view);

        void onDownOrMove(HorizontalSlidingView horizontalSlidingView);

    }

    public void setSlidingButtonListener(HorizontalSlidingViewListener horizontalSlidingViewListener) {
        this.mHorizontalSlidingViewListener = horizontalSlidingViewListener;
    }

}
