package com.asking.pad.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.asking.pad.app.commom.DeviceUtil;

import java.math.BigDecimal;

/**
 * Created by jswang on 2017/6/8.
 */

public class FoursquareImgeView extends ImageView {
    public FoursquareImgeView(Context context, AttributeSet attrs,
                              int defStyle) {
        super(context, attrs, defStyle);
    }

    public FoursquareImgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FoursquareImgeView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((childWidthSize * 1 / 2), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
