package com.asking.pad.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.math.BigDecimal;

/**
 * Created by jswang on 2017/6/8.
 */

public class HalfFoursquareRelativeLayout extends RelativeLayout {
    public HalfFoursquareRelativeLayout(Context context, AttributeSet attrs,
                                        int defStyle) {
        super(context, attrs, defStyle);
    }

    public HalfFoursquareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HalfFoursquareRelativeLayout(Context context) {
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);

       int height = new BigDecimal(childWidthSize).multiply(new BigDecimal(145))
               .divide(new BigDecimal(259),2,BigDecimal.ROUND_HALF_UP).intValue();

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
