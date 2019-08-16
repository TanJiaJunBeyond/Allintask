package com.allintask.lingdao.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by TanJiaJun on 2017/4/5 0005.
 */

public class CommonNestScrollView extends NestedScrollView {

    private int touchSlop;
    private int downY;
    private int headerViewHeight;

//    private OverScroller overScroller;

    public CommonNestScrollView(Context context) {
        super(context);

        init(context);
    }

    public CommonNestScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
//        overScroller = new OverScroller(context);
    }

    public void setHeaderViewHeight(int headerViewHeight) {
        this.headerViewHeight = headerViewHeight;
    }

//    public void fling(int velocityY) {
//        overScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, headerViewHeight);
//        invalidate();
//    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getRawY();

                if (Math.abs(moveY - downY) > touchSlop) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        boolean isHiddenTopView = dy > 0 && getScrollY() < headerViewHeight;
        boolean isShowTopView = dy < 0 && getScrollY() > 0 && !ViewCompat.canScrollVertically(target, -1);

        if (isHiddenTopView || isShowTopView) {
            scrollBy(dx, dy);

            consumed[1] = dy;
        }
    }

//    @Override
//    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
//        if (getScrollY() >= headerViewHeight) {
//            return false;
//        }
//
//        fling((int) velocityY);
//        return true;
//    }

//    @Override
//    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
//        return false;
//    }

//    @Override
//    public void scrollTo(int x, int y) {
//        if (y < 0) {
//            y = 0;
//        }
//
//        if (y > headerViewHeight) {
//            y = headerViewHeight;
//        }
//
//        if (y != getScrollY()) {
//            super.scrollTo(x, y);
//        }
//    }

//    @Override
//    public void computeScroll() {
//        if (overScroller.computeScrollOffset()) {
//            scrollTo(0, overScroller.getCurrY());
//            invalidate();
//        }
//    }

}
