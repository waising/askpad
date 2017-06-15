package com.asking.pad.app.ui.superclass.tutorial.topic;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.alibaba.fastjson.JSON;
import com.asking.pad.app.R;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.entity.SuperSuperClassSpeakerEntity;
import com.asking.pad.app.entity.classex.SubjectClass;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.AskMathView;

import java.util.ArrayList;
import java.util.List;

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


    private int start = 0;
    private int limit = 10;

    boolean isBuy;

    private SuperSuperClassSpeakerEntity.ListBean mClassSpeaker;

    private List<SubjectClass> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_item);
        ButterKnife.bind(this);

        mClassSpeaker = (SuperSuperClassSpeakerEntity.ListBean) getIntent().getSerializableExtra("SuperSuperClassSpeakerEntity.ListBean");
        isBuy = this.getIntent().getBooleanExtra("isBuy", false);
    }

    @Override
    public void initView() {
        super.initView();
        setToolbar(toolBar, "变式题");


        tit_mathView.formatMath().showWebImage();
        String strTmp = "";

        try {
            if (mClassSpeaker.getSubject().getSubjectTypeBean().getType_id().equals("1")) {//选择题
                for (SuperSuperClassSpeakerEntity.ListBean.SubjectBean.OptionsBean o : mClassSpeaker.getSubject().getOptions()) {
                    strTmp = strTmp + o.getOptionName() + ". " + o.getOptionContentHtml().substring(3, o.getOptionContentHtml().length() - 4) + "<br/>";
                }
                tit_mathView.setText(mClassSpeaker.getSubject().getSubjectDescriptionHtml() + strTmp);
            } else {
                tit_mathView.setText(mClassSpeaker.getSubject().getSubjectDescriptionHtml());
            }
        } catch (Exception e) {
        }

        mPagerAdapter = new CommPagerAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(mPagerAdapter);

        indicator.setupWithViewPager(mViewpager);
        indicator.setTabsFromPagerAdapter(mPagerAdapter);

        initLoad();
    }

    @Override
    public void initLoad() {
        mPresenter.getSubjectMul(isBuy, mClassSpeaker.getKindId(), getIntent().getStringExtra("classType").substring(0, 1), start, limit, new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                List<SubjectClass> list = JSON.parseArray(res, SubjectClass.class);
                dataList.clear();
                dataList.addAll(list);

                indicator.removeAllTabs();
                for (SuperSuperClassSpeakerEntity.ListBean.TabListBean e : mClassSpeaker.getTabList()) {
                    indicator.addTab(indicator.newTab().setText(e.getTabTypeName()));
                }
                indicator.addTab(indicator.newTab().setText("详细解题"));
                for (int i = 0; i < dataList.size(); i++) {
                    indicator.addTab(indicator.newTab().setText("变式题" + (i + 1)));
                }

                mPagerAdapter.notifyDataSetChanged();
            }

        });
    }

    public class CommPagerAdapter extends FragmentStatePagerAdapter {

        public CommPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return dataList.size() + mClassSpeaker.getTabList().size() + 1;
        }

        @Override
        public Fragment getItem(int position) {
            int index = mClassSpeaker.getTabList().size();

            if (position < index) {
                return TopicWebTxtFragment.newInstance(mClassSpeaker.getTabList().get(position).getTabContentHtml());
            }

            if (index == position) {
                return TopicWebTxtFragment.newInstance(mClassSpeaker.getSubject().getDetailsAnswerHtml());
            }
            position = position - (index + 1);
            SubjectClass e = dataList.get(position);
            return TopicItemFragment.newInstance(position, e);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            int index = mClassSpeaker.getTabList().size();

            if (position < index) {
                return mClassSpeaker.getTabList().get(position).getTabTypeName();
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
