package com.asking.pad.app.ui.superclass.examreview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseEvenAppCompatActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.superclass.classify.ClassifyActivty;
import com.asking.pad.app.ui.superclass.examreview.classex.ClassExamDialogFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/4/13.
 */

public class ExamReviewFirstActivity extends BaseEvenAppCompatActivity<UserPresenter, UserModel> {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.radio_group)
    RadioGroup radio_group;

    @BindView(R.id.rb_tab2)
    RadioButton rb_tab2;

    private int mCurTabIndex = 0;
    List<Fragment> fragments = new ArrayList<>();

    boolean isBuy;
    String knowledgeId;
    String classType;
    String knowledgeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_review_first);
        ButterKnife.bind(this);

        isBuy = this.getIntent().getBooleanExtra("isBuy",false);
        knowledgeId = this.getIntent().getStringExtra("knowledgeId");
        classType = this.getIntent().getStringExtra("classType");
        knowledgeName = this.getIntent().getStringExtra("knowledgeName");
    }

    @Override
    public void initView() {
        super.initView();

        tv_title.setText(knowledgeName);

        if (TextUtils.equals("M2", classType) || TextUtils.equals("P2", classType)) {
            rb_tab2.setText("备战中考|3个要求");
        } else if (TextUtils.equals("M3", classType) || TextUtils.equals("P3", classType)) {
            rb_tab2.setText("备战高考|3个要求");
        }

        fragments.add(AskDoctorFirstFragment.newInstance(knowledgeId));
        fragments.add(ExamFirstRequireFragment.newInstance(knowledgeId));
        fragments.add(ExamReviewExerFragment.newInstance(knowledgeId));
        fragments.add(ClassExamDialogFragment.newInstance(0,knowledgeId));

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment,fragments.get(0))
                .add(R.id.fragment,fragments.get(1))
                .add(R.id.fragment,fragments.get(2))
                .add(R.id.fragment,fragments.get(3))
                .hide(fragments.get(1))
                .hide(fragments.get(2))
                .hide(fragments.get(3))
                .show(fragments.get(0)).commit();

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
                    case R.id.rb_tab4:
                        setFragmentPage(3);
                        break;
                }
            }
        });
    }

    @OnClick({ R.id.tv_title})
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_title:
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromExamReview",true);
                bundle.putBoolean("isBuy",isBuy);
                bundle.putString("classType",classType);
                bundle.putString("className", Constants.getClassName(classType));
                bundle.putBoolean("isSelectNode",true);
                bundle.putString("mExamReviewType","0");
                CommonUtil.openActivity(ClassifyActivty.class,bundle);
                break;
        }
    }

    private void reinitLoadData(String knowledgeId){
        ((AskDoctorFirstFragment)fragments.get(0)).initLoadData(knowledgeId);
        ((ExamFirstRequireFragment)fragments.get(1)).initLoadData(knowledgeId);
        ((ExamReviewExerFragment)fragments.get(2)).initLoadData(knowledgeId);
        ((ClassExamDialogFragment)fragments.get(3)).initLoadData(knowledgeId);
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type){
            case AppEventType.CLASSIFY_REQUEST:
                isBuy = (boolean)event.values[0];
                classType = (String)event.values[1];
                knowledgeId = (String)event.values[2];
                knowledgeName = (String)event.values[3];
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
