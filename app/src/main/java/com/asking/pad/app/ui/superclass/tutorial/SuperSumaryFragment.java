package com.asking.pad.app.ui.superclass.tutorial;

import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.AskMathView;
import com.asking.pad.app.widget.MultiStateView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/4/19.
 */

public class SuperSumaryFragment extends BaseFrameFragment<UserPresenter, UserModel> {
    @BindView(R.id.say_mathview)
    AskMathView say_mathview;

    @BindView(R.id.load_View)
    MultiStateView load_View;

    boolean isBuy;
    String gradeId;
    String knowledgeId;

    public static SuperSumaryFragment newInstance(String gradeId, String knowledgeId, boolean isBuy) {
        SuperSumaryFragment fragment = new SuperSumaryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("gradeId", gradeId);
        bundle.putString("knowledgeId", knowledgeId);
        bundle.putBoolean("isBuy", isBuy);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_super_tutorial_say);
        ButterKnife.bind(this, getContentView());
        Bundle bundle = getArguments();
        if (bundle != null) {
            gradeId = bundle.getString("gradeId");
            knowledgeId = bundle.getString("knowledgeId");
            isBuy = bundle.getBoolean("isBuy");
        }
    }

    @Override
    public void initView() {
        super.initView();

        refreshData(gradeId, knowledgeId, isBuy);
    }

    public void refreshData(String gradeId, String knowledgeId,boolean isBuy) {
        this.gradeId = gradeId;
        this.knowledgeId = knowledgeId;
        this.isBuy = isBuy;

        say_mathview.formatMath().showWebImage(load_View);

        if (isBuy) {
            mPresenter.getSuperBuyFragment1(gradeId, knowledgeId, 3, buy4Listener);
        } else {
            mPresenter.synclesson(gradeId, knowledgeId, 3, buy4Listener);
        }
    }

    ApiRequestListener buy4Listener = new ApiRequestListener<String>() {
        @Override
        public void onResultSuccess(String res) {
            JSONObject jsenRes = JSON.parseObject(res);
            say_mathview.setWebText(jsenRes.getString("attrContentHtml"));
            load_View.setViewState(load_View.VIEW_STATE_CONTENT);
        }

        @Override
        public void onResultFail() {
            load_View.setViewState(load_View.VIEW_STATE_ERROR);
        }
    };
}
