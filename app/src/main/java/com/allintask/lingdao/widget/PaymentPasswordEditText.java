package com.allintask.lingdao.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.EditText;

import com.allintask.lingdao.R;
import com.hyphenate.util.DensityUtil;

/**
 * Created by TanJiaJun on 2017/6/8.
 */

public class PaymentPasswordEditText extends EditText {

    /**
     * 背景矩形颜色
     */
    private int backgroundRectangleColor;

    /**
     * 背景线颜色
     */
    private int backgroundLineColor;

    /**
     * 密码圆形颜色
     */
    private int passwordCircleColor;

    /**
     * 密码圆形半径
     */
    private float passwordCircleRadius;

    /**
     * 密码长度
     */
    private int passwordLength = 6;

    /**
     * 背景矩形的画笔
     */
    private Paint backgroundRectanglePaint;

    /**
     * 背景线的画笔
     */
    private Paint backgroundLinePaint;

    /**
     * 密码圆形的画笔
     */
    private Paint passwordCirclePaint;

    /**
     * 控件宽度
     */
    private int width;

    /**
     * 控件高度
     */
    private int height;

    /**
     * 输入密码的长度
     */
    private int inputPasswordTextLength;

    public PaymentPasswordEditText(Context context) {
        super(context);
        init(context, null);
    }

    public PaymentPasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PaymentPasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PaymentPasswordEditText);

            if (null != typedArray) {
                backgroundRectangleColor = typedArray.getColor(R.styleable.PaymentPasswordEditText_ppet_background_rectangle_color, getResources().getColor(R.color.black));
                backgroundLineColor = typedArray.getColor(R.styleable.PaymentPasswordEditText_ppet_background_line_color, getResources().getColor(R.color.black));
                passwordCircleColor = typedArray.getColor(R.styleable.PaymentPasswordEditText_ppet_password_circle_color, getResources().getColor(R.color.black));
                passwordCircleRadius = typedArray.getDimension(R.styleable.PaymentPasswordEditText_ppet_password_circle_radius, 10);
                passwordLength = typedArray.getInt(R.styleable.PaymentPasswordEditText_ppet_password_length, 6);
            }
        }

        setTextSize(0);
        setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(passwordLength)
        });
        setCursorVisible(false);
        setLongClickable(false);

        backgroundRectanglePaint = new Paint();
        backgroundRectanglePaint.setAntiAlias(true);
        backgroundRectanglePaint.setDither(true);
        backgroundRectanglePaint.setStyle(Paint.Style.STROKE);
        backgroundRectanglePaint.setColor(backgroundRectangleColor);

        backgroundLinePaint = new Paint();
        backgroundLinePaint.setAntiAlias(true);
        backgroundLinePaint.setDither(true);
        backgroundLinePaint.setColor(backgroundLineColor);

        passwordCirclePaint = new Paint();
        passwordCirclePaint.setAntiAlias(true);
        passwordCirclePaint.setDither(true);
        passwordCirclePaint.setColor(passwordCircleColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(widthMeasureSpec));

        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawRectangleBackground(canvas);
        drawLine(canvas);
        drawPasswordText(canvas);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        inputPasswordTextLength = text.toString().trim().length();
        invalidate();
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result;

        if (MeasureSpec.EXACTLY == specMode) {
            result = specSize;
        } else {
            result = getPaddingLeft() + DensityUtil.dip2px(getContext(), 300) + getPaddingRight();

            if (MeasureSpec.AT_MOST == specMode) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result;

        if (MeasureSpec.EXACTLY == specMode) {
            result = specSize / passwordLength;
        } else {
            result = getPaddingTop() + DensityUtil.dip2px(getContext(), 50) + getPaddingBottom();

            if (MeasureSpec.AT_MOST == specMode) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private void drawRectangleBackground(Canvas canvas) {
        canvas.drawRect(0, 0, width, height, backgroundRectanglePaint);
    }

    private void drawLine(Canvas canvas) {
        for (int i = 0; i <= passwordLength; i++) {
            float x = width * i / passwordLength;
            canvas.drawLine(x, 0, x, height, backgroundLinePaint);
        }
    }

    private void drawPasswordText(Canvas canvas) {
        float y = height / 2;

        for (int i = 0; i < inputPasswordTextLength; i++) {
            float x = height / 2 + width * i / passwordLength;
            canvas.drawCircle(x, y, passwordCircleRadius, passwordCirclePaint);
        }
    }

    public void reset() {
        setText("");
        invalidate();
    }

}
