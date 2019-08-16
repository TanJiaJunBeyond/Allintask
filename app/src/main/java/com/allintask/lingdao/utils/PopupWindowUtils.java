package com.allintask.lingdao.utils;

import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by TanJiaJun on 2018/4/7.
 */

public class PopupWindowUtils {

    public static void showAsDropDown(PopupWindow popupWindow, View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            popupWindow.setHeight(height);
            popupWindow.showAsDropDown(anchor);
        } else {
            popupWindow.showAsDropDown(anchor);
        }
    }

}
