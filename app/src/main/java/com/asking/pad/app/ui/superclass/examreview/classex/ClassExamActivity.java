package com.asking.pad.app.ui.superclass.examreview.classex;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.entity.classex.SubjectClass;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.MultiStateView;
import com.asking.pad.app.widget.indicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/5/23.
 */

public class ClassExamActivity extends BaseFrameActivity<UserPresenter,UserModel> {
    @BindView(R.id.tv_time)
    Chronometer tv_time;

    @BindView(R.id.tv_pre)
    TextView tv_pre;

    @BindView(R.id.tv_next)
    TextView tv_next;

    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    @BindView(R.id.tv_all_count)
    TextView tv_all_count;

    @BindView(R.id.indicator)
    TabPageIndicator indicator;

    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.load_view)
    MultiStateView load_view;

    String classId;
    String difficultyId;
    String choice_count;
    String filling_count;
    String solving_count;

    /**
     * 0-一轮复习  1-二轮复习
     */
    int typeFrom;

    List<SubjectClass> dataList = new ArrayList<>();

    CommPagerAdapter mPagerAdapter;

    int curIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_exam);
        ButterKnife.bind(this);

        classId = this.getIntent().getStringExtra("classId");
        difficultyId = this.getIntent().getStringExtra("difficultyId");
        choice_count = this.getIntent().getStringExtra("choice_count");
        filling_count = this.getIntent().getStringExtra("filling_count");
        solving_count = this.getIntent().getStringExtra("solving_count");
        typeFrom = this.getIntent().getIntExtra("typeFrom",0);
    }

    @Override
    public void initView() {
        super.initView();

        tv_pre.setVisibility(View.GONE);

        mPagerAdapter = new CommPagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(mPagerAdapter);
        indicator.setViewPager(mViewpager);

        load_view.setErrorRefBtnTxt2(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netBookData();
            }
        });

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_pre.setVisibility(View.VISIBLE);
                tv_pre.setVisibility(View.VISIBLE);
                if(position == 0){
                    tv_pre.setVisibility(View.GONE);
                }
                if(position == dataList.size()-1){
                    tv_next.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        netBookData();
    }

    private void netBookData(){
        load_view.setViewState(load_view.VIEW_STATE_LOADING);
        if(typeFrom == 0){
            mPresenter.suitcy(classId,difficultyId,choice_count,filling_count,solving_count, new ApiRequestListener<String>(){
                @Override
                public void onResultSuccess(String res) {
                    initSubjectData(res);
                }

                @Override
                public void onResultFail() {
                    load_view.setViewState(load_view.VIEW_STATE_ERROR);
                }
            });
        }else{
            mPresenter.tuozyy(classId,difficultyId,choice_count,filling_count,solving_count, new ApiRequestListener<String>(){
                @Override
                public void onResultSuccess(String res) {
                    initSubjectData(res);
                }

                @Override
                public void onResultFail() {
                    load_view.setViewState(load_view.VIEW_STATE_ERROR);
                }
            });
        }
    }

    private void initSubjectData(String res){
        load_view.setViewState(load_view.VIEW_STATE_CONTENT);

        JSONObject jsonRes = JSON.parseObject(res);
        List<SubjectClass> xuanztList = JSON.parseArray(jsonRes.getString("xuanzt"),SubjectClass.class);
        List<SubjectClass> jiedtList = JSON.parseArray(jsonRes.getString("jiedt"),SubjectClass.class);
        List<SubjectClass> tianktList = JSON.parseArray(jsonRes.getString("tiankt"),SubjectClass.class);
        dataList.clear();
        dataList.addAll(xuanztList);
        dataList.addAll(jiedtList);
        dataList.addAll(tianktList);

        mPagerAdapter.notifyDataSetChanged();
        indicator.notifyDataSetChanged();

        tv_all_count.setText(String.format("共  %s  题",dataList.size()));

        // 将计时器清零
        tv_time.setBase(SystemClock.elapsedRealtime());
        //开始计时
        tv_time.start();
    }

    class CommPagerAdapter extends FragmentStatePagerAdapter {


        public CommPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Fragment getItem(int position) {
            SubjectClass e = dataList.get(position);
            return ClassExamItemFragment.newInstance(position,e);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == mViewpager.getCurrentItem()){
                return (position+1)+"";
            }
            return "";
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    @OnClick({ R.id.tv_pre, R.id.tv_next, R.id.btn_submit})
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_pre:
                tv_next.setVisibility(View.VISIBLE);

                if(curIndex > 0){
                    mViewpager.post(new Runnable() {
                        @Override
                        public void run() {
                            curIndex = mViewpager.getCurrentItem()-1;
                            mViewpager.setCurrentItem(curIndex);
                            if(curIndex == 0){
                                tv_pre.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                break;
            case R.id.tv_next:
                tv_pre.setVisibility(View.VISIBLE);
                if(curIndex < dataList.size()){
                    mViewpager.post(new Runnable() {
                        @Override
                        public void run() {
                            curIndex = mViewpager.getCurrentItem()+1;
                            mViewpager.setCurrentItem(curIndex);
                            if(curIndex == dataList.size()-1){
                                tv_next.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                break;

            case R.id.btn_submit:
                tv_time.stop();
                btn_submit.setVisibility(View.INVISIBLE);
                for(SubjectClass e : dataList){
                    e.isShowDetailsAnswer = true;
                }
                mPagerAdapter.notifyDataSetChanged();
                mViewpager.post(new Runnable() {
                    @Override
                    public void run() {
                        mViewpager.setCurrentItem(0);
                    }
                });
                break;
        }
    }

}
