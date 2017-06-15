package com.asking.pad.app.ui.personalcenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.indicator.TabPageIndicatorFontSize;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 收藏夹页面
 * create by linbin
 */

public class CollectionActivity extends BaseFrameActivity<UserPresenter, UserModel> {


    @BindView(R.id.indicator)
    TabPageIndicatorFontSize indicator;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    ArrayList<String> tabList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();
        tabList.add(getString(R.string.title)); // 题目
        tabList.add(getString(R.string.refer)); // 教育资讯
        tabList.add(getString(R.string.knowledge)); // 知识点
        indicator.setLayoutResource(R.layout.layout_indicator_tab_view_collection);//tab页布局
        CommAdapter mAdapter = new CommAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        indicator.setViewPager(viewPager);
        mAdapter.notifyDataSetChanged();
        indicator.notifyDataSetChanged();
    }

    class CommAdapter extends FragmentStatePagerAdapter {

        public CommAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f;
            if (position == 0) {//题目
                f = SubjectFragment.newInstance();
            } else if (position == 1) {//教育咨询
                f = EducationConsultFragment.newInstance();
            } else {//知识点
                f = KnowledageFragment.newInstance();
            }
            return f;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabList.get(position);
        }

        @Override
        public int getCount() {
            return tabList.size();
        }
    }
}
