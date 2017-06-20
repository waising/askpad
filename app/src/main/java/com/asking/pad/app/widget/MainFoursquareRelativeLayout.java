package com.asking.pad.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.asking.pad.app.commom.DeviceUtil;

import java.math.BigDecimal;

/**
 * Created by jswang on 2017/6/8.
 */

public class MainFoursquareRelativeLayout extends RelativeLayout {
    public MainFoursquareRelativeLayout(Context context, AttributeSet attrs,
                                        int defStyle) {
        super(context, attrs, defStyle);
    }

    public MainFoursquareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainFoursquareRelativeLayout(Context context) {
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

        int width = new BigDecimal(DeviceUtil.getScreenWidth(getContext())).multiply(new BigDecimal(249))
                .divide(new BigDecimal(1280),2,BigDecimal.ROUND_HALF_UP).intValue();

        int childWidthSize = width;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);

        int height = new BigDecimal(childWidthSize).multiply(new BigDecimal(286))
                .divide(new BigDecimal(249),2,BigDecimal.ROUND_HALF_UP).intValue();

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
