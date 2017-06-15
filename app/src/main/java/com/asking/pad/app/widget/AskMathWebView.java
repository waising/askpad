package com.asking.pad.app.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.WebAppClient;
import com.asking.pad.app.commom.WebAppInterface;
import com.x5.template.Chunk;
import com.x5.template.Theme;
import com.x5.template.providers.AndroidTemplates;


public class AskMathWebView extends WebView {
    private String mText;

    @SuppressLint("SetJavaScriptEnabled")
    public AskMathWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        getSettings().setJavaScriptEnabled(true);
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        getSettings().setDomStorageEnabled(true);
        getSettings().setBlockNetworkImage(false);
        setBackgroundColor(Color.TRANSPARENT);

        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        } else {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
    }

    @SuppressLint("AddJavascriptInterface")
    public AskMathWebView showWebImage(MultiStateView multiStateView){
        showWebImage(getContext(),multiStateView);
        return this;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    @SuppressLint("AddJavascriptInterface")
    public AskMathWebView showWebImage(Context context, final MultiStateView multiStateView){
        this.setWebViewClient(new WebAppClient(context, multiStateView, this));
        this.addJavascriptInterface(new WebAppInterface(context), Constants.PLATFORM);

        return this;
    }


    /**
     * 点击图片
     * @return
     */
    @SuppressLint("AddJavascriptInterface")
    public AskMathWebView showWebImage(){
        return showWebImage(getContext(),null);
    }

    @SuppressLint("AddJavascriptInterface")
    public AskMathWebView showWebImage(Context context){
        return showWebImage(context,null);
    }

    public AskMathWebView setWebText(String text){
        setText(text);
        return this;
    }

    public void setText(String text) {
        if(!TextUtils.isEmpty(text)){
            mText = CommonUtil.changePathImg(text);
            Chunk chunk = getChunk();
            String TAG_FORMULA = "formula";
            chunk.set(TAG_FORMULA, mText);
            this.loadDataWithBaseURL(null, chunk.toString(), "text/html", "utf-8", "about:blank");
        }
    }

    /**
     * 格式化数学公式
     * @return
     */
    public AskMathWebView formatMath(){
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setVerticalScrollBarEnabled(false);
        setVerticalScrollbarOverlay(false);
        setHorizontalScrollBarEnabled(false);
        setHorizontalScrollbarOverlay(false);
        return this;
    }


    private Chunk getChunk() {
        String TEMPLATE_MATHJAX = "mathjax";
        AndroidTemplates loader = new AndroidTemplates(getContext());
        return new Theme(loader).makeChunk(TEMPLATE_MATHJAX);
    }

    public String getText(){
        return mText;
    }

}