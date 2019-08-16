package com.allintask.lingdao.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by TanJiaJun on 2017/3/30 0030.
 */

public class RoundRectImageView extends ImageView {

    private Paint paint;

    public RoundRectImageView(Context context) {
        super(context);

        init();
    }

    public RoundRectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public RoundRectImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(0xff424242);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();

        if (null != drawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap roundBitmap = getRoundBitmap(bitmap, 20);

            final Rect rectSrc = new Rect(0, 0, roundBitmap.getWidth(), roundBitmap.getHeight());
            final Rect rectDest = new Rect(0, 0, getWidth(), getHeight());

            paint.reset();

            canvas.drawBitmap(roundBitmap, rectSrc, rectDest, paint);
        } else {
            super.onDraw(canvas);
        }
    }

    private Bitmap getRoundBitmap(Bitmap bitmap, int roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

}
