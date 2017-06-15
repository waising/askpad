package com.asking.pad.app.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 可禁止右滑的viewPager
 * Created by linbin on 2017/3/4.
 */

public class NoRightClipViewPager extends ViewPager {
    /**
     * 是否可滑动,默认可以左右滑动
     */

    private boolean isCanScroll = true;


    /**
     * 上一次x坐标
     */
    private float beforeX;

    public boolean isScrollble() {
        return isCanScroll;
    }
    /**
     * 设置 是否可以左右都能滑动
     *
     * @param isCanScroll
     */
    public void setScrollble(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }


    /**
     * Constructor
     *
     * @param context the context
     */
    public NoRightClipViewPager(Context context) {
        super(context);
    }

    /**
     * Constructor
     *
     * @param context the context
     * @param attrs   the attribute set
     */
    public NoRightClipViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
////遍历viewpager的每个childView,找出高度最大的那个childView的高度，并把这个高度设置为viewpager的高度。
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int childSize = getChildCount();
//        int maxHeight = 0;
//        for (int i = 0; i < childSize; i++) {
//            View child = getChildAt(i);
//            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//            if (child.getMeasuredHeight() > maxHeight) {
//                maxHeight = child.getMeasuredHeight();
//            }
//        }
//
//        if (maxHeight > 0) {
//            setMeasuredDimension(getMeasuredWidth(), maxHeight);
//        }
//
//    }

    //-----禁止右滑-------
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isCanScroll) {//能左右滑动,调用父类方法
            return super.dispatchTouchEvent(ev);
        } else {//
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN://按下如果‘仅’作为‘上次坐标’，不妥，因为可能存在左滑，motionValue大于0的情况（来回滑，只要停止坐标在按下坐标的右边，左滑仍然能滑过去）
                    beforeX = ev.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float motionValue = ev.getX() - beforeX;
                    if (motionValue < 0) {//禁止滑到右边页
                        return true;
                    }
                    beforeX = ev.getX();//手指移动时，再把当前的坐标作为下一次的‘上次坐标’，解决上述问题
                    break;
                default:
                    break;
            }
            return super.dispatchTouchEvent(ev);
        }

    }
}
