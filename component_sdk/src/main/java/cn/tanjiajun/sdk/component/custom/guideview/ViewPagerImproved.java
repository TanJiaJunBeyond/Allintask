package cn.tanjiajun.sdk.component.custom.guideview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 简易的ViewPager实现类
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class ViewPagerImproved extends ViewPager {

    private boolean isScrollable = true;

    public ViewPagerImproved(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ViewPagerImproved(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        boolean result = false;
        if (isScrollable) {
            result = super.onInterceptTouchEvent(arg0);
        }

        return result;
    }

    public void setScrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }

    @Override
    public void scrollTo(int x, int y) {
        if (isScrollable) {
            super.scrollTo(x, y);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        boolean result = false;
        if (isScrollable) {
            result = super.onTouchEvent(arg0);
        }
        return result;
    }

}
