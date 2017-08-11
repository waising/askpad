package com.asking.pad.app.ui.superclass.tutorial.topic;

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
import com.asking.pad.app.entity.classex.SubjectClass;
import com.asking.pad.app.entity.classex.SubjectOption;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.superclass.examreview.classex.adapter.TopicItemOpAdapter;
import com.asking.pad.app.widget.AskMathView;
import com.asking.pad.app.widget.StarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/5/24.
 */

public class TopicItemFragment extends BaseFrameFragment<UserPresenter, UserModel> {
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

    String classType;
    private int index = 0;

    private SubjectClass mSubjectClass;
    TopicItemOpAdapter optionsAdapter;

    public static TopicItemFragment newInstance(int index,SubjectClass mSubjectClass) {
        TopicItemFragment fragment = new TopicItemFragment();
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
        setContentView(R.layout.fragment_topic_item);
        ButterKnife.bind(this, getContentView());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        super.initView();

        topic_mathview.formatMath();
        answer_mathView.formatMath().showWebImage();

        optionsAdapter = new TopicItemOpAdapter(getActivity(), mSubjectClass.getOptions());
        optionsAdapter.userAnswer = mSubjectClass.userAnswer;
        rv_topic.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_topic.setAdapter(optionsAdapter);

        tv_index.setText((index + 1) + ".");

        String optionDes = "";
        try{
            for (SubjectOption a : mSubjectClass.getOptions()) {
                optionDes = optionDes + a.getOptionName() + ". " + a.getOptionContentHtml();
            }
        }catch (Exception e){}
        topic_mathview.setText(mSubjectClass.getSubjectDescriptionHtml() + optionDes);
        answer_mathView.setText(mSubjectClass.getDetailsAnswerHtml());
        start_view.setmStarNum(mSubjectClass.getDifficulty());

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
                for(SubjectOption e:mSubjectClass.getOptions()){
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

                String answerstr = String.format("{\"subList\":[{\"id\":\"%s\",\"subject_type\":{\"type_id\":\"%s\"},\"user_answer\":\"%s\"}]}",
                        mSubjectClass.getSubjectid(), mSubjectClass.getSubjectType().getTypeId(), userAnswer);

                mPresenter.subjectClassic(answerstr, classType, new ApiRequestListener<String>() {
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
