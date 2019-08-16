package com.allintask.lingdao.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.allintask.lingdao.R;

/**
 * Created by TanJiaJun on 2018/1/8.
 */

public class RecordImageView extends View {

    private Context mContext;

    //表示音量等级
    private int level = 1;
    //最短的宽度
    private int baseWidth = 15;
    //宽度每个等级增长量
    private int widthIncrease = 4;
    //单位高度
    private int countHeight = 4;
    //每次增长的高度
    private int heightIncrease = 13;

    private int width = 60;
    private int height = getResources().getDimensionPixelOffset(R.dimen.record_image_view_size);

    public RecordImageView(Context context) {
        this(context, null);
    }

    public RecordImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, widthMeasureSpec), MeasureSpec.makeMeasureSpec(height, heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();
        p.setAntiAlias(true);// 设置画笔的锯齿效果
        p.setColor(Color.WHITE);// 设置白色
        p.setStyle(Paint.Style.FILL);//设置填满
        for (int i = 0; i < level; i++) {
            Path path = new Path();
            path.moveTo(0, height - i * heightIncrease);// 此点为多边形的起点
            path.lineTo(0, height - i * heightIncrease - countHeight);
            path.lineTo(baseWidth + i * widthIncrease, height - i * heightIncrease - countHeight);
            path.lineTo(baseWidth + i * widthIncrease, height - i * heightIncrease);
            path.close(); // 使这些点构成封闭的多边形
            canvas.drawPath(path, p);
        }
    }

    public void setLevel(int level) {
        this.level = level;
        postInvalidate();
    }

}
