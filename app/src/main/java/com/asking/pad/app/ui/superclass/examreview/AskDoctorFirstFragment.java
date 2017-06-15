package com.asking.pad.app.ui.superclass.examreview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * Created by jswang on 2017/3/2.
 */

public class AskDoctorFirstFragment extends BaseFrameFragment<UserPresenter, UserModel> {
    String knowledgeId;

    @BindView(R.id.mathView)
    AskMathView mathView;

    @BindView(R.id.multiStateView)
    MultiStateView multiStateView;

    public static AskDoctorFirstFragment newInstance(String knowledgeId) {
        AskDoctorFirstFragment fragment = new AskDoctorFirstFragment();
        Bundle bundle = new Bundle();
        bundle.putString("knowledgeId", knowledgeId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            knowledgeId = bundle.getString("knowledgeId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setContentView(R.layout.fragment_supertutorial_superclass_talking);
        ButterKnife.bind(this, getContentView());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        super.initView();
        if(!TextUtils.isEmpty(knowledgeId)){
            initLoadData(knowledgeId);
        }
    }

    public void initLoadData(String knowledgeId) {
        this.knowledgeId = knowledgeId;
        if(multiStateView !=null){
            multiStateView.setViewState(multiStateView.VIEW_STATE_LOADING);
        }
        mPresenter.firstreviewkaoqfx(knowledgeId,new ApiRequestListener<String>(){
            @Override
            public void onResultSuccess(String res) {
                if (mathView != null) {
                    mathView.formatMath().showWebImage(multiStateView);
                    if(!TextUtils.isEmpty(res)) {
                        mathView.setWebText(res);
                        multiStateView.setViewState(multiStateView.VIEW_STATE_CONTENT);
                    }else{
                        multiStateView.setViewState(multiStateView.VIEW_STATE_EMPTY);
                    }
                }
            }
        });
    }
}
