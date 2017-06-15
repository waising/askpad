package com.asking.pad.app.ui.superclass.examreview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseEvenAppCompatActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.ParamHelper;
import com.asking.pad.app.entity.ExamReviewTree;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.superclass.classify.ClassifyActivty;
import com.asking.pad.app.ui.superclass.examreview.classex.ClassExamDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by jswang on 2017/4/14.
 */

public class ExamReviewSecondActivity extends BaseEvenAppCompatActivity<UserPresenter, UserModel> {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.radio_group)
    RadioGroup radio_group;

    @BindView(R.id.tv_pre)
    TextView tv_pre;

    @BindView(R.id.tv_next)
    TextView tv_next;

    private int mCurTabIndex = 0;
    List<Fragment> fragments = new ArrayList<>();

    boolean isBuy;
    String knowledgeId;
    String classType;
    String knowledgeName;
    int knowledgeIndex;
    ArrayList<ExamReviewTree> examList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_review_second);
        ButterKnife.bind(this);

        isBuy = this.getIntent().getBooleanExtra("isBuy",false);
        knowledgeId = this.getIntent().getStringExtra("knowledgeId");
        classType = this.getIntent().getStringExtra("classType");
        knowledgeName = this.getIntent().getStringExtra("knowledgeName");
        knowledgeIndex = this.getIntent().getIntExtra("knowledgeIndex",0);

        HashMap<String, Object> mParams = ParamHelper.acceptParams(ClassifyActivty.class.getName());
        examList.clear();
        examList.addAll((List<ExamReviewTree>) mParams.get("examList"));
    }

    @Override
    public void initView() {
        super.initView();

        tv_title.setText(knowledgeName);

        fragments.add(AskDoctorSecondFragment.newInstance(knowledgeId));
        fragments.add(ExamSecondRequireFragment.newInstance(knowledgeId));
        fragments.add(ClassExamDialogFragment.newInstance(1,knowledgeId));

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment,fragments.get(0))
                .add(R.id.fragment,fragments.get(1))
                .add(R.id.fragment,fragments.get(2))
                .hide(fragments.get(1)).hide(fragments.get(2)).show(fragments.get(0)).commit();

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_tab1:
                        setFragmentPage(0);
                        break;
                    case R.id.rb_tab2:
                        setFragmentPage(1);
                        break;
                    case R.id.rb_tab3:
                        setFragmentPage(2);
                        break;
                }
            }
        });

        showPageIndex();
    }

    @OnClick({ R.id.tv_title,R.id.tv_pre, R.id.tv_next})
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_title:
                Bundle bundle = new Bundle();
                bundle.putBoolean("isBuy",isBuy);
                bundle.putString("classType",classType);
                bundle.putString("className", Constants.getClassName(this,classType));
                bundle.putBoolean("isSelectNode",true);
                bundle.putBoolean("isFromExamReview",true);
                bundle.putString("mExamReviewType","1");
                CommonUtil.openActivity(ClassifyActivty.class,bundle);
                break;
            case R.id.tv_pre:
                knowledgeIndex = knowledgeIndex - 1;
                if(knowledgeIndex >= 0){
                    setPage();
                }
                break;
            case R.id.tv_next:
                knowledgeIndex = knowledgeIndex + 1;
                if(knowledgeIndex < examList.size()){
                    setPage();
                }
                break;
        }
    }

    private void setPage(){
        ExamReviewTree e = examList.get(knowledgeIndex);
        knowledgeName = e.name;
        knowledgeId = e.id;

        reinitLoadData(knowledgeId);
    }

    private void reinitLoadData(String knowledgeId){
        ((AskDoctorSecondFragment)fragments.get(0)).initLoadData(knowledgeId);
        ((ExamSecondRequireFragment)fragments.get(1)).initLoadData(knowledgeId);
        ((ClassExamDialogFragment)fragments.get(2)).initLoadData(knowledgeId);

        tv_title.setText(knowledgeName);
    }

    private void showPageIndex(){
        tv_pre.setVisibility(View.VISIBLE);
        tv_next.setVisibility(View.VISIBLE);
        if(knowledgeIndex <= 0){
            tv_pre.setVisibility(View.INVISIBLE);
        }else if(knowledgeIndex >= examList.size()-1){
            tv_next.setVisibility(View.INVISIBLE);
        }
        tv_title.setText(knowledgeName);
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type){
            case AppEventType.CLASSIFY_REQUEST:
                isBuy = (boolean)event.values[0];
                classType = (String)event.values[1];
                knowledgeId = (String)event.values[2];
                knowledgeName = (String)event.values[5];
                knowledgeIndex = (int)event.values[4];
                tv_title.setText(knowledgeName);
                reinitLoadData(knowledgeId);
                break;
        }
    }

    private void setFragmentPage(int index) {
        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        trx.hide(fragments.get(mCurTabIndex));
        if (!fragments.get(index).isAdded()) {
            trx.add(R.id.fragment, fragments.get(index));
        }
        trx.show(fragments.get(index)).commit();

        mCurTabIndex = index;
    }
}
