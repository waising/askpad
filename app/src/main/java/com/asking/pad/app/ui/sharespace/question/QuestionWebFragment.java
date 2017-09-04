package com.asking.pad.app.ui.sharespace.question;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFragment;
import com.asking.pad.app.commom.FileUtils;
import com.asking.pad.app.entity.QuestionEntity;
import com.asking.pad.app.ui.commom.PhotoShowActivity;
import com.asking.pad.app.ui.sharespace.QuestionAnwserActivity;
import com.asking.pad.app.widget.WebViewScroll;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/8/29.
 */

public class QuestionWebFragment extends BaseFragment {

    @BindView(R.id.webview)
    WebViewScroll webview;

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

        webview.addJavascriptInterface(new WebAppInterface(), "WebAppInterface");
        webview.loadUrl("http://192.168.9.57:8020/test/test1.html?__hbt=1504251548950");
    }

    public class WebAppInterface {

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
        public void openActivity(String str) {
            QuestionEntity e = JSON.parseObject(str,QuestionEntity.class);
            QuestionAnwserActivity.openActivity(0,e);
        }
    }
}






























