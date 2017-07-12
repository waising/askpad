package com.asking.pad.app.ui.superclass;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseEvenAppCompatActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.ParamHelper;
import com.asking.pad.app.entity.superclass.SuperLessonTree;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.pay.PayAskActivity;
import com.asking.pad.app.ui.superclass.classify.ClassifyActivty;
import com.asking.pad.app.ui.superclass.exercises.SuperExercisesFragment;
import com.asking.pad.app.ui.superclass.tutorial.SuperTutorialFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/4/10.
 */

public class SuperClassActiity extends BaseEvenAppCompatActivity<UserPresenter, UserModel> {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_supertutorial)
    TextView tv_supertutorial;

    @BindView(R.id.tv_pre)
    TextView tv_pre;

    @BindView(R.id.tv_next)
    TextView tv_next;

    @BindView(R.id.tv_exercises)
    TextView tv_exercises;

    @BindView(R.id.ll_tab)
    View ll_tab;

    private int mCurTabIndex = 0;
    List<Fragment> fragments = new ArrayList<>();

    private View[] mTabs;

    boolean isBuy;
    String gradeId;
    String knowledgeName;
    String knowledgeId;
    int free;
    int knowledgeIndex;

    String classType;
    String className;

    List<SuperLessonTree> treeLessonList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_class);
        ButterKnife.bind(this);

        classType = this.getIntent().getStringExtra("classType");
        className = this.getIntent().getStringExtra("className");

        isBuy = this.getIntent().getBooleanExtra("isBuy", false);
        gradeId = this.getIntent().getStringExtra("gradeId");
        knowledgeId = this.getIntent().getStringExtra("knowledgeId");
        knowledgeName = this.getIntent().getStringExtra("knowledgeName");
        knowledgeIndex = this.getIntent().getIntExtra("knowledgeIndex", 0);
        free = this.getIntent().getIntExtra("free", 0);

        HashMap<String, Object> mParams = ParamHelper.acceptParams(ClassifyActivty.class.getName());
        treeLessonList.clear();
        treeLessonList.addAll((List<SuperLessonTree>) mParams.get("treeLessonList"));
    }

    @Override
    public void initView() {

        mTabs = new View[2];
        mTabs[0] = tv_supertutorial;
        mTabs[1] = tv_exercises;

        mTabs[mCurTabIndex].setSelected(true);

        fragments.add(SuperTutorialFragment.newInstance(getIntent().getExtras()));
        fragments.add(SuperExercisesFragment.newInstance(getIntent().getExtras()));

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, fragments.get(0))
                .add(R.id.fragment, fragments.get(1))
                .hide(fragments.get(1)).show(fragments.get(0)).commit();


        showPageIndex();
    }

    private void initDataView(String gradeId, String knowledgeId, boolean isBuy) {
        ((SuperTutorialFragment) fragments.get(0)).refreshData(gradeId, knowledgeId, isBuy);
        ((SuperExercisesFragment) fragments.get(1)).refreshData(gradeId, knowledgeId, isBuy);
        showPageIndex();
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type) {
            case AppEventType.CLASSIFY_REQUEST:

                isBuy = (boolean) event.values[0];
                gradeId = (String) event.values[1];
                knowledgeId = (String) event.values[2];
                knowledgeName = (String) event.values[3];
                knowledgeIndex = (int) event.values[4];
                free = (int) event.values[5];
                treeLessonList.clear();
                treeLessonList.addAll((List<SuperLessonTree>)event.values[6]);

                initDataView(gradeId, knowledgeId, isBuy);
                break;
        }
    }

    private void setFragmentPage(int ind) {
        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        trx.hide(fragments.get(mCurTabIndex));
        if (!fragments.get(ind).isAdded()) {
            trx.add(R.id.fragment, fragments.get(ind));
        }
        trx.show(fragments.get(ind)).commit();

        if (mCurTabIndex >= 0) {
            mTabs[mCurTabIndex].setSelected(false);
        }
        mTabs[ind].setSelected(true);
    }

    @OnClick({R.id.tv_supertutorial, R.id.tv_exercises, R.id.tv_title, R.id.tv_pre, R.id.tv_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_supertutorial:
            case R.id.tv_exercises:
                int index = 0;
                for (int i = 0; i < mTabs.length; i++) {
                    if (view.getId() == mTabs[i].getId()) {
                        index = i;
                    }
                }
                if (index < 0 || index > mTabs.length - 1 || mCurTabIndex == index)
                    return;
                setFragmentPage(index);
                mCurTabIndex = index;
                break;
            case R.id.tv_title:
                ClassifyActivty.openActivity(classType,className,true);
                break;
            case R.id.tv_pre:
                knowledgeIndex = knowledgeIndex - 1;
                if (knowledgeIndex >= 0) {
                    if(!setPage()){
                        knowledgeIndex = knowledgeIndex + 1;
                    }
                }
                break;
            case R.id.tv_next:
                knowledgeIndex = knowledgeIndex + 1;
                if (knowledgeIndex < treeLessonList.size()) {
                    if(!setPage()){
                        knowledgeIndex = knowledgeIndex - 1;
                    }
                }
                break;
        }
    }

    private void showPageIndex() {
        tv_pre.setVisibility(View.VISIBLE);
        tv_next.setVisibility(View.VISIBLE);
        if (knowledgeIndex <= 0) {
            tv_pre.setVisibility(View.INVISIBLE);
        } else if (knowledgeIndex >= treeLessonList.size() - 1) {
            tv_next.setVisibility(View.INVISIBLE);
        }
        tv_title.setText(knowledgeName);
    }

    private boolean setPage() {
        SuperLessonTree e = treeLessonList.get(knowledgeIndex);
        knowledgeName = e.name;
        knowledgeId = e.id;
        free = e.free;
        if (free != 0 || isBuy) {
            initDataView(gradeId, knowledgeId, isBuy);
        }else{
            CommonUtil.openAuthActivity(PayAskActivity.class);
            finish();
            return  false;
        }
        return  true;
    }
}
