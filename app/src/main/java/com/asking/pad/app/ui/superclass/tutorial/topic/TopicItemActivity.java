package com.asking.pad.app.ui.superclass.tutorial.topic;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.commom.ParamHelper;
import com.asking.pad.app.entity.classex.SubjectClass;
import com.asking.pad.app.entity.superclass.SuperClassSpeaker;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.AskMathView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by wxiao on 2016/12/12.
 * 来讲题-查看解析-变式题
 */

public class TopicItemActivity extends BaseFrameActivity<UserPresenter, UserModel> {
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    @BindView(R.id.indicator)
    TabLayout indicator;

    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    @BindView(R.id.tit_mathView)
    AskMathView tit_mathView;

    CommPagerAdapter mPagerAdapter;

    boolean isBuy;
    String classType;

    private SuperClassSpeaker mClassSpeaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_item);
        ButterKnife.bind(this);

        HashMap<String, Object> mParams = ParamHelper.acceptParams(TopicItemActivity.class.getName());
        mClassSpeaker = (SuperClassSpeaker) mParams.get("SuperClassSpeaker");
        isBuy = this.getIntent().getBooleanExtra("isBuy", false);
        classType = this.getIntent().getStringExtra("classType");
    }

    @Override
    public void initView() {
        super.initView();
        setToolbar(toolBar, "典例");

        tit_mathView.formatMath().showWebImage();

        tit_mathView.setText(mClassSpeaker.getSubjectDescriptionHtml());

        mPagerAdapter = new CommPagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(mPagerAdapter);

        indicator.setupWithViewPager(mViewpager);
        indicator.setTabsFromPagerAdapter(mPagerAdapter);

        initLoad();
    }

    public class CommPagerAdapter extends FragmentStatePagerAdapter {

        public CommPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mClassSpeaker.subjectmuls.size() + mClassSpeaker.getClassTabList().size() + 1;
        }

        @Override
        public Fragment getItem(int position) {
            int index = mClassSpeaker.getClassTabList().size();

            if (position < index) {
                return TopicWebTxtFragment.newInstance(mClassSpeaker.getClassTabList().get(position).tabContentHtml);
            }

            if (index == position) {
                return TopicWebTxtFragment.newInstance(mClassSpeaker.subject.detailsAnswerHtml);
            }
            position = position - (index + 1);
            SubjectClass e = mClassSpeaker.subjectmuls.get(position);
            return TopicItemFragment.newInstance(position,classType, e);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            int index = mClassSpeaker.getClassTabList().size();

            if (position < index) {
                return mClassSpeaker.getClassTabList().get(position).tabTypeName;
            }

            if (index == position) {
                return "详细解题";
            }
            position = position - (index + 1);
            return "变式题" + (position + 1);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
