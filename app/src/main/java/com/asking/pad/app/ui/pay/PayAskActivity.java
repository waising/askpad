package com.asking.pad.app.ui.pay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.asking.pad.app.R;
import com.asking.pad.app.base.BaseFrameActivity;
import com.asking.pad.app.presenter.UserModel;
import com.asking.pad.app.presenter.UserPresenter;
import com.asking.pad.app.widget.indicator.TabPageIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 充值页面
 */

public class PayAskActivity extends BaseFrameActivity<UserPresenter, UserModel> {
    @BindView(R.id.toolBar)
    Toolbar mToolbar;

    @BindView(R.id.indicator)
    TabPageIndicator indicator;

    @BindView(R.id.viewPager)
    ViewPager viewPager;


    ArrayList<String> tabList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_ask);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {
        super.initView();
        setToolbar(mToolbar, getString(R.string.pay));

        tabList.add(getString(R.string.pay_super_class));
        tabList.add(getString(R.string.pay_ask_money));

        indicator.setLayoutResource(R.layout.layout_indicator_tab_view3);
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
            if (position == 0) {
                f = PaySuperClassFragment.newInstance();
            } else {
                f = PayAskCoinFragment.newInstance();
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
