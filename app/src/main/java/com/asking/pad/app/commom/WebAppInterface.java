package com.asking.pad.app.commom;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseActivity;
import com.asking.pad.app.ui.commom.PhotoShowActivity;
import com.asking.pad.app.ui.commom.WebDialog;


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
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("data:image/png;base64,")) {
                String data = url.replace("data:image/png;base64,","");
                FileUtils.writeBookImg(data,data.hashCode()+"",new ApiRequestListener<String>(){
                    @Override
                    public void onResultSuccess(String res) {
                        PhotoShowActivity.openActivity(res);
                    }
                });
            } else {
                PhotoShowActivity.openActivity(url);
            }
        }
    }

    @JavascriptInterface
    public void showTipDataLink(String id) {
        WebDialog selectDialog = WebDialog.newInstance(id);
        selectDialog.show(((BaseActivity)context).getSupportFragmentManager(),"WebDialog");
    }
}