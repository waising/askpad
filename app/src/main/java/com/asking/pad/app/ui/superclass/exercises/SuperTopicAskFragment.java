package com.asking.pad.app.ui.superclass.exercises;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.entity.superclass.exer.SubjectExerEntity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.AskMathView;
import com.asking.pad.app.widget.StarView;

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

    @BindView(R.id.tv_subject_type)
    TextView tv_subject_type;

    private int index = 0;
    private String classType;
    private SubjectExerEntity mSubjectClass;
    SuperTopicAskOpAdapter optionsAdapter;

    public static SuperTopicAskFragment newInstance(String classType,SubjectExerEntity mSubjectClass, int index) {
        SuperTopicAskFragment fragment = new SuperTopicAskFragment();
        fragment.mSubjectClass = mSubjectClass;
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        bundle.putString("classType", classType);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            index = bundle.getInt("index");
            classType = bundle.getString("classType");
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

        topic_mathview.formatMath();
        answer_mathView.formatMath();

        optionsAdapter = new SuperTopicAskOpAdapter(getActivity(), mSubjectClass);
        optionsAdapter.userAnswer = mSubjectClass.userAnswer;
        rv_topic.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_topic.setAdapter(optionsAdapter);

        tv_index.setText((index + 1) + ".");


        StringBuffer optionDes = new StringBuffer(mSubjectClass.getSubjectDescriptionHtml());
        try{
            for (SubjectExerEntity.SubjectOption a : mSubjectClass.getOptions()) {
                optionDes.append("<p>").append(a.getOptionName())
                        .append(a.getOptionContentHtml().replace("<p>","").replace("</p>",""))
                        .append("</p>");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        topic_mathview.setText(optionDes.toString());

        answer_mathView.setText(mSubjectClass.getDetailsAnswer());
        start_view.setmStarNum(mSubjectClass.getDifficulty());

        answer_mathView.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(mSubjectClass.userAnswer)){
            optionsAdapter.userAnswer = mSubjectClass.userAnswer;
            optionsAdapter.rightAnswer = mSubjectClass.getRightAnswer();

            btn_submit.setVisibility(View.INVISIBLE);
            cb_detail.setVisibility(View.VISIBLE);

            cb_detail.setChecked(mSubjectClass.isShowDetailsAnswer);
            answer_mathView.setVisibility(mSubjectClass.isShowDetailsAnswer ? View.VISIBLE : View.GONE);
        }

        tv_subject_type.setVisibility(View.GONE);
        if(!TextUtils.equals(mSubjectClass.getSubjectType().getTypeId(),"1")){
            tv_subject_type.setVisibility(View.VISIBLE);
            btn_submit.setVisibility(View.INVISIBLE);
            cb_detail.setVisibility(View.VISIBLE);
        }

        optionsAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.btn_submit, R.id.cb_detail})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                String userAnswer = null;
                for(SubjectExerEntity.SubjectOption e:mSubjectClass.getOptions()){
                    if(e.isSelect){
                        userAnswer = e.getOptionName();
                    }
                }
                if(TextUtils.isEmpty(userAnswer)){
                    showShortToast("请先答题");
                    return;
                }

                mSubjectClass.userAnswer = userAnswer;
                optionsAdapter.userAnswer = userAnswer;
                optionsAdapter.rightAnswer = mSubjectClass.getRightAnswer();
                optionsAdapter.notifyDataSetChanged();

                mPresenter.errorsubject(Constants.getClassType(classType), mSubjectClass.getSubjectId()
                        , userAnswer,new ApiRequestListener<String>() {
                    @Override
                    public void onResultSuccess(String res) {
                    }
                });

                btn_submit.setVisibility(View.INVISIBLE);
                cb_detail.setVisibility(View.VISIBLE);
                break;
            case R.id.cb_detail:
                mSubjectClass.isShowDetailsAnswer = !mSubjectClass.isShowDetailsAnswer;
                cb_detail.setChecked(mSubjectClass.isShowDetailsAnswer);
                answer_mathView.setVisibility(mSubjectClass.isShowDetailsAnswer ? View.VISIBLE : View.GONE);
                break;
        }
    }
}