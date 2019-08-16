package cn.tanjiajun.sdk.component.custom.guideview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.lang.reflect.Field;
import java.util.List;

import cn.tanjiajun.sdk.component.R;

/**
 * 图片轮播控件
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class AutoScrollImagePager extends RelativeLayout {

    private static final long DEFAULT_DELAYED_TIME = 2500;

    public static final int LEFT = 10;
    public static final int RIGHT = 11;

    private static final int MSG_UPDATE_IMAGE = 0;
    private static final int MSG_KEEP_SILENT = 1;
    private static final int MSG_BREAK_SILENT = 2;

    private ViewPager mViewPager;
    private LinearLayout mDotFrame;

    private List<View> imageSource;
    private ImagePagerAdapter mPagerAdapter;
    private ImagePagerChangeListener mPagerChangeListener;

    /**
     * auto scroll direction, default is {@link #RIGHT} *
     */
    private int direction = RIGHT;

    /**
     * duration of auto scroll, default is {@link #DEFAULT_DELAYED_TIME} *
     */
    private long mAutoSrcollDuration = DEFAULT_DELAYED_TIME;

    /**
     * duration of auto scroll animation, default is -1 *
     */
    private int mTransformDuration = -1;

    /**
     * whether is auto scroll, default is true *
     */
    private boolean isAutoScroll = true;

    /**
     * whether automatic cycle when auto scroll reaching the first or the last
     * item, default is true *
     */
    private boolean isCycle = true;

    /**
     * the image resource ID of normal state dot, default is
     * R.drawable.ic_dot_normal *
     */
    private int mNormalDotResource = R.mipmap.ic_dot_normal;

    /**
     * the image resource ID of focus state dot, default is
     * R.drawable.ic_dot_focus *
     */
    private int mFocusDotResource = R.mipmap.ic_dot_focus;

    /**
     * true if the supplied Page Transformer requires page views to be drawn
     * from last to first instead of first to last. *
     */
    private boolean reverseDrawingOrder = true;

    /**
     * Page Transformer that will modify each page's animation properties
     */
    private ViewPager.PageTransformer transformer;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    scrollToNextPage(mViewPager.getCurrentItem());

                    removeMessages(MSG_UPDATE_IMAGE); // 清除未执行的MSG_UPDATE_IMAGE
                    sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, mAutoSrcollDuration);
                    break;

                case MSG_KEEP_SILENT:
                    removeMessages(MSG_UPDATE_IMAGE);
                    break;

                case MSG_BREAK_SILENT:
                    sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, mAutoSrcollDuration);
                    break;

                default:
                    break;
            }
        }
    };

    public AutoScrollImagePager(Context context) {
        super(context);
        init();
    }

    public AutoScrollImagePager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.commom_autoscrollimagepager, null, false);
        addView(rootView, params);

        //        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        //        DisplayMetrics outMetrics = new DisplayMetrics();
        //        manager.getDefaultDisplay().getMetrics(outMetrics);
        //        int screenWidth = outMetrics.widthPixels;
        //        int screenHeight = outMetrics.heightPixels;

        mViewPager = (ViewPager) findViewById(R.id.ximagelooper_content_vp);
        mDotFrame = (LinearLayout) findViewById(R.id.ximagelooper_dot_ll);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mViewPager = (ViewPager) findViewById(R.id.ximagelooper_content_vp);
        mDotFrame = (LinearLayout) findViewById(R.id.ximagelooper_dot_ll);

    }

    /**
     * 设置视图页面变化监听器
     *
     * @param listener
     */
    public void setImagePagerChangeListener(ImagePagerChangeListener listener) {
        this.mPagerChangeListener = listener;
    }

    /**
     * 设置是否实现视图轮播
     *
     * @param isAutoScroll
     */
    public void setAutoPlayEnable(boolean isAutoScroll) {
        this.isAutoScroll = isAutoScroll;
    }

    /**
     * 设置是否支持循环播放视图
     *
     * @param isCycle
     */
    public void setCycle(boolean isCycle) {
        this.isCycle = isCycle;
    }

    /**
     * 设置视图滑动的方向，
     *
     * @param direction {@link #LEFT} 或者 {@link #RIGHT}, 默认是{@link #RIGHT}
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * 设置视图切换动画
     *
     * @param transformer Page Transformer that will modify each page's animation
     *                    properties.
     */
    public void setPagerTransformer(ViewPager.PageTransformer transformer) {
        setPagerTransformer(true, transformer);
    }

    /**
     * 设置视图切换动画
     *
     * @param reverseDrawingOrder true if the supplied Page Transformer requires page views to
     *                            be drawn from last to first instead of first to last.
     * @param transformer         Page Transformer that will modify each page's animation
     *                            properties.
     */
    public void setPagerTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer) {
        this.reverseDrawingOrder = reverseDrawingOrder;
        this.transformer = transformer;
    }

    /**
     * 视图滑动的间隔时间
     *
     * @param mAutoSrcollDuration
     */
    public void setAutoScrollInterval(long mAutoSrcollDuration) {
        this.mAutoSrcollDuration = mAutoSrcollDuration;
    }

    /**
     * 设置视图滑动的速度
     *
     * @param mTransformDuration
     */
    public void setAutoScrollSpeed(int mTransformDuration) {
        this.mTransformDuration = mTransformDuration;
    }

    //    /**
    //     * 设置指示视图的资源文件ID
    //     * @param normalDotResource
    //     * @param focusDotResource
    //     */
    //    public void setDotResource(int normalDotResource, int focusDotResource) {
    //        try {
    //            if (null != getResources().getDrawable(normalDotResource)) {
    //                this.mNormalDotResource = normalDotResource;
    //            }
    //
    //            if (null != getResources().getDrawable(focusDotResource)) {
    //                this.mFocusDotResource = focusDotResource;
    //            }
    //        } catch (Resources.NotFoundException e) {
    //            e.printStackTrace();
    //        }
    //    }

    /**
     * 设置自定义的视图集合
     *
     * @param source 视图集合
     */
    public void setImageSource(List<View> source) {
        this.imageSource = source;
    }

    private int getImageCount() {
        return (imageSource == null) ? 0 : imageSource.size();
    }

    /**
     * 启动控件
     */
    public void start() {
        showImagePager();
    }

    private void createDots(int dotCount) {
        if (null == mDotFrame || dotCount <= 0) {
            return;
        }

        mDotFrame.removeAllViews();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        params.setMargins(8, 8, 8, 8);

        for (int i = 0; i < dotCount; i++) {
            ImageView mDot = new ImageView(getContext());
            mDot.setImageResource(mNormalDotResource);
            mDot.setLayoutParams(params);
            mDotFrame.addView(mDot);
        }
    }

    private void setCurrentDot(int currentItem) {
        if (null == mDotFrame) {
            return;
        }

        for (int index = 0; index < mDotFrame.getChildCount(); index++) {
            View mDot = mDotFrame.getChildAt(index);
            if (null == mDot) {
                continue;
            }

            if (currentItem == index) {
                if (mDot instanceof ImageView) {
                    ((ImageView) mDot).setImageResource(mFocusDotResource);
                } else {
                    mDot.setBackgroundResource(mFocusDotResource);
                }
            } else {
                if (mDot instanceof ImageView) {
                    ((ImageView) mDot).setImageResource(mNormalDotResource);
                } else {
                    mDot.setBackgroundResource(mNormalDotResource);
                }
            }
        }
    }

    private void scrollToNextPage(int currentItem) {
        if (null == mViewPager || null == mPagerAdapter) {
            return;
        }

        int totalCount = mPagerAdapter.getCount();
        int nextItem = (direction == RIGHT) ? ++currentItem : --currentItem;

        if (nextItem < 0) { /* left border */
            nextItem = totalCount - 1;

            if (isCycle) {
                mViewPager.setCurrentItem(nextItem, false);
            }
        } else if (nextItem >= totalCount) { /* right border */
            nextItem = 0;

            if (isCycle) {
                mViewPager.setCurrentItem(nextItem, false);
            }
        } else {
            mViewPager.setCurrentItem(nextItem, true);
        }

        int dotPosition = mPagerAdapter.getPosition(nextItem);
        setCurrentDot(dotPosition);
    }

    private void showImagePager() {
        int imageCount = getImageCount();
        if (0 == imageCount) {
            return;
        }

        if (null == mViewPager) {
            return;
        }

        // create dots
        createDots(imageCount);
        setCurrentDot(0);

        // init ViewPager
        if (null == mPagerAdapter) {
            mPagerAdapter = new ImagePagerAdapter(imageSource);
        }
        mViewPager.setAdapter(mPagerAdapter);
        int currentItem = (mPagerAdapter.canCycle()) ? (Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % imageCount) : 0; /*
                                                                                                                          * 设置ViewPager的当前项为一个比较大的数
                                                                                                                          * ，
                                                                                                                          * 以便一开始就可以左右循环滑动
                                                                                                                          */
        mViewPager.setCurrentItem(currentItem, true);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (null != mPagerChangeListener) {
                    mPagerChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (null != mPagerChangeListener) {
                    mPagerChangeListener.onPageSelected(position);
                }
                setCurrentDot((isCycle) ? position % getImageCount() : position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (isAutoScroll) {
                    switch (state) {
                        case ViewPager.SCROLL_STATE_DRAGGING:
                            mHandler.sendEmptyMessage(MSG_KEEP_SILENT);
                            break;

                        case ViewPager.SCROLL_STATE_SETTLING:
                            break;

                        case ViewPager.SCROLL_STATE_IDLE:
                            mHandler.sendEmptyMessage(MSG_BREAK_SILENT);
                            break;

                        default:
                            break;
                    }
                }

                if (null != mPagerChangeListener) {
                    mPagerChangeListener.onPageScrollStateChanged(state);
                }
            }
        });

        if (null != transformer) {
            mViewPager.setPageTransformer(this.reverseDrawingOrder, this.transformer);
        }

        if (mTransformDuration >= 0) {
            ViewPagerSpeedSetter mSpeedSetter = new ViewPagerSpeedSetter(getContext());
            mSpeedSetter.setDuration(mTransformDuration);
            mSpeedSetter.initViewPagerSpeedSetter(mViewPager);
        }

        if (isAutoScroll) {
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, mAutoSrcollDuration);
        }
    }

    public interface ImagePagerChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private List<View> imageViewList;
        private boolean canCycle;

        public ImagePagerAdapter(List<View> imageViewList) {
            this.imageViewList = imageViewList;

            if (null != imageViewList) {
                int count = imageViewList.size();
                canCycle = isCycle && (count >= 3);
            }
        }

        public boolean canCycle() {
            return canCycle;
        }

        public int getPosition(int position) {
            return (canCycle) ? position % imageViewList.size() : position;
        }

        @Override
        public int getCount() {
            return canCycle ? Integer.MAX_VALUE : imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View mImage = imageViewList.get(getPosition(position));
            if (null != mImage.getParent()) {
                ViewGroup mParentView = (ViewGroup) mImage.getParent();
                mParentView.removeView(mImage);
            }
            container.addView(mImage);

            return mImage;
        }
    }

//    /**
//     * 默认的切换动画 (最低需API11，因此需要 引入nineoldandroids包，或者修改API等级)
//     */
//    private class DepthPageTransformer implements ViewPager.PageTransformer {
//        private static final float MIN_SCALE = 0.75f;
//
//        /*
//         * position == 0 ：当前界面位于屏幕中心的时候 position == 1 ：当前Page刚好滑出屏幕右侧 position
//         * == -1 ：当前Page刚好滑出屏幕左侧
//         */
//        public void transformPage(View view, float position) {
//            int pageWidth = view.getWidth();
//
//            if (position < -1) { // [-Infinity,-1)        左边界（-1）以左
//                // This page is way off-screen to the left.
//                view.setAlpha(0);
//
//            } else if (position <= 0) { // [-1,0]         <---------- 当前页面向左滑动 （0 -> -1）
//                // Use the default slide transition when moving to the left page
//                view.setAlpha(1);
//                view.setTranslationX(0);
//                view.setScaleX(1);
//                view.setScaleY(1);
//
//            } else if (position <= 1) { // (0,1]          -----------> 当前页面向右滑动 （1 -> 0）
//                // Fade the page out.
//                view.setAlpha(1 - position);
//
//                // Counteract the default slide transition
//                view.setTranslationX(pageWidth * -position);
//
//                // Scale the page down (between MIN_SCALE and 1)
//                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
//                view.setScaleX(scaleFactor);
//                view.setScaleY(scaleFactor);
//
//            } else { // (1,+Infinity]                     右边界（1）以右
//                // This page is way off-screen to the right.
//                view.setAlpha(0);
//            }
//        }
//    }

    /**
     * 利用Java反射，控制页面切换速度
     */
    private class ViewPagerSpeedSetter extends Scroller {

        private int mDuration = 0;

        public ViewPagerSpeedSetter(Context context) {
            super(context);
        }

        public ViewPagerSpeedSetter(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

//        public ViewPagerSpeedSetter(Context context, Interpolator interpolator, boolean flywheel) {
//            super(context, interpolator, flywheel);
//        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void setDuration(int mDuration) {
            this.mDuration = mDuration;
        }

        public void initViewPagerSpeedSetter(ViewPager mViewPager) {
            try {
                Field mScroller = ViewPager.class.getDeclaredField("mScroller");
                mScroller.setAccessible(true);
                mScroller.set(mViewPager, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
