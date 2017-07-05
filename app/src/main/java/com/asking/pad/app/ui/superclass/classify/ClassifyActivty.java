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

import com.asking.pad.app.AppContext;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseEvenAppCompatActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.entity.superclass.StudyClassSubject;
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
    boolean isSelectNode;
    ArrayList<StudyClassSubject> versionList = new ArrayList<>();

    private int mCurTabIndex = 0;
    List<Fragment> fragments = new ArrayList<>();

    StudyClassSubject mVersion;
    String productId="";

    String versionId="";
    String gradeId="";

    public static void openActivity(String classType,String className,boolean isSelectNode){
        Bundle bundle = new Bundle();
        bundle.putString("classType", classType);
        bundle.putString("className", className);
        bundle.putBoolean("isSelectNode", isSelectNode);
        CommonUtil.openActivity(ClassifyActivty.class, bundle);
    }

    public static void openActivity(String classType,String versionId,String gradeId){
        Bundle bundle = new Bundle();
        bundle.putString("classType", classType);
        bundle.putString("className", Constants.getClassName(classType));
        bundle.putString("versionId", versionId);
        bundle.putString("gradeId", gradeId);
        CommonUtil.openActivity(ClassifyActivty.class, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        ButterKnife.bind(this);
        classType = this.getIntent().getStringExtra("classType");
        className = this.getIntent().getStringExtra("className");
        versionId = this.getIntent().getStringExtra("versionId");
        gradeId = this.getIntent().getStringExtra("gradeId");
        isSelectNode = this.getIntent().getBooleanExtra("isSelectNode", false);
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
                        DownBookActivity.openActivity(productId);
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

                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void OnCommItem(StudyClassSubject e) {
        this.mVersion = e;
        switch (e.dataType) {  //0-版本  1-精学
            case 0:
                this.productId = e.getProductId();
                setFragmentPage(0);
                ((ClassifySuperFragment)fragments.get(0)).initGradeData("",e.getGradeList());
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
        Observable<Object> mObservable = Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(final Subscriber<? super Object> subscriber) {
                StudyClassSubject e = AppContext.getInstance().findTreeListWithAllCourse(classType);
                if(e != null){
                    versionList.clear();
                    versionList.addAll(e.nodelist);
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
    }

    private void selectVersionData(StudyClassSubject e) {
        e.isSelect = true;
        this.mVersion = e;
        this.productId = e.getProductId();
        ((ClassifySuperFragment)fragments.get(0)).initGradeData(gradeId,e.getGradeList());
    }

    private void initVersionData() {
        if(versionList.size()>0){
            if(TextUtils.isEmpty(versionId)){
                selectVersionData(versionList.get(0));
            }else{
                for(StudyClassSubject e:versionList){
                    if(TextUtils.equals(e.getProductId(),versionId)){
                        selectVersionData(e);
                        break;
                    }
                }
            }
        }

        String name = "";
        if (TextUtils.equals("XK01", classType) || TextUtils.equals("XK02", classType)) {
            name = "中考精学-";
        } else if (TextUtils.equals("XK03", classType) || TextUtils.equals("XK04", classType)) {
            name = "高考精学-";
        }
        versionList.add(new StudyClassSubject(name + className));
        if(versionList.size()>0){
            load_view.setViewState(load_view.VIEW_STATE_CONTENT);
        }else{
            load_view.setViewState(load_view.VIEW_STATE_EMPTY);
        }
        versionAdapter.notifyDataSetChanged();
    }

}
