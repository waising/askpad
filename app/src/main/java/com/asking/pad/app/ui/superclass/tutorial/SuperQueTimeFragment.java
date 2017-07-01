package com.asking.pad.app.ui.superclass.tutorial;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.commom.MusicPlayer;
import com.asking.pad.app.entity.superclass.SuperQueTime;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.superclass.tutorial.adapter.SuperQueTimeAdapter;
import com.asking.pad.app.widget.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/4/19.
 */

public class SuperQueTimeFragment extends BaseFrameFragment<UserPresenter, UserModel> {
    @BindView(R.id.rv_quetime)
    RecyclerView rv_quetime;

    @BindView(R.id.load_View)
    MultiStateView load_View;

    boolean isBuy;
    String gradeId;
    String knowledgeId;

    List<SuperQueTime> mQueTimes = new ArrayList<>();
    SuperQueTimeAdapter mQueTimeAdapter;

    private MusicPlayer musicPlayer;
    private OnPayVoiceListener mListener;

    public static SuperQueTimeFragment newInstance(String gradeId, String knowledgeId, boolean isBuy
            , MusicPlayer musicPlayer, OnPayVoiceListener mListener) {
        SuperQueTimeFragment fragment = new SuperQueTimeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("gradeId", gradeId);
        bundle.putString("knowledgeId", knowledgeId);
        bundle.putBoolean("isBuy", isBuy);
        fragment.setArguments(bundle);
        fragment.musicPlayer = musicPlayer;
        fragment.mListener = mListener;
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_super_tutorial_quetime);
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

        rv_quetime.setLayoutManager(new LinearLayoutManager(getContext()));
        mQueTimeAdapter = new SuperQueTimeAdapter(this.getActivity(), mQueTimes, rv_quetime, musicPlayer, mListener);
        rv_quetime.setAdapter(mQueTimeAdapter);

        refreshData(gradeId, knowledgeId, isBuy);
    }

    public void refreshData(String gradeId, String knowledgeId, boolean isBuy) {
        this.gradeId = gradeId;
        this.knowledgeId = knowledgeId;
        this.isBuy = isBuy;
        load_View.setViewState(MultiStateView.VIEW_STATE_LOADING);
        mPresenter.synclesson(isBuy,gradeId, knowledgeId, 2, buy2Listener);
    }

    ApiRequestListener buy2Listener = new ApiRequestListener<String>() {
        @Override
        public void onResultSuccess(String res) {
            mQueTimes.clear();
            mQueTimes.addAll(JSON.parseArray(res, SuperQueTime.class));
            mQueTimeAdapter.notifyDataSetChanged();
            if (mQueTimes.size() > 0) {
                load_View.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            } else {
                load_View.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            }
        }

        @Override
        public void onResultFail() {
            load_View.setViewState(load_View.VIEW_STATE_ERROR);
        }
    };

}
