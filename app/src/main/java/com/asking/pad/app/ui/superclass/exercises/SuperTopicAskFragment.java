package com.asking.pad.app.ui.superclass.exercises;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.entity.ResultEntity;
import com.asking.pad.app.entity.SubjectEntity;
import com.asking.pad.app.entity.SuperBuyClearanceEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.AskMathView;
import com.asking.pad.app.widget.StarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/4/12.
 */

public class SuperTopicAskFragment extends BaseFrameFragment<UserPresenter, UserModel> {
    @BindView(R.id.tv_index)
    TextView tv_index;

    @BindView(R.id.topic_mathview)
    AskMathView topic_mathview;

    @BindView(R.id.rv_topic)
    RecyclerView rv_topic;

    @BindView(R.id.start_view)
    StarView start_view;

    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.cb_detail)
    CheckBox cb_detail;

    @BindView(R.id.answer_mathView)
    AskMathView answer_mathView;

    String classType;
    private int index = 0;

    private String answerTmp;

    private SuperBuyClearanceEntity.ListBean mTopicEntity;
    private List<SuperBuyClearanceEntity.ListBean.OptionsBean> optionsList = new ArrayList<>();

    SuperTopicAskOpAdapter optionsAdapter;

    public static SuperTopicAskFragment newInstance(SuperBuyClearanceEntity.ListBean mTopicEntity, String classType, int index) {
        SuperTopicAskFragment fragment = new SuperTopicAskFragment();
        fragment.mTopicEntity = mTopicEntity;
        Bundle bundle = new Bundle();
        bundle.putString("classType", classType);
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            classType = bundle.getString("classType");
            index = bundle.getInt("index");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setContentView(R.layout.fragment_super_topicask);
        ButterKnife.bind(this, getContentView());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        super.initView();

        optionsAdapter = new SuperTopicAskOpAdapter(getActivity(), optionsList, new SuperTopicAskOpAdapter.OnCommItemListener() {
            @Override
            public void sendNextFragmentMessage(String id, String answer) {
                answerTmp = answer;
            }

            @Override
            public void sendIndex(int index) {
            }
        });
        rv_topic.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_topic.setAdapter(optionsAdapter);
        if (mTopicEntity != null) {
            tv_index.setText((index + 1) + ".");

            String answerTmp = "";
            for (SuperBuyClearanceEntity.ListBean.OptionsBean a : mTopicEntity.getOptions()) {
                answerTmp = answerTmp + a.getOptionName() + ". " + a.getOptionContentHtml().substring(3, a.getOptionContentHtml().length() - 4) + "<br/>";
            }
            topic_mathview.setText(mTopicEntity.getSubjectDescriptionHtml() + answerTmp);
            answer_mathView.setText(mTopicEntity.getDetailsAnswerHtml());
            start_view.setmStarNum(mTopicEntity.getDifficulty());

            optionsList.clear();
            optionsList.addAll(mTopicEntity.getOptions());

            if (optionsList.size() > 0 && optionsList.get(0).getResultEntity() != null) {
                btn_submit.setVisibility(View.INVISIBLE);
                cb_detail.setVisibility(View.VISIBLE);
                answer_mathView.setVisibility(cb_detail.isChecked() ? View.GONE : View.VISIBLE);
            }

            if (optionsList.size() <= 0){
                btn_submit.setVisibility(View.INVISIBLE);
                cb_detail.setVisibility(View.VISIBLE);
                answer_mathView.setVisibility(cb_detail.isChecked() ? View.GONE : View.VISIBLE);
            }

            optionsAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.btn_submit, R.id.cb_detail})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (answerTmp == null) {
                    showShortToast("请先答题");
                    return;
                }
                String answerstr = String.format("{\"subList\":[{\"id\":\"%s\",\"subject_type\":{\"type_id\":\"%s\"},\"user_answer\":\"%s\"}]}",
                        mTopicEntity.getId(), mTopicEntity.getSubjectType().getTypeId(), answerTmp);

                mPresenter.subjectClassic(answerstr, classType, new ApiRequestListener<String>() {
                    @Override
                    public void onResultSuccess(String res) {
                        SubjectEntity sEntity = JSON.parseObject(res, SubjectEntity.class);
                        ResultEntity rEntity = new ResultEntity(sEntity.getId(), sEntity.getUserAnswer(),
                                sEntity.getRightAnswer(), 0);
                    }
                });

                ResultEntity e = new ResultEntity(mTopicEntity.getId(), answerTmp, mTopicEntity.getRightAnswer(), 0);
                setAdapterResultEntity(e);
                btn_submit.setVisibility(View.INVISIBLE);
                cb_detail.setVisibility(View.VISIBLE);
                break;
            case R.id.cb_detail:
                answer_mathView.setVisibility(cb_detail.isChecked() ? View.VISIBLE : View.GONE);
                break;
        }
    }

    public void setAdapterResultEntity(ResultEntity resultEntity) {
        for (int i = 0; i < optionsList.size(); i++) {
            optionsList.get(i).setResultEntity(resultEntity);
        }
        optionsAdapter.updateResult(resultEntity);
    }
}