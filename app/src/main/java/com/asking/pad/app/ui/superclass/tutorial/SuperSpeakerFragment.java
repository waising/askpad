package com.asking.pad.app.ui.superclass.tutorial;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.entity.superclass.SuperClassSpeaker;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.superclass.tutorial.adapter.SuperSpeakerAdapter;
import com.asking.pad.app.widget.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/4/19.
 */

public class SuperSpeakerFragment extends BaseFrameFragment<UserPresenter, UserModel> {
    @BindView(R.id.rv_speaker)
    RecyclerView rv_speaker;

    @BindView(R.id.load_View)
    MultiStateView load_View;

    List<SuperClassSpeaker> mSpeakerTimes = new ArrayList<>();
    SuperSpeakerAdapter mSeakerAdapter;

    boolean isBuy;
    String gradeId;
    String knowledgeId;

    int start;
    int limit = 10;

    public static SuperSpeakerFragment newInstance(String gradeId, String knowledgeId, boolean isBuy) {
        SuperSpeakerFragment fragment = new SuperSpeakerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("gradeId", gradeId);
        bundle.putString("knowledgeId", knowledgeId);
        bundle.putBoolean("isBuy", isBuy);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_super_tutorial_speaker);
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

        rv_speaker.setLayoutManager(new LinearLayoutManager(getContext()));
        mSeakerAdapter = new SuperSpeakerAdapter(getActivity(),isBuy, mSpeakerTimes);
        rv_speaker.setAdapter(mSeakerAdapter);

        refreshData(gradeId, knowledgeId, isBuy);
    }
    public void refreshData(String gradeId, String knowledgeId, boolean isBuy) {
        this.gradeId = gradeId;
        this.knowledgeId = knowledgeId;
        this.isBuy = isBuy;

        if (isBuy) {
            mPresenter.getSuperBuyCoach(gradeId, knowledgeId, start, limit, buy3Listener);
        } else {
            mPresenter.synclesson(gradeId, knowledgeId,4, buy3Listener);
        }
    }

    ApiRequestListener buy3Listener = new ApiRequestListener<String>() {
        @Override
        public void onResultSuccess(String res) {
            JSONObject jsonRes = JSON.parseObject(res);
            String content = jsonRes.getString("content");
            mSpeakerTimes.clear();
            mSpeakerTimes.addAll(JSON.parseArray(content,SuperClassSpeaker.class));
            if (mSpeakerTimes.size() > 0) {
                load_View.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                mSeakerAdapter.notifyDataSetChanged();
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
