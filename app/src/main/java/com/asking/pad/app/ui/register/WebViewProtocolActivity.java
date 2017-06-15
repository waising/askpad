package com.asking.pad.app.ui.register;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


/**
 * Created by wxiao on 2016/10/26.
 * 服务条款
 */

public class WebViewProtocolActivity extends BaseActivity {

    @BindView(R.id.web_view)
    WebView mWebView;

    @BindView(R.id.ok)
    Button ok;

    @BindView(R.id.bottom)
    LinearLayout bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_protocol);
        ButterKnife.bind(this);
        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new AppEventType(AppEventType.WEB_VIEW_PROTOCOL_REQUEST));
                finish();
            }
        });
    }

    private String mWebUrl, mTitle;

    @Override
    public void initData() {
        super.initData();
        mWebUrl = getIntent().getStringExtra(Constants.WEB_URL);
        mTitle = getIntent().getStringExtra(Constants.WEB_TITLE);
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initLoad() {
        super.initLoad();

        mWebView.loadUrl(mWebUrl, getAuth());
    }

    @Override
    public void finish() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.finish();
        }
    }
}
