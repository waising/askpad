package com.asking.pad.app.ui.superclass.examreview.classex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFrameFragment;
import com.asking.pad.app.entity.classex.SubjectClass;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.superclass.examreview.classex.adapter.ClassExamItemOpAdapter;
import com.asking.pad.app.widget.AskMathView;
import com.asking.pad.app.widget.StarView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jswang on 2017/5/24.
 */

public class ClassExamItemFragment extends BaseFrameFragment<UserPresenter, UserModel> {

    @BindView(R.id.topic_mathview)
    AskMathView topic_mathview;

    @BindView(R.id.rv_topic)
    RecyclerView rv_topic;

    @BindView(R.id.start_view)
    StarView start_view;

    @BindView(R.id.answer_mathView)
    AskMathView answer_mathView;

    @BindView(R.id.tv_subject_type)
    TextView tv_subject_type;

    @BindView(R.id.ll_answer)
    View ll_answer;

    String classType;
    private int index = 0;

    private SubjectClass mSubjectClass;

    ClassExamItemOpAdapter optionsAdapter;

    public static ClassExamItemFragment newInstance(int index, SubjectClass mSubjectClass) {
        ClassExamItemFragment fragment = new ClassExamItemFragment();
        fragment.mSubjectClass = mSubjectClass;
        Bundle bundle = new Bundle();
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
        setContentView(R.layout.fragment_class_exam_item);
        ButterKnife.bind(this, getContentView());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        super.initView();

        topic_mathview.formatMath();
        answer_mathView.formatMath();

        optionsAdapter = new ClassExamItemOpAdapter(getActivity(), mSubjectClass);
        optionsAdapter.rightAnswer = mSubjectClass.getRightAnswer();
        optionsAdapter.userAnswer = mSubjectClass.userAnswer;
        rv_topic.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_topic.setAdapter(optionsAdapter);

        String optionDes = "";
        for (SubjectClass.SubjectOption a : mSubjectClass.getOptions()) {
            optionDes = optionDes + a.getOptionName() + ". " + a.getOptionContentHtml().substring(3, a.getOptionContentHtml().length() - 4) + "<br/>";
        }
        topic_mathview.setText((index + 1) + "." + mSubjectClass.getSubjectDescriptionHtml() + optionDes);

        start_view.setmStarNum(mSubjectClass.getDifficulty());

        ll_answer.setVisibility(mSubjectClass.isShowDetailsAnswer ? View.VISIBLE : View.INVISIBLE);
        if (mSubjectClass.isShowDetailsAnswer) {
            String answer = mSubjectClass.getDetailsAnswerHtml();
            answer_mathView.setText(answer);
        }

        tv_subject_type.setVisibility(View.GONE);
        if(!TextUtils.equals(mSubjectClass.getSubjectType().getTypeId(),"1")){
            tv_subject_type.setVisibility(View.VISIBLE);
        }

        optionsAdapter.notifyDataSetChanged();
    }
}
