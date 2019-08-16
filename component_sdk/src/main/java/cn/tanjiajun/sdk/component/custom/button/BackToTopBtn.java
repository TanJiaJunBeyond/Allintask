package cn.tanjiajun.sdk.component.custom.button;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.tanjiajun.sdk.component.R;

/**
 * 回滚置顶按钮
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class BackToTopBtn extends ImageButton implements OnClickListener {
    private int visiblePosition = 4;

    private boolean isShowing = false;

    private AnimatorSet showAnimSet;
    private AnimatorSet dimissAnimSet;

    private List<Animator> showAnimList;
    private List<Animator> dimissAnimList;

    private ListView listView;

    public BackToTopBtn(Context context) {
        super(context);

        init();
    }

    public BackToTopBtn(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public BackToTopBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void init() {
        setBackground(getResources().getDrawable(R.mipmap.common_btn_back2top));
        setOnClickListener(this);

        setAlpha(0f);
        initShowAnimatorSet();
        initDimissAnimatorSet();

//        setVisibility(GONE);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initShowAnimatorSet() {
        showAnimSet = new AnimatorSet();
        showAnimList = new ArrayList<Animator>();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
        alpha.setDuration(800);
        showAnimList.add(alpha);

        showAnimSet.playTogether(showAnimList);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initDimissAnimatorSet() {
        dimissAnimSet = new AnimatorSet();
        dimissAnimList = new ArrayList<Animator>();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
        alpha.setDuration(800);
        dimissAnimList.add(alpha);

        dimissAnimSet.playTogether(dimissAnimList);

    }

    public void onVisibilityChanged(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem >= visiblePosition) {
            if (!isShowing) {
                isShowing = true;
                show();
            }
        } else {
            if (isShowing) {
                isShowing = false;
                dismiss();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void show() {
        Log.i("TanJiaJun", "backtotop:show");

        showAnimSet.start();
        setVisibility(View.VISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void dismiss() {
        Log.i("TanJiaJun", "backtotop:dismiss");

        dimissAnimSet.start();
        setVisibility(View.GONE);
    }

    public void addShowAnimator(Animator animator) {
        showAnimList.add(animator);
    }

    public void addDimissAnimator(Animator animator) {
        dimissAnimList.add(animator);
    }

    public void bindListView(ListView listView) {
        this.listView = listView;
    }

    public int getVisiblePosition() {
        return visiblePosition;
    }

    public void setVisiblePosition(int visiblePosition) {
        this.visiblePosition = visiblePosition;
    }

    @Override
    public void onClick(View v) {
        if (null != listView) {
            listView.setSelection(4);
//            listView.smoothScrollToPositionFromTop(0, 0);
//            listView.setSelectionAfterHeaderView();
            listView.setSelectionFromTop(0, 0);
        }
    }

}
