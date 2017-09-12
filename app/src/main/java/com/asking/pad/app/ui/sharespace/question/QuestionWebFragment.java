package com.asking.pad.app.ui.sharespace.question;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.api.Networks;
import com.asking.pad.app.base.BaseFragment;
import com.asking.pad.app.commom.FileUtils;
import com.asking.pad.app.entity.QuestionEntity;
import com.asking.pad.app.ui.commom.PhotoShowActivity;
import com.asking.pad.app.ui.sharespace.QuestionAnwserActivity;
import com.asking.pad.app.widget.MultiStateView;
import com.asking.pad.app.widget.WebViewScroll;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Cookie;

/**
 * Created by jswang on 2017/8/29.
 */

public class QuestionWebFragment extends BaseFragment {

    @BindView(R.id.webview)
    WebViewScroll webview;

    @BindView(R.id.load_View)
    MultiStateView load_View;

    int dataType;

    public static QuestionWebFragment newInstance(int dataType) {
        QuestionWebFragment fragment = new QuestionWebFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("dataType", dataType);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_questions_web);
        ButterKnife.bind(this, getContentView());

        Bundle bundle = getArguments();
        if (bundle != null) {
            dataType = bundle.getInt("dataType");
        }
    }

    @Override
    public void initView() {
        super.initView();
        webview.addJavascriptInterface(new WebAppInterface(), "WebAppInterface");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (load_View.getViewState() == MultiStateView.VIEW_STATE_LOADING) {
                    load_View.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                load_View.setViewState(MultiStateView.VIEW_STATE_ERROR);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                load_View.setViewState(MultiStateView.VIEW_STATE_ERROR);
            }
        });
        load_View.setErrorRefBtnTxt2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshPage();
            }
        });
        refreshPage();
    }

    private void refreshPage() {
        load_View.setViewState(MultiStateView.VIEW_STATE_LOADING);
        switch (dataType) {
            case 0:
                webview.loadUrl("https://apis.91asking.com/communionapi/test1.html");
                break;
            case 1:
                webview.loadUrl("https://apis.91asking.com/communionapi/reward.html");
                break;
            case 2:
                webview.loadUrl("https://apis.91asking.com/communionapi/accept.html");
                break;
            case 3:
                webview.loadUrl("https://apis.91asking.com/communionapi/myask.html");
                break;
        }
    }

    public class WebAppInterface {

        @JavascriptInterface
        public void openImage(String url) {
            if (!TextUtils.isEmpty(url)) {
                if (url.startsWith("data:image/png;base64,")) {
                    String data = url.replace("data:image/png;base64,", "");
                    FileUtils.writeBookImg(data, data.hashCode() + "", new ApiRequestListener<String>() {
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
        public void openActivity(String str) {
            QuestionEntity e = JSON.parseObject(str, QuestionEntity.class);
            QuestionAnwserActivity.openActivity(0, e);
        }

        @JavascriptInterface
        public void onReceivedError(final int type) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (type == -1) {
                        load_View.setViewState(MultiStateView.VIEW_STATE_ERROR);
                    } else {
                        load_View.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                    }
                }
            });
        }

        @JavascriptInterface
        public String getCookies() {
            String cookies = "";
            try {
                Cookie e = Networks.cookies.get(0);
                cookies = e.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return cookies;
        }
    }
}






























