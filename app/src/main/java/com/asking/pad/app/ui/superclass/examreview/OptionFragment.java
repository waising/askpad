package com.asking.pad.app.ui.superclass.examreview;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFragment;
import com.asking.pad.app.entity.ExerAskEntity;
import com.asking.pad.app.widget.AskMathView;
import com.asking.pad.app.widget.AskMathWebView;
import com.asking.pad.app.widget.MultiStateView;

import net.cachapa.expandablelayout.ExpandableLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/4/20.
 */

public class OptionFragment extends BaseFragment {
    @BindView(R.id.load_view)
    MultiStateView load_view;

    @BindView(R.id.tit_webview)
    AskMathWebView tit_webview;

    @BindView(R.id.cb_detail)
    CheckBox cb_detail;

    @BindView(R.id.el_layout)
    ExpandableLayout el_layout;

    @BindView(R.id.answer_load_view)
    MultiStateView answer_load_view;

    @BindView(R.id.web_answer)
    AskMathView web_answer;

    ExerAskEntity.OptionsBean mOptionsBean;
    int position;
    
    public static OptionFragment newInstance(int position, ExerAskEntity.OptionsBean mOptionsBean) {
        OptionFragment fragment = new OptionFragment();
        fragment.position = position;
        fragment.mOptionsBean = mOptionsBean;
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_exam_second_topic);
        ButterKnife.bind(this, getContentView());
    }

    @Override
    public void initView() {
        super.initView();

        tit_webview.formatMath().showWebImage(load_view);
        String tit = (position + 1) + "：" + mOptionsBean.getDetailsOptions();
        tit_webview.setText(tit);

        if (mOptionsBean.isSelect) {
            el_layout.expand(false);
        } else {
            el_layout.collapse(false);
        }
        cb_detail.setChecked(mOptionsBean.isSelect);
        cb_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOptionsBean.isSelect = !mOptionsBean.isSelect;
                cb_detail.setChecked(mOptionsBean.isSelect);
                if (mOptionsBean.isSelect) {
                    el_layout.expand(false);
                } else {
                    el_layout.collapse(false);
                }
            }
        });

        web_answer.formatMath().showWebImage(answer_load_view);
        web_answer.setText(mOptionsBean.details_answer_html);

    }
}
