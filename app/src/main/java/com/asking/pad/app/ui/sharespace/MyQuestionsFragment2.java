package com.asking.pad.app.ui.sharespace;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenFrameFragment;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.FileUtils;
import com.asking.pad.app.entity.QuestionEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.commom.PhotoShowActivity;
import com.asking.pad.app.widget.MultiStateView;
import com.asking.pad.app.widget.WebViewScroll;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 问答广场
 */

public class MyQuestionsFragment2 extends BaseEvenFrameFragment<UserPresenter, UserModel> {

    @BindView(R.id.webview)
    WebViewScroll webview;

    @BindView(R.id.load_View)
    MultiStateView load_View;

    private int start = 0;
    private int limit = 6;
    int position;

    public static MyQuestionsFragment2 newInstance(int position) {
        MyQuestionsFragment2 fragment = new MyQuestionsFragment2();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_questions_web);
        ButterKnife.bind(this, getContentView());

        position = getArguments().getInt("position");
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
        webview.loadUrl("https://apis.91asking.com/communionapi/myask.html");
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type) {
            case AppEventType.QUESTION_ASK:
                getDataNow();
                break;
        }
    }

    public class WebAppInterface {

        @JavascriptInterface
        public void refshAdapt(int sta, int lim) {
            start = sta;
            limit = lim;
            getDataNow();
        }

        @JavascriptInterface
        public void openActivity(String str) {
            QuestionEntity e = JSON.parseObject(str, QuestionEntity.class);
            QuestionAnwserActivity.openActivity(0, e);
        }

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
    }

    private void getDataNow() {
        load_View.setViewState(MultiStateView.VIEW_STATE_LOADING);
        switch (position) {
            case 0://我的提问
                mPresenter.getMyQuestionAskList(start, limit, apiRequestListener);
                break;
            case 1://我的回答
                mPresenter.getMyQuestionAnswerList(start, limit, apiRequestListener);
                break;
        }
    }

    ApiRequestListener<String> apiRequestListener = new ApiRequestListener<String>() {
        @Override
        public void onResultSuccess(String resStr) {//数据返回成功
            load_View.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            if (start > 0) {
                webview.loadUrl(String.format("javascript:addItems(%s)", resStr));
            } else {
                JSONObject jsonObj = JSON.parseObject(resStr);
                List<QuestionEntity> list = JSON.parseArray(jsonObj.getString("list"), QuestionEntity.class);
                if (list != null && list.size() > 0) {
                    webview.loadUrl(String.format("javascript:refreshItems(%s)", resStr));
                } else {
                    load_View.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                }
            }
        }

        @Override
        public void onResultFail() {
            super.onResultFail();
            load_View.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    };
}
