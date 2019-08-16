package com.allintask.lingdao.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by TanJiaJun on 2016/10/25.
 */

public class CommonScrollView extends ScrollView {

    private float lastX;
    private float lastY;
    private float distanceX;
    private float distanceY;

    public CommonScrollView(Context context) {
        super(context);
    }

    public CommonScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                distanceX = 0f;
                distanceY = 0f;

                lastX = ev.getX();
                lastY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                float newX = ev.getX();
                float newY = ev.getY();

                distanceX += Math.abs(newX - lastX);
                distanceY += Math.abs(newY - lastY);

                lastX = newX;
                lastY = newY;

                if (distanceX > distanceY) {
                    return false;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

}
