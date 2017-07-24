package com.asking.pad.app.widget.banner.utils;

import android.content.Context;
import android.view.WindowManager;

public class DensityUtil {

	public static int getFitDensity(Context context, float f) {
		return (int) (context.getResources().getDisplayMetrics().density * f + 0.5f);
	}

	public static int getWindowWidth(Context context) {
		return ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getWidth();
	}

	public static int getWindowHeight(Context context) {
		return ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getHeight();
	}

}