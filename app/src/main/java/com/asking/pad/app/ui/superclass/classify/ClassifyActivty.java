package com.asking.pad.app.ui.superclass.classify;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenAppCompatActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.entity.superclass.StudyClassSubject;
import com.asking.pad.app.entity.superclass.StudyClassVersion;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.ui.downbook.DownBookActivity;
import com.asking.pad.app.ui.superclass.classify.adapter.VersionAdapter;
import com.asking.pad.app.widget.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by jswang on 2017/4/14.
 */

public class ClassifyActivty extends BaseEvenAppCompatActivity<UserPresenter, UserModel> implements VersionAdapter.OnCommItemListener {
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    @BindView(R.id.ll_version)
    View ll_version;

    @BindView(R.id.rv_version)
    RecyclerView rv_version;

    VersionAdapter versionAdapter;

    @BindView(R.id.load_view)
    MultiStateView load_view;

    String classType;
    String className;
    /**
     * M2-初中数学（8）  P2-初中物理（6）  M3-高中数学（9）  P3-高中物理（7）
     */
    String actionType = "";
    boolean isBuy;
    boolean isSelectNode;
    ArrayList<StudyClassVersion> versionList = new ArrayList<>();

    private int mCurTabIndex = 0;
    List<Fragment> fragments = new ArrayList<>();

    StudyClassVersion mVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        ButterKnife.bind(this);
        classType = this.getIntent().getStringExtra("classType");
        className = this.getIntent().getStringExtra("className");
        isBuy = this.getIntent().getBooleanExtra("isBuy", false);
        isSelectNode = this.getIntent().getBooleanExtra("isSelectNode", false);

        actionType = Constants.getActionType(classType);
    }

    @Override
    public void initView() {
        toolBar.setNavigationIcon(Constants.getClassIcon(classType));
        setToolbar(toolBar, className);

        toolBar.inflateMenu(R.menu.menu_down_book);
        toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_info:
                        CommonUtil.openActivity(DownBookActivity.class);
                        break;
                }
                return true;
            }
        });

        versionAdapter = new VersionAdapter(this,versionList, this);
        rv_version.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_version.setAdapter(versionAdapter);



        if(getIntent().getBooleanExtra("isFromExamReview", false)){
            ll_version.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment,ClassifyExamReviewFragment.newInstance(getIntent().getExtras())).commit();
        }else {
            fragments.add(ClassifySuperFragment.newInstance(getIntent().getExtras()));
            fragments.add(ClassifyExamReviewFragment.newInstance(getIntent().getExtras()));
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment,fragments.get(0))
                    .add(R.id.fragment,fragments.get(1))
                    .hide(fragments.get(1)).show(fragments.get(0)).commit();
            initClassVersion();
        }

        load_view.setErrorRefBtnTxt2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initClassVersion();
            }
        });
    }

    public void onEventMainThread(AppEventType event) {
        try {
            if(event.type== AppEventType.BOOK_OPEN_REQUEST){
                if(mVersion!=null){
                    for(StudyClassVersion e:versionList){
                        e.isSelect = false;
                        String v1 = e.getVersionId();
                        String v2 = (String)event.values[0];
                        if(TextUtils.equals(v1,v2)){
                            this.mVersion = e;
                            e.isSelect = true;
                            ((ClassifySuperFragment)fragments.get(0)).classGrade((String)event.values[0],(String)event.values[1],e.getChildren());
                        }
                    }
                    versionAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void OnCommItem(StudyClassVersion e) {
        this.mVersion = e;
        switch (e.dataType) {  //0-版本  1-精学
            case 0:
                setFragmentPage(0);
                if (isBuy) {
                    ((ClassifySuperFragment)fragments.get(0)).classGrade2(e.getVersionId(),e.getChildren());
                }else{
                    ((ClassifySuperFragment)fragments.get(0)).classGrade(e.getVersionId());
                }
                break;
            case 1:
                setFragmentPage(1);
                ((ClassifyExamReviewFragment)fragments.get(1)).classExam(0+"");
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

    private void initClassVersion() {
        load_view.setViewState(load_view.VIEW_STATE_LOADING);
        if (isBuy) {
            Observable<Object> mObservable = Observable.create(new Observable.OnSubscribe<Object>() {
                @Override
                public void call(final Subscriber<? super Object> subscriber) {
                    StudyClassSubject e = AppContext.getInstance().getStudyClassic(classType);
                    if(e != null){
                        versionList.clear();
                        versionList.addAll(e.getChildren());
                    }
                    subscriber.onCompleted();
                }
            });
            mPresenter.newThread(mObservable,new ApiRequestListener<String>(){
                @Override
                public void onResultSuccess(String res) {
                    initVersionData();
                }
            });
        } else {
            mPresenter.classFreeVersion(classType, new ApiRequestListener<String>() {
                @Override
                public void onResultSuccess(String res) {
                    List<StudyClassVersion> list = JSON.parseArray(res, StudyClassVersion.class);
                    versionList.clear();
                    versionList.addAll(list);
                    initVersionData();
                }

                @Override
                public void onResultFail() {
                    load_view.setViewState(load_view.VIEW_STATE_ERROR);
                }
            });
        }
    }

    private void initVersionData() {
        if (isBuy) {
            if(versionList.size()>0){
                StudyClassVersion e = versionList.get(0);
                e.isSelect = true;
                this.mVersion = e;
                ((ClassifySuperFragment)fragments.get(0)).classGrade2(e.getVersionId(),e.getChildren());
            }

            String name = "";
            if (TextUtils.equals("M2", classType) || TextUtils.equals("P2", classType)) {
                name = "中考精学-";
            } else if (TextUtils.equals("M3", classType) || TextUtils.equals("P3", classType)) {
                name = "高考精学-";
            }
            versionList.add(new StudyClassVersion(name + className));
        }else{
            if(versionList.size()>0){
                StudyClassVersion e = versionList.get(0);
                e.isSelect = true;
                ((ClassifySuperFragment)fragments.get(0)).classGrade(e.getVersionId());
            }
        }
        if(versionList.size()>0){
            load_view.setViewState(load_view.VIEW_STATE_CONTENT);
        }else{
            load_view.setViewState(load_view.VIEW_STATE_EMPTY);
        }
        versionAdapter.notifyDataSetChanged();
    }

}
