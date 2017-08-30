package com.asking.pad.app.ui.sharespace.question;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFragment;
import com.asking.pad.app.commom.WebAppInterface;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/8/29.
 */

public class QuestionWebFragment extends BaseFragment {
//
//    @BindView(R.id.load_view)
//    MultiStateView load_view;

    @BindView(R.id.webview)
    WebView webview;

    String dataType = "9";

    public static QuestionWebFragment newInstance(String dataType) {
        QuestionWebFragment fragment = new QuestionWebFragment();
        Bundle bundle = new Bundle();
        bundle.putString("dataType", dataType);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_questions_web);
        ButterKnife.bind(this, getContentView());

        Bundle bundle = getArguments();
        if (bundle != null) {
            dataType = bundle.getString("dataType");
        }
    }

    @Override
    public void initView() {
        super.initView();

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setAppCacheEnabled(true);

        webview.setHorizontalScrollBarEnabled(false);
        webview.setHorizontalScrollbarOverlay(false);

        webview.getSettings().setSupportZoom(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webview.getSettings().setDisplayZoomControls(false);
        } else {
            webview.getSettings().setBuiltInZoomControls(true);
        }
        webview.getSettings().setGeolocationEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        // webview.setWebViewClient(new WebAppClient(getActivity(), load_view, webview));

        webview.addJavascriptInterface(new WebAppInterface(getActivity()), "WebAppInterface");

        //webview.getLayoutParams().width = 1500;

        webview.loadUrl("https://apis.91asking.com/communionapi/");

    }
}






























