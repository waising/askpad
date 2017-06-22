package com.asking.pad.app.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;


public class AskMathView extends RelativeLayout {
    private AskMathWebView web_name;
    private TextView tv_name;

    private String txtColor = "#333333";

    public AskMathView(Context context) {
        super(context);
        init(context);
    }

    public AskMathView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public AskMathView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_math_web_txt_view, this);
        web_name = (AskMathWebView)findViewById(R.id.math_web_name);
        tv_name = (TextView)findViewById(R.id.math_tv_name);

        web_name.setVisibility(View.GONE);
        tv_name.setVisibility(View.GONE);

        web_name.setOnTouchListener(getMathOnTouchListener());
        tv_name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.OnClick();
                }
            }
        });
    }

    private View.OnTouchListener getMathOnTouchListener(){
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()){
                    if(mListener != null){
                        mListener.OnClick();
                    }
                }
                return false;
            }
        };
    }

    public AskMathWebView showWebImage(Context context, final MultiStateView multiStateView){
        return web_name.showWebImage(context,multiStateView);
    }

    public AskMathWebView showWebImage(MultiStateView multiStateView){
        return web_name.showWebImage(multiStateView);
    }

    public AskMathWebView showWebImage(){
        return web_name.showWebImage(getContext(),null);
    }

    public AskMathWebView showWebImage(Context context){
        return web_name.showWebImage(context,null);
    }

    public void loadUrl(String url) {
        web_name.setVisibility(View.VISIBLE);
        web_name.setText(url);
    }

    public AskMathWebView setWebText(String text){
        setText(text);
        return web_name;
    }

    public void setTextColor(String txtColor) {
        this.txtColor = txtColor;
        tv_name.setTextColor(Color.parseColor(txtColor));
    }

    public void setText(String text) {
        web_name.setVisibility(View.GONE);
        tv_name.setVisibility(View.GONE);
        web_name.setText("");
        tv_name.setText("");
        if(!TextUtils.isEmpty(text)){
            if(CommonUtil.isEnglish(text)){
                web_name.setVisibility(View.VISIBLE);
                String color = TextUtils.isEmpty(txtColor)?"#333333":txtColor;
                web_name.setText(String.format(Constants.SuperTutorialHtmlCss2,color,text));
            }else{
                tv_name.setVisibility(View.VISIBLE);
                tv_name.setText(text);
            }
        }
    }

    public WebSettings getSettings(){
        return web_name.getSettings();
    }

    public void  setWebViewClient(WebViewClient client){
        web_name.setWebViewClient(client);
    }

    public void setWebChromeClient(WebChromeClient mClient){
        web_name.setWebChromeClient(mClient);
    }

    public AskMathWebView getMathWebView(){
        return web_name;
    }

    /**
     * 格式化数学公式
     * @return
     */
    public AskMathWebView formatMath(){
        return web_name.formatMath();
    }

    public String getText(){
        if(web_name.getVisibility() == View.VISIBLE){
            return  web_name.getText();
        }else{
            return tv_name.getText().toString();
        }
    }

    OnAskMathClickListener mListener;
    public void setOnAskMathClickListener(OnAskMathClickListener mListener){
        this.mListener = mListener;
    }
    public interface OnAskMathClickListener{
        void OnClick();
    }
}