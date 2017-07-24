package com.asking.pad.app.widget.banner;

import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.widget.banner.utils.DensityUtil;

public class PagerControl extends LinearLayout {
    private int pagerDotsResId;
    private int b;
    private boolean c;
    private int d;
    private int currentIndex;
    private int f;
    private PageIndicator pagerIndicator;

    public PagerControl(Context context) {
        super(context);
        this.b = 3;
        this.c = false;
        this.f = 0;
        this.b = (int) context.getResources().getDimension(R.dimen.banner_indicator_margin);
    }

    public PagerControl(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.b = 3;
        this.c = false;
        this.f = 0;
        this.b = (int) context.getResources().getDimension(R.dimen.banner_indicator_margin);
    }

    private void c() {
        setFocusable(false);
        setWillNotDraw(false);
        pagerDotsResId = R.drawable.pager_dots;
    }

    private void d() {
    	pagerIndicator = new PageIndicator(this, this.getContext());
    	pagerIndicator.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtil.getFitDensity(this.getContext(),3.0f)));
        addView(pagerIndicator);
    }

    private void e() {
        int i = 0;
        int i2 = 0;
        while (i < getChildCount()) {
            View childAt = getChildAt(i);
            if (childAt instanceof ImageView) {
                TransitionDrawable transitionDrawable = (TransitionDrawable) ((ImageView) childAt).getDrawable();
                if (i - i2 == currentIndex) {
                    transitionDrawable.startTransition(50);
                } else {
                    transitionDrawable.resetTransition();
                }
            } else {
                i2++;
            }
            i++;
        }
    }

    private void f() {
        View childAt = getChildAt(0);
        if (childAt instanceof TextView) {
            ((TextView) childAt).setText((currentIndex + 1) + "/" + this.d);
        }
    }

    public void a() {
        removeAllViews();
        int a = DensityUtil.getFitDensity(getContext(),(float) this.b);
        new LinearLayout(getContext()).setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        int childCount = getChildCount();
        int i = childCount;
        while (i < this.d + childCount) {
        	ImageView imageView = new ImageView(getContext());
            TransitionDrawable transitionDrawable = (TransitionDrawable) getResources().getDrawable(pagerDotsResId);
            transitionDrawable.setCrossFadeEnabled(true);
            imageView.setImageDrawable(transitionDrawable);
            LayoutParams layoutParams = new  LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(a, a, a, 0);
            imageView.setLayoutParams(layoutParams);
            if (i - childCount == currentIndex) {
                ((TransitionDrawable) imageView.getDrawable()).startTransition(50);
            }
            addView(imageView, i);
            i++;
        }
        postInvalidate();
    }

    public void a(int i) {
        this.f = i;
        this.c = true;
        switch (i) {
            case 0:
                d();
                break;
            case 1:
                c();
                break;
            default:
                break;
        }
    }

    public void b() {
    	TextView textView = new TextView(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT
        		, LayoutParams.WRAP_CONTENT);
        int a = DensityUtil.getFitDensity(getContext(),(float) this.b);
        layoutParams.setMargins(a, a, a, a);
        textView.setLayoutParams(layoutParams);
        textView.setText((currentIndex + 1) + "/" + this.d);
        addView(textView);
        postInvalidate();
    }

    public void b(int i) {
        if (this.f == 0) {
        	pagerIndicator.c(i);
        }
    }

    public int getCurrentPage() {
        return currentIndex;
    }

    public int getIndicatorType() {
        return this.f;
    }

    public int getNumPages() {
        return this.d;
    }

    public void setCurrentPager(int position) {
        if (position != currentIndex) {
        	currentIndex = position;
            switch (this.f) {
                case 0:
                	pagerIndicator.b(position);
                	break;
                case 1:
                    e();
                    break;
                case 2:
                    f();
                    break;
                default:
                    break;
            }
        }
    }

    public void setNumPages(int i) {
        if (!this.c) {
            throw new IllegalArgumentException("没有明确初始化数据，请调用lazyInit()方法进行初始化参数。");
        } else if (i > 0) {
            this.d = i;
            switch (this.f) {
                case 0:
                	pagerIndicator.a(i);
                	break;
                case 1:
                    a();
                    break;
                case 2:
                    b();
                    break;
                default:
                    break;
            }
        }
    }
}