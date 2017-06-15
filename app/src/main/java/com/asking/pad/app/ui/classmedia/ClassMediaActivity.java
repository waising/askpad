package com.asking.pad.app.ui.classmedia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseEvenNoPreActivity;
import com.asking.pad.app.commom.AppEventType;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.widget.indicator.TabPageIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jswang on 2017/6/1.
 */

public class ClassMediaActivity extends BaseEvenNoPreActivity {
    @BindView(R.id.indicator)
    TabPageIndicator indicator;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    ArrayList<String> tabList = new ArrayList<>();
    ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_media);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();

        tabList.add("数学");
        tabList.add("物理");

        fragments.add(ClassMediaFragment.newInstance("KM01"));
        fragments.add(ClassMediaFragment.newInstance("KM02"));

        indicator.setLayoutResource(R.layout.layout_indicator_tab_view5);
        CommAdapter mAdapter = new CommAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        indicator.setViewPager(viewPager);
    }

    public void onEventMainThread(AppEventType event) {
        switch (event.type) {
            case AppEventType.PAY_SUCCESSS_REQUEST:
                ((ClassMediaFragment)fragments.get(0)).requstData();
                ((ClassMediaFragment)fragments.get(1)).requstData();
                break;
        }
    }


    class CommAdapter extends FragmentStatePagerAdapter {

        public CommAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabList.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }


    @OnClick({R.id.tv_all_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_all_pay:
                Bundle mBundle = new Bundle();
                mBundle.putString("orderType", "TC");
                CommonUtil.openActivity(PayClassMediaActivity.class, mBundle);
                break;
        }
    }

}
