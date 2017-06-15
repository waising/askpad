package com.asking.pad.app.ui.camera.utils;

import android.util.DisplayMetrics;

import com.asking.pad.app.AppContext;

/**
 * Created by jswang on 2017/2/16.
 */

public class ScreenUtils {
    public static DisplayMetrics getDisplayMetrics() {
        return AppContext.getInstance().getResources().getDisplayMetrics();
    }


    public static int getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return getDisplayMetrics().heightPixels;
    }



}
