package com.asking.pad.app.ui.superclass.examreview;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.asking.pad.app.R;
import com.asking.pad.app.base.SwipeBackActivity;
import com.asking.pad.app.entity.ExerAskEntity;
import com.asking.pad.app.entity.LabelEntity;
import com.asking.pad.app.ui.superclass.tutorial.topic.TopicWebTxtFragment;
import com.asking.pad.app.widget.AskMathView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by jswang on 2017/2/28.
 */

public class ExamTopicDetailsActivity extends SwipeBackActivity {
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    @BindView(R.id.indicator)
    TabLayout indicator;

    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    @BindView(R.id.tit_mathView)
    AskMathView tit_mathView;

    public List<ExerAskEntity.OptionsBean> dataList = new ArrayList<>();

    CommPagerAdapter mPagerAdapter;

    /**
     * 1-一轮复习备高考|3个要求 0-二轮复习题型诊断分析
     */
    private int showType;
    private String zhanycm;
    private ExerAskEntity mEntity;

    ArrayList<LabelEntity> tabList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_topic_details);
        ButterKnife.bind(this);
        setSwipeBackEnable(false);
    }

    @Override
    public void initData() {
        super.initData();
        setToolbar(toolBar, "典例示范");

        showType = getIntent().getIntExtra("showType", 0);
        zhanycm = getIntent().getStringExtra("zhanycm");
        mEntity = (ExerAskEntity) getIntent().getSerializableExtra("ExerAskEntity");

        tit_mathView.formatMath().showWebImage();
        tit_mathView.setText(mEntity.subject_description_html);

        dataList.clear();
        if (showType == 1) {
            dataList.addAll(mEntity.bianst);
        } else {
            dataList.addAll(mEntity.biansts);
        }

        if(!TextUtils.isEmpty(zhanycm)){
            tabList.add(new LabelEntity(0,"斩妖除“模”",zhanycm));
        }

        String silyx = mEntity.getSilyx();
        if (!TextUtils.isEmpty(silyx)) {
            tabList.add(new LabelEntity(0,"思路研析",silyx));
        }

        if (!TextUtils.isEmpty(mEntity.xiangxjt)) {
            tabList.add(new LabelEntity(0,"详细解题",mEntity.xiangxjt));
        }

        if (!TextUtils.isEmpty(mEntity.qiaoxqj)) {
            tabList.add(new LabelEntity(0,"巧学巧记",mEntity.qiaoxqj));
        }

        mPagerAdapter = new CommPagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(mPagerAdapter);

        indicator.setupWithViewPager(mViewpager);
        indicator.setTabsFromPagerAdapter(mPagerAdapter);
    }

    public class CommPagerAdapter extends FragmentStatePagerAdapter {

        public CommPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return dataList.size() + tabList.size();
        }

        @Override
        public Fragment getItem(int position) {
            int index = tabList.size();

            if (position < index) {
                return TopicWebTxtFragment.newInstance(tabList.get(position).getName());
            }

            position = position - index;
            ExerAskEntity.OptionsBean e = dataList.get(position);
            return OptionFragment.newInstance(position, e);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            int index = tabList.size();

            if (position < index) {
                return tabList.get(position).getId();
            }

            position = position - index;
            return "变式题" + (position + 1);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

}
