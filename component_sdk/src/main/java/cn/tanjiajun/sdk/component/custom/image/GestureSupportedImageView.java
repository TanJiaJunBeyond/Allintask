package cn.tanjiajun.sdk.component.custom.image;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import cn.tanjiajun.sdk.component.custom.guideview.ViewPagerImproved;

/**
 * 支持手势操作的图片控件
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public class GestureSupportedImageView extends ImageView {

    private int imageWidth;
    private int imageHeight;
    private int screenWidth;
    private int screenHeight;

    private float minZoom = 1.0f;
    private float maxZoom = 5.0f;

    private float initTransX = 0f;

    private ViewPagerImproved viewPagerImproved;

    public GestureSupportedImageView(Context context) {
        super(context);
        init(context);
    }

    public GestureSupportedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
    }

    public void setViewPagerImproved(ViewPagerImproved viewPagerImproved) {
        this.viewPagerImproved = viewPagerImproved;
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        this.screenHeight = MeasureSpec.getSize(heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        layoutCenter();

    }

    private void layoutCenter() {
        Drawable drawable = getDrawable();

        if (null != drawable) {
            layoutCenter(getDrawable().getIntrinsicWidth(), getDrawable().getIntrinsicHeight());
        }
    }

    private void init(Context context) {
        setScaleType(ScaleType.MATRIX);
        setOnTouchListener(new ImageViewGestureSupportedOnTouchListener());
        setLongClickable(true);
    }

    private void layoutCenter(int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;

        Matrix matrix = new Matrix();
        matrix.postTranslate((screenWidth - imageWidth) / 2, (screenHeight - imageHeight) / 2);

        float scaleX = (screenWidth * 1f) / imageWidth;
        float scaleY = (screenHeight * 1f) / imageHeight;

        minZoom = scaleX;
        maxZoom = 2 * (scaleX > scaleY ? scaleX : scaleY);

        matrix.postScale(minZoom, minZoom, screenWidth / 2, screenHeight / 2);

        float[] values = new float[9];
        matrix.getValues(values);
        this.initTransX = values[Matrix.MTRANS_X];
        setImageMatrix(matrix);
    }

    private class ImageViewGestureSupportedOnTouchListener implements OnTouchListener {

        private static final int NONE = 0;
        private static final int DRAG = 1;
        private static final int ZOOM = 2;

        private Matrix matrix = new Matrix();
        private Matrix savedMatrix = new Matrix();

        private int mode = NONE;

        // Remember some things for zooming
        private PointF start = new PointF();
        private PointF mid = new PointF();

        private float oldDist = 1f;

        private long lastClickTime = 0;
        private long lastDoubleClickTime = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    long currentClickTime = event.getEventTime();

                    if (currentClickTime - lastClickTime < 300 && currentClickTime - lastDoubleClickTime > 500) {
                        lastDoubleClickTime = currentClickTime;

                        float[] values = new float[9];

                        matrix.set(savedMatrix);
                        matrix.getValues(values);

                        float transX = 0;
                        float transY = 0;
                        float scale = values[Matrix.MSCALE_X];
                        float zoomScale = minZoom;

                        if (scale == minZoom) {
                            zoomScale = maxZoom;
                            transX = (screenWidth / 2 - event.getX()) * (maxZoom / minZoom - 1);
                        }

                        matrix.reset();
                        matrix.postTranslate((screenWidth - imageWidth) / 2, (screenHeight - imageHeight) / 2);
                        matrix.postScale(zoomScale, zoomScale, screenWidth / 2, screenHeight / 2);
                        matrix.postTranslate(transX, transY);

                        mode = NONE;
                    } else {
                        matrix.set(getImageMatrix());
                        savedMatrix.set(matrix);
                        start.set(event.getX(), event.getY());
                        mode = DRAG;
                    }

                    lastClickTime = currentClickTime;
                    break;

                case MotionEvent.ACTION_POINTER_DOWN: // 多点触控
                    oldDist = spacing(event);

                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) { // 此实现图片的拖动功能
                        matrix.set(savedMatrix);

                        float[] values = new float[9];
                        matrix.getValues(values);

                        float lastTransX = values[Matrix.MTRANS_X];
                        float lastTransY = values[Matrix.MTRANS_Y];
                        float lastScaleX = values[Matrix.MSCALE_X];
                        float lastScaleY = values[Matrix.MSCALE_Y];

                        float transX = event.getX() - start.x;

                        if (lastTransX + transX - initTransX > 0) {
                            transX = initTransX - lastTransX;
                        } else if (imageWidth * lastScaleX + lastTransX + transX < screenWidth - initTransX) {
                            transX = screenWidth - initTransX - imageWidth * lastScaleX - lastTransX;
                        }

                        float initTransY = 0;
                        float transY = event.getY() - start.y;

                        if (lastScaleY < (screenHeight * 1f) / imageHeight) {
                            transY = 0;
                        } else {
                            if (lastTransY + transY - initTransY > 0) {
                                transY = initTransY - lastTransY;
                            } else if (imageHeight * lastScaleY + lastTransY + transY < screenHeight - initTransY) {
                                transY = screenHeight - initTransY - imageHeight * lastScaleY - lastTransY;
                            }
                        }

                        matrix.postTranslate(transX, transY);

                    } else if (mode == ZOOM) { // 此实现图片的缩放功能
                        float newDist = spacing(event);

                        if (newDist > 10) {
                            matrix.set(savedMatrix);

                            float[] values = new float[9];
                            matrix.getValues(values);

                            float oldScale = values[Matrix.MSCALE_X];
                            float scale = newDist / oldDist * oldScale;

                            if (scale > maxZoom) {
                                scale = maxZoom;
                            } else if (scale < minZoom) {
                                scale = minZoom;
                            }

                            matrix.reset();
                            matrix.postTranslate((screenWidth - imageWidth) / 2, (screenHeight - imageHeight) / 2);
                            matrix.postScale(scale, scale, screenWidth / 2, screenHeight / 2);
                        }
                    }
                    break;
            }

            setImageMatrix(matrix);

            if (null != viewPagerImproved) {
                float[] values = new float[9];
                matrix.getValues(values);

                if (minZoom == values[Matrix.MSCALE_X]) {
                    viewPagerImproved.setScrollable(true);
                } else {
                    viewPagerImproved.setScrollable(false);
                }
            }
            return true;
        }

        private float spacing(MotionEvent event) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        }

        private void midPoint(PointF point, MotionEvent event) {
            float x = event.getX(0) + event.getX(1);
            float y = event.getY(0) + event.getY(1);
            point.set(x / 2, y / 2);
        }
    }

}
