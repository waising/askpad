package com.asking.pad.app.commom;

import android.content.Context;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import com.asking.pad.app.ui.commom.PhotoShowActivity;



/**
 * Created by wxwang on 2016/11/25.
 */
public class WebAppInterface {
    private Context context;


    public WebAppInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void openImage(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.WEB_IMAGE_URL, url);
        CommonUtil.openActivity(PhotoShowActivity.class,bundle);
    }
}