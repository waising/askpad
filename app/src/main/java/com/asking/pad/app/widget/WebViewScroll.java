package com.asking.pad.app.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.asking.pad.app.R;
import com.asking.pad.app.commom.FileUtils;

/**
 * Created by jswang on 2017/8/31.
 */

public class WebViewScroll extends WebView {

    @SuppressLint("SetJavaScriptEnabled")
    public WebViewScroll(Context context, AttributeSet attrs) {
        super(context, attrs);

        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setUseWideViewPort(true);
        getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        getSettings().setAllowFileAccess(true);
        getSettings().setAppCacheEnabled(true);

        getSettings().setSupportZoom(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getSettings().setDisplayZoomControls(false);
        } else {
            getSettings().setBuiltInZoomControls(true);
        }
        getSettings().setGeolocationEnabled(true);
        getSettings().setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }


    public void setMathText(String text) {
        StringBuilder sb = new StringBuilder();
        String headhtml = FileUtils.readRawText(R.raw.header);
        String footerhtml = FileUtils.readRawText(R.raw.footer);
        sb.append(headhtml).append(text).append(footerhtml);
        String htmlStr = sb.toString();
        loadDataWithBaseURL(null, htmlStr, "text/html", "utf-8", null);
    }
}