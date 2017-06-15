package com.asking.pad.app.ui.superclass.tutorial.topic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFragment;
import com.asking.pad.app.widget.AskMathView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/5/26.
 */

public class TopicWebTxtFragment  extends BaseFragment {
    String content;

    @BindView(R.id.mathView)
    AskMathView mathView;


    public static TopicWebTxtFragment newInstance(String content) {
        TopicWebTxtFragment fragment = new TopicWebTxtFragment();
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            content = bundle.getString("content");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setContentView(R.layout.fragment_topic_web_math);
        ButterKnife.bind(this, getContentView());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        super.initView();
        mathView.formatMath().showWebImage();

        mathView.setText(content);
    }
}
