package com.allintask.lingdao.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.allintask.lingdao.R;

/**
 * Created by zping on 2017/11/23.
 */

public class SearchView extends AppCompatEditText {

    private float searchSize = 0;
    private int searchIconResourceId;
    private float textSize = 0;
    private int textColor = 0xFF000000;
    private Drawable mDrawable;
    private Paint paint;

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initResource(context, attrs);
        initPaint();
    }

    private void initResource(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.searchedit);
        float density = context.getResources().getDisplayMetrics().density;
        searchSize = mTypedArray.getDimension(R.styleable.searchedit_imagewidth, 18 * density + 0.5F);
        searchIconResourceId = mTypedArray.getResourceId(R.styleable.searchedit_sv_search_icon_resource_id, R.mipmap.ic_search_gray);
        textColor = mTypedArray.getColor(R.styleable.searchedit_textColor, 0xFFb8beb4);
        textSize = mTypedArray.getDimension(R.styleable.searchedit_textSize, 14 * density + 0.5F);
        mTypedArray.recycle();
    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSearchIcon(canvas);
    }

    private void drawSearchIcon(Canvas canvas) {
        if (this.getText().toString().length() == 0) {
            float textWidth = paint.measureText("搜索服务、需求");
            float textHeight = getFontLeading(paint);

            float dx = (getWidth() - searchSize - textWidth - 8) / 2;
            float dy = (getHeight() - searchSize) / 2;

            canvas.save();
            canvas.translate(getScrollX() + dx, getScrollY() + dy);
            if (mDrawable != null) {
                mDrawable.draw(canvas);
            }
            canvas.drawText("搜索服务、需求", getScrollX() + searchSize + 8, getScrollY() + (getHeight() - (getHeight() - textHeight) / 2) - paint.getFontMetrics().bottom - dy, paint);
            canvas.restore();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mDrawable == null) {
            try {
                mDrawable = getContext().getResources().getDrawable(searchIconResourceId);
                mDrawable.setBounds(0, 0, (int) searchSize, (int) searchSize);
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mDrawable != null) {
            mDrawable.setCallback(null);
            mDrawable = null;
        }
        super.onDetachedFromWindow();
    }

    public float getFontLeading(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.bottom - fm.top;
    }

}
