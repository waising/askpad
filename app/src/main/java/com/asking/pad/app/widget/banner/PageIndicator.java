package com.asking.pad.app.widget.banner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

class PageIndicator extends View {
    final PagerControl pagerControl;
    private int b;
    private int c;
    private int d;
    private Paint e;
    private Paint f;
    private int g;
    private int h;
    private float i;
    private Animation animation;

    public PageIndicator(PagerControl pagerControl, Context context) {
    	super(context);
        this.pagerControl = pagerControl;
        init();
    }

    private void init() {
        this.g = 0;
        this.h = 0;
        this.i = 0.0f;
        this.e = new Paint();
        this.e.setColor(-1435011209);
        this.f = new Paint();
        this.f.setColor(-1442775296);
        animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration((long) this.h);
        animation.setRepeatCount(0);
        animation.setInterpolator(new LinearInterpolator());
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
    }

    private void c() {
        if (this.h > 0) {
            clearAnimation();
            animation.setStartTime(AnimationUtils.currentAnimationTimeMillis() + ((long) this.g));
            setAnimation(animation);
        }
    }

    public int a() {
        return getWidth() / this.b;
    }

    public void a(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("numPages must be positive");
        }
        this.b = i;
        invalidate();
        c();
    }

    public void b(int i) {
        if (i < 0 || i >= this.b) {
            throw new IllegalArgumentException("currentPage parameter out of bounds");
        } else if (this.c != i) {
            this.c = i;
            this.d = a() * i;
            invalidate();
            c();
        }
    }

    public void c(int i) {
        if (this.d != i) {
            this.d = i;
            invalidate();
            c();
        }
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(new RectF(0.0f, 0.0f, (float) getWidth(), (float) getHeight()), this.i, this.i, this.e);
        canvas.drawRoundRect(new RectF((float) this.d, 0.0f, (float) (this.d + getWidth() / this.b), (float) getHeight()), this.i, this.i, this.f);
    }
}