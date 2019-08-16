package com.allintask.lingdao.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.allintask.lingdao.R;

import java.util.ArrayList;
import java.util.List;

import cn.tanjiajun.sdk.common.utils.DensityUtils;

/**
 * 九宫解锁图
 *
 * @author TanJiaJun
 * @version 0.0.2
 */
public class LockPatternView extends View {

    // 3*3 解锁矩阵
    private static final int MATRIX_WIDTH = 3;

    private float[] moveNoPoint;
    private Point[][] pointArray;
    private List<Point> selectedPointList;

    private float squareWidth;
    private float squareHeight;

    private int minSelectedCircleNumber = 4;

    private int commonBigCircleColor;
    private int commonSmallCircleColor;

    private int errorBigCircleColor;
    private int errorSmallCircleColor;

    private int bigCircleRadius;
    private int smallCircleRadius;

    private int commonLineColor;
    private int errorLineColor;

    private float lineWidth;

    // 普通大圆形的画笔
    private Paint bigCirclePaint;

    // 普通小圆形的画笔
    private Paint smallCirclePaint;

    // 圆之间连线的画笔
    private Paint linePaint;

    private boolean showGesturePasswordPath = true;

    private OnPatternListener mOnPatternListener;

    public LockPatternView(Context context) {
        this(context, null);
    }

    public LockPatternView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LockPatternView, defStyleAttr, defStyleAttr);

        minSelectedCircleNumber = typedArray.getInteger(R.styleable.LockPatternView_lpv_min_selected_circle_number, 1);
        commonBigCircleColor = typedArray.getColor(R.styleable.LockPatternView_lpv_common_big_circle_color, getResources().getColor(R.color.lock_pattern_view_common_color));
        commonSmallCircleColor = typedArray.getColor(R.styleable.LockPatternView_lpv_common_small_circle_color, getResources().getColor(R.color.lock_pattern_view_common_color));
        errorBigCircleColor = typedArray.getColor(R.styleable.LockPatternView_lpv_error_big_circle_color, getResources().getColor(R.color.lock_pattern_view_error_color));
        errorSmallCircleColor = typedArray.getColor(R.styleable.LockPatternView_lpv_error_small_circle_color, getResources().getColor(R.color.lock_pattern_view_error_color));
        commonLineColor = typedArray.getColor(R.styleable.LockPatternView_lpv_common_line_color, getResources().getColor(R.color.lock_pattern_view_common_color));
        errorLineColor = typedArray.getColor(R.styleable.LockPatternView_lpv_error_line_color, getResources().getColor(R.color.lock_pattern_view_error_color));
        lineWidth = typedArray.getDimension(R.styleable.LockPatternView_lpv_line_width, 0);

        initPoints();

        bigCirclePaint = new Paint();
        bigCirclePaint.setAntiAlias(true);
        bigCirclePaint.setDither(true);
        bigCirclePaint.setStyle(Paint.Style.STROKE);
        bigCirclePaint.setStrokeWidth(2F);
        bigCirclePaint.setColor(commonBigCircleColor);

        smallCirclePaint = new Paint();
        smallCirclePaint.setAntiAlias(true);
        smallCirclePaint.setDither(true);
        smallCirclePaint.setColor(commonSmallCircleColor);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
        linePaint.setColor(commonLineColor);
        linePaint.setStrokeWidth(lineWidth);
    }

    private void initPoints() {
        int index = 0;
        pointArray = new Point[MATRIX_WIDTH][MATRIX_WIDTH];
        for (int i = 0; i < MATRIX_WIDTH; i++) {
            for (int j = 0; j < MATRIX_WIDTH; j++) {
                Point point = new Point(i, j);
                point.index = index++;
                pointArray[i][j] = point;
            }
        }

        moveNoPoint = new float[2];
        moveNoPoint[0] = -1;
        moveNoPoint[1] = -1;

        selectedPointList = new ArrayList<>();

        bigCircleRadius = DensityUtils.dip2px(getContext(), 30);
        smallCircleRadius = DensityUtils.dip2px(getContext(), 7);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int squareGap = DensityUtils.dip2px(getContext(), 70);
        squareWidth = bigCircleRadius + squareGap;
        squareHeight = squareWidth;
    }

    // TODO 优化：局部刷新
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画圆
        for (int i = 0; i < MATRIX_WIDTH; i++) {
            for (int j = 0; j < MATRIX_WIDTH; j++) {
                Point point = pointArray[i][j];
                if (null != point) {
                    float left = getCenterXForColumn(point.column) + getPaddingLeft();
                    float top = getCenterYForRow(point.row) + getPaddingTop();

                    switch (point.state) {
                        case Point.STATE_NORMAL:
                            canvas.drawCircle(left, top, bigCircleRadius, bigCirclePaint);
                            break;

                        case Point.STATE_FOCUS:
                            if (showGesturePasswordPath) {
                                canvas.drawCircle(left, top, bigCircleRadius, bigCirclePaint);
                                canvas.drawCircle(left, top, smallCircleRadius, smallCirclePaint);
                            } else {
                                canvas.drawCircle(left, top, bigCircleRadius, bigCirclePaint);
                            }
                            break;

                        case Point.STATE_ERROR:
                            if (showGesturePasswordPath) {
                                canvas.drawCircle(left, top, bigCircleRadius, bigCirclePaint);
                                canvas.drawCircle(left, top, smallCircleRadius, smallCirclePaint);
                            } else {
                                canvas.drawCircle(left, top, bigCircleRadius, bigCirclePaint);
                            }
                            break;

                        default:
                            break;
                    }
                }
            }
        }

        if (showGesturePasswordPath) {
            // 画线
            if (null != selectedPointList && selectedPointList.size() > 0) {
                if (selectedPointList.size() > 1) {
                    for (int i = 1; i < selectedPointList.size(); i++) {
                        Point beforePoint = selectedPointList.get(i - 1);
                        Point afterPoint = selectedPointList.get(i);

                        if (null != beforePoint && null != afterPoint) {
                            float[] beforeSet = new float[]{getCenterXForColumn(beforePoint.column), getCenterYForRow(beforePoint.row)};
                            float[] afterSet = new float[]{getCenterXForColumn(afterPoint.column), getCenterYForRow(afterPoint.row)};
                            float[][] target = convertPointToCoordinates(beforeSet, afterSet);
                            canvas.drawLine(target[0][0], target[0][1], target[1][0], target[1][1], linePaint);
                        }
                    }
                }

                if (moveNoPoint[0] != -1 && moveNoPoint[1] != -1) {
                    Point lastPoint = selectedPointList.get(selectedPointList.size() - 1);
                    if (null != lastPoint) {
                        float[] beforeSet = new float[]{getCenterXForColumn(lastPoint.column), getCenterYForRow(lastPoint.row)};
                        float[][] target = convertPointToCoordinates(beforeSet, moveNoPoint);
                        canvas.drawLine(target[0][0], target[0][1], moveNoPoint[0], moveNoPoint[1], linePaint);
                    }
                }
            }
        }
    }

    private float[][] convertPointToCoordinates(float[] beforeSet, float[] afterSet) {
        float beforeX;
        float beforeY;
        float afterX;
        float afterY;

        float beforeCenterX = beforeSet[0];
        float beforeCenterY = beforeSet[1];
        float afterCenterX = afterSet[0];
        float afterCenterY = afterSet[1];

        if (beforeCenterY == afterCenterY) { /* 同一行 */
            if (beforeCenterX < afterCenterX) {
                beforeX = beforeCenterX + bigCircleRadius;
                afterX = afterCenterX - bigCircleRadius;
            } else {
                beforeX = beforeCenterX - bigCircleRadius;
                afterX = afterCenterX + bigCircleRadius;
            }
            beforeY = afterY = afterCenterY;
        } else if (beforeCenterX == afterCenterX) { /* 同一列 */
            if (beforeCenterY < afterCenterY) {
                beforeY = beforeCenterY + bigCircleRadius;
                afterY = afterCenterY - bigCircleRadius;
            } else {
                beforeY = beforeCenterY - bigCircleRadius;
                afterY = afterCenterY + bigCircleRadius;
            }
            beforeX = afterX = afterCenterX;
        } else { /* 不同行并且不同列 */
            float y = afterCenterY - beforeCenterY;
            float x = afterCenterX - beforeCenterX;
            float offsetX = (float) (Math.abs(x) / Math.sqrt(x * x + y * y) * bigCircleRadius);
            float offsetY = (float) (Math.abs(y) / Math.sqrt(x * x + y * y) * bigCircleRadius);

            if (beforeCenterX < afterCenterX) {
                if (beforeCenterY < afterCenterY) {
                    beforeX = beforeCenterX + offsetX; /* 第四象限 */
                    beforeY = beforeCenterY + offsetY;
                    afterX = afterCenterX - offsetX; /* 第二象限 */
                    afterY = afterCenterY - offsetY;
                } else {
                    beforeX = beforeCenterX + offsetX; /* 第一象限 */
                    beforeY = beforeCenterY - offsetY;
                    afterX = afterCenterX - offsetX; /* 第三象限 */
                    afterY = afterCenterY + offsetY;
                }
            } else {
                if (beforeCenterY < afterCenterY) {
                    beforeX = beforeCenterX - offsetX; /* 第三象限 */
                    beforeY = beforeCenterY + offsetY;
                    afterX = afterCenterX + offsetX; /* 第一象限 */
                    afterY = afterCenterY - offsetY;
                } else {
                    beforeX = beforeCenterX - offsetX; /* 第二象限 */
                    beforeY = beforeCenterY - offsetY;
                    afterX = afterCenterX + offsetX; /* 第四象限 */
                    afterY = afterCenterY + offsetY;
                }
            }
        }

        return new float[][]{{beforeX, beforeY}, {afterX, afterY}};
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleActionDown(event);
                break;

            case MotionEvent.ACTION_MOVE:
                handleActionMove(event);
                break;

            case MotionEvent.ACTION_UP:
                handleActionUp(event);
                break;

            default:
                break;
        }
        return true;
    }

    /**
     * Method to find the row that y falls into.
     *
     * @param y The y coordinate
     * @return The row that y falls in, or -1 if it falls in no row.
     */
    private int getRowHit(float y) {
        float hitSize = bigCircleRadius * 2; /* effective area */
        float offset = getPaddingTop() + (squareHeight - hitSize) / 2;

        for (int i = 0; i < MATRIX_WIDTH; i++) {
            float hitTop = offset + squareHeight * i;
            if (y >= hitTop && y <= hitTop + hitSize) { /*
                                                         * whether y falls into
                                                         * the effective area
                                                         */
                return i;
            }
        }
        return -1;
    }

    /**
     * Method to find the column x falls into.
     *
     * @param x The x coordinate
     * @return The column that x falls in, or -1 if it falls in no column.
     */
    private int getColumnHit(float x) {
        float hitSize = bigCircleRadius * 2;
        float offset = getPaddingLeft() + (squareWidth - hitSize) / 2;

        for (int i = 0; i < MATRIX_WIDTH; i++) {
            float hitLeft = offset + squareWidth * i;
            if (x >= hitLeft && x <= hitLeft + hitSize) {
                return i;
            }
        }
        return -1;
    }

    private Point checkForNewHit(float x, float y) {
        int row = getRowHit(y);

        if (row == -1) {
            return null;
        }

        int column = getColumnHit(x);

        if (column == -1) {
            return null;
        }

        return of(row, column);
    }

    private void handleActionDown(MotionEvent event) {
        reset();

        float x = event.getX();
        float y = event.getY();

        Point mDownPoint = checkForNewHit(x, y);

        if (null != mDownPoint) {
            mDownPoint.state = Point.STATE_FOCUS;
            selectedPointList.add(mDownPoint);

            if (null != mOnPatternListener) {
                mOnPatternListener.onPatternStart();
            }
        }

        postInvalidate();
    }

    private void handleActionMove(MotionEvent event) {
        boolean isDownCheck = (null != selectedPointList && selectedPointList.size() > 0) ? true : false;

        if (isDownCheck) {
            float x = event.getX();
            float y = event.getY();
            Point mMovePoint = checkForNewHit(x, y);

            if (null == mMovePoint) {
                if (null != moveNoPoint) {
                    moveNoPoint[0] = x;
                    moveNoPoint[1] = y;
                }
            } else {
                if (mMovePoint.state != Point.STATE_FOCUS) {
                    mMovePoint.state = Point.STATE_FOCUS;
                    selectedPointList.add(mMovePoint);
                }

                if (null != moveNoPoint) {
                    moveNoPoint[0] = -1;
                    moveNoPoint[1] = -1;
                }

            }

            postInvalidate();
        }
    }

    private void handleActionUp(MotionEvent event) {
        if (null != moveNoPoint) {
            moveNoPoint[0] = -1;
            moveNoPoint[1] = -1;
        }

        if (null != selectedPointList) {
            if (selectedPointList.size() < minSelectedCircleNumber) {
                Toast.makeText(getContext(), "应至少连接" + minSelectedCircleNumber + "点", Toast.LENGTH_SHORT).show();

                reset();
            } else {
                String gesturePassword = convertPasswordIndexListToString(getCompletePatternIndexes());

                if (null != mOnPatternListener) {
                    mOnPatternListener.onPatternComplete(getCompletePatternIndexes(), gesturePassword);
                }
            }
        }

        postInvalidate();
    }

    private List<Integer> getCompletePatternIndexes() {
        List<Integer> indexList = new ArrayList<>();

        if (null != selectedPointList) {
            for (Point point : selectedPointList) {
                if (null != point) {
                    indexList.add(point.index);
                }
            }
        }
        return indexList;
    }

    private String convertPasswordIndexListToString(List<Integer> indexList) {
        StringBuilder passwordSB = new StringBuilder();

        if (null != indexList) {
            for (int index : indexList) {
                index++;
                passwordSB.append(index);
            }
        }
        return passwordSB.toString();
    }

    /**
     * reset params
     */
    public void reset() {
        for (int i = 0; i < MATRIX_WIDTH; i++) {
            for (int j = 0; j < MATRIX_WIDTH; j++) {
                pointArray[i][j].reset();
            }
        }

        selectedPointList.clear();

        if (null != mOnPatternListener) {
            mOnPatternListener.onPatternCleared();
        }

        postInvalidate();
    }

    private float getCenterXForColumn(int column) {
        return getPaddingLeft() + column * squareWidth + squareWidth / 2f;
    }

    private float getCenterYForRow(int row) {
        return getPaddingTop() + row * squareHeight + squareHeight / 2f;
    }

    /**
     * Search the specified (row, column) position of Point
     *
     * @param row    which row
     * @param column which column
     * @return point
     */
    private Point of(int row, int column) {
        return pointArray[row][column];
    }

    private class Point {

        public static final int STATE_NORMAL = 0;
        public static final int STATE_FOCUS = 1;
        public static final int STATE_ERROR = -1;

        public int row; /* 取值：[0, MATRIX_WIDTH) */
        public int column; /* 取值：[0, MATRIX_WIDTH) */
        public int state = STATE_NORMAL;
        public int index;   /* [0, 8] */

        private Point(int row, int column) {
            checkRange(row, column);
            this.row = row;
            this.column = column;
        }

        /**
         * reset points state
         */
        public void reset() {
            this.state = STATE_NORMAL;

            bigCirclePaint.setColor(commonBigCircleColor);
            smallCirclePaint.setColor(commonSmallCircleColor);
            linePaint.setColor(commonLineColor);
        }

        private void checkRange(int row, int column) {
            if (row < 0 || row > MATRIX_WIDTH - 1) {
                throw new IllegalArgumentException("row must be in range 0-" + (MATRIX_WIDTH - 1));
            }
            if (column < 0 || column > MATRIX_WIDTH - 1) {
                throw new IllegalArgumentException("column must be in range 0-" + (MATRIX_WIDTH - 1));
            }
        }
    }

    public void setShowGesturePasswordPath(boolean showGesturePasswordPathPath) {
        this.showGesturePasswordPath = showGesturePasswordPathPath;
    }

    public void setOnPatternListener(OnPatternListener mOnPatternListener) {
        this.mOnPatternListener = mOnPatternListener;
    }

    public void setErrorStatus() {
        for (int i = 0; i < MATRIX_WIDTH; i++) {
            for (int j = 0; j < MATRIX_WIDTH; j++) {
                Point point = pointArray[i][j];
                int index = point.index;

                for (int k = 0; k < selectedPointList.size(); k++) {
                    Point selectedPoint = selectedPointList.get(k);

                    if (null != selectedPoint) {
                        int selectedIndex = selectedPoint.index;

                        if (selectedIndex == index) {
                            point.state = Point.STATE_ERROR;
                        }
                    }
                }
            }
        }

        bigCirclePaint.setColor(errorBigCircleColor);
        smallCirclePaint.setColor(errorSmallCircleColor);
        linePaint.setColor(errorLineColor);

        postInvalidate();
    }

    public interface OnPatternListener {

        void onPatternStart();

        void onPatternCleared();

        void onPatternComplete(List<Integer> indexList, String gesturePassword);

    }

}
