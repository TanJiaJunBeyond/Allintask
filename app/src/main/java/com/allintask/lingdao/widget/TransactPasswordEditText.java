package com.allintask.lingdao.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.InputFilter;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.EditText;

import com.allintask.lingdao.R;

import cn.tanjiajun.sdk.common.utils.DensityUtils;

/**
 * Created by TanJiaJun on 2016/12/18.
 */

public class TransactPasswordEditText extends EditText {

    // 背景颜色
    private int backgroundColor;

    // 密码长度
    private int passwordTextLength = 6;

    // 密码文字颜色
    private int passwordTextColor;

    // 未输入时下划线颜色
    private int normalUnderlineColor;

    // 输入时下划线颜色
    private int inputUnderlineColor;

    // 背景的画笔
    private Paint backgroundPaint;

    // 密码的画笔
    private TextPaint passwordTextPaint;

    // 线的画笔
    private Paint linePaint;

    // 控件宽度
    private int width;

    // 控件高度
    private int height;

    // 输入密码的长度
    private int inputPasswordTextLength;

    public TransactPasswordEditText(Context context) {
        super(context);
        init(context, null);
    }

    public TransactPasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TransactPasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (null != attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TransactPasswordEditText);

            if (null != typedArray) {
                backgroundColor = typedArray.getColor(R.styleable.TransactPasswordEditText_tpet_background_color, getResources().getColor(R.color.white));
                passwordTextLength = typedArray.getInt(R.styleable.TransactPasswordEditText_tpet_password_text_length, 6);
                passwordTextColor = typedArray.getColor(R.styleable.TransactPasswordEditText_tpet_password_text_color, getResources().getColor(R.color.transact_password_edit_text_orange));
                normalUnderlineColor = typedArray.getColor(R.styleable.TransactPasswordEditText_tpet_normal_underline_color, getResources().getColor(R.color.transact_password_edit_text_gray));
                inputUnderlineColor = typedArray.getColor(R.styleable.TransactPasswordEditText_tpet_input_underline_color, getResources().getColor(R.color.transact_password_edit_text_orange));
            }
        }

        setTextSize(0);
        setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(passwordTextLength)
        });
        setCursorVisible(false);
        setLongClickable(false);

        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setDither(true);
        backgroundPaint.setColor(backgroundColor);

        passwordTextPaint = new TextPaint();
        passwordTextPaint.setAntiAlias(true);
        passwordTextPaint.setDither(true);
        passwordTextPaint.setColor(passwordTextColor);
        passwordTextPaint.setTextSize(100);
        passwordTextPaint.setTextAlign(Paint.Align.CENTER);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
        linePaint.setStrokeWidth(20);
        linePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));

        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBackground(canvas);
        drawPasswordText(canvas);
        drawLine(canvas);
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
            result = getPaddingLeft() + DensityUtils.dip2px(getContext(), 300) + getPaddingRight();

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
            result = specSize;
        } else {
            result = getPaddingTop() + DensityUtils.dip2px(getContext(), 40) + getPaddingBottom();

            if (MeasureSpec.AT_MOST == specMode) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawRect(0, 0, width, height, backgroundPaint);
    }

    private void drawPasswordText(Canvas canvas) {
        String password = getText().toString().trim();

        for (int i = 0; i < inputPasswordTextLength; i++) {
            char number = password.charAt(i);
            float x = DensityUtils.dip2px(getContext(), 5) + width * i / passwordTextLength + (width / passwordTextLength - width / passwordTextLength / 6) / 2;
            canvas.drawText(String.valueOf(number), x, height - DensityUtils.dip2px(getContext(), 15), passwordTextPaint);
        }
    }

    private void drawLine(Canvas canvas) {
        for (int i = 0; i <= passwordTextLength; i++) {
            float startX = DensityUtils.dip2px(getContext(), 5) + width * i / passwordTextLength;
            float stopX = startX + width / passwordTextLength - width / passwordTextLength / 6;

            if (i < inputPasswordTextLength) {
                linePaint.setColor(inputUnderlineColor);
            } else {
                linePaint.setColor(normalUnderlineColor);
            }

            canvas.drawLine(startX, height, stopX, height, linePaint);
        }
    }

    public void reset() {
        setText("");
        invalidate();
    }

}
