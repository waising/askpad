package com.asking.pad.app.ui.camera.utils;

import android.content.Context;


/**
 * Created by jswang on 2017/2/17.
 */

public class APPUtil {
    public static int dpToPx(Context context, float f) {
        return (int) ((context.getResources().getDisplayMetrics().density * f) + 0.5f);
    }

}
