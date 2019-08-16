package cn.tanjiajun.sdk.component.custom.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * 可滑动WebView
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class ScrollWebView extends WebView {

    public OnScrollChangeListener listener;

    public ScrollWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollWebView(Context context) {
        super(context);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        super.onScrollChanged(l, t, oldl, oldt);

        float webcontent = getContentHeight() * getScale();// webview的高度
        float webnow = getHeight() + getScrollY();// 当前webview的高度
        if (Math.abs(webcontent - webnow) < 1) {
            // 已经处于底端
            // Log.i("TAG1", "已经处于底端");
            if (listener != null) {
                listener.onPageEnd(l, t, oldl, oldt);
            }
        } else if (getScrollY() == 0) {
            // Log.i("TAG1", "已经处于顶端");
            if (listener != null) {
                listener.onPageTop(l, t, oldl, oldt);
            }
        } else {
            if (listener != null) {
                listener.onScrollChanged(l, t, oldl, oldt);
            }
        }

    }

    public void setOnScrollChangeListener(OnScrollChangeListener listener) {

        this.listener = listener;

    }

    public interface OnScrollChangeListener {

        public void onPageEnd(int l, int t, int oldl, int oldt);

        public void onPageTop(int l, int t, int oldl, int oldt);

        public void onScrollChanged(int l, int t, int oldl, int oldt);

    }

}
