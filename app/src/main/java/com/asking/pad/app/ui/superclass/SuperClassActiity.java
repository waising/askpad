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
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.ParamHelper;
import com.asking.pad.app.commom.TreeItemHolder;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.superclass.classify.ClassifyActivty;
import com.asking.pad.app.ui.superclass.exercises.SuperExercisesFragment;
import com.asking.pad.app.ui.superclass.tutorial.SuperTutorialFragment;
import com.unnamed.b.atv.model.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/4/10.
 */

public class SuperClassActiity extends BaseEvenAppCompatActivity<UserPresenter,UserModel> {
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
    String classType;
    String gradeId;

    String knowledgeName;
    String knowledgeId;

    int knowledgeIndex;

    List<TreeNode> treeNodeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_class);
        ButterKnife.bind(this);

        isBuy = this.getIntent().getBooleanExtra("isBuy",false);
        classType = this.getIntent().getStringExtra("classType");
        gradeId = this.getIntent().getStringExtra("gradeId");
        knowledgeId = this.getIntent().getStringExtra("knowledgeId");
        knowledgeName = this.getIntent().getStringExtra("knowledgeName");
        knowledgeIndex = this.getIntent().getIntExtra("knowledgeIndex",0);

        HashMap<String, Object> mParams = ParamHelper.acceptParams(ClassifyActivty.class.getName());
        treeNodeList.clear();
        treeNodeList.addAll((List<TreeNode>) mParams.get("treeNodeList"));

        if(!isBuy){
            ll_tab.setVisibility(View.GONE);
        }
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
                .add(R.id.fragment,fragments.get(0))
                .add(R.id.fragment,fragments.get(1))
                .hide(fragments.get(1)).show(fragments.get(0)).commit();


        showPageIndex();
    }

    private void initDataView(String gradeId, String knowledgeId, String classType, boolean isBuy){
        ((SuperTutorialFragment)fragments.get(0)).refreshData(gradeId,knowledgeId,classType,isBuy);
        ((SuperExercisesFragment)fragments.get(1)).refreshData(gradeId,knowledgeId,classType);
        showPageIndex();
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type){
            case AppEventType.CLASSIFY_REQUEST:

                gradeId = (String)event.values[2];
                knowledgeId = (String)event.values[4];
                classType = (String)event.values[1];
                isBuy = (boolean)event.values[0];
                knowledgeName = (String)event.values[3];
                knowledgeIndex = (int)event.values[5];

                initDataView(gradeId,knowledgeId,classType,isBuy);
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
        switch(view.getId()){
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
                Bundle bundle = new Bundle();
                bundle.putBoolean("isBuy",isBuy);
                bundle.putString("classType",classType);
                bundle.putString("className", Constants.getClassName(this,classType));
                bundle.putBoolean("isSelectNode",true);
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
                if(knowledgeIndex < treeNodeList.size()){
                    setPage();
                }
                break;
        }
    }

    private void showPageIndex(){
        tv_pre.setVisibility(View.VISIBLE);
        tv_next.setVisibility(View.VISIBLE);
        if(knowledgeIndex <= 0){
            tv_pre.setVisibility(View.INVISIBLE);
        }else if(knowledgeIndex >= treeNodeList.size()-1){
            tv_next.setVisibility(View.INVISIBLE);
        }
        tv_title.setText(knowledgeName);
    }

    private void setPage(){
        TreeNode e = treeNodeList.get(knowledgeIndex);
        knowledgeName = ((TreeItemHolder.IconTreeItem) e.getValue()).getText();
        knowledgeId = ((TreeItemHolder.IconTreeItem) e.getValue()).getId();

        initDataView(gradeId,knowledgeId,classType,isBuy);
    }
}
