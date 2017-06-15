package com.asking.pad.app.widget;

/**
 * Created by jswang on 2017/5/2.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.asking.pad.app.R;
import com.asking.pad.app.ui.downbook.download.DownState;

public class RoundProgressBar extends View {
    private static Bitmap progress_start;
    private static Bitmap progress_stop1;
    private Paint paint;
    private int roundColor;
    private int roundProgressColor;
    private int roundTextColor;
    private float roundTextSize;
    private float roundWidth;
    private int max;
    private int progress;
    private boolean textIsDisplayable;
    private int style;

    private byte mDownState = DownState.START;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.paint = new Paint();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.RoundProgressBar);
        this.roundColor = obtainStyledAttributes.getColor(R.styleable.RoundProgressBar_roundColor, Color.parseColor("#ffffff"));
        this.roundProgressColor = obtainStyledAttributes.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.parseColor("#46C6FF"));
        this.roundTextColor = obtainStyledAttributes.getColor(R.styleable.RoundProgressBar_roundTextColor, Color.parseColor("#46C6FF"));
        this.roundTextSize = obtainStyledAttributes.getDimension(R.styleable.RoundProgressBar_roundTextSize, 15.0f);
        this.roundWidth = obtainStyledAttributes.getDimension(R.styleable.RoundProgressBar_roundWidth, 5.0f);
        this.max = obtainStyledAttributes.getInteger(R.styleable.RoundProgressBar_max, 100);
        this.textIsDisplayable = obtainStyledAttributes.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
        this.style = obtainStyledAttributes.getInt(R.styleable.RoundProgressBar_style, 0);
        obtainStyledAttributes.recycle();

        progress_start = BitmapFactory.decodeResource(getResources(), R.mipmap.progress_start);
        progress_stop1 = BitmapFactory.decodeResource(getResources(), R.mipmap.progress_stop1);
    }

    public synchronized void setDownPause(byte mDownState) {
        this.mDownState = mDownState;
        postInvalidate();
    }

    public synchronized byte getStop() {
        return this.mDownState;
    }

    public int getCricleColor() {
        return this.roundColor;
    }

    public int getCricleProgressColor() {
        return this.roundProgressColor;
    }

    public synchronized int getMax() {
        return max;
    }

    public synchronized int getProgress() {
        return this.progress;
    }

    public float getRoundWidth() {
        return this.roundWidth;
    }

    public int getTextColor() {
        return this.roundTextColor;
    }

    public float getTextSize() {
        return this.roundTextSize;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth() / 2;
        int i = (int) (((float) width) - (this.roundWidth / 2.0f));
        paint.setColor(this.roundColor);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(this.roundWidth);
        paint.setAntiAlias(true);
        canvas.drawCircle((float) width, (float) width, (float) i, paint);
        paint.setStrokeWidth(0.0f);
        paint.setColor(this.roundTextColor);
        paint.setTextSize(this.roundTextSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        int i2 = Math.round(100.0f * progress / max);
        float measureText = paint.measureText(new StringBuilder(String.valueOf(i2)).append("%").toString());
        if (this.textIsDisplayable && i2 >= 0 && this.style == 0) {
            if (mDownState == DownState.START) {
                canvas.drawBitmap(progress_start, (float) ((getWidth() / 2) - (progress_start.getWidth() / 2)), (float) ((getHeight() - progress_start.getHeight()) / 2), null);
            }else if (mDownState == DownState.PAUSE) {
                canvas.drawBitmap(progress_stop1, (float) ((getWidth() / 2) - (progress_stop1.getWidth() / 2)), (float) ((getHeight() - progress_stop1.getHeight()) / 2), null);
            } else {
                canvas.drawText(new StringBuilder(String.valueOf(i2)).append("%").toString(), ((float) width) - (measureText / 2.0f), ((float) width) + (this.roundTextSize / 2.0f), paint);
                paint.setStrokeWidth(this.roundWidth);
                paint.setColor(this.roundProgressColor);
                RectF rectF = new RectF((float) (width - i), (float) (width - i), (float) (width + i), (float) (width + i));
                switch (this.style) {
                    case 0 /*0*/:
                        paint.setStyle(Style.STROKE);
                        canvas.drawArc(rectF, -90, (float)Math.round(360.0f * progress / max), false, paint);
                        break;
                    case 1 /*1*/:
                        paint.setStyle(Style.FILL_AND_STROKE);
                        if (progress != 0) {
                            canvas.drawArc(rectF, -90, (float)Math.round(360.0f * progress / max), true, paint);
                        }
                        break;
                    default:
                }
            }
        }
    }

    public void setCricleColor(int roundColor) {
        this.roundColor = roundColor;
    }

    public void setCricleProgressColor(int roundProgressColor) {
        this.roundProgressColor = roundProgressColor;
    }

    public synchronized void setMax(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = i;
    }

    public synchronized void setProgress(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (i > this.max) {
            i = this.max;
        }
        if (i <= this.max) {
            progress = i;
            postInvalidate();
        }
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public void setTextColor(int roundTextColor) {
        this.roundTextColor = roundTextColor;
    }

    public void setTextSize(float roundTextSize) {
        this.roundTextSize = roundTextSize;
    }
}
